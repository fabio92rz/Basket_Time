package com.example.android.baskettime;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * Created by fabio on 11/11/2015.
 */
public class HistoryActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ListView mDrawerList;
    private String mActivityTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayAdapter<String> mAdapter;
    private LinearLayout mLayout0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        mLayout0 = (LinearLayout) findViewById(R.id.linear_layout0);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();
        mDrawerList = (ListView) findViewById(R.id.drawer_list);
        mDrawerList.setOnItemClickListener(new selectItem());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        addDrawerItems();
        setupDrawer();

        mLayout0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HistoryActivity.this, Popup0Activity.class));
            }
        });
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

    /** Gestione scelta nel Drawer List **/

    private class selectItem implements ListView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id){
            switch (pos) {
                case 0: {
                    Intent live = new Intent(HistoryActivity.this, LiveActivity.class);
                    startActivity(live);
                    break;
                }
                case 1: {
                    Intent hist = new Intent(HistoryActivity.this, HistoryActivity.class);
                    startActivity(hist);
                    break;
                }
            }
            drawerLayout.closeDrawer(mDrawerList);
        }
    }
}


