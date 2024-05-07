package medicare.application.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import medicare.application.R;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

public class Appointment_date_time extends AppCompatActivity {
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String PATIENT_ID_KEY = "patientId";
    private static final String Doctor_ID_KEY = "doctorId";

    private TextView tvSelectedDate;
    private Button btnPickDate, btnPickTime, btnConfirmDate;
    private String selectedDate;
    private String selectedTime;
    private int doctorId;
    private int patientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_date_time);

        doctorId = retrieveDoctorId(); // Retrieve doctorId
        patientId = retrievePatientId(); // Retrieve patientId from SharedPreferences

        if (doctorId == -1 || patientId == -1) {
            Toast.makeText(this, "Error: Doctor or patient information not specified.", Toast.LENGTH_LONG).show();
            finish(); // Close this activity if crucial information is missing
            return;
        }

        initializeViews();
        setupListeners();
    }

    private int retrievePatientId() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.getInt(PATIENT_ID_KEY, -1); // Return -1 if not found
    }

    private int retrieveDoctorId() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.getInt(Doctor_ID_KEY, -1); // Return -1 if not found
    }

    private void initializeViews() {
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        btnPickDate = findViewById(R.id.btnPickDate);
        btnPickTime = findViewById(R.id.btnPickTime);
        btnConfirmDate = findViewById(R.id.btnConfirmDate);
    }

    private void setupListeners() {
        btnPickDate.setOnClickListener(view -> showDatePickerDialog());
        btnPickTime.setOnClickListener(view -> showTimeSelectionDialog());
        btnConfirmDate.setOnClickListener(view -> confirmDateAndTime());
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (datePicker, year, month, dayOfMonth) -> {
                    selectedDate = String.format("%d-%02d-%02d", year, month + 1, dayOfMonth);
                    tvSelectedDate.setText("Date: " + selectedDate + "\nTime: " + (selectedTime != null ? selectedTime : "Not selected"));
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void showTimeSelectionDialog() {
        final String[] times = {"9 AM", "11 AM", "2 PM"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Pick a Time")
                .setItems(times, (dialog, which) -> {
                    selectedTime = times[which];
                    tvSelectedDate.setText("Date: " + selectedDate + "\nTime: " + selectedTime);
                });
        builder.create().show();
    }

    private void confirmDateAndTime() {
        if (selectedDate == null || selectedTime == null) {
            Toast.makeText(this, "Please select both date and time.", Toast.LENGTH_SHORT).show();
            return;
        }
        checkDoctorAvailability(doctorId, selectedTime, selectedDate);
    }

    private void checkDoctorAvailability(int doctorId, String slot, String date) {
        new Thread(() -> {
            try {
                JSONObject payload = new JSONObject();
                payload.put("doctor_id", doctorId);
                payload.put("slot", slot);
                payload.put("date", date);

                URL url = new URL("http://ec2-18-189-180-8.us-east-2.compute.amazonaws.com/check_doctor_availability");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                os.write(payload.toString().getBytes());
                os.close();

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    JSONObject responseJson = new JSONObject(response.toString());
                    boolean available = responseJson.getBoolean("available");
                    if (available) {
                        bookAppointment(doctorId, patientId, slot, date);
                    } else {
                        runOnUiThread(() -> Toast.makeText(Appointment_date_time.this, "Doctor is busy, select another time.", Toast.LENGTH_LONG).show());
                    }
                } else {
                    runOnUiThread(() -> Toast.makeText(Appointment_date_time.this, "Error checking availability.", Toast.LENGTH_LONG).show());
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(Appointment_date_time.this, "Failed to check availability: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }

    private void bookAppointment(int doctorId, int patientId, String slot, String date) {
        new Thread(() -> {
            try {
                JSONObject payload = new JSONObject();
                payload.put("doctor_id", doctorId);
                payload.put("patient_id", patientId);
                payload.put("appointment_time", slot);
                payload.put("date", date);
                payload.put("consultationFee", 200); // This should be fetched or passed along from previous data

                URL url = new URL("http://ec2-18-189-180-8.us-east-2.compute.amazonaws.com/book_appointment");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                os.write(payload.toString().getBytes());
                os.close();

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    runOnUiThread(() -> {
                        Toast.makeText(Appointment_date_time.this, "Appointment booked successfully!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(Appointment_date_time.this, patient_dashboard.class));
                        finish();
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(Appointment_date_time.this, "Error booking appointment.", Toast.LENGTH_LONG).show());
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(Appointment_date_time.this, "Failed to book appointment: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }
}
