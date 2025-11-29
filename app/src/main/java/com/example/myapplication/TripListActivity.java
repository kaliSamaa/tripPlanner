package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class TripListActivity extends AppCompatActivity implements TripAdapter.OnTripClickListener {

    private RecyclerView tripsRecyclerView;
    private TripAdapter tripAdapter;
    private List<Trip> tripList;
    private List<Trip> filteredTripList;
    private TextView emptyStateTextView;
    private FloatingActionButton fabAddTrip;
    private SharedPreferencesManager preferencesManager;
    private SearchView searchView;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_list);

        // Initialize SharedPreferences Manager
        preferencesManager = new SharedPreferencesManager(this);

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("My Trips");
        }

        // Initialize views
        tripsRecyclerView = findViewById(R.id.tripsRecyclerView);
        emptyStateTextView = findViewById(R.id.emptyStateTextView);
        fabAddTrip = findViewById(R.id.fabAddTrip);

        // Setup RecyclerView
        tripsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tripsRecyclerView.setHasFixedSize(true);

        // Load trips from SharedPreferences
        loadTrips();

        // Setup Adapter
        tripAdapter = new TripAdapter(this, filteredTripList, this);
        tripsRecyclerView.setAdapter(tripAdapter);

        // Update UI based on trip list
        updateEmptyState();

        // FAB Click Listener
        fabAddTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TripListActivity.this, AddEditTripActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload trips when returning to this activity
        loadTrips();
        tripAdapter.updateList(filteredTripList);
        updateEmptyState();
    }

    private void loadTrips() {
        tripList = preferencesManager.getAllTrips();
        filteredTripList = new ArrayList<>(tripList);
    }

    private void updateEmptyState() {
        if (filteredTripList.isEmpty()) {
            emptyStateTextView.setVisibility(View.VISIBLE);
            tripsRecyclerView.setVisibility(View.GONE);
        } else {
            emptyStateTextView.setVisibility(View.GONE);
            tripsRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_trip_list, menu);

        // Setup SearchView
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search trips...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterTrips(newText);
                return true;
            }
        });

        return true;
    }

    private void filterTrips(String query) {
        filteredTripList.clear();

        if (query.isEmpty()) {
            filteredTripList.addAll(tripList);
        } else {
            String lowerCaseQuery = query.toLowerCase().trim();
            for (Trip trip : tripList) {
                if (trip.getDestination().toLowerCase().contains(lowerCaseQuery) ||
                        trip.getTripType().toLowerCase().contains(lowerCaseQuery) ||
                        trip.getStatus().toLowerCase().contains(lowerCaseQuery)) {
                    filteredTripList.add(trip);
                }
            }
        }

        tripAdapter.notifyDataSetChanged();
        updateEmptyState();
    }

    @Override
    public void onTripClick(Trip trip, int position) {
        // View trip details
        Intent intent = new Intent(TripListActivity.this, TripDetailActivity.class);
        intent.putExtra("TRIP_ID", trip.getId());
        startActivity(intent);
    }

    @Override
    public void onEditClick(Trip trip, int position) {
        // Edit trip
        Intent intent = new Intent(TripListActivity.this, AddEditTripActivity.class);
        intent.putExtra("TRIP_ID", trip.getId());
        intent.putExtra("EDIT_MODE", true);
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Trip trip, int position) {
        // Show confirmation dialog
        new AlertDialog.Builder(this)
                .setTitle("Delete Trip")
                .setMessage("Are you sure you want to delete \"" + trip.getDestination() + "\"?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    // Delete trip
                    preferencesManager.deleteTrip(trip.getId());
                    loadTrips();
                    tripAdapter.updateList(filteredTripList);
                    updateEmptyState();
                    Toast.makeText(TripListActivity.this, "Trip deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clear search view if exists
        if (searchView != null) {
            searchView.setOnQueryTextListener(null);
        }
    }
}