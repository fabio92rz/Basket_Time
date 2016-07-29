package com.example.android.baskettime;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Fabio on 03/03/2016.
 */
public class GuestActivity extends HistoryActivity {

    RecyclerView rv;
    LinearLayoutManager llm;
    List<Games> matchList;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);
        setTitle("Partite Concluse");

        matchList = new ArrayList<>();

        rv = (RecyclerView) findViewById(R.id.recyclerView);
        rv.setHasFixedSize(true);

        llm = new LinearLayoutManager(GuestActivity.this);
        rv.setLayoutManager(llm);

        final RVAdapter rvAdapter = new RVAdapter(matchList, GuestActivity.this);
        rv.setAdapter(rvAdapter);

        //Inizializzo la Toolbar e la inserisco nell'actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        StringRequest gameRequest = new StringRequest(Request.Method.POST, ConfigActivity.ENTRY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject j = null;
                    j = new JSONObject(response);
                    JSONArray matches = j.getJSONArray(ConfigActivity.JSON_GAMES_TAG);

                    for (int i = 0; i < matches.length(); i++) {

                        JSONObject jsonObject = matches.getJSONObject(i);
                        Games games = new Games();

                        games.date = jsonObject.getString("day");
                        games.time = jsonObject.getString("time");
                        games.championship = jsonObject.getString(ConfigActivity.TAG_CHAMP_HIST);
                        games.teamHome = jsonObject.getString(ConfigActivity.TAG_HOME_TEAM_ID);
                        games.scoreHome = jsonObject.getInt(ConfigActivity.TAG_SCORE_HOME);
                        games.teamVisitor = jsonObject.getString(ConfigActivity.TAG_VISITOR_TEAM_ID);
                        games.scoreVisitor = jsonObject.getInt(ConfigActivity.TAG_SCORE_VISITOR);
                        games.quarter = jsonObject.getInt(ConfigActivity.TAG_QUARTER);
                        games.id_game = jsonObject.getInt(ConfigActivity.TAG_ID_GAME);

                        //Log.d("Prova idgame", "Partita numero=" + games.id_game);
                        //Log.d("Prova teamhome", "String=" + games.teamHome);
                        //Log.d("Prova teamvis", "String=" + games.teamVisitor);
                        //Log.d("Prova scorehome", "String=" + games.scoreHome);
                        //Log.d("Prova scoreVisitor", "String=" + games.scoreVisitor);

                        //champ.setText(champ1);
                        matchList.add(games);

                    }

                    rvAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                final String function = "getGames";
                params.put("f", function);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(gameRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                this.finish();

        }
        return true;
    }

}
