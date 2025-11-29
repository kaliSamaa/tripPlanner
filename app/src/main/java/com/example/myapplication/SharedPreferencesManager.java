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

    public SharedPreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void saveTrips(List<Trip> trips) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_TRIPS, gson.toJson(trips));
        editor.apply();
    }

    public List<Trip> getAllTrips() {
        String json = sharedPreferences.getString(KEY_TRIPS, null);
        if (json == null) return new ArrayList<>();
        Type type = new TypeToken<List<Trip>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public void addTrip(Trip trip) {
        List<Trip> trips = getAllTrips();
        trips.add(trip);
        saveTrips(trips);
    }

    public void updateTrip(Trip updatedTrip) {
        List<Trip> trips = getAllTrips();
        for (int i = 0; i < trips.size(); i++) {
            if (trips.get(i).getId().equals(updatedTrip.getId())) {
                trips.set(i, updatedTrip);
                break;
            }
        }
        saveTrips(trips);
    }

    public void deleteTrip(String tripId) {
        List<Trip> trips = getAllTrips();
        trips.removeIf(trip -> trip.getId().equals(tripId));
        saveTrips(trips);
    }

    public Trip getTripById(String tripId) {
        for (Trip trip : getAllTrips()) {
            if (trip.getId().equals(tripId)) return trip;
        }
        return null;
    }

    public boolean tripExists(String tripId) {
        return getTripById(tripId) != null;
    }

    public List<Trip> getTripsByStatus(String status) {
        List<Trip> filtered = new ArrayList<>();
        for (Trip trip : getAllTrips()) {
            if (trip.getStatus().equals(status)) filtered.add(trip);
        }
        return filtered;
    }

    public List<Trip> getTripsByPriority(String priority) {
        List<Trip> filtered = new ArrayList<>();
        for (Trip trip : getAllTrips()) {
            if (trip.getPriority().equals(priority)) filtered.add(trip);
        }
        return filtered;
    }

    public void clearAllTrips() {
        sharedPreferences.edit().remove(KEY_TRIPS).apply();
    }

    public int getTripCount() {
        return getAllTrips().size();
    }
}
