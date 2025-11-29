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

    // Interface for click listeners
    public interface OnTripClickListener {
        void onTripClick(Trip trip, int position);
        void onEditClick(Trip trip, int position);
        void onDeleteClick(Trip trip, int position);
    }

    // Constructor
    public TripAdapter(Context context, List<Trip> tripList, OnTripClickListener listener) {
        this.context = context;
        this.tripList = tripList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout
        View view = LayoutInflater.from(context).inflate(R.layout.item_trip, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        // Get current trip
        Trip trip = tripList.get(position);

        // Set trip data to views
        holder.destinationTextView.setText(trip.getDestination());
        holder.dateRangeTextView.setText(trip.getStartDate() + " - " + trip.getEndDate());
        holder.budgetTextView.setText("$" + trip.getBudget());
        holder.tripTypeTextView.setText(trip.getTripType());
        holder.statusTextView.setText(trip.getStatus());
        holder.priorityTextView.setText(trip.getPriority());

        // Set status badge color
//        int statusColor = getStatusColor(trip.getStatus());
//        holder.statusTextView.setBackgroundResource(statusColor);
//
//        // Set priority badge color
//        int priorityColor = getPriorityColor(trip.getPriority());
//        holder.priorityTextView.setBackgroundResource(priorityColor);

        // Click listener - view trip details
        holder.cardView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTripClick(trip, holder.getAdapterPosition());
            }
        });

        // Edit button click
        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(trip, holder.getAdapterPosition());
            }
        });

        // Delete button click
        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(trip, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    // Update list when searching
    public void updateList(List<Trip> newList) {
        this.tripList = newList;
        notifyDataSetChanged();
    }

    // Get status badge color
//    private int getStatusColor(String status) {
//        switch (status) {
//            case "Planned":
//                return R.drawable.badge_planned;
//            case "Ongoing":
//                return R.drawable.badge_ongoing;
//            case "Completed":
//                return R.drawable.badge_completed;
//            default:
//                return R.drawable.badge_planned;
//        }
//    }
//
//    // Get priority badge color
//    private int getPriorityColor(String priority) {
//        switch (priority) {
//            case "Low":
//                return R.drawable.badge_priority_low;
//            case "Medium":
//                return R.drawable.badge_priority_medium;
//            case "High":
//                return R.drawable.badge_priority_high;
//            default:
//                return R.drawable.badge_priority_medium;
//        }
//    }

    // ViewHolder class - holds references to views
    public static class TripViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView destinationTextView;
        TextView dateRangeTextView;
        TextView budgetTextView;
        TextView tripTypeTextView;
        TextView statusTextView;
        TextView priorityTextView;
        ImageButton btnEdit;
        ImageButton btnDelete;

        public TripViewHolder(@NonNull View itemView) {
            super(itemView);
            // Find all views in item_trip.xml
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