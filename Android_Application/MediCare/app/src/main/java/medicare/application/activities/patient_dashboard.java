package medicare.application.activities;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import medicare.application.R;

/*
 * Main Activity class that loads {@link MainFragment5}.
 */
public class patient_dashboard extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_dashboard);
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.main_browse_fragment, new medicare.application.MainFragment5())
//                    .commitNow();
//        }
    }
}