package com.example.saw_india.modalClasses;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.saw_india.R;

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
    public void onBindViewHolder(@NonNull RecyclerViewHolderForSearchNearbyFragment holder, final int position) {
        final String number = listOfAnimalShelters.get(position).getPhoneNumber();
        holder.shelterName.setText("Name : " + listOfAnimalShelters.get(position).getName());
        holder.shelterPhone.setText("Contact : " + number);
        holder.shelterAdd.setText("Address : " + listOfAnimalShelters.get(position).getAddress());
        holder.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (number.compareTo("Not Available") == 0){
                    Toast.makeText(v.getContext(), "Contact Number Not Available", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + number));
                    v.getContext().startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listOfAnimalShelters.size();
    }

    class RecyclerViewHolderForSearchNearbyFragment extends RecyclerView.ViewHolder{
        TextView shelterName;
        TextView shelterPhone;
        TextView shelterAdd;
        Button callButton;

        public RecyclerViewHolderForSearchNearbyFragment(@NonNull View itemView) {
            super(itemView);
            shelterName = itemView.findViewById(R.id.shelterName);
            shelterPhone = itemView.findViewById(R.id.shelterPhone);
            shelterAdd = itemView.findViewById(R.id.shelterAdd);
            callButton = itemView.findViewById(R.id.callButton);
        }
    }
}
