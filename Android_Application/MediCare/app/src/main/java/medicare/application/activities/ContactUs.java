package medicare.application.activities;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import medicare.application.R;
import medicare.application.fragments.DrawerFragment;

/*
 * Main Activity class that loads {@link MainFragment8}.
 */
public class ContactUs extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.main_browse_fragment, new medicare.application.MainFragment8())
//                    .commitNow();
//        }
    }
}