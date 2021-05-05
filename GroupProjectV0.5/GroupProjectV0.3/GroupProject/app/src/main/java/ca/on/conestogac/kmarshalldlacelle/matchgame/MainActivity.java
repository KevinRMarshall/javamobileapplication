package ca.on.conestogac.kmarshalldlacelle.matchgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.drm.DrmStore;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //drawer
    private DrawerLayout drawer;
    //set up imagebuttons
    private ImageButton imageButtonOne;
    private ImageButton imageButtonTwo;
    private ImageButton imageButtonThree;
    private ImageButton imageButtonFour;
    private ImageButton imageButtonFive;
    private ImageButton imageButtonSix;

    //set up button
    private Button  buttonPlay;
    private Button  buttonName;

    //set up textfield
    private EditText playerName;

    //Sound handler
    SoundHandler sh;
    //Arrays for imageView and ImageButtons for easier management
    ArrayList<ImageButton> cardFacades;
    ArrayList<Card> cards;

    //declare shared preferences variable
    private SharedPreferences sharedPref;

    //keep track of how many cards are in play
    ArrayList<Card> flipped;
    int movesUntilWin;

    //if false you cant flip another card, this is to avoid spamming cards. you need to wait for
    // the animation to finish until you can flip another card see method: SetUpButtonWithResID
    boolean canFlipCard;

    private boolean gameStarted = false;
    Toolbar toolbar;
    MatchGameApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        ApplyTheme();

        //setTheme(R.style.AppTheme);
        //setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //drawer menu setup
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        UpdateToolbar();
        drawer= findViewById(R.id.mainLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        app = ((MatchGameApplication)getApplication()); //to use db operations
        final AppCompatActivity SELF = this;

        //setting up soundeffects
        sh = new SoundHandler();
        final MediaPlayer soundEffect = MediaPlayer.create(this, R.raw.sound);
        sh.Add("soundEffect", soundEffect);
        final MediaPlayer startGame = MediaPlayer.create(this, R.raw.start);
        sh.Add("startGame", startGame);
        final MediaPlayer winGame = MediaPlayer.create(this, R.raw.winner);
        sh.Add("winGame", winGame);
        final MediaPlayer misMatch = MediaPlayer.create(this, R.raw.mismatch);
        sh.Add("misMatch", misMatch);
        final MediaPlayer match = MediaPlayer.create(this, R.raw.match);
        sh.Add("match", match);

        //getting imagebutton ids
        imageButtonOne = findViewById(R.id.cardOne);
        imageButtonTwo = findViewById(R.id.cardTwo);
        imageButtonThree = findViewById(R.id.cardThree);
        imageButtonFour= findViewById(R.id.cardFour);
        imageButtonFive = findViewById(R.id.cardFive);
        imageButtonSix = findViewById(R.id.cardSix);

        //get Arrays for imageView and ImageButtons for easier management
        cardFacades = new ArrayList<ImageButton>();
        cards = new ArrayList<Card>();
        flipped = new ArrayList<Card>();
        cardFacades.add(imageButtonOne);
        cardFacades.add(imageButtonTwo);
        cardFacades.add(imageButtonThree);
        cardFacades.add(imageButtonFour);
        cardFacades.add(imageButtonFive);
        cardFacades.add(imageButtonSix);

        //randomize the cards
        Random rand = new Random();
        StartGame();

        //floating action button
        final String currentScore = "Your current moves: ";
        FloatingActionButton fab = findViewById(R.id.fabScore);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(findViewById(android.R.id.content), currentScore+ String.valueOf(movesUntilWin), Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });

    }

    //this method connects a res id for an image with a card
    private void SetUpCard(Card card, MediaPlayer sound) {
        final int id = card.getImageId();
        final MediaPlayer fx = sound;
        final ImageButton b = card.getCard();
        final Card c = card;
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canFlipCard) {
                    canFlipCard = false;
                    fx.start();

                    b.setImageResource(id);
                    v.setEnabled(false);
                    v.setAlpha(0f);
                    v.animate().alpha(1f).setDuration(500).setListener(
                            new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                        CardsInPlay(c);
                                        canFlipCard = true;
                                }
                            }
                    );
                }
            }
        });
    }

    //check if the two cards are a match
    private void CheckIfMatch() {
        if (flipped.get(0).getImageId() == flipped.get(1).getImageId()) {
            //set the two cards property to match
            flipped.get(0).setMatched(true);
            flipped.get(1).setMatched(true);
            //check if card images are matching
            sh.getMedia("match").start();
            CheckIfWin();
        }
        else
        {
            //reverse cards back over
            ReverseUnmatchedCards(false);
            sh.getMedia("misMatch").start();
        }

    }

    //check to see if player is a winner
    private void CheckIfWin() {
        boolean allMatched = true;
        for (Card card: cards)
        {
            if(!card.isMatched()) {
                allMatched = false;
            }
        }
        if(allMatched) {
            String won = "YOU WON! Score: " + CalculateScore();
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), won, Snackbar.LENGTH_LONG);
            snackbar.show();
            //restart game
            sh.getMedia("winGame").start();

            //write the score to a file with users saved name default 'You' for now
            //get current date
            Date date = new Date();
            SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy");
            String name = sharedPref.getString("signature", "You");
            app.AddMatch(new MatchObject(CalculateScore(), df.format(date), name));
            StartGame();
        }
    }

    //count how many active cards are in play
    private void CardsInPlay(Card card) {
        flipped.add(card);

        if (flipped.size() >= 2) {
            movesUntilWin++;
            CheckIfMatch();
            flipped.clear();
        }
    }

    //reverses all cards that are not matched or all cards
    private void ReverseUnmatchedCards(boolean reverseAll) {
        for (Card card: cards)
        {
            if(reverseAll) {
                card.getCard().setImageResource(R.drawable.ic_launcher_foreground);
                card.getCard().setEnabled(true);
            }
            else {
                if(!card.isMatched()) {
                    card.getCard().setImageResource(R.drawable.ic_launcher_foreground);
                    card.getCard().setEnabled(true);
                }
                else {
                    card.getCard().setEnabled(false);
                }
            }
        }
    }

    //refreshes the board and starts the game
    private void StartGame() {
        ReverseUnmatchedCards(true);
        movesUntilWin = 0;
        canFlipCard = true;

        sh.getMedia("startGame").start();


        //randomize the cards and their locations
        int[] res = {R.drawable.ic_burger, R.drawable.ic_burger,
                R.drawable.ic_fries, R.drawable.ic_fries,
                R.drawable.ic_pizza, R.drawable.ic_pizza};
        Random rand = new Random();
        for (int i = 0; i < res.length; i++) {
            int randomIndexToSwap = rand.nextInt(res.length);
            int temp = res[randomIndexToSwap];
            res[randomIndexToSwap] = res[i];
            res[i] = temp;
        }

        //enable playing field and add on click listener to cards with res id
        int index = 0;
        for (ImageButton ib : cardFacades) {
            Card card = new Card(ib, res[index]);
            cards.add(card);
            SetUpCard(card, sh.getMedia("soundEffect"));
            index++;
        }
    }

    //onPause onResume
    @Override
    protected void onPause() {

        //write to storage
        SharedPreferences.Editor ed = sharedPref.edit();
        //save information
        ed.putInt("playerMoves", movesUntilWin);
        //using Gson to save ArrayList
        /*Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String json = gson.toJson(flipped);
        ed.putString("cards", json);*/
        //comit to storage
        ed.commit();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //retrieve back preferences
        movesUntilWin = sharedPref.getInt("playerMoves", movesUntilWin);
        /*Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String json = sharedPref.getString("cards", "");
        Type type = new TypeToken<ArrayList<ImageButton>>() {}.getType();
        flipped= gson.fromJson(json, type);*/
    }

    //calculate the score for a game
    private int CalculateScore() {
        double baseScore = 1000;
        baseScore  *=  (3 / (double)movesUntilWin);
        return (int)baseScore;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.nav_score:
                startActivity(new Intent(getApplicationContext(), ScoreActivity.class));
                break;
            case R.id.nav_settings:
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                break;
            case R.id.nav_game:
               // startActivity(new Intent(getApplicationContext(), MainActivity.class));
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
