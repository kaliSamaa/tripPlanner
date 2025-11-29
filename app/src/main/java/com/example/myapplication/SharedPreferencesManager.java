package com.example.myapplication;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SharedPreferencesManager {

    private static final String PREF_NAME = "AdventurePlannerPrefs";
    private static final String KEY_TRIPS = "trips";

    private SharedPreferences sharedPreferences;
    private Gson gson;

    // Constructor
    public SharedPreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    // Save all trips to storage
    public void saveTrips(List<Trip> trips) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = gson.toJson(trips); // Convert trips to JSON string
        editor.putString(KEY_TRIPS, json);
        editor.apply(); // Save asynchronously
    }

    // Get all trips from storage
    public List<Trip> getAllTrips() {
        String json = sharedPreferences.getString(KEY_TRIPS, null);
        if (json == null) {
            return new ArrayList<>(); // Return empty list if no trips saved
        }
        Type type = new TypeToken<List<Trip>>() {}.getType();
        return gson.fromJson(json, type); // Convert JSON back to List<Trip>
    }

    // Add a new trip
    public void addTrip(Trip trip) {
        List<Trip> trips = getAllTrips();
        trips.add(trip);
        saveTrips(trips);
    }

    // Update existing trip
    public void updateTrip(Trip updatedTrip) {
        List<Trip> trips = getAllTrips();
        for (int i = 0; i < trips.size(); i++) {
            if (trips.get(i).getId().equals(updatedTrip.getId())) {
                trips.set(i, updatedTrip); // Replace old trip with updated one
                break;
            }
        }
        saveTrips(trips);
    }

    // Delete trip by ID
    public void deleteTrip(String tripId) {
        List<Trip> trips = getAllTrips();
        trips.removeIf(trip -> trip.getId().equals(tripId)); // Remove matching trip
        saveTrips(trips);
    }

    // Get single trip by ID
    public Trip getTripById(String tripId) {
        List<Trip> trips = getAllTrips();
        for (Trip trip : trips) {
            if (trip.getId().equals(tripId)) {
                return trip;
            }
        }
        return null; // Trip not found
    }

    // Check if trip exists
    public boolean tripExists(String tripId) {
        return getTripById(tripId) != null;
    }

    // Get trips by status (Planned, Ongoing, Completed)
    public List<Trip> getTripsByStatus(String status) {
        List<Trip> allTrips = getAllTrips();
        List<Trip> filteredTrips = new ArrayList<>();
        for (Trip trip : allTrips) {
            if (trip.getStatus().equals(status)) {
                filteredTrips.add(trip);
            }
        }
        return filteredTrips;
    }

    // Get trips by priority (Low, Medium, High)
    public List<Trip> getTripsByPriority(String priority) {
        List<Trip> allTrips = getAllTrips();
        List<Trip> filteredTrips = new ArrayList<>();
        for (Trip trip : allTrips) {
            if (trip.getPriority().equals(priority)) {
                filteredTrips.add(trip);
            }
        }
        return filteredTrips;
    }

    // Clear all trips (useful for testing)
    public void clearAllTrips() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_TRIPS);
        editor.apply();
    }

    // Get total number of trips
    public int getTripCount() {
        return getAllTrips().size();
    }
}