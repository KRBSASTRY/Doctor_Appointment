package medicare.application.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.fragment.app.FragmentActivity;
import medicare.application.R;

public class patient_dashboard extends FragmentActivity {
    private Button btnBookAppointment;
    private Button btnAppointmentHistory;
    private Button btnMyAppointments;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_dashboard);

        initializeButtons();
        setupListeners();
    }

    private void initializeButtons() {
        btnBookAppointment = findViewById(R.id.tvBookAppointment);
        btnAppointmentHistory = findViewById(R.id.tvSummary);
        btnMyAppointments = findViewById(R.id.tvMyAppointments);
    }

    private void setupListeners() {
        btnBookAppointment.setOnClickListener(v -> {
            // Intent to navigate to the BookAppointmentActivity
            startActivity(new Intent(this, BookAppointment.class));
        });

        btnAppointmentHistory.setOnClickListener(v -> {
            // Intent to navigate to the AppointmentHistoryActivity
            startActivity(new Intent(this, patient_Appointment_History.class));
        });
//
        btnMyAppointments.setOnClickListener(v -> {
            // Intent to navigate to the MyAppointmentsActivity
            startActivity(new Intent(this, patient_upcoming_appointments.class));
        });
    }
}
