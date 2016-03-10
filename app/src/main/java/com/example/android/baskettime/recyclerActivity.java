package com.example.android.baskettime;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

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
 * Created by Fabio on 10/03/2016.
 */
public class recyclerActivity extends AppCompatActivity {

    RecyclerView rv;
    LinearLayoutManager llm;
    List <Games> matchList;
    TextView champ;
    private Toolbar mtoolbar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        setTitle("Storico Partite");

        matchList = new ArrayList<>();

        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);

        champ = (TextView) findViewById(R.id.championship);


        rv = (RecyclerView) findViewById(R.id.recyclerView);
        rv.setHasFixedSize(true);

        llm = new LinearLayoutManager(recyclerActivity.this);
        rv.setLayoutManager(llm);

        final RVAdapter rvAdapter = new RVAdapter(matchList);
        rv.setAdapter(rvAdapter);

        StringRequest jsonArrayRequest = new StringRequest(ConfigActivity.GET_GAME, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject j = null;
                    j = new JSONObject(response);
                    String champ1 = "";
                    JSONArray matches = j.getJSONArray(ConfigActivity.JSON_GAMES_TAG);

                    for (int i = 0; i < matches.length(); i++) {

                        JSONObject jsonObject = matches.getJSONObject(i);
                        Games games = new Games();

                        champ1 = jsonObject.getString(ConfigActivity.TAG_CHAMP_HIST);
                        games.teamHome = jsonObject.getString(ConfigActivity.TAG_HOME_TEAM_ID);
                        games.scoreHome = jsonObject.getInt(ConfigActivity.TAG_SCORE_HOME);
                        games.teamVisitor = jsonObject.getString(ConfigActivity.TAG_VISITOR_TEAM_ID);
                        games.scoreVisitor = jsonObject.getInt(ConfigActivity.TAG_SCORE_VISITOR);

                        Log.d("Prova teamhome", "String=" +games.teamHome);
                        Log.d("Prova teamvis", "String=" +games.teamVisitor);
                        Log.d("Prova scorehome", "String=" +games.scoreHome);
                        Log.d("Prova scoreVisitor", "String=" +games.scoreVisitor);

                        champ.setText(champ1);
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
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(jsonArrayRequest);
    }
}
