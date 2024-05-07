package medicare.application.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.FragmentActivity;
import medicare.application.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public class patient_login extends FragmentActivity {

    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String PATIENT_ID_KEY = "patientId";

    private Button loginButton, signupButton, forgotPasswordButton;
    private EditText usernameEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_login);

        // Initialize buttons and edit texts
        loginButton = findViewById(R.id.loginButton);
        signupButton = findViewById(R.id.signupButton);
        forgotPasswordButton = findViewById(R.id.forgotPassword);
        usernameEditText = findViewById(R.id.userName);
        passwordEditText = findViewById(R.id.password);

        // Set onClickListeners
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(patient_login.this, medicare.application.activities.PatientSignup.class));
            }
        });

        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(patient_login.this, medicare.application.activities.PatientRecovery.class));
            }
        });
    }

    private void performLogin() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://ec2-18-189-180-8.us-east-2.compute.amazonaws.com/user_login");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setDoOutput(true);

                    JSONObject json = new JSONObject();
                    json.put("username", usernameEditText.getText().toString());
                    json.put("password", passwordEditText.getText().toString());
                    json.put("userType", "user");

                    OutputStream os = conn.getOutputStream();
                    os.write(json.toString().getBytes());
                    os.close();

                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String line;
                        StringBuilder response = new StringBuilder();
                        while ((line = br.readLine()) != null) {
                            response.append(line);
                        }
                        br.close();

                        JSONObject responseObject = new JSONObject(response.toString());
                        if (responseObject.getString("message").equals("login success")) {
                            JSONObject userObject = responseObject.getJSONObject("user");
                            int patientId = userObject.getInt("patient_id"); // Correctly accessing patient_id

                            // Retrieve patient ID from response
//                            String patientId = responseObject.getString("patient_id"); // Assuming 'patient_id' is part of the response

//                            // Store patient ID in SharedPreferences
                            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt(PATIENT_ID_KEY, patientId);
                            editor.apply();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(patient_login.this, medicare.application.activities.patient_dashboard.class));
                                    finish();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(patient_login.this, "Invalid credentials, please try again.", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
