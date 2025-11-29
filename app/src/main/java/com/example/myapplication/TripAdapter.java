package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripViewHolder> {

    private Context context;
    private List<Trip> tripList;
    private OnTripClickListener listener;

    public interface OnTripClickListener {
        void onTripClick(Trip trip, int position);
        void onEditClick(Trip trip, int position);
        void onDeleteClick(Trip trip, int position);
    }

    public TripAdapter(Context context, List<Trip> tripList, OnTripClickListener listener) {
        this.context = context;
        this.tripList = tripList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_trip, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        Trip trip = tripList.get(position);

        holder.destinationTextView.setText(trip.getDestination());
        holder.dateRangeTextView.setText(trip.getStartDate() + " - " + trip.getEndDate());
        holder.budgetTextView.setText("$" + trip.getBudget());
        holder.tripTypeTextView.setText(trip.getTripType());
        holder.statusTextView.setText(trip.getStatus());
        holder.priorityTextView.setText(trip.getPriority());

        holder.cardView.setOnClickListener(v -> {
            if (listener != null) listener.onTripClick(trip, holder.getAdapterPosition());
        });

        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEditClick(trip, holder.getAdapterPosition());
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDeleteClick(trip, holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    public void updateList(List<Trip> newList) {
        this.tripList = newList;
        notifyDataSetChanged();
    }

    public static class TripViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView destinationTextView, dateRangeTextView, budgetTextView, tripTypeTextView, statusTextView, priorityTextView;
        ImageButton btnEdit, btnDelete;

        public TripViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            destinationTextView = itemView.findViewById(R.id.destinationTextView);
            dateRangeTextView = itemView.findViewById(R.id.dateRangeTextView);
            budgetTextView = itemView.findViewById(R.id.budgetTextView);
            tripTypeTextView = itemView.findViewById(R.id.tripTypeTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            priorityTextView = itemView.findViewById(R.id.priorityTextView);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
