package com.example.saw_india;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.saw_india.modalClasses.SliderAdapter;
import com.example.saw_india.modalClasses.SliderImage;
import com.razorpay.Checkout;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;

public class DonationsFragment extends Fragment {

    SliderView sliderView;
    TextView textView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_donations_fragment, container, false);
        sliderView = view.findViewById(R.id.sliderView);
        textView = view.findViewById(R.id.header_title);
        new GetSliderImagesFromJSON().execute();
        sliderView.setAutoCycleDirection(View.LAYOUT_DIRECTION_LTR);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();
        LinkedList<SliderImage> l = new LinkedList<>();
        return view;
    }

    class GetSliderImagesFromJSON extends AsyncTask<String, Integer, String> {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected String doInBackground(String... string) {
            String result = null;
            try {
                URL url = new URL("https://hritikraheja1.github.io/sliderImages.json");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(reader);
                    StringBuilder sb = new StringBuilder();
                    String temp;
                    while ((temp = bufferedReader.readLine()) != null) {
                        sb.append(temp);
                        sb.append("\n");
                    }
                    reader.close();
                    connection.disconnect();
                    result = sb.toString();
                } else {
                    result = "ERROR: HTTP NOT OK";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            LinkedList<SliderImage> allSliderImages = new LinkedList<>();
            try {
                JSONObject allData = new JSONObject(s);
                JSONArray array = allData.getJSONArray("results");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    String imageUrl = obj.getString("imageUrl");
                    allSliderImages.add(new SliderImage(imageUrl));
                }
                Collections.shuffle(allSliderImages);
                LinkedList<SliderImage> selectedSliderImages = new LinkedList<>();
                int i = 0;
                for(SliderImage image:allSliderImages){
                    if(i>=10){
                        break;
                    }
                    selectedSliderImages.add(image);
                    i++;
                }
                sliderView.setSliderAdapter(new SliderAdapter(selectedSliderImages));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}