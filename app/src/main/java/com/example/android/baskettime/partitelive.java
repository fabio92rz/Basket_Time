package com.example.android.baskettime;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Fabio on 10/02/2016.
 */
public class partitelive extends AppCompatActivity implements View.OnClickListener {



    JSONArray games;
    ArrayList <String> livegamesHome;
    ArrayList <String> livegamesVisitor;
    ListView gameslist;
    CustomList adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history2);

        livegamesHome = new ArrayList<String>();
        livegamesVisitor = new ArrayList<String>();

        gameslist = (ListView) findViewById(R.id.game_list);
        getGames();

    }

    private void getGames() {
        StringRequest stringRequest = new StringRequest(ConfigActivity.GET_GAME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject j = null;

                        try {
                            j = new JSONObject(response);
                            games = j.getJSONArray(ConfigActivity.JSON_GAMES_TAG);

                            for (int i = 0; i < games.length(); i++) {

                                try {

                                    JSONObject json = games.getJSONObject(i);
                                    livegamesHome.add(json.getString(ConfigActivity.TAG_HOME_TEAM));
                                    livegamesVisitor.add(json.getString(ConfigActivity.TAG_VISITOR_TEAM));

                                    adapter = new CustomList(partitelive.this, livegamesHome, livegamesVisitor);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        gameslist.setAdapter(adapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {

    }
}
