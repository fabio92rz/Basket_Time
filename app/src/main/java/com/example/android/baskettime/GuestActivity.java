package com.example.android.baskettime;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fabio on 03/03/2016.
 */
public class GuestActivity extends HistoryActivity {

    private List<Match> matchList = new ArrayList<Match>();
    private ListView gameslist;
    private CustomList adapter;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setTitle("Partite Concluse");

        gameslist = (ListView) findViewById(R.id.game_list);
        adapter = new CustomList(this, matchList);

        //Inizializzo la Toolbar e la inserisco nell'actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                this.finish();
                overridePendingTransition(R.anim.push_out_right, R.anim.pull_in_left);

        }
        return true;
    }

}
