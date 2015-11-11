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

    int scoreTeamFp = 0;
    int scoreTeamVis = 0;
    int quarter = 1;
    Button start;
    EditText teamhome;
    EditText teamvis;


    private DrawerLayout drawerLayout;
    private ListView listView;
    private String mActivityTitle;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_activity);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();
        listView = (ListView) findViewById(R.id.drawer_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        setupDrawer();
        displayForFp(0);
        displayForVis(0);
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

    public void resethome (EditText team){
        EditText teamhome = (EditText) findViewById(R.id.team_home_text);
        teamhome.setText(null);
    }

    public void resetvisit (EditText team){
        EditText teamVis = (EditText) findViewById(R.id.team_visitors_text);
        teamVis.setText(null);
    }

    public void stream (View v){
        Button start = (Button) findViewById(R.id.start_button);

    }

    public void displayForFp(int score) {
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
        scoreTeamFp += 1;
        displayForFp(scoreTeamFp);
    }

    public void addOnePointVis(View v) {
        scoreTeamVis += 1;
        displayForVis(scoreTeamVis);
    }

    public void addQuarter(View v) {
        if (quarter >= 1 && quarter <= 3)
            quarter += 1;
        displayForQuarter(quarter + "°");
    }

    public void subOne(View v) {
        if (scoreTeamFp > 0)
            scoreTeamFp -= 1;
        displayForFp(scoreTeamFp);
    }

    public void subOneVis(View v) {
        if (scoreTeamVis > 0)
            scoreTeamVis -= 1;
        displayForVis(scoreTeamVis);
    }

    public void subOneQuart(View v) {
        if (quarter > 1)
            quarter -= 1;
        displayForQuarter(quarter + "°");
    }

    /** Reset Punteggi **/

    public void resetScore(View v){
        scoreTeamFp=0;
        scoreTeamVis=0;
        quarter = 1;

        resethome(teamhome);
        resetvisit(teamvis);
        displayForFp(scoreTeamFp);
        displayForVis(scoreTeamVis);
        displayForQuarter(quarter + "°");
    }

    /** Oggetto Drawer **/

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
}