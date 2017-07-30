package com.example.alex.phonepotato;


import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    Button button;

    static final String PREFS_NAME = "MyPrefFile";
    TextView hScore;
    int highScore;
    public SharedPreferences prefs;
    public static Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        context = this;

        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume(){
        super.onResume();
        SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        highScore = prefs.getInt("myPrefsKey", 0); //0 is the default value

        setContentView(R.layout.activity_main);
        hScore = (TextView)findViewById(R.id.highScore);

        hScore.setText(Integer.toString(highScore)+ " cm");


        button=(Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(),Calculation.class);
                startActivity(i);

            }
        });

    }

    public static SharedPreferences getSharedPreferences (Context ctxt) {
        return ctxt.getSharedPreferences("myPrefsKey", 0);
    }
}
