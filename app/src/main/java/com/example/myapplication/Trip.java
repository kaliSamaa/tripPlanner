package com.example.myapplication;
import java.io.Serializable;

public class Trip implements Serializable {

    private String id;
    private String destination;
    private String startDate;
    private String endDate;
    private String budget;
    private String tripType; // Solo, Family, Business, Adventure
    private String status; // Planned, Ongoing, Completed
    private String priority; // Low, Medium, High
    private boolean accommodationBooked;
    private boolean notificationsEnabled;
    private String notes;
    private long timestamp; // For sorting

    // Constructor
    public Trip() {
        this.id = String.valueOf(System.currentTimeMillis());
        this.timestamp = System.currentTimeMillis();
    }

    public Trip(String destination, String startDate, String endDate, String budget,
                String tripType, String status, String priority,
                boolean accommodationBooked, boolean notificationsEnabled, String notes) {
        this.id = String.valueOf(System.currentTimeMillis());
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.budget = budget;
        this.tripType = tripType;
        this.status = status;
        this.priority = priority;
        this.accommodationBooked = accommodationBooked;
        this.notificationsEnabled = notificationsEnabled;
        this.notes = notes;
        this.timestamp = System.currentTimeMillis();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getTripType() {
        return tripType;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public boolean isAccommodationBooked() {
        return accommodationBooked;
    }

    public void setAccommodationBooked(boolean accommodationBooked) {
        this.accommodationBooked = accommodationBooked;
    }

    public boolean isNotificationsEnabled() {
        return notificationsEnabled;
    }

    public void setNotificationsEnabled(boolean notificationsEnabled) {
        this.notificationsEnabled = notificationsEnabled;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "id='" + id + '\'' +
                ", destination='" + destination + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", budget='" + budget + '\'' +
                ", tripType='" + tripType + '\'' +
                ", status='" + status + '\'' +
                ", priority='" + priority + '\'' +
                ", accommodationBooked=" + accommodationBooked +
                ", notificationsEnabled=" + notificationsEnabled +
                ", notes='" + notes + '\'' +
                '}';
    }
}