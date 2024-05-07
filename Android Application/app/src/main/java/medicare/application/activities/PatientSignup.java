package medicare.application.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import org.json.JSONObject;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import medicare.application.R;

public class PatientSignup extends AppCompatActivity {

    private EditText etName, etEmail, etPhoneNumber, etUserName, etPassword, etAge, etCity, etBP, etDiabetes, etHeight, etWeight;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_signup);
        initializeViews();
        setupButtonClickListener();
    }

    private void initializeViews() {
        etName = findViewById(R.id.etEnterName);
        etEmail = findViewById(R.id.etEnterEmail);
        etPhoneNumber = findViewById(R.id.etEnterPhoneNumber);
        etUserName = findViewById(R.id.etEnterUserName);
        etPassword = findViewById(R.id.etEnterPassword);
        etAge = findViewById(R.id.etEnterAge);
        etCity = findViewById(R.id.etEnterCity);
        etBP = findViewById(R.id.etEnterBP);
        etDiabetes = findViewById(R.id.etEnterDiabetes);
        etHeight = findViewById(R.id.etEnterHeight);
        etWeight = findViewById(R.id.etEnterWeight);
        btnRegister = findViewById(R.id.btnRegister);
    }

    private void setupButtonClickListener() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://ec2-3-144-21-20.us-east-2.compute.amazonaws.com/patient_signup");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setDoOutput(true);

                    JSONObject json = new JSONObject();
                    json.put("name", etName.getText().toString());
                    json.put("email", etEmail.getText().toString());
                    json.put("phoneno", etPhoneNumber.getText().toString());
                    json.put("username", etUserName.getText().toString());
                    json.put("password", etPassword.getText().toString());
                    json.put("age", etAge.getText().toString());
                    json.put("city", etCity.getText().toString());
                    json.put("bp", etBP.getText().toString());
                    json.put("diabetes", etDiabetes.getText().toString());
                    json.put("height", etHeight.getText().toString());
                    json.put("weight", etWeight.getText().toString());

                    OutputStream os = conn.getOutputStream();
                    os.write(json.toString().getBytes());
                    os.close();

                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            response.append(line);
                        }
                        br.close();

                        JSONObject responseObject = new JSONObject(response.toString());
                        if (responseObject.getString("message").equals("User created")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Navigate to patient dashboard
                                    startActivity(new Intent(PatientSignup.this, patient_login.class));
                                    finish(); // Close the signup activity
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Prompt user to enter details again
                                    Toast.makeText(PatientSignup.this, "Registration failed, please check your details and try again.", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(PatientSignup.this, "Error connecting to the server.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PatientSignup.this, "An error occurred: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();
    }
}
