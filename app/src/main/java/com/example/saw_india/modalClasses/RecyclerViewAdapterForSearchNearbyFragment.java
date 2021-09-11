package com.example.saw_india.modalClasses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolderForSearchNearbyFragment holder, int position) {
        holder.shelterName.setText("Name : " + listOfAnimalShelters.get(position).getName());
        holder.shelterPhone.setText("Contact : " + listOfAnimalShelters.get(position).getPhoneNumber());
        holder.shelterAdd.setText("Address : " + listOfAnimalShelters.get(position).getAddress());

    }

    @Override
    public int getItemCount() {
        return listOfAnimalShelters.size();
    }

    class RecyclerViewHolderForSearchNearbyFragment extends RecyclerView.ViewHolder{
        TextView shelterName;
        TextView shelterPhone;
        TextView shelterAdd;

        public RecyclerViewHolderForSearchNearbyFragment(@NonNull View itemView) {
            super(itemView);
            shelterName = itemView.findViewById(R.id.shelterName);
            shelterPhone = itemView.findViewById(R.id.shelterPhone);
            shelterAdd = itemView.findViewById(R.id.shelterAdd);
        }
    }
}
