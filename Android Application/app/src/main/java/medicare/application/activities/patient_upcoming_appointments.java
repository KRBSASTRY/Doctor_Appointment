package medicare.application.activities;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import medicare.application.R;

public class patient_upcoming_appointments extends AppCompatActivity {
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String PATIENT_ID_KEY = "patientId";
    private int patientId;
    private RecyclerView recyclerView;
    private List<Appointment> appointmentList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_upcoming_appointments);

        recyclerView = findViewById(R.id.recyclerViewAppointments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        patientId = retrievePatientId();
        // Fetch upcoming appointments
        fetchUpcomingAppointments();
    }

    private int retrievePatientId() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.getInt(PATIENT_ID_KEY, -1); // Return -1 if not found
    }

    private void fetchUpcomingAppointments() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    // Construct the API URL
                    URL url = new URL("http://ec2-18-189-180-8.us-east-2.compute.amazonaws.com/upcoming_patient_appointments");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setDoOutput(true);

                    // Create JSON object for the payload
                    JSONObject payload = new JSONObject();
                    try {
                        JSONObject value = new JSONObject();
                        value.put("patient_id", patientId);
                        payload.put("_value", value);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // Write payload to the output stream
                    conn.getOutputStream().write(payload.toString().getBytes());

                    int responseCode = conn.getResponseCode();

                    // Check if the request was successful (response code 200)
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        // Read data from the server
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;

                        while ((line = bufferedReader.readLine()) != null) {
                            response.append(line);
                        }

                        // Close the input stream
                        bufferedReader.close();

                        // Return the response as a string
                        return response.toString();
                    } else {
                        // If the request was not successful, return null
                        return null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    // Return null in case of an exception
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String jsonResponse) {
                // Check if jsonResponse is not null and parse it
                if (jsonResponse != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(jsonResponse);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject appointmentJson = jsonArray.getJSONObject(i);
                            String status = appointmentJson.getString("Status");
                            String appointmentDate = appointmentJson.getString("appointmentDate");
                            String appointmentTime = appointmentJson.getString("appointmentTime");
                            String doctorName = appointmentJson.getString("doctorName");
                            String doctorSpecialization = appointmentJson.getString("doctorSpecialization");

                            // Create Appointment object and add to list
                            Appointment appointment = new Appointment(status, appointmentDate, appointmentTime, doctorName, doctorSpecialization);
                            appointmentList.add(appointment);
                        }

                        // Populate RecyclerView with appointmentList
                        AppointmentAdapter appointmentAdapter = new AppointmentAdapter(appointmentList);
                        recyclerView.setAdapter(appointmentAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(patient_upcoming_appointments.this, "Error parsing JSON", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(patient_upcoming_appointments.this, "Failed to fetch upcoming appointments", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    // Appointment class
    private static class Appointment {
        private String status;
        private String appointmentDate;
        private String appointmentTime;
        private String doctorName;
        private String doctorSpecialization;

        public Appointment(String status, String appointmentDate, String appointmentTime, String doctorName, String doctorSpecialization) {
            this.status = status;
            this.appointmentDate = appointmentDate;
            this.appointmentTime = appointmentTime;
            this.doctorName = doctorName;
            this.doctorSpecialization = doctorSpecialization;
        }

        public String getStatus() {
            return status;
        }

        public String getAppointmentDate() {
            return appointmentDate;
        }

        public String getAppointmentTime() {
            return appointmentTime;
        }

        public String getDoctorName() {
            return doctorName;
        }

        public String getDoctorSpecialization() {
            return doctorSpecialization;
        }
    }

    // AppointmentAdapter class
    private static class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {

        private List<Appointment> appointmentList;

        public AppointmentAdapter(List<Appointment> appointmentList) {
            this.appointmentList = appointmentList;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvStatus, tvDate, tvTime, tvDoctorName, tvSpecialization;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvStatus = itemView.findViewById(R.id.tvStatus);
                tvDate = itemView.findViewById(R.id.tvDate);
                tvTime = itemView.findViewById(R.id.tvTime);
                tvDoctorName = itemView.findViewById(R.id.tvDoctorName);
                tvSpecialization = itemView.findViewById(R.id.tvSpecialization);
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appointment, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Appointment appointment = appointmentList.get(position);
            holder.tvStatus.setText(appointment.getStatus());
            holder.tvDate.setText(appointment.getAppointmentDate());
            holder.tvTime.setText(appointment.getAppointmentTime());
            holder.tvDoctorName.setText(appointment.getDoctorName());
            holder.tvSpecialization.setText(appointment.getDoctorSpecialization());
        }

        @Override
        public int getItemCount() {
            return appointmentList.size();
        }
    }
}
