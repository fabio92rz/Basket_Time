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

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import android.graphics.Matrix;

public class LiveActivity extends AppCompatActivity implements View.OnClickListener {

    int scoreTeamHm = 0, scoreTeamVis = 0, quarter = 1, imageHeight, imageWidth;
    EditText teamhome, teamvis;

    //TextView per i dati dell'utente
    private TextView tvName_surname;
    private TextView tvEmail;

    //Bottone per il logout
    private Button logoutButton;

    //Variabili per lo scaling dell'immagine
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;

    //Varibili per la Navigation View
    private Toolbar toolbar;
    private NavigationView navigationView;
    protected DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_activity);

        //Inizializzo la Toolbar e la inserisco nell'actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Inizializzo le TextView
        tvName_surname = (TextView) findViewById(R.id.username_header);
        tvEmail = (TextView) findViewById(R.id.email_header);

        //Inizializzo il Bottone per il logout
        logoutButton = (Button) findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(this);

        //Catturo i dati e li inserisco nell'header
        SharedPreferences sharedPreferences = getSharedPreferences(ConfigActivity.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String email = sharedPreferences.getString(ConfigActivity.EMAIL_SHARED_PREF, "Not Available");

        //Mostro la mail dell'utente loggato
        //tvEmail.setText(email);

        //Inizializzo la NavigationView, utilizzata per il drawer
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

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
                        break;

                    case R.id.storico_partite:
                        Intent history = new Intent(LiveActivity.this, HistoryActivity.class);
                        startActivity(history);
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


        displayForHm(0);
        displayForVis(0);

    }

    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

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

    //Gestione degli oggetti a schermo

    public void reset(EditText team, EditText team1) {
        EditText teamhome = (EditText) findViewById(R.id.team_home_text);
        teamhome.setText(null);
        EditText teamVis = (EditText) findViewById(R.id.team_visitors_text);
        teamVis.setText(null);
    }

    public void stream(View v) {
        Button start = (Button) findViewById(R.id.start_button);
    }

    public void stopStream(View v) {
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

    //Gestione Live

    public void addOnePoint(View v) {
        scoreTeamHm += 1;
        displayForHm(scoreTeamHm);
    }

    public void addOnePointVis(View v) {
        scoreTeamVis += 1;
        displayForVis(scoreTeamVis);
    }

    public void subOne(View v) {
        if (scoreTeamHm > 0)
            scoreTeamHm -= 1;
        displayForHm(scoreTeamHm);
    }

    public void subOnePointVis(View v) {
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

    //Reset Punteggi

    public void resetScore(View v) {
        scoreTeamHm = 0;
        scoreTeamVis = 0;
        quarter = 1;

        reset(teamhome, teamvis);
        displayForHm(scoreTeamHm);
        displayForVis(scoreTeamVis);
        displayForQuarter(quarter + "°");
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
    }
}