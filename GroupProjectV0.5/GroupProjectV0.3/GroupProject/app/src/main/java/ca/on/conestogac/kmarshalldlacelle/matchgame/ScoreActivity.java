package ca.on.conestogac.kmarshalldlacelle.matchgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class ScoreActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{

    //Recycler view vars
    private RecyclerView scoreList;
    private RecyclerView.Adapter scoreAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<MatchObject> scores;
    private DrawerLayout drawer;
    private Toolbar toolbar;

    MatchGameApplication app;

    //declare shared preferences variable
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        ApplyTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        //drawer menu setup
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        UpdateToolbar();
        drawer= findViewById(R.id.scoreLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //setting our match game application as app in order to call database operations
        app = ((MatchGameApplication)getApplication());
        //get scores from our database
        scores = app.GetAllMatches();

        //find the recycler view, and create adapter and layout manager
        scoreList = findViewById(R.id.listScores);
        scoreAdapter = new ScoreAdapter(scores);
        layoutManager = new LinearLayoutManager(this); //want cards to display linearly

        scoreList.setHasFixedSize(true); // never updates while on activity; more efficient
        //assign adapter and layout manager to the recycler view
        scoreList.setLayoutManager(layoutManager);
        scoreList.setAdapter(scoreAdapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.nav_score:
                //startActivity(new Intent(getApplicationContext(), ScoreActivity.class));
                break;
            case R.id.nav_settings:
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
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