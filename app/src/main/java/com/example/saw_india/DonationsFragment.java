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

    Button payButton;
    EditText amountEditText;
    SliderView sliderView;
    TextView textView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_donations_fragment, container, false);
        amountEditText = view.findViewById(R.id.amountEditText);
        payButton = view.findViewById(R.id.payButton);
        sliderView = view.findViewById(R.id.sliderView);
        textView = view.findViewById(R.id.header_title);
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String amountInString = amountEditText.getText().toString();
                    int amount = Math.round(Float.parseFloat(amountInString) * 100);
                    Checkout checkout = new Checkout();
                    checkout.setKeyID("rzp_test_iifDyEQ5Tenyca");
                    checkout.setImage(R.drawable.final_logo_saw);
                    JSONObject object = new JSONObject();
                    try {
                        object.put("name", "SAW, INDIA");
                        object.put("desciption", "Every drop makes an ocean what it is. Thanks for your help!");
                        object.put("theme.color", "#FF8C00");
                        object.put("currency", "INR");
                        object.put("amount", amount);
                        object.put("prefill.contact", "6398945909");
                        object.put("prefill.email", "hritikraheja27@gmail.com");
                        checkout.open(getActivity(), object);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    Toast.makeText(v.getContext(), "Enter A Valid Amount", Toast.LENGTH_SHORT).show();
                }
            }
        });
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