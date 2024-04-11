package medicare.application.activities;

import android.os.Bundle;
import medicare.application.R;
import androidx.fragment.app.FragmentActivity;
/*
 * Main Activity class that loads {@link MainFragment11}.
 */
public class DoctorDashboard extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_doctor_dashboard);
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//
//                    .replace(R.id.main_browse_fragment, new medicare.application.MainFragment11())
//                    .commitNow();
//        }
    }
}