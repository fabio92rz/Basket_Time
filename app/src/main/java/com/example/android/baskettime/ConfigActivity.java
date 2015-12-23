package com.example.android.baskettime;

/**
 * Created by fabio on 16/12/2015.
 */
public class ConfigActivity {

    //Url a login2.php
    public static final String LOGIN_URL = "http://95.85.23.84/prova.php";
    public static final String JSON_URL = "http://95.85.23.84/prova2.php";

    //Keys per Email e Password come definito nel file prova.php
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";

    //Se il Server risponde e quindi il login è andato bene
    public static final String LOGIN_SUCCESS = "success";

    //Keys per le Shared Preferences
    //Questo sarà il nome delle nostre Shared Preferences
    public static final String SHARED_PREF_NAME = "Dati Personali";

    //Questo verrà usato per conservare l'email dell'utente corrente
    public static final String EMAIL_SHARED_PREF = "email";

    //Userò questa variabile booleana nelle Shared Preference pre seguire se l'utente è dentro o no
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";

    //Tag JSON

    public static final String JSON_ARRAY = "success";
    public static final String TAG_ID = "id";
    public static final String TAG_NAME = "name";
    public static final String TAG_SURNAME = "surname";

}
