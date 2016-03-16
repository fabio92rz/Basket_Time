package com.example.android.baskettime;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Config;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.String;
import java.io.BufferedInputStream;
import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import android.graphics.Matrix;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

public class LiveActivity extends AppCompatActivity implements View.OnClickListener {

    int scoreTeamHm = 0, scoreTeamVis = 0, quarter = 1, imageHeight, imageWidth;

    //TextView per i dati dell'utente
    private TextView tvEmail;
    private TextView tvNameSurname;

    //Bottone per il logout
    private Button logoutButton;
    private Button updateResult;

    //Variabili per lo scaling dell'immagine
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;

    //Varibili per la Navigation View
    private Toolbar toolbar;
    private NavigationView navigationView;
    protected DrawerLayout drawerLayout;

    private String teamHome = "";
    private String teamVisitor = "";
    private TextView teamhome;
    private TextView teamvis;
    private TextView quarterView;
    private TextView scoreView;
    private TextView scoreViewVisitor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_activity);
        setTitle("Partita Live !");


        //Creo un inflater per inflazionare il layout dell'header
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Inizializzo la Toolbar e la inserisco nell'actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Inizializzo il Bottone per il logout
        logoutButton = (Button) findViewById(R.id.logout_button);
        updateResult = (Button) findViewById(R.id.start_button);
        logoutButton.setOnClickListener(this);
        updateResult.setOnClickListener(this);

        //Inizializzo le textView
        teamhome = (TextView) findViewById(R.id.team_home_text);
        teamvis = (TextView) findViewById(R.id.team_visitors_text);

        //Catturo i dati e li inserisco nell'header
        SharedPreferences sharedPreferences = getSharedPreferences(ConfigActivity.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String email = sharedPreferences.getString(ConfigActivity.EMAIL_SHARED_PREF, "Not Available");
        String nameSurname = sharedPreferences.getString(ConfigActivity.NAME_SURNAME_PREF, "Not Available");
        Log.d("Live Activity", "getString()" + email);

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

        quarterView = (TextView) findViewById(R.id.quarto_textView);
        quarterView.setText(String.valueOf(quarter) + "°");

        scoreView = (TextView) findViewById(R.id.score_team_home);
        scoreView.setText(String.valueOf(scoreTeamHm));

        scoreViewVisitor = (TextView) findViewById(R.id.score_team_visitor);
        scoreView.setText(String.valueOf(scoreTeamVis));

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
                        Intent live = new Intent(LiveActivity.this, LiveActivity.class);
                        startActivity(live);
                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                        break;

                    case R.id.storico_partite:
                        Intent history = new Intent(LiveActivity.this, HistoryActivity.class);
                        startActivity(history);
                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                        break;

                    case R.id.insert_championship:
                        Intent championship = new Intent(LiveActivity.this, ChampActivity.class);
                        startActivity(championship);
                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                        break;
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

        getMatch();

    }

    private void getMatch() {

        SharedPreferences sharedPreferences1 = getSharedPreferences(ConfigActivity.TAG_ID_GAME, Context.MODE_PRIVATE);
        final String id_game = String.valueOf(sharedPreferences1.getInt(ConfigActivity.ID_GAME, 0));
        Log.d("Live Activity", "valore id_game" + id_game);

        class getMatch extends AsyncTask<Void, Void, String> {


            @Override
            protected String doInBackground(Void... params) {

                HashMap<String, String> param = new HashMap<>();
                param.put(ConfigActivity.KEY_MATCH_ID, id_game);

                RequestHandler requestHandler = new RequestHandler();
                String res = requestHandler.sendPostRequest(ConfigActivity.GET_GAME, param);

                JSONObject jsonObject = null;
                JSONArray matchDetails = null;

                try {

                    jsonObject = new JSONObject(res);
                    matchDetails = jsonObject.getJSONArray(ConfigActivity.TAG_SINGLE_MATCH);

                    for (int i = 0; i < matchDetails.length(); i++) {

                        try {

                            JSONObject json = matchDetails.getJSONObject(i);

                            teamHome = json.getString(ConfigActivity.TAG_CURRENT_TEAM_HOME);
                            teamVisitor = json.getString(ConfigActivity.TAG_CURRENT_TEAM_VISITOR);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return res;
            }

            @Override
            protected void onPreExecute() {
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                teamhome.setText(teamHome);
                teamvis.setText(teamVisitor);

            }
        }

        getMatch gt = new getMatch();
        gt.execute();
    }

    private void insertQuarter() {

        SharedPreferences sharedPreferences1 = getSharedPreferences(ConfigActivity.TAG_ID_GAME, Context.MODE_PRIVATE);

        final String getQuarter = String.valueOf(quarterView.getText().toString());
        final String getHomeScore = String.valueOf(scoreView.getText().toString());
        final String getVisitorScore = String.valueOf(scoreViewVisitor.getText().toString());
        final String id_game = String.valueOf(sharedPreferences1.getInt(ConfigActivity.ID_GAME, 0));

        Log.d("String get quarter", "=" + getQuarter);

        class insertQuarter extends AsyncTask<Void, Void, String> {


            @Override
            protected String doInBackground(Void... params) {

                HashMap<String, String> param = new HashMap<>();
                param.put(ConfigActivity.KEY_SCORE_QUARTER, getQuarter);
                param.put(ConfigActivity.KEY_SCORE_HOME_TEAM, getHomeScore);
                param.put(ConfigActivity.KEY_SCORE_VISITOR_TEAM, getVisitorScore);
                param.put(ConfigActivity.KEY_ID_CURRENT_MATCH, id_game);


                RequestHandler requestHandler = new RequestHandler();
                String res = requestHandler.sendPostRequest(ConfigActivity.INSERT_QUARTER, param);

                return res;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                JSONObject j = null;
                String uploadOk = "";

                try {

                    j = new JSONObject(s);
                    JSONArray upload = j.getJSONArray(ConfigActivity.TAG_QUARTER);

                    for (int i = 0; i<upload.length(); i++){

                        try {

                            JSONObject jsonObject = upload.getJSONObject(i);
                            uploadOk = jsonObject.getString(ConfigActivity.TAG_STATUS);

                        } catch (JSONException e){

                            e.printStackTrace();
                        }
                    }

                }catch (JSONException e){

                    e.printStackTrace();
                }

                Toast.makeText(LiveActivity.this, uploadOk, Toast.LENGTH_LONG).show();

            }
        }

        insertQuarter iq = new insertQuarter();
        iq.execute();
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Gestione Live

    public void addOnePoint(View v) {
        scoreTeamHm += 1;
        scoreView.setText(String.valueOf(scoreTeamHm));
    }

    public void addOnePointVis(View v) {
        scoreTeamVis += 1;
        scoreViewVisitor.setText(String.valueOf(scoreTeamVis));
    }

    public void subOne(View v) {
        if (scoreTeamHm > 0)
            scoreTeamHm -= 1;
        scoreView.setText(String.valueOf(scoreTeamHm));
    }

    public void subOnePointVis(View v) {
        if (scoreTeamVis > 0)
            scoreTeamVis -= 1;
        scoreViewVisitor.setText(String.valueOf(scoreTeamVis));
    }

    public void addQuarter(View v) {
            quarter += 1;
        quarterView.setText(String.valueOf(quarter) + "°");
    }

    public void subOneQuart(View v) {
        if (quarter > 1)
            quarter -= 1;
        quarterView.setText(String.valueOf(quarter) + "°");
    }

    //Reset Punteggi

    public void resetScore(View v) {
        scoreTeamHm = 0;
        scoreTeamVis = 0;
        quarter = 1;

        scoreView.setText(String.valueOf(scoreTeamHm));
        scoreViewVisitor.setText(String.valueOf(scoreTeamVis));
        quarterView.setText(String.valueOf(quarter) + "°");
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
                        Intent intent = new Intent(LiveActivity.this, LoginActivity.class);
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

    @Override
    public void onClick(View v) {
        if (v == logoutButton) {
            logout();
        }

        if (v == updateResult) {
            insertQuarter();
        }
    }


    public void loadImagefromGallery(View view) {

        //Creo l'intento per aprire un'applicazione di gestione d'immagini
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //Lancio l'intento
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    public void getIMGSize(Uri uri) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(new File(uri.getPath()).getAbsolutePath(), options);
        imageHeight = options.outHeight;
        imageWidth = options.outWidth;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        //Inizializzo l'immagine
                        CircleImageView imgView = (CircleImageView) findViewById(R.id.profile_image);
                        getIMGSize(selectedImage);

                        //Ruoto l'immagine, in verticale non risulta giusta l'orientamento
                        imgView.setImageBitmap(decodeUri(selectedImage));
                        imgView.setPivotX(imgView.getWidth() / 2);
                        imgView.setPivotY(imgView.getHeight() / 2);
                        imgView.setRotation(270);


                    } catch (FileNotFoundException e) {

                        //In caso di errore
                        e.printStackTrace();
                    }

                }
        }
    }

    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {

        //Metodo per scaling dell'immagine
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(
                getContentResolver().openInputStream(selectedImage), null, o);

        //Massima dimensione consentita
        final int REQUIRED_SIZE = 100;

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
    }

}