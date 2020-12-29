package com.example.eshoppingecommerce.ACTIVITY;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.eshoppingecommerce.R;

public class UserDashboard extends AppCompatActivity implements View.OnClickListener {

    private ImageView imgProfile, imgProducts, imgCart, imgOrders, imgNews, imgLogout;
    private SensorManager sensorManager;
    private SensorEventListener proxilistener;
    private Sensor sensor;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        getSupportActionBar().hide();

        imgProfile=findViewById(R.id.profile);
        imgProducts=findViewById(R.id.products);
        imgCart=findViewById(R.id.cart);
        imgOrders=findViewById(R.id.orders);
        imgNews=findViewById(R.id.news);
        imgLogout=findViewById(R.id.logout);

        imgProfile.setOnClickListener(this);
        imgProducts.setOnClickListener(this);
        imgCart.setOnClickListener(this);
        imgOrders.setOnClickListener(this);
        imgNews.setOnClickListener(this);
        imgLogout.setOnClickListener(this);


//        sensor
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        proxInstance();

//        shared prefecence
        sharedPreferences = getSharedPreferences("LOGGED_IN_USER_DATA", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.profile){
            Intent intent= new Intent(getApplicationContext(), Profile.class);
            startActivity(intent);
            overridePendingTransition(0,0);
        }

        else if (view.getId()==R.id.products){
            Intent intent= new Intent(getApplicationContext(), BrowsePhone.class);
            startActivity(intent);
            overridePendingTransition(0,0);

        }

        else if (view.getId()==R.id.cart){
            Intent intent= new Intent(getApplicationContext(), Cart.class);
            startActivity(intent);
            overridePendingTransition(0,0);

        }

        else if (view.getId()==R.id.orders){
            Intent intent= new Intent(getApplicationContext(), Orders.class);
            startActivity(intent);
            overridePendingTransition(0,0);
        }

        else if (view.getId()==R.id.news){
            Intent intent= new Intent(getApplicationContext(), News.class);
            startActivity(intent);
            overridePendingTransition(0,0);
        }

        else if (view.getId()==R.id.logout){
        LogoutUser();
        }
    }


    private void proxInstance() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        proxilistener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.values[0] >= -4 && event.values[0] <= 4) {
                    sensorManager.unregisterListener(proxilistener);
                    LogoutUser();
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(proxilistener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(proxilistener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }


    public void LogoutUser() {
        sensorManager.unregisterListener(proxilistener);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout").
                setMessage("Are you sure to logout?").

                setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        editor.putBoolean("IS_LOGGED_IN", false);

                        editor.putString("LOGGED_IN_USER_EMAIL", "");
                        editor.putString("LOGGED_IN_USER_MOBILE", "");
                        editor.putString("LOGGED_IN_USER_FULLNAME", "");
                        editor.putString("LOGGED_IN_USER_ADDRESS", "");
                        editor.putString("LOGGED_IN_USER_ID", "");
                        editor.commit();

                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        editor.commit();
                        overridePendingTransition(0, 0);
                        startActivity(intent);
                        finish();
                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sensorManager.registerListener(proxilistener, sensor, SensorManager.SENSOR_DELAY_NORMAL);

                    }
                })

                .show();

    }

}
