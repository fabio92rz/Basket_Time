package com.example.android.baskettime;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by fabio on 11/11/2015.
 */
public class HistoryActivity extends AppCompatActivity implements View.OnClickListener, ListView.OnItemSelectedListener {

    //TextView per i dati dell'utente
    private TextView tvEmail;
    private TextView tvNameSurname;

    //Bottone per il logout
    private Button logoutButton;
    private FloatingActionButton newgame;

    private List<Match> matchList = new ArrayList<Match>();
    private ListView gameslist;
    private CustomList adapter;


    /**private LinearLayout mLayout0;
    private LinearLayout mLayout1;
    private LinearLayout mLayout2;**/

    private Toolbar toolbar;
    private NavigationView navigationView;
    protected DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history2);
        setTitle("Storico Partite");


        //Creo un inflater per inflazionare il layout dell'header
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        gameslist = (ListView) findViewById(R.id.game_list);
        adapter = new CustomList(this, matchList);

        //Inizializzo il Bottone per il logout
        logoutButton = (Button) findViewById(R.id.logout_button);
        newgame = (FloatingActionButton) findViewById(R.id.new_game);
        logoutButton.setOnClickListener(this);
        newgame.setOnClickListener(this);


        //Inizializzo la Toolbar e la inserisco nell'actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Catturo i dati e li inserisco nell'header
        SharedPreferences sharedPreferences = getSharedPreferences(ConfigActivity.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String email = sharedPreferences.getString(ConfigActivity.EMAIL_SHARED_PREF, "Not Available");
        String nameSurname = sharedPreferences.getString(ConfigActivity.NAME_SURNAME_PREF, "Not Available");

        //Inizializzo la NavigationView, utilizzata per il drawer
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        //Inflaziono i layout in modo tale da mostralo nella Navigation View
        View vi = inflater.inflate(R.layout.header, navigationView, false);

        ImageView background = (ImageView) vi.findViewById(R.id.header_image);
        Picasso.with(this).load("http://i.imgur.com/bKFyqyE.jpg").into(background);


        //Inizializzo ed imposto la mail della persona loggata
        tvEmail = (TextView) vi.findViewById(R.id.email_header);
        tvEmail.setText(email);

        tvNameSurname = (TextView) vi.findViewById(R.id.username_header);
        tvNameSurname.setText(nameSurname);

        //Aggiungo la View
        navigationView.addHeaderView(vi);

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
                        Intent live = new Intent(HistoryActivity.this, LiveActivity.class);
                        startActivity(live);
                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                        break;

                    case R.id.storico_partite:
                        Intent history = new Intent(HistoryActivity.this, HistoryActivity.class);
                        startActivity(history);
                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                        break;

                    case R.id.insert_championship:
                        Intent championship = new Intent(HistoryActivity.this, ChampActivity.class);
                        startActivity(championship);
                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

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
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //Chiamo syncState per far comparire il toggle
        actionBarDrawerToggle.syncState();

        //Inizializzo i tre layout
        /**mLayout0 = (LinearLayout) findViewById(R.id.linear_layout0);
        mLayout1 = (LinearLayout) findViewById(R.id.linear_layout1);
        mLayout2 = (LinearLayout) findViewById(R.id.linear_layout2);


        //Imposto il click per il layout per aprire il popup

        mLayout0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HistoryActivity.this, Popup0Activity.class));
            }
        });

        mLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HistoryActivity.this, Popup0Activity.class));
            }
        });

        mLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HistoryActivity.this, Popup0Activity.class));
            }
        });**/

        StringRequest matchReq = new StringRequest(ConfigActivity.GET_GAME, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject j = null;

                try {

                    j = new JSONObject(response);

                    JSONArray matches = j.getJSONArray(ConfigActivity.JSON_GAMES_TAG);
                    ArrayList<String> teamHome = new ArrayList<String>();
                    ArrayList<String> teamVis = new ArrayList<String>();
                    ArrayList<Integer> scoreTeamHome = new ArrayList<Integer>();
                    ArrayList<Integer> scoreTeamVis = new ArrayList<Integer>();

                    for (int i = 0; i<matches.length(); i++){

                        JSONObject json = matches.getJSONObject(i);

                        teamHome.add(json.getString(ConfigActivity.TAG_HOME_TEAM_ID));
                        teamVis.add(json.getString(ConfigActivity.TAG_VISITOR_TEAM_ID));
                        scoreTeamHome.add(json.getInt(ConfigActivity.TAG_SCORE_HOME));
                        scoreTeamVis.add(json.getInt(ConfigActivity.TAG_SCORE_VISITOR));


                        Log.d("Squadra Casalinga", "squadra " + teamHome);
                        Log.d("Squadra Ospite", "squadra " + teamVis);
                        Log.d("Punteggio squadra Casa", "punteggio " + scoreTeamHome);
                        Log.d("Punteggio squadra Ospi", "punteggio " + scoreTeamVis);
                    }

                    Match match = new Match(teamHome, teamVis, scoreTeamHome, scoreTeamVis);

                    match.setHomeTeam(teamHome);
                    match.setScoreHome(scoreTeamHome);
                    match.setVisitorTeam(teamVis);
                    match.setScoreVisitors(scoreTeamVis);

                    matchList.add(match);

                    Log.d("prova match", "match =" + matchList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                gameslist.setAdapter(adapter);

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(matchReq);

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

                        //Setto il valore Booleano a Falso
                        editor.putBoolean(ConfigActivity.LOGGEDIN_SHARED_PREF, false);

                        //Metto un valore vuto nella mail
                        editor.putString(ConfigActivity.EMAIL_SHARED_PREF, "");

                        //Salvo le SharedPreference
                        editor.commit();

                        //Faccio partire la LoginActivity
                        Intent intent = new Intent(HistoryActivity.this, LoginActivity.class);
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


    //Elimina gli effetti di transizione
    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        if (v == logoutButton) {
            logout();
        }

        if (v == newgame) {
            Intent newgame = new Intent(HistoryActivity.this, NewGameActivity.class);
            startActivity(newgame);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}


