package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

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
    private LinearLayout emptyStateTextView;

    private FloatingActionButton fabAddTrip;
    private SharedPreferencesManager preferencesManager;
    private SearchView searchView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_list);

        preferencesManager = new SharedPreferencesManager(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("My Trips");
        }

        tripsRecyclerView = findViewById(R.id.tripsRecyclerView);
        emptyStateTextView = findViewById(R.id.emptyStateTextView);
        fabAddTrip = findViewById(R.id.fabAddTrip);

        tripsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadTrips();

        tripAdapter = new TripAdapter(this, filteredTripList, this);
        tripsRecyclerView.setAdapter(tripAdapter);

        updateEmptyState();

        fabAddTrip.setOnClickListener(v -> {
            Intent intent = new Intent(TripListActivity.this, AddEditTripActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTrips();
        tripAdapter.updateList(filteredTripList);
        updateEmptyState();
    }

    private void loadTrips() {
        tripList = preferencesManager.getAllTrips();
        filteredTripList = new ArrayList<>(tripList);
    }

    private void updateEmptyState() {
        boolean isEmpty = filteredTripList.isEmpty();
        emptyStateTextView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        tripsRecyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_trip_list, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search trips...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

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
            String q = query.toLowerCase().trim();
            for (Trip trip : tripList) {
                if (trip.getDestination().toLowerCase().contains(q) ||
                        trip.getTripType().toLowerCase().contains(q) ||
                        trip.getStatus().toLowerCase().contains(q)) {

                    filteredTripList.add(trip);
                }
            }
        }

        tripAdapter.notifyDataSetChanged();
        updateEmptyState();
    }

    @Override
    public void onTripClick(Trip trip, int position) {
        Intent intent = new Intent(TripListActivity.this, TripDetailActivity.class);
        intent.putExtra("TRIP_ID", trip.getId());
        startActivity(intent);
    }

    @Override
    public void onEditClick(Trip trip, int position) {
        Intent intent = new Intent(TripListActivity.this, AddEditTripActivity.class);
        intent.putExtra("TRIP_ID", trip.getId());
        intent.putExtra("EDIT_MODE", true);
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Trip trip, int position) {
        preferencesManager.deleteTrip(trip.getId());
        loadTrips();
        tripAdapter.updateList(filteredTripList);
        updateEmptyState();
        Toast.makeText(this, "Trip deleted", Toast.LENGTH_SHORT).show();
    }
}
