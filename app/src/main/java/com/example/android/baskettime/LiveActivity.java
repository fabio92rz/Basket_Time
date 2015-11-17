package com.example.android.baskettime;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import java.lang.String;
import java.io.BufferedInputStream;
import java.io.IOException;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;


import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;

public class LiveActivity extends AppCompatActivity{

    int scoreTeamHm = 0, scoreTeamVis = 0, quarter = 1;
    Button start, plus, minus;
    EditText teamhome, teamvis;


    private DrawerLayout drawerLayout;
    private ListView mDrawerList;
    private String mActivityTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_activity);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();
        mDrawerList = (ListView) findViewById(R.id.drawer_list);
        mDrawerList.setOnItemClickListener(new selectItem());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        addDrawerItems();
        setupDrawer();
        displayForHm(0);
        displayForVis(0);

    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = drawerLayout.isDrawerOpen(mDrawerList);

        for (int index = 0; index < menu.size(); index++){
            MenuItem menuItem = menu.getItem(index);
            if (menuItem != null){
                menuItem.setVisible(!drawerOpen);
            }
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /** Gestione degli oggetti a schermo **/

    public void reset (EditText team, EditText team1){
        EditText teamhome = (EditText) findViewById(R.id.team_home_text);
        teamhome.setText(null);
        EditText teamVis = (EditText) findViewById(R.id.team_visitors_text);
        teamVis.setText(null);
    }

    public void stream (View v){
        Button start = (Button) findViewById(R.id.start_button);
    }

    public void stopStream (View v){
        Button stop = (Button) findViewById(R.id.stop_button);
    }

    public void displayForHm(int score) {
        TextView scoreView = (TextView) findViewById(R.id.score_team_home);
        scoreView.setText(String.valueOf(score));
    }

    public void displayForVis(int score) {
        TextView scoreView = (TextView) findViewById(R.id.score_team_visitor);
        scoreView.setText(String.valueOf(score));
    }

    public void displayForQuarter(String quart) {
        TextView quarterView = (TextView) findViewById(R.id.quarto_textView);
        quarterView.setText(String.valueOf(quart));
    }

    /** Gestione Live **/



    public void addOnePoint(View v) {
        scoreTeamHm += 1;
        displayForHm(scoreTeamHm);
    }

    public void addOnePointVis (View v){
        scoreTeamVis += 1;
        displayForVis(scoreTeamVis);
    }

    public void subOne(View v) {
        if (scoreTeamHm > 0)
            scoreTeamHm -= 1;
        displayForHm(scoreTeamHm);
    }

    public void subOnePointVis (View v){
        if (scoreTeamVis > 0)
            scoreTeamVis -= 1;
        displayForVis(scoreTeamVis);
    }

    public void addQuarter(View v) {
        if (quarter >= 1 && quarter <= 3)
            quarter += 1;
        displayForQuarter(quarter + "°");
    }

    public void subOneQuart(View v) {
        if (quarter > 1)
            quarter -= 1;
        displayForQuarter(quarter + "°");
    }

    /** Reset Punteggi **/

    public void resetScore(View v){
        scoreTeamHm=0;
        scoreTeamVis=0;
        quarter = 1;

        reset(teamhome, teamvis);
        displayForHm(scoreTeamHm);
        displayForVis(scoreTeamVis);
        displayForQuarter(quarter + "°");
    }

    /** Oggetto Drawer **/

    private void addDrawerItems(){
        String [] menu = {"Live !", "Storico partite", "Ecc.."};
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menu);
        mDrawerList.setAdapter(mAdapter);
    }

    private void setupDrawer(){
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerOpened(View drawerView){
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Account Personale");
                invalidateOptionsMenu();
            }
            public void onDrawerClosed(View view){
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu();
            }

        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(mDrawerToggle);
    }

    /** Gestione elementi nel Drawer List **/

    private class selectItem implements ListView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id){
            switch (pos) {
                case 0: {
                    Intent live = new Intent(LiveActivity.this, LiveActivity.class);
                    startActivity(live);
                    break;
                }
                case 1: {
                    Intent hist = new Intent(LiveActivity.this, HistoryActivity.class);
                    startActivity(hist);
                    break;
                }
            }
            drawerLayout.closeDrawer(mDrawerList);
        }
    }
}