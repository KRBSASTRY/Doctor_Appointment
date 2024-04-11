package medicare.application.activities;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import medicare.application.R;
/*
 * Main Activity class that loads {@link MainFragment10}.
 */
public class RegistrationSuccess extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_success);
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.main_browse_fragment, new medicare.application.MainFragment10())
//                    .commitNow();
//        }
    }
}