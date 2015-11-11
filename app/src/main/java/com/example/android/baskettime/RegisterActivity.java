package com.example.android.baskettime;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

/**
 * Created by fabio on 11/11/2015.
 */
public class RegisterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);


    }
}
