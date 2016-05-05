package com.example.android.baskettime;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Config;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.String;
import java.io.BufferedInputStream;
import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import biz.kasual.materialnumberpicker.MaterialNumberPicker;
import de.hdodenhof.circleimageview.CircleImageView;

import android.graphics.Matrix;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ClearCacheRequest;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.vi.swipenumberpicker.OnValueChangeListener;
import com.vi.swipenumberpicker.SwipeNumberPicker;

public class LiveActivity extends AppCompatActivity implements View.OnClickListener {

    int scoreTeamHm = 0, scoreTeamVis = 0, quarter = 1;

    //TextView per i dati dell'utente
    private TextView tvEmail;
    private TextView tvNameSurname;

    private SwipeNumberPicker quarterPicker;
    private SwipeNumberPicker homePicker;
    private SwipeNumberPicker visitorPicker;

    //Bottone per il logout
    private Button logoutButton;
    private Button updateResult;
    private Button endGame;

    //Varibili per la Navigation View
    private Toolbar toolbar;
    private NavigationView navigationView;
    protected DrawerLayout drawerLayout;

    private String teamHome = "";
    private String teamVisitor = "";
    private String time = "";
    private String championship = "";
    private TextView teamhome;
    private TextView teamvis;
    private TextView teamHomeRecap;
    private TextView teamVisitorRecap;
    private TextView scoreHomeRecap;
    private TextView scoreVisitorRecap;
    private TextView championshipRecap;
    private TextView timeRecap;
    private TextView quarterRecap;
    private TextView quarterView;
    private TextView scoreView;
    private TextView scoreViewVisitor;
    private CircleImageView profilePicture;
    final String function = "insertQuarter";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_activity);
        setTitle("Partita Live !");


        //Creo un inflater per inflazionare il layout dell'header
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Inizializzo la Toolbar e la inserisco nell'actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Inizializzo il Bottone per il logout
        logoutButton = (Button) findViewById(R.id.logout_button);
        updateResult = (Button) findViewById(R.id.start_button);
        endGame = (Button) findViewById(R.id.stop_button);
        logoutButton.setOnClickListener(this);
        updateResult.setOnClickListener(this);
        endGame.setOnClickListener(this);

        //Inizializzo le textView
        teamhome = (TextView) findViewById(R.id.team_home_text);
        teamvis = (TextView) findViewById(R.id.team_visitors_text);

        teamHomeRecap = (TextView) findViewById(R.id.teamHome_cv_live);
        teamVisitorRecap = (TextView) findViewById(R.id.teamVisitor_cv_live);

        quarterRecap = (TextView) findViewById(R.id.actual_quarter);

        assert quarterRecap != null;
        quarterRecap.setText("1°");

        scoreHomeRecap = (TextView) findViewById(R.id.scoreHome_cv_live);
        scoreVisitorRecap = (TextView) findViewById(R.id.scoreVisitor_cv_live);

        timeRecap = (TextView) findViewById(R.id.time_cv_live);
        championshipRecap = (TextView) findViewById(R.id.championship_cv_live);


        quarterPicker = (SwipeNumberPicker) findViewById(R.id.quarterPicker);
        homePicker = (SwipeNumberPicker) findViewById(R.id.homePicker);
        visitorPicker = (SwipeNumberPicker) findViewById(R.id.visitorPicker);

        //Catturo i dati e li inserisco nell'header
        SharedPreferences sharedPreferences = getSharedPreferences(ConfigActivity.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String email = sharedPreferences.getString(ConfigActivity.EMAIL_SHARED_PREF, "Not Available");
        String nameSurname = sharedPreferences.getString(ConfigActivity.NAME_SURNAME_PREF, "Not Available");
        String profilePic = sharedPreferences.getString(ConfigActivity.SERVER_PATH, "");

        Log.d("Live Activity", "getString()" + email);

        //Inizializzo la NavigationView, utilizzata per il drawer
        navigationView = (NavigationView) findViewById(R.id.navigation_view);


        //Inflaziono i layout in modo tale da mostralo nella Navigation View
        View vi = inflater.inflate(R.layout.header, navigationView, false);

        ImageView background = (ImageView) vi.findViewById(R.id.header_image);
        Picasso.with(this).load("http://i.imgur.com/6etUX3l.jpg").into(background);

        //Inizializzo ed imposto la mail della persona loggata
        tvEmail = (TextView) vi.findViewById(R.id.email_header);
        tvEmail.setText(email);

        tvNameSurname = (TextView) vi.findViewById(R.id.username_header);
        tvNameSurname.setText(nameSurname);

        quarterView = (TextView) findViewById(R.id.quarto_textView);
        //quarterView.setText(String.valueOf(quarter) + "°");

        scoreView = (TextView) findViewById(R.id.score_team_home);
        //scoreView.setText(String.valueOf(scoreTeamHm));

        scoreViewVisitor = (TextView) findViewById(R.id.score_team_visitor);
        //scoreView.setText(String.valueOf(scoreTeamVis));

        //Aggiungo la View
        navigationView.addHeaderView(vi);

        profilePicture = (CircleImageView) vi.findViewById(R.id.profile_image);
        profilePicture.setOnClickListener(null);


        if (!"".equalsIgnoreCase(profilePic)) {
            Picasso.with(this)
                    .load(profilePic)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .placeholder(R.drawable.account_circle)
                    .into(profilePicture);
        }

        quarterPicker.setOnValueChangeListener(new OnValueChangeListener() {
            @Override
            public boolean onValueChange(SwipeNumberPicker view, int oldValue, int newValue) {

                if (newValue >= 5) {

                    //quarterPicker.setText(String.valueOf("1° OT"));

                }else {

                    quarterPicker.setText(String.valueOf(newValue + "°"));

                }
                return true;
            }
        });

        homePicker.setOnValueChangeListener(new OnValueChangeListener() {
            @Override
            public boolean onValueChange(SwipeNumberPicker view, int oldValue, int newValue) {

                homePicker.setText(String.valueOf(newValue));
                return true;
            }
        });

        visitorPicker.setOnValueChangeListener(new OnValueChangeListener() {
            @Override
            public boolean onValueChange(SwipeNumberPicker view, int oldValue, int newValue) {

                visitorPicker.setText(String.valueOf(newValue));
                return true;
            }
        });

        //Imposto la NavigationView con un clicklistener per gestire gli eventi della navigazione del menù **/
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            //Questo metodo gestisce i click della navigazione
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Controlla se l'elemento è segnato o no, sennò lo segna
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Chiude il drawer dopo un click
                drawerLayout.closeDrawers();

                //Controlla quale click è stato fatto e esegue la giusta operazione
                switch (menuItem.getItemId()) {

                    case R.id.live:
                        break;

                    case R.id.storico_partite:
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent history = new Intent(LiveActivity.this, HistoryActivity.class);
                                startActivity(history);
                            }
                        }, 340);
                        break;

                    case R.id.insert_championship:
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent championship = new Intent(LiveActivity.this, ChampActivity.class);
                                startActivity(championship);
                            }
                        }, 340);
                        break;
                }
                return true;
            }
        });

        //Inizializza il drawer layout e il Toggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {

                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {

                super.onDrawerClosed(drawerView);
            }
        };

        //Imposto il toggle e lo indirizzio al drawer
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        //Chiamo syncState per far comparire il toggle
        actionBarDrawerToggle.syncState();
        getMatch();

    }

    private void getMatch() {

        SharedPreferences sharedPreferences1 = getSharedPreferences(ConfigActivity.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences2 = getSharedPreferences(ConfigActivity.TAG_ID_GAME, Context.MODE_PRIVATE);

        final String id_game = String.valueOf(sharedPreferences2.getInt(ConfigActivity.ID_GAME, 0));
        final String idSession = sharedPreferences1.getString(ConfigActivity.SESSION_ID, "");
        final String function = "getGames";

        Log.d("Live Activity", "valore id_game" + id_game);
        Log.d("Live Activity", "Sessione numero: " + idSession);

        class getMatch extends AsyncTask<Void, Void, String> {


            @Override
            protected String doInBackground(Void... params) {

                HashMap<String, String> param = new HashMap<>();
                param.put(ConfigActivity.KEY_MATCH_ID, id_game);
                param.put(ConfigActivity.KEY_ID_SESSION, idSession);
                param.put("f", function);

                RequestHandler requestHandler = new RequestHandler();
                String res = requestHandler.sendPostRequest(ConfigActivity.ENTRY, param);

                JSONObject jsonObject = null;
                JSONArray matchDetails = null;

                try {

                    jsonObject = new JSONObject(res);
                    matchDetails = jsonObject.getJSONArray(ConfigActivity.TAG_SINGLE_MATCH);

                    for (int i = 0; i < matchDetails.length(); i++) {

                        JSONObject teams = matchDetails.getJSONObject(i);

                        teamHome = teams.getString(ConfigActivity.TAG_CURRENT_TEAM_HOME);
                        teamVisitor = teams.getString(ConfigActivity.TAG_CURRENT_TEAM_VISITOR);
                        time = teams.getString("time");
                        championship = teams.getString("champ");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return res;
            }

            @Override
            protected void onPreExecute() {
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                Log.d("Prova teamHome", "TeamHome = " + teamHome);

                teamhome.setText(teamHome);
                teamvis.setText(teamVisitor);
                teamHomeRecap.setText(teamHome);
                teamVisitorRecap.setText(teamVisitor);
                championshipRecap.setText(championship);
                timeRecap.setText(time);

            }
        }

        getMatch gt = new getMatch();
        gt.execute();
    }

    private void insertQuarter() {

        final SharedPreferences sharedPreferences1 = getSharedPreferences(ConfigActivity.TAG_ID_GAME, Context.MODE_PRIVATE);

        final String quarter = String.valueOf(quarterPicker.getValue());
        final String homeScore = String.valueOf(homePicker.getValue());
        final String visitorScore = String.valueOf(visitorPicker.getValue());
        final String id_game = String.valueOf(sharedPreferences1.getInt(ConfigActivity.ID_GAME, 0));

        Log.d("String get quarter", "=" + quarter);

        class insertQuarter extends AsyncTask<Void, Void, String> {


            @Override
            protected String doInBackground(Void... params) {

                HashMap<String, String> param = new HashMap<>();
                param.put(ConfigActivity.KEY_SCORE_QUARTER, quarter);
                param.put(ConfigActivity.KEY_SCORE_HOME_TEAM, homeScore);
                param.put(ConfigActivity.KEY_SCORE_VISITOR_TEAM, visitorScore);
                param.put(ConfigActivity.KEY_ID_CURRENT_MATCH, id_game);
                param.put(ConfigActivity.KEY_ID_SESSION, sharedPreferences1.getString(ConfigActivity.SESSION_ID, ""));
                param.put("f", function);


                RequestHandler requestHandler = new RequestHandler();
                String res = requestHandler.sendPostRequest(ConfigActivity.ENTRY, param);

                return res;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                JSONObject j = null;
                String uploadOk = "";
                String actualQuarter = "";
                String actualScoreHome = "";
                String actualScoreVisitor = "";

                try {

                    j = new JSONObject(s);
                    JSONArray upload = j.getJSONArray(ConfigActivity.TAG_QUARTER);

                    for (int i = 0; i < upload.length(); i++) {

                        try {

                            JSONObject jsonObject = upload.getJSONObject(i);
                            uploadOk = jsonObject.getString(ConfigActivity.TAG_STATUS);
                            actualQuarter = jsonObject.getString("nquarter");
                            actualScoreHome = jsonObject.getString("score_home_team");
                            actualScoreVisitor = jsonObject.getString("score_visitor_team");



                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                }

                quarterRecap.setText(actualQuarter + "°");
                scoreHomeRecap.setText(actualScoreHome);
                scoreVisitorRecap.setText(actualScoreVisitor);
                Toast.makeText(LiveActivity.this, uploadOk, Toast.LENGTH_LONG).show();

            }
        }

        insertQuarter iq = new insertQuarter();
        iq.execute();
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Gestione Live

    public void addOnePoint(View v) {
        scoreTeamHm += 1;
        scoreView.setText(String.valueOf(scoreTeamHm));
    }

    public void addOnePointVis(View v) {
        scoreTeamVis += 1;
        scoreViewVisitor.setText(String.valueOf(scoreTeamVis));
    }

    public void subOne(View v) {
        if (scoreTeamHm > 0)
            scoreTeamHm -= 1;
        scoreView.setText(String.valueOf(scoreTeamHm));
    }

    public void subOnePointVis(View v) {
        if (scoreTeamVis > 0)
            scoreTeamVis -= 1;
        scoreViewVisitor.setText(String.valueOf(scoreTeamVis));
    }

    public void addQuarter(View v) {
        quarter += 1;
        quarterView.setText(String.valueOf(quarter) + "°");
    }

    public void subOneQuart(View v) {
        if (quarter > 1)
            quarter -= 1;
        quarterView.setText(String.valueOf(quarter) + "°");
    }

    //Reset Punteggi

    public void resetScore(View v) {
        scoreTeamHm = 0;
        scoreTeamVis = 0;
        quarter = 1;

        scoreView.setText(String.valueOf(scoreTeamHm));
        scoreViewVisitor.setText(String.valueOf(scoreTeamVis));
        quarterView.setText(String.valueOf(quarter) + "°");
    }

    //Funzione Logout
    private void logout() {
        //Credo un dialogo di allerta per confermare il logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Sei sicuro di voler uscire?");
        alertDialogBuilder.setPositiveButton("Si",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        //Rimuovo le SharedPreference
                        SharedPreferences preferences = getSharedPreferences(ConfigActivity.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                        //Prendo l'editor
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();

                        editor.apply();

                        //Faccio partire la LoginActivity
                        Intent intent = new Intent(LiveActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = LiveActivity.this.getSharedPreferences(ConfigActivity.SHARED_PREF_NAME, MODE_PRIVATE);
        boolean profilePictureBoolean = sharedPreferences.getBoolean(ConfigActivity.PROFILE_PIC_BOOLEAN, false);

        if (profilePictureBoolean) {

            String profilePic = "";
            profilePic = sharedPreferences.getString(ConfigActivity.SERVER_PATH, "");
            Log.d("Prova ProfilePic", "Path: " + profilePic);

            View header = navigationView.getHeaderView(0);
            CircleImageView profilePict = (CircleImageView) header.findViewById(R.id.profile_image);

            Picasso.with(this)
                    .load(profilePic)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .placeholder(R.drawable.account_circle)
                    .into(profilePict);
        }
    }

    @Override
    public void onClick(View v) {

        if (v == logoutButton) {
            logout();
        }

        if (v == updateResult) {
            insertQuarter();
        }

        if (v == endGame) {
            endGame();
        }
    }

    private void endGame() {

        //Credo un dialogo di allerta per confermare il logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Partita terminata?");
        alertDialogBuilder.setPositiveButton("Si",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        //Faccio partire la LoginActivity
                        Intent intent = new Intent(LiveActivity.this, HistoryActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
