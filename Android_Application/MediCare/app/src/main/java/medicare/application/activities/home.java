package medicare.application.activities;
import android.content.Intent;

import android.os.Bundle;
import medicare.application.R;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

public class home extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle navigation view item clicks here
                int id = item.getItemId();

                if (id == R.id.nav_login_doctor) {
                    // Handle doctor login action
                    Intent intent = new Intent(home.this, medicare.application.activities.doctor_login.class);
                    startActivity(intent);
                    return true;

                } else if (id == R.id.nav_login_patient) {
                    // Handle patient login action
                    Intent intent = new Intent(home.this, medicare.application.activities.patient_login.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.nav_contact_us) {
                    // Handle contact us action
                    Intent intent = new Intent(home.this, medicare.application.activities.ContactUs.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.nav_about_us) {
                    // Handle about us action
                    Intent intent = new Intent(home.this, medicare.application.activities.AboutUs.class);
                    startActivity(intent);
                    return true;
                } else if (id == R.id.nav_services) {
                    // Handle services action
                    Toast.makeText(home.this, "Services", Toast.LENGTH_SHORT).show();
                    return true;
                }

                return false;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle toggle button click events
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
