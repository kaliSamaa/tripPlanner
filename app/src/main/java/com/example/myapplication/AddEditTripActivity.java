package com.example.myapplication;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddEditTripActivity extends AppCompatActivity {

    private TextInputEditText etDestination, etBudget, etNotes;
    private TextView tvStartDate, tvEndDate;
    private RadioGroup rgTripType, rgStatus;
    private Spinner spinnerPriority;
    private CheckBox cbAccommodation;
    private SwitchCompat switchNotifications;
    private Button btnSave;


    private SharedPreferencesManager preferencesManager;
    private Trip currentTrip;
    private boolean isEditMode = false;

    // Date handling
    private Calendar startCalendar = Calendar.getInstance();
    private Calendar endCalendar = Calendar.getInstance();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_trip);

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize views
        initializeViews();

        // Initialize SharedPreferences
        preferencesManager = new SharedPreferencesManager(this);

        // Check if edit mode (editing existing trip)
        if (getIntent().hasExtra("TRIP_ID")) {
            isEditMode = true;
            String tripId = getIntent().getStringExtra("TRIP_ID");
            currentTrip = preferencesManager.getTripById(tripId);
            loadTripData();
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Edit Trip");
            }
        } else {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Add New Trip");
            }
        }

        // Setup listeners
        setupListeners();
    }

    private void initializeViews() {
        etDestination = findViewById(R.id.etDestination);
        etBudget = findViewById(R.id.etBudget);
        etNotes = findViewById(R.id.etNotes);
        tvStartDate = findViewById(R.id.tvStartDate);
        tvEndDate = findViewById(R.id.tvEndDate);
        rgTripType = findViewById(R.id.rgTripType);
        rgStatus = findViewById(R.id.rgStatus);
        spinnerPriority = findViewById(R.id.spinnerPriority);
        cbAccommodation = findViewById(R.id.cbAccommodation);
        switchNotifications = findViewById(R.id.switchNotifications);
        btnSave = findViewById(R.id.btnSave);

        // Setup priority spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.priority_levels, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(adapter);
    }

    private void setupListeners() {
        // Start Date Picker
        tvStartDate.setOnClickListener(v -> showDatePicker(true));

        // End Date Picker
        tvEndDate.setOnClickListener(v -> showDatePicker(false));

        // Save Button
        btnSave.setOnClickListener(v -> saveTrip());
    }

    // Show DatePicker dialog
    private void showDatePicker(boolean isStartDate) {
        Calendar calendar = isStartDate ? startCalendar : endCalendar;

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    if (isStartDate) {
                        tvStartDate.setText(dateFormat.format(calendar.getTime()));
                    } else {
                        tvEndDate.setText(dateFormat.format(calendar.getTime()));
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    // Load existing trip data (for edit mode)
    private void loadTripData() {
        if (currentTrip != null) {
            etDestination.setText(currentTrip.getDestination());
            tvStartDate.setText(currentTrip.getStartDate());
            tvEndDate.setText(currentTrip.getEndDate());
            etBudget.setText(currentTrip.getBudget());
            etNotes.setText(currentTrip.getNotes());
            cbAccommodation.setChecked(currentTrip.isAccommodationBooked());
            switchNotifications.setChecked(currentTrip.isNotificationsEnabled());

            // Set trip type radio button
            setRadioButton(rgTripType, currentTrip.getTripType());

            // Set status radio button
            setRadioButton(rgStatus, currentTrip.getStatus());

            // Set priority spinner
            String[] priorities = getResources().getStringArray(R.array.priority_levels);
            for (int i = 0; i < priorities.length; i++) {
                if (priorities[i].equals(currentTrip.getPriority())) {
                    spinnerPriority.setSelection(i);
                    break;
                }
            }
        }
    }

    // Helper to set radio button by text
    private void setRadioButton(RadioGroup radioGroup, String value) {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
            if (radioButton.getText().toString().equals(value)) {
                radioButton.setChecked(true);
                break;
            }
        }
    }

    // Save or update trip
    private void saveTrip() {
        // Get values from inputs
        String destination = etDestination.getText().toString().trim();
        String startDate = tvStartDate.getText().toString();
        String endDate = tvEndDate.getText().toString();
        String budget = etBudget.getText().toString().trim();
        String notes = etNotes.getText().toString().trim();

        // Validate inputs
        if (destination.isEmpty()) {
            etDestination.setError("Please enter destination");
            etDestination.requestFocus();
            return;
        }

        if (startDate.equals("Select Date")) {
            Toast.makeText(this, "Please select start date", Toast.LENGTH_SHORT).show();
            return;
        }

        if (endDate.equals("Select Date")) {
            Toast.makeText(this, "Please select end date", Toast.LENGTH_SHORT).show();
            return;
        }

        if (budget.isEmpty()) {
            etBudget.setError("Please enter budget");
            etBudget.requestFocus();
            return;
        }

        // Get selected trip type
        int selectedTripTypeId = rgTripType.getCheckedRadioButtonId();
        RadioButton selectedTripType = findViewById(selectedTripTypeId);
        String tripType = selectedTripType.getText().toString();

        // Get selected status
        int selectedStatusId = rgStatus.getCheckedRadioButtonId();
        RadioButton selectedStatus = findViewById(selectedStatusId);
        String status = selectedStatus.getText().toString();

        // Get priority
        String priority = spinnerPriority.getSelectedItem().toString();

        // Get checkbox and switch values
        boolean accommodationBooked = cbAccommodation.isChecked();
        boolean notificationsEnabled = switchNotifications.isChecked();

        // Create or update trip
        if (isEditMode && currentTrip != null) {
            // Update existing trip
            currentTrip.setDestination(destination);
            currentTrip.setStartDate(startDate);
            currentTrip.setEndDate(endDate);
            currentTrip.setBudget(budget);
            currentTrip.setTripType(tripType);
            currentTrip.setStatus(status);
            currentTrip.setPriority(priority);
            currentTrip.setAccommodationBooked(accommodationBooked);
            currentTrip.setNotificationsEnabled(notificationsEnabled);
            currentTrip.setNotes(notes);

            preferencesManager.updateTrip(currentTrip);
            Toast.makeText(this, "Trip updated!", Toast.LENGTH_SHORT).show();
        } else {

            Trip newTrip = new Trip(destination, startDate, endDate, budget,
                    tripType, status, priority, accommodationBooked, notificationsEnabled, notes);
            preferencesManager.addTrip(newTrip);
            Toast.makeText(this, "Trip added!", Toast.LENGTH_SHORT).show();
        }

        finish(); // Close activity and return to list
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