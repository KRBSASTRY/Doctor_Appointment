package medicare.application.activities;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import medicare.application.R;

/*
 * Main Activity class that loads {@link MainFragment7}.
 */
public class PasswordUpdateSuccess extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_update_success);
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.main_browse_fragment, new medicare.application.MainFragment7())
//                    .commitNow();
//        }
    }
}