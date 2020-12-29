package com.example.eshoppingecommerce.ACTIVITY;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.eshoppingecommerce.API.OrderAPI;
import com.example.eshoppingecommerce.CHANNEL.Channel;
import com.example.eshoppingecommerce.R;
import com.example.eshoppingecommerce.RETROFIT_OBJECT.RetrofitClient;
import com.example.eshoppingecommerce.ResponsesModel.OrdersResponseModel;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ConfirmOrder extends AppCompatActivity implements View.OnClickListener {

    float acceLast, acce, acceCurrent;
    private ImageView confirmOrderImage;
    private TextView confirmOrderPrice, confirmOrderName, billingUsername, billingEmail, billingMobile, billingAddress;
    private String id, productid, userid, price, name, image, username, mobile, address, email;
    private Button btnConfirmOrder;
    private RetrofitClient retrofitClient = new RetrofitClient();
    private String BASE_URL = retrofitClient.BASE_URL();
    private Retrofit retrofit;
    private OrderAPI orderAPI;
    private NotificationManagerCompat notificationManagerCompat;
    private SensorManager sensorManager;
    private SensorEventListener accelistener;
    private Sensor sensor;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        Imageview
        confirmOrderImage = findViewById(R.id.confirmOrderImage);

//        TextViews
        confirmOrderName = findViewById(R.id.confirmOrderName);
        confirmOrderPrice = findViewById(R.id.confirmOrderPrice);
        billingUsername = findViewById(R.id.billingUsername);
        billingEmail = findViewById(R.id.billingEmail);
        billingMobile = findViewById(R.id.billingMobile);
        billingAddress = findViewById(R.id.billingAddress);

        btnConfirmOrder = findViewById(R.id.btnConfirmOrder);
        btnConfirmOrder.setOnClickListener(this);


        // sensors
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        AcceleroInstance();

//        Vibrator
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);


//notification
        notificationManagerCompat = NotificationManagerCompat.from(this);

        Channel channel = new Channel(this);
        channel.createChannel();


        setBillingDetails();

    }

    private void setBillingDetails() {


        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        userid = bundle.getString("userid");
        productid = bundle.getString("productId");
        name = bundle.getString("productName");
        price = bundle.getString("productPrice");
        image = bundle.getString("productImage");

        username = bundle.getString("username");
        mobile = bundle.getString("mobile");
        address = bundle.getString("address");
        email = bundle.getString("email");

        Picasso.with(this).load(BASE_URL + "assets/images/" + image).into(confirmOrderImage);


        confirmOrderPrice.setText("Rs. " + price);
        confirmOrderName.setText(name);

        billingUsername.setText(username);
        billingEmail.setText(email);
        billingMobile.setText(mobile);
        billingAddress.setText(address);

    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnConfirmOrder) {
            ConfirmOrder();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        sensorManager.unregisterListener(accelistener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(accelistener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void ConfirmOrder() {
        retrofit = retrofitClient.createInstance();
        orderAPI = retrofit.create(OrderAPI.class);

        Call<Object> placeOrder = orderAPI.orderProduct(new OrdersResponseModel("", name, Integer.parseInt(price), image, userid, false));

        placeOrder.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                int statusCode = response.code();
                if (response.isSuccessful()) {
                    if (statusCode == 200) {
                        Toast.makeText(ConfirmOrder.this, "Order has been placed", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ConfirmOrder.this, Orders.class);
                        finish();
                        startActivity(intent);
                        DisplayNotification();
                        vibrator.vibrate(500);
                    } else if (statusCode == 500) {
                        Toast.makeText(ConfirmOrder.this, "Cannot place order", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(ConfirmOrder.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void AcceleroInstance() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        accelistener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float[] values = event.values;
                float xAxis = values[0];
                float yAxis = values[1];
                float zAxis = values[2];
                acceLast = acceCurrent;
                acceCurrent = (float) Math.sqrt((double) (xAxis * xAxis + yAxis * yAxis + zAxis * zAxis));
                float delta = acceCurrent - acceLast;
                acce = acce * 0.9f + delta;
                if (acce > 11) {
                    sensorManager.unregisterListener(accelistener);
//
                    ConfirmOrder();
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        sensorManager.registerListener(accelistener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void DisplayNotification() {
        Notification notification = new NotificationCompat.Builder(this, Channel.CHANNEL_1)
                .setSmallIcon(R.drawable.ic_navorder)
                .setContentTitle("Place order")
                .setContentText(name + "  order placed")
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManagerCompat.notify(1, notification);

    }

}
