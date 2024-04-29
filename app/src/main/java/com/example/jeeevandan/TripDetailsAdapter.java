package com.example.jeeevandan;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TripDetailsAdapter extends RecyclerView.Adapter<TripDetailsAdapter.ViewHolder> {

    private List<Driver.TripDetails> tripDetailsList;

    public TripDetailsAdapter(List<Driver.TripDetails> tripDetailsList) {
        this.tripDetailsList = tripDetailsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Driver.TripDetails tripDetails = tripDetailsList.get(position);
        holder.bind(tripDetails);
    }

    @Override
    public int getItemCount() {
        return tripDetailsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTransplantId, tvDriverName, tvDonorLatitude, tvDonorLongitude, tvReceiverLatitude, tvReceiverLongitude;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTransplantId = itemView.findViewById(R.id.tvTransplantId);
            tvDriverName = itemView.findViewById(R.id.tvDriverName);
            tvDonorLatitude = itemView.findViewById(R.id.tvDonorLatitude);
            tvDonorLongitude = itemView.findViewById(R.id.tvDonorLongitude);
            tvReceiverLatitude = itemView.findViewById(R.id.tvReceiverLatitude);
            tvReceiverLongitude = itemView.findViewById(R.id.tvReceiverLongitude);
        }

        public void bind(Driver.TripDetails tripDetails) {
            tvTransplantId.setText(String.valueOf(tripDetails.getT_id()));
            tvDriverName.setText(tripDetails.getDriver());
            tvDonorLatitude.setText(String.valueOf(tripDetails.getD_lat()));
            tvDonorLongitude.setText(String.valueOf(tripDetails.getD_lngt()));
            tvReceiverLatitude.setText(String.valueOf(tripDetails.getR_lat()));
            tvReceiverLongitude.setText(String.valueOf(tripDetails.getR_lngt()));
        }
    }
}