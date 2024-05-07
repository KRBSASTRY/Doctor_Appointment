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

public class DoctorSchedule extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Appointment> appointmentList = new ArrayList<>();
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String Doctor_ID_KEY = "doctorId";
private int doctorId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_schedule);

        recyclerView = findViewById(R.id.recyclerViewDoctorSchedule);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        doctorId=retrieveDoctorId();

        // Fetch doctor schedule
        fetchDoctorSchedule();
    }

    private int retrieveDoctorId() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        return sharedPreferences.getInt(Doctor_ID_KEY, -1); // Return -1 if not found
    }

    private void fetchDoctorSchedule() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    // Construct the API URL
                    URL url = new URL("http://ec2-18-189-180-8.us-east-2.compute.amazonaws.com/doctor_schedule");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setDoOutput(true);

                    // Create JSON object for the payload
                    JSONObject payload = new JSONObject();
                    payload.put("doctorId", doctorId); // Replace 123 with actual doctor id


                    // Send the payload
                    conn.getOutputStream().write(payload.toString().getBytes());

                    // Check if the request was successful (response code 200)
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
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
                } catch (IOException  | JSONException e) {
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
                            String appointmentDate = appointmentJson.getString("appointment_date");
                            int appointmentId = appointmentJson.getInt("appointment_id");
                            String appointmentTime = appointmentJson.getString("appointment_time");
                            String patientName = appointmentJson.getString("patient_name");
                            String status = appointmentJson.getString("status");

                            // Create Appointment object and add to list
                            Appointment appointment = new Appointment(appointmentDate, appointmentId, appointmentTime, patientName, status);
                            appointmentList.add(appointment);
                        }

                        // Populate RecyclerView with appointmentList
                        DoctorScheduleAdapter adapter = new DoctorScheduleAdapter(appointmentList);
                        recyclerView.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(DoctorSchedule.this, "Error parsing JSON", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DoctorSchedule.this, "Failed to fetch doctor schedule", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    // Appointment class
    private static class Appointment {
        private String appointmentDate;
        private int appointmentId;
        private String appointmentTime;
        private String patientName;
        private String status;

        public Appointment(String appointmentDate, int appointmentId, String appointmentTime, String patientName, String status) {
            this.appointmentDate = appointmentDate;
            this.appointmentId = appointmentId;
            this.appointmentTime = appointmentTime;
            this.patientName = patientName;
            this.status = status;
        }
    }

    // DoctorScheduleAdapter class
    private class DoctorScheduleAdapter extends RecyclerView.Adapter<DoctorScheduleAdapter.ViewHolder> {
        private List<Appointment> appointmentList;

        public DoctorScheduleAdapter(List<Appointment> appointmentList) {
            this.appointmentList = appointmentList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appointment_schedule, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Appointment appointment = appointmentList.get(position);
            holder.tvAppointmentDate.setText(appointment.appointmentDate);
//            holder.tvAppointmentId.setText(String.valueOf(appointment.appointmentId));
            holder.tvAppointmentTime.setText(appointment.appointmentTime);
            holder.tvPatientName.setText(appointment.patientName);
            holder.tvStatus.setText(appointment.status);
        }

        @Override
        public int getItemCount() {
            return appointmentList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvAppointmentDate, tvAppointmentId, tvAppointmentTime, tvPatientName, tvStatus;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvAppointmentDate = itemView.findViewById(R.id.tvAppointmentDate);
//                tvAppointmentId = itemView.findViewById(R.id.tvAppointmentId);
                tvAppointmentTime = itemView.findViewById(R.id.tvAppointmentTime);
                tvPatientName = itemView.findViewById(R.id.tvPatientName);
                tvStatus = itemView.findViewById(R.id.tvStatus);
            }
        }
    }
}
