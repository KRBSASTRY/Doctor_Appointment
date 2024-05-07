package medicare.application.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import medicare.application.R;

public class BookAppointment extends AppCompatActivity {
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String Doctor_ID_KEY = "doctorId";

    private List<JSONObject> filteredDoctorsList = new ArrayList<>();
    private List<JSONObject> fetchedDoctors =new ArrayList<>();
    private RecyclerView recyclerView;
    private JSONObject selectedDoctor;


    private Spinner spinnerCity, spinnerSpecialization;
    private Button btnBookAppointment;
    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_book_appointment);
//
//        recyclerView = findViewById(R.id.recyclerViewDoctors);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        // Initialize the list to store fetched and filtered doctors
//        fetchedDoctors = new ArrayList<>();
//        filteredDoctorsList = new ArrayList<>();
//
//        // Fetch doctors data from the API
//        fetchDoctors();
//    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        spinnerCity = findViewById(R.id.spinnerCity);
        spinnerSpecialization = findViewById(R.id.spinnerSpecialization);
        recyclerView = findViewById(R.id.recyclerViewDoctors);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        btnBookAppointment = findViewById(R.id.btnBookAppointment);

        // Initialize the list to store filtered doctors
        filteredDoctorsList = new ArrayList<>();

        // Populate city and specialization spinners
        populateSpinners();

        // Fetch doctors data from the API
        fetchDoctors();

        // Book appointment button click listener
        btnBookAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookAppointment();
            }
        });
    }

    private void populateSpinners() {
        // Dummy data for spinners
        List<String> cities = new ArrayList<>();
        cities.add("Albany");
        cities.add("MaryLand");
        cities.add("Texas");
        cities.add("Balmore");

        List<String> specializations = new ArrayList<>();
        specializations.add("Dentist");
        specializations.add("Cardiologist");
        specializations.add("Dermatologist");
        specializations.add("Gynecologist");
        specializations.add("Neurologist");
        specializations.add("Psychiatrist");

        // Set adapters for spinners
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cities);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setAdapter(cityAdapter);

        ArrayAdapter<String> specializationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, specializations);
        specializationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSpecialization.setAdapter(specializationAdapter);

        // Set listeners for spinners
        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterDoctors();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerSpecialization.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterDoctors();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }




    private void fetchDoctors() {
        new AsyncTask<Void, Void, JSONObject>() {
            @Override
            protected JSONObject doInBackground(Void... voids) {
                try {
                    // Construct the URL for the API call
                    String apiUrl = "http://ec2-18-189-180-8.us-east-2.compute.amazonaws.com/get_doctors";
                    URL url = new URL(apiUrl);

                    // Create a HttpURLConnection
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");

                    // Check the response code
                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        // Parse the JSON response
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String inputLine;
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                        return new JSONObject(response.toString());
                    } else {
                        // Handle HTTP error
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(BookAppointment.this, "Failed to fetch doctors. HTTP error: " + responseCode, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    // Handle exception
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(BookAppointment.this, "Failed to fetch doctors. Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                return null;
            }

            @Override
            protected void onPostExecute(JSONObject jsonResponse) {
                if (jsonResponse != null) {
                    try {
                        JSONArray doctorsArray = jsonResponse.getJSONArray("payload");
                        // Populate fetchedDoctors list with doctors data
                        fetchedDoctors.clear();
                        for (int i = 0; i < doctorsArray.length(); i++) {
                            fetchedDoctors.add(doctorsArray.getJSONObject(i));
                        }
                        // Filter doctors based on selected city and specialization
                        filterDoctors();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.execute();
    }

    private void filterDoctors() {
        filteredDoctorsList.clear();

        String selectedCity = spinnerCity.getSelectedItem().toString();
        String selectedSpecialization = spinnerSpecialization.getSelectedItem().toString();

        for (JSONObject doctor : fetchedDoctors) {
            try {
                if (doctor.getString("city").equals(selectedCity) && doctor.getString("specialization").equals(selectedSpecialization)) {
                    filteredDoctorsList.add(doctor);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Update RecyclerView with filtered doctors list
        DoctorAdapter doctorAdapter = new DoctorAdapter(filteredDoctorsList);
        recyclerView.setAdapter(doctorAdapter);
    }




    private class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ViewHolder> {

        private List<JSONObject> doctorList;


        // Constructor to initialize the list of doctors
        public DoctorAdapter(List<JSONObject> doctorList) {
            this.doctorList = doctorList;
        }

        // ViewHolder class to hold the views for each item in the RecyclerView
        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView textViewDoctorName;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                textViewDoctorName = itemView.findViewById(R.id.doctorNameTextView);
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Inflate the layout for each item and create a new ViewHolder instance
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_doctor_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            // Bind the data to the views in each item
            JSONObject doctor = doctorList.get(position);
            try {
                String doctorName = doctor.getString("name");
                int experience = doctor.getInt("experience");
                String specialization = doctor.getString("specialization");
                int consultationFee = doctor.getInt("consultationFee");

                // Set doctor details to TextViews in the item layout
                holder.textViewDoctorName.setText("Name: " + doctorName + "\nExperience: " + experience + " years\nSpecialization: " + specialization + "\nConsultation Fee: $" + consultationFee);

                // Set click listener for the item view
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Store the selected doctor
                        selectedDoctor = doctor;
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return doctorList.size();
        }

        // Method to get the selected doctor
        public JSONObject getSelectedDoctor() {
            return selectedDoctor;
        }
    }


    public void bookAppointment() {
            if (selectedDoctor != null) {
                int doctorId=Integer.parseInt(selectedDoctor.optString("doctor_id"));
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(Doctor_ID_KEY, doctorId);
                editor.apply();

//                // Redirect to appointment date-time screen
                Intent intent = new Intent(BookAppointment.this, Appointment_date_time.class);
//                // Pass the selected doctor's ID to the next activity
//                intent.putExtra("doctorId", selectedDoctor.optString("doctor_id"));
                startActivity(intent);
            } else {
                Toast.makeText(BookAppointment.this, "Please select a doctor", Toast.LENGTH_SHORT).show();
            }
        }

}
