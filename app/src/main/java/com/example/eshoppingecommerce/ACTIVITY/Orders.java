package com.example.eshoppingecommerce.ACTIVITY;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eshoppingecommerce.ADAPTER.OrdersAdapter;
import com.example.eshoppingecommerce.API.OrderAPI;
import com.example.eshoppingecommerce.R;
import com.example.eshoppingecommerce.RETROFIT_OBJECT.RetrofitClient;
import com.example.eshoppingecommerce.ResponsesModel.OrdersResponseModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Orders extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private RecyclerView recyclerView;
    private List<OrdersResponseModel> ordersResponseModelList;

    private RetrofitClient retrofitClient = new RetrofitClient();
    private String BASE_URL = retrofitClient.BASE_URL();
    private Retrofit retrofit;
    private OrderAPI orderAPI;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private SensorManager sensorManager;
    private SensorEventListener proxilistener;
    private Sensor sensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);


        getSupportActionBar().setTitle("Orders");
        setupBottomNav();

        sharedPreferences = getSharedPreferences("LOGGED_IN_USER_DATA", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        proxInstance();


        recyclerView = findViewById(R.id.ordersListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        getOrdersData();
    }


    private void setupBottomNav() {

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_order);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_cart:
                        finish();
                        startActivity(new Intent(getApplicationContext(), Cart.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.navigation_dashboard:
                        finish();
                        startActivity(new Intent(getApplicationContext(), UserDashboard.class));
                        overridePendingTransition(0, 0);
                        return true;


                }
                return false;
            }
        });
    }

    private void getOrdersData() {

        String userid = sharedPreferences.getString("LOGGED_IN_USER_ID", "");

        retrofit = retrofitClient.createInstance();
        orderAPI = retrofit.create(OrderAPI.class);

        Call<List<OrdersResponseModel>> ordersResponseModelCall = orderAPI.getUserOrders(userid);

        ordersResponseModelCall.enqueue(new Callback<List<OrdersResponseModel>>() {
            @Override
            public void onResponse(Call<List<OrdersResponseModel>> call, Response<List<OrdersResponseModel>> response) {
                int statusCode = response.code();
                if (response.isSuccessful()) {
                    if (statusCode == 200) {
                        recyclerView.setAdapter(new OrdersAdapter(response.body(), getApplicationContext()));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<OrdersResponseModel>> call, Throwable t) {

            }
        });


    }

    public void LogoutUser() {
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

                        startActivity(intent);
                        finish();
                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sensorManager.registerListener(proxilistener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
                        Intent intent= getIntent();
                        finish();
                        startActivity(intent);
                    }
                })

                .show();

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

}
