package ca.on.conestogac.kmarshalldlacelle.matchgame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.material.navigation.NavigationView;

public class SettingsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //declare shared preferences variable
    private SharedPreferences sharedPref;
    Toolbar toolbar;
    private DrawerLayout drawer;
    private Button resetData;
    MatchGameApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        app = ((MatchGameApplication)getApplication());
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        ApplyTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        //get button
        resetData = findViewById(R.id.resetDataButton);
        resetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app.DeleteAllMatches();
            }
        });
        //drawer menu setup
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        UpdateToolbar();
        drawer= findViewById(R.id.settingLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.nav_score:
                startActivity(new Intent(getApplicationContext(), ScoreActivity.class));
                break;
            case R.id.nav_settings:
                // startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                break;
            case R.id.nav_game:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
            default:
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }else
        {
            super.onBackPressed();
        }
    }

    void ApplyTheme() {
        boolean isDarkTheme = sharedPref.getBoolean("darkMode", false);
        if (isDarkTheme) {
            setTheme(R.style.DarkTheme);
        }
        else {
            setTheme(R.style.LightTheme);
        }
    }

    void UpdateToolbar() {
        ColorDrawable colorL = new ColorDrawable(Color.parseColor("#6200EE"));
        ColorDrawable colorD = new ColorDrawable(Color.parseColor("#8e8ea4"));
        boolean isDarkTheme = sharedPref.getBoolean("darkMode", false);
        if (isDarkTheme) {
            toolbar.setBackground(colorD);
        }
        else {
            toolbar.setBackground(colorL);
        }
    }
}