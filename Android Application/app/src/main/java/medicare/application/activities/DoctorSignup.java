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

public class DoctorSignup extends AppCompatActivity {

    private EditText etName, etUsername, etPassword, etSpecialization, etCity, etPhoneNumber, etConsultationFee, etExperience;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        setupButtonClickListener();
    }

    private void initializeViews() {
        etName = findViewById(R.id.etEnterName);
        etUsername = findViewById(R.id.etEnterUserName);
        etPassword = findViewById(R.id.etEnterPassword);
        etSpecialization = findViewById(R.id.etEnterSpecialization);
        etCity = findViewById(R.id.etEnterCity);
        etPhoneNumber = findViewById(R.id.etEnterPhoneNumber);
        etConsultationFee = findViewById(R.id.etEnterConsultationFee);
        etExperience = findViewById(R.id.etEnterExperience);
        btnRegister = findViewById(R.id.btnRegister);
    }

    private void setupButtonClickListener() {
        btnRegister.setOnClickListener(v -> registerDoctor());
    }

    private void registerDoctor() {
        new Thread(() -> {
            try {
                URL url = new URL("http://ec2-3-144-21-20.us-east-2.compute.amazonaws.com/doctor_signup");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                JSONObject json = new JSONObject();
                json.put("name", etName.getText().toString());
                json.put("username", etUsername.getText().toString());
                json.put("password", etPassword.getText().toString());
                json.put("specialization", etSpecialization.getText().toString());
                json.put("city", etCity.getText().toString());
                json.put("phoneno", etPhoneNumber.getText().toString());
                json.put("consultationFee", etConsultationFee.getText().toString());
                json.put("experience", etExperience.getText().toString());

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
                    if (responseObject.getString("message").equals("Doctor created")) {
                        runOnUiThread(() -> {
                            Toast.makeText(DoctorSignup.this, "Registration successful!", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(DoctorSignup.this, doctor_login.class)); // Assuming LoginActivity is the next step
                            finish();
                        });
                    } else {
                        runOnUiThread(() -> Toast.makeText(DoctorSignup.this, "Registration failed, please try again.", Toast.LENGTH_LONG).show());
                    }
                } else {
                    runOnUiThread(() -> Toast.makeText(DoctorSignup.this, "Error connecting to the server.", Toast.LENGTH_LONG).show());
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(DoctorSignup.this, "An error occurred: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }
}
