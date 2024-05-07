package medicare.application.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.FragmentActivity;

import medicare.application.R;

/*
 * Main Activity class that loads {@link MainFragment11}.
 */
public class DoctorDashboard extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_dashboard);

        // Schedule Button
        Button scheduleButton = findViewById(R.id.schedule);
        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open DoctorScheduleActivity
                Intent intent = new Intent(DoctorDashboard.this, DoctorSchedule.class);
                startActivity(intent);
            }
        });
        // Schedule Button
        Button reviewButton = findViewById(R.id.HISTORY);
        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open AppointmentReviewActivity
                Intent intent = new Intent(DoctorDashboard.this, AppointmentReview.class);
                startActivity(intent);
            }
        });
    }
}
