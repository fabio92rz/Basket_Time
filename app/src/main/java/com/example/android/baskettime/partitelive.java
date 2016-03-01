package com.example.android.baskettime;

import android.content.Intent;
import android.graphics.ColorFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

/**
 * Created by Fabio on 10/02/2016.
 */
public class partitelive extends AppCompatActivity implements View.OnClickListener, ListView.OnItemSelectedListener {

    private List<Match> matchList = new ArrayList<Match>();
    private ListView gameslist;
    private CustomList adapter;
    public Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history2);
        setTitle("Storico Partite");

        //Inizializzo la Toolbar e la inserisco nell'actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        gameslist = (ListView) findViewById(R.id.game_list);
        adapter = new CustomList(this, matchList);


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
