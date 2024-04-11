package medicare.application;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

/*
 * Details activity class that loads LeanbackDetailsFragment class
 */
public class DetailsActivity11 extends FragmentActivity {
    public static final String SHARED_ELEMENT_NAME = "hero";
    public static final String MOVIE = "Movie";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details11);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.details_fragment, new VideoDetailsFragment11())
                    .commitNow();
        }
    }

}