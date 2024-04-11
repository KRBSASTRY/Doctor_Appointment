package medicare.application.activities;

import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import medicare.application.R;
/*
 * Main Activity class that loads {@link MainFragment4}.
 */
public class doctor_login extends FragmentActivity {

    private Button loginButton;
    private Button signupButton;
    private Button forgotPasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_login);

        // Initialize buttons
        loginButton = findViewById(R.id.loginButton);
        signupButton = findViewById(R.id.signupButton);
        forgotPasswordButton = findViewById(R.id.forgotPassword);

        // Set onClickListeners
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call Flask API for login
                startActivity(new Intent(doctor_login.this, medicare.application.activities.DoctorDashboard.class));
                // Handle response accordingly
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to signup activity
                startActivity(new Intent(doctor_login.this, medicare.application.activities.Signup.class));
            }
        });

        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to password recovery activity
                startActivity(new Intent(doctor_login.this, medicare.application.activities.PatientRecovery.class));
            }
        });
    }
}
