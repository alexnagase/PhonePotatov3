package com.example.alex.phonepotato;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

import static com.example.alex.phonepotato.MainActivity.context;

public class Calculation extends AppCompatActivity {

    Button resetButton;
    Button backButton;
    SensorManager sensorManager;
    Sensor accelerometer, gyroscope;
    TextView textView1; //debug textviews
    TextView textView2;
    TextView textView3;
    TextView rotationData;
    TextView newScore;
    TextView ach;
    Boolean inFlight = false;
    long startTime;
    long timeOfFlight = 0;
    Boolean finish = false;
    double launchAccel;
    double landingAccel;
    Boolean startTimer = false;
    double gravity = 10;
    double radSpeedX,radSpeedY,radSpeedZ = 0;
    int i = 0;//gyroscope average calculation
    long timeout = 123456789;
    int flightHeightCentimeters;
    int hiScore;
    boolean executed = false;
    String a = "Did you know ";
    String taller = " has a height of ";
    String longer = " has a length of ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculation);
        try {
            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            sensorManager.registerListener(accelListener, accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL);
            sensorManager.registerListener(accelListener, gyroscope,
                    SensorManager.SENSOR_DELAY_FASTEST);
        }catch (Exception e){
            Toast.makeText(this, "Hardware Compatibility Issue", Toast.LENGTH_LONG).show();
        }
        //textView1 = (TextView)findViewById(R.id.text1);
        //textView2 = (TextView)findViewById(R.id.text2);
        textView3 = (TextView)findViewById(R.id.text3);
        ach = (TextView)findViewById(R.id.ach);
        rotationData = (TextView)findViewById(R.id.rotation);
        newScore = (TextView)findViewById(R.id.newScore);
        newScore.setVisibility(View.GONE);
        ach.setVisibility(View.GONE);
        resetButton=(Button)findViewById(R.id.resetButton);
        backButton=(Button)findViewById(R.id.backButton);
        resetButton.setVisibility(View.GONE);
        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);

            }
        });

    }

    public void onResume() {
        super.onResume();
        sensorManager.registerListener(accelListener, accelerometer,
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(accelListener, gyroscope,
                SensorManager.SENSOR_DELAY_FASTEST);

        SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        hiScore = prefs.getInt("myPrefsKey", 0); //0 is the default value
        executed = false;

    }

    public void onStop() {
        super.onStop();
        sensorManager.unregisterListener(accelListener);
    }



    SensorEventListener accelListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int acc) { }

        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType()==Sensor.TYPE_ACCELEROMETER) { //accelerometer changes
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];
                double xyz = Math.sqrt(((double) x) * ((double) x) + ((double) y) * ((double) y) + ((double) z) * ((double) z));
                //textView1.setText(Double.toString(xyz));
                if (xyz > 20 && inFlight == false && finish == false) {
                    //textView2.setText("launch detected!");
                    inFlight = true;
                    startTimer = true;
                }
                if (xyz < 3 && inFlight == true && startTimer == true) {
                    startTime = System.currentTimeMillis();
                   // textView2.setText("Start time: " + Long.toString(startTime));
                    xyz = launchAccel;
                    startTimer = false;
                }

                //while (executed = false) {


                    if (xyz > 10 && inFlight == true && startTimer == false) {
                        timeOfFlight = System.currentTimeMillis() - startTime;
                        // textView2.setText("Flight time was: " + Long.toString(timeOfFlight));
                        finish = true;
                        inFlight = false;
                        if (timeOfFlight > timeout) {
                            timeOfFlight = 0;

                        }
                    }
                    executed = true;
                //}
                if (finish == true) {

                    resetButton.setVisibility(View.VISIBLE);
                    if (timeOfFlight == 0){
                        textView3.setText("Sensor Error");

                    }else {
                        double flightHeightMeters = 0.5 * (timeOfFlight / ((double) 2000)) * gravity * Math.pow((timeOfFlight / (double) (2000)), 2);
                        flightHeightCentimeters = (int) (flightHeightMeters * 100.0);
                        textView3.setText("Flight Height: " + Integer.toString(flightHeightCentimeters) + " cm");

                        //achivements
                        if (flightHeightCentimeters > 0) {
                            ach.setVisibility(View.VISIBLE);
                            ach.setText("Come on you can do better than this!");
                        }
                        if (flightHeightCentimeters > 29) {
                            ach.setText(a+"a meerkat"+taller+"30 cm?");
                        }
                        if (flightHeightCentimeters > 69) {
                            ach.setText(a+"a koala"+taller+"70 cm?");
                        }
                        if (flightHeightCentimeters > 121) {
                            ach.setText(a+"an Emperor Penguin"+taller+"122 cm?");
                        }
                        if (flightHeightCentimeters > 139) {
                            ach.setText(a+"a zebra"+taller+"140 cm?");
                        }
                        if (flightHeightCentimeters > 149) {
                            ach.setText(a+"a flamingo"+taller+"150 cm?");
                        }
                        if (flightHeightCentimeters > 160) {
                            ach.setText(a+"an average US female"+taller+"161 cm?");
                        }
                        if (flightHeightCentimeters > 175) {
                            ach.setText(a+"an average US male"+taller+"176 cm?");
                        }
                        if (flightHeightCentimeters > 199) {
                            ach.setText(a+"a kangaroo"+taller+"200 cm?");
                        }
                        if (flightHeightCentimeters > 228) {
                            ach.setText(a+"Yao Ming"+taller+"229 cm?");
                        }
                        if (flightHeightCentimeters > 304) {
                            ach.setText(a+"a brown bear"+taller+"305 cm?");
                        }
                        if (flightHeightCentimeters > 399) {
                            ach.setText(a+"an african elephant"+taller+"400 cm?");
                        }
                        if (flightHeightCentimeters > 549) {
                            ach.setText(a+"a giraffe"+taller+"550 cm?");
                        }
                        if (flightHeightCentimeters > 969) {
                            ach.setText(a+"an anaconda"+longer+"970 cm?");
                        }


                        if (flightHeightCentimeters > hiScore){

                            newScore.setVisibility(View.VISIBLE);
                            SharedPreferences prefs = context.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putInt("myPrefsKey", flightHeightCentimeters);
                            editor.commit();

                        }

                    }

                    resetButton.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            // TODO Auto-generated method stub
                            Intent i = new Intent(getApplicationContext(), Calculation.class);
                            startActivity(i);

                        }
                    });
                }

            }
            if (event.sensor.getType()==Sensor.TYPE_GYROSCOPE) {
                double x = event.values[0];
                double y = event.values[1];
                double z = event.values[2];

                if (inFlight == false && finish == false) {
                    //rotationData.setText("X: " + Double.toString(x) + ", Y: " + Double.toString(y) + ", Z: " + Double.toString(z));

                }
                if (inFlight){

                    if (x>radSpeedX){
                        radSpeedX=x;
                    }
                    if (y>radSpeedY){
                        radSpeedY=y;
                    }
                    if (z>radSpeedZ){
                        radSpeedZ=z;
                    }
                }
                if (finish == true){
                    int spinX = (int)((radSpeedX/6.38)*(timeOfFlight/1000.0));
                    int spinY = (int)((radSpeedY/6.38)*(timeOfFlight/1000.0));
                    int spinZ = (int)((radSpeedZ/6.38)*(timeOfFlight/1000.0));
                    rotationData.setText("X flips: " + Integer.toString(spinX) + ", Y flips: " + Integer.toString(spinY) + ", Z flips: " + Integer.toString(spinZ));
//                    rotationData.setText("Finished");
                }
            }
        }
    };
}