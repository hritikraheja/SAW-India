package com.example.saw_india.modalClasses;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.saw_india.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;

public class RecyclerViewAdapterForSearchNearbyFragment extends RecyclerView.Adapter<RecyclerViewAdapterForSearchNearbyFragment.RecyclerViewHolderForSearchNearbyFragment>{

    LinkedList<AnimalShelter> listOfAnimalShelters;

    public RecyclerViewAdapterForSearchNearbyFragment(LinkedList<AnimalShelter> listOfAnimalShelters) {
        this.listOfAnimalShelters = listOfAnimalShelters;
    }

    @NonNull
    @Override
    public RecyclerViewHolderForSearchNearbyFragment onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.card_layout_for_nearby_organsizations, parent,false);
        return new RecyclerViewHolderForSearchNearbyFragment(view);
    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolderForSearchNearbyFragment holder, final int position) {
        holder.shelterName.setText("Name : " + listOfAnimalShelters.get(position).getName());
        holder.shelterAdd.setText("Address : " + listOfAnimalShelters.get(position).getAddress());
        holder.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final String placeId = listOfAnimalShelters.get(position).getPlaceId();
                Toast.makeText(v.getContext(), "Fetching Contact Number", Toast.LENGTH_SHORT).show();
                HelpersDatabaseHandler databaseHandler = new HelpersDatabaseHandler();
                final LinkedList<Helper> helpers = new LinkedList<>();
                databaseHandler.getDatabaseByPlaceId(placeId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            Helper helper = dataSnapshot.getValue(Helper.class);
                            helpers.add(helper);
                            break;
                        }
                        if (helpers.size() != 0){
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + helpers.get(0).getHelperMobileNumber()));
                            v.getContext().startActivity(intent);
                        } else {
                            new GetPhoneNumberFromUrl(v.getContext()).execute(placeId);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(v.getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return listOfAnimalShelters.size();
    }

    class RecyclerViewHolderForSearchNearbyFragment extends RecyclerView.ViewHolder{
        TextView shelterName;
        TextView shelterAdd;
        Button callButton;

        public RecyclerViewHolderForSearchNearbyFragment(@NonNull View itemView) {
            super(itemView);
            shelterName = itemView.findViewById(R.id.shelterName);
            shelterAdd = itemView.findViewById(R.id.shelterAdd);
            callButton = itemView.findViewById(R.id.callButton);
        }
    }

    @SuppressLint("StaticFieldLeak")
    class GetPhoneNumberFromUrl extends AsyncTask<String, Integer, String> {

        String phoneNumber;
        Context context;

        public GetPhoneNumberFromUrl(Context context){
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            String pId = strings[0];
            String result = "";
            try {
                URL url = new URL("https://maps.googleapis.com/maps/api/place/details/json?place_id=" + pId + "&fields=formatted_phone_number&key=" + context.getString(R.string.GOOGLE_PLACES_API_KEY));
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
                    connection.disconnect();
                    reader.close();
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
            try {
                JSONObject object = new JSONObject(s).getJSONObject("result");
                try {
                    phoneNumber = object.getString("formatted_phone_number");
                } catch (JSONException e) {
                    phoneNumber = "Not Available";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (phoneNumber.compareTo("Not Available") != 0) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "Contact Number Not Available On Google", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
