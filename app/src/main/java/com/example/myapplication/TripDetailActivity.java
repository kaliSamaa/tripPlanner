package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class TripDetailActivity extends AppCompatActivity {

    private TextView tvDestination, tvDateRange, tvBudget, tvTripType;
    private TextView tvStatus, tvPriority, tvAccommodation, tvNotifications, tvNotes;
    private Button btnEdit, btnDelete;

    private SharedPreferencesManager preferencesManager;
    private Trip currentTrip;
    private String tripId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Trip Details");
        }

        initializeViews();
        preferencesManager = new SharedPreferencesManager(this);

        if (getIntent().hasExtra("TRIP_ID")) {
            tripId = getIntent().getStringExtra("TRIP_ID");
            loadTripDetails();
        } else {
            Toast.makeText(this, "Error loading trip", Toast.LENGTH_SHORT).show();
            finish();
        }

        setupListeners();
    }

    private void initializeViews() {
        tvDestination = findViewById(R.id.tvDestination);
        tvDateRange = findViewById(R.id.tvDateRange);
        tvBudget = findViewById(R.id.tvBudget);
        tvTripType = findViewById(R.id.tvTripType);
        tvStatus = findViewById(R.id.tvStatus);
        tvPriority = findViewById(R.id.tvPriority);
        tvAccommodation = findViewById(R.id.tvAccommodation);
        tvNotifications = findViewById(R.id.tvNotifications);
        tvNotes = findViewById(R.id.tvNotes);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
    }

    private void loadTripDetails() {
        currentTrip = preferencesManager.getTripById(tripId);

        if (currentTrip != null) {
            tvDestination.setText(currentTrip.getDestination());
            tvDateRange.setText(currentTrip.getStartDate() + " - " + currentTrip.getEndDate());
            tvBudget.setText("$" + currentTrip.getBudget());
            tvTripType.setText(currentTrip.getTripType());
            tvStatus.setText(currentTrip.getStatus());
            tvPriority.setText(currentTrip.getPriority());
            tvAccommodation.setText(currentTrip.isAccommodationBooked() ? "Yes" : "No");
            tvNotifications.setText(currentTrip.isNotificationsEnabled() ? "Enabled" : "Disabled");

            if (currentTrip.getNotes() != null && !currentTrip.getNotes().isEmpty()) {
                tvNotes.setText(currentTrip.getNotes());
            } else {
                tvNotes.setText("No notes added");
                tvNotes.setTextColor(getColor(R.color.text_hint));
            }
        }
    }

    private void setupListeners() {
        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(TripDetailActivity.this, AddEditTripActivity.class);
            intent.putExtra("TRIP_ID", tripId);
            intent.putExtra("EDIT_MODE", true);
            startActivity(intent);
        });

        btnDelete.setOnClickListener(v -> {
            preferencesManager.deleteTrip(tripId);
            Toast.makeText(this, "Trip deleted", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTripDetails();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
