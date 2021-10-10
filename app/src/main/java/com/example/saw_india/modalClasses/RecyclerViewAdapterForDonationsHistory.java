package com.example.saw_india.modalClasses;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.saw_india.R;

import java.util.ArrayList;

public class RecyclerViewAdapterForDonationsHistory extends RecyclerView.Adapter<RecyclerViewAdapterForDonationsHistory.DonationsViewHolder>{

    ArrayList<Donation> donations;

    public RecyclerViewAdapterForDonationsHistory(ArrayList<Donation> donations) {
        this.donations = donations;
    }

    @NonNull
    @Override
    public DonationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.card_layout_for_donations_history, parent, false);
        return new DonationsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonationsViewHolder holder, int position) {
        holder.amountTv.setText("₹ " + donations.get(position).getDonationAmount());
        holder.paymentIdTV.setText("PaymentID : " + donations.get(position).getTransactionID());
        holder.statusTV.setText(donations.get(position).getStatus());
        holder.dateAndTimeTV.setText(donations.get(position).getDateAndTime());
        if (donations.get(position).getStatus().compareTo("Success") == 0){
            holder.amountTv.setTextColor(Color.GREEN);
            holder.statusTV.setTextColor(Color.GREEN);
        } else if (donations.get(position).getStatus().compareTo("Failed") == 0){
            holder.statusTV.setTextColor(Color.RED);
        }
    }

    @Override
    public int getItemCount() {
        return donations.size();
    }

    class DonationsViewHolder extends RecyclerView.ViewHolder{
        TextView amountTv;
        TextView statusTV;
        TextView paymentIdTV;
        TextView dateAndTimeTV;

        public DonationsViewHolder(@NonNull View itemView) {
            super(itemView);
            amountTv = itemView.findViewById(R.id.amountTV);
            statusTV = itemView.findViewById(R.id.statusTV);
            paymentIdTV = itemView.findViewById(R.id.paymentIdTV);
            dateAndTimeTV = itemView.findViewById(R.id.dateAndTimeTV);
        }
    }

}
