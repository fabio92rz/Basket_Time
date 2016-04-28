package com.example.android.baskettime;

import android.os.Bundle;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.vi.swipenumberpicker.OnValueChangeListener;
import com.vi.swipenumberpicker.SwipeNumberPicker;

/**
 * Created by Fabio on 28/04/2016.
 */
public class prova extends AppCompatActivity {

    SwipeNumberPicker numberPicker;
    SwipeNumberPicker numberPicker2;
    TextView result;
    TextView result2;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prova);

        numberPicker = (SwipeNumberPicker) findViewById(R.id.snp_implemented);
        numberPicker2 = (SwipeNumberPicker) findViewById(R.id.snp_custom);
        final String quarter = "°";


        result = (TextView) findViewById(R.id.tv_result_1);
        result2 = (TextView) findViewById(R.id.tv_result_2);
        result2.setText(String.valueOf(numberPicker2.getValue()));


        numberPicker2.setOnValueChangeListener(new OnValueChangeListener() {
            @Override
            public boolean onValueChange(SwipeNumberPicker view, int oldValue, int newValue) {

                result2.setText(String.valueOf(newValue));
                numberPicker2.setText(String.valueOf(newValue + quarter));

                return true;
            }
        });




    }



}
