package com.example.android.baskettime;

import android.graphics.ColorFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
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
import java.util.List;

/**
 * Created by Fabio on 10/02/2016.
 */
public class partitelive extends AppCompatActivity implements View.OnClickListener {

    private List<Match> matchList = new ArrayList<Match>();
    private ListView gameslist;
    private CustomList adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history2);

        gameslist = (ListView) findViewById(R.id.game_list);
        adapter = new CustomList(this, matchList);
        gameslist.setAdapter(adapter);

        StringRequest matchReq = new StringRequest(ConfigActivity.GET_GAME, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject j = null;

                    try {

                        j = new JSONObject(response);
                        Match match = new Match();

                        JSONArray matches = j.getJSONArray(ConfigActivity.JSON_GAMES_TAG);
                        ArrayList<String> teamHome = new ArrayList<String>();
                        ArrayList<String> teamVis = new ArrayList<String>();

                        for (int i = 0; i<matches.length(); i++){

                            teamHome.add((String) matches.get(ConfigActivity.TAG_HOME_TEAM)));
                            teamVis.add((String) matches.get(Integer.parseInt(ConfigActivity.TAG_VISITOR_TEAM)));
                        }

                        match.setHomeTeam(teamHome);
                        match.setVisitorTeam(teamVis);

                        matchList.add(match);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
}
