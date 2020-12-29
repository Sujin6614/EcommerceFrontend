package com.example.eshoppingecommerce.ACTIVITY;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eshoppingecommerce.ADAPTER.FeedbackRatingAdapter;
import com.example.eshoppingecommerce.API.CartAPI;
import com.example.eshoppingecommerce.API.FeedbackAPI;
import com.example.eshoppingecommerce.CHANNEL.Channel;
import com.example.eshoppingecommerce.R;
import com.example.eshoppingecommerce.RETROFIT_OBJECT.RetrofitClient;
import com.example.eshoppingecommerce.ResponsesModel.CartResponseModel;
import com.example.eshoppingecommerce.ResponsesModel.FeedbackresponseModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BrowseSingleProduct extends AppCompatActivity implements View.OnClickListener {


    private String productid, price, warrenty, name, description, addedDate, image, brand;
    private TextView txtName, txtPrice, txtDesc, txtWarrenty, txtAddedDate, txtToggleShowRatingDetails, txtBrand;
    private ImageView imgProductImage, imgToggleShowProductDetails;
    private Button addToCart, submitratingFeedback;
    private ScrollView scrollViewproductDetails, scrollViewratingandFeedback;
    private LinearLayout bottomButtonsLayout;
    private EditText etFeedback, etRating;
    private RecyclerView recyclerView;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private RetrofitClient retrofitClient = new RetrofitClient();
    private String BASE_URL = retrofitClient.BASE_URL();
    private Retrofit retrofit;
    private CartAPI cartAPI;
    private FeedbackAPI fedFeedbackAPI;

    private NotificationManagerCompat notificationManagerCompat;

   private SensorManager sensorManager;
   private SensorEventListener accelistener;
   private Sensor sensor;
    float acceLast, acce, acceCurrent;

    private Vibrator vibrator;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_single_product);

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        txtName = findViewById(R.id.singleViewProductName);
        txtPrice = findViewById(R.id.singleViewProductPrice);
        txtDesc = findViewById(R.id.singleViewProductDescription);
        txtWarrenty = findViewById(R.id.singleViewProductWarrenty);
        txtAddedDate = findViewById(R.id.singleViewProductAddedDate);
        txtBrand=findViewById(R.id.singleViewProductBrand);
        imgProductImage = findViewById(R.id.singleViewProductImage);

//       txt toggle View and rating and feedback
        txtToggleShowRatingDetails = findViewById(R.id.toggleViewFeedbackDetails);
        txtToggleShowRatingDetails.setOnClickListener(this);


//        Buttons
        addToCart = findViewById(R.id.btnAddToCart);
        addToCart.setOnClickListener(this);


        submitratingFeedback = findViewById(R.id.btnSubmitRatingFeedback);
        submitratingFeedback.setOnClickListener(this);


//        Scroll views
        scrollViewproductDetails = findViewById(R.id.productDetailsScrollView);
        scrollViewratingandFeedback = findViewById(R.id.feedbackAndRatingScollview);


//        Linearlayout
        bottomButtonsLayout = findViewById(R.id.linearLayoutBottomButtons);


//        ImgtoglleViewProductDetails
        imgToggleShowProductDetails = findViewById(R.id.imgToggleVeiewProductDetails);
        imgToggleShowProductDetails.setOnClickListener(this);


//      edit texts
        etFeedback = findViewById(R.id.etFeedback);
        etRating = findViewById(R.id.etRating);



//        Recyclerview
        recyclerView = findViewById(R.id.feedbackAndratingRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        Shared preference
        sharedPreferences = getSharedPreferences("LOGGED_IN_USER_DATA", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

//        sensors
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        AcceleroInstance();


//        Vibrator
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);


//

        notificationManagerCompat= NotificationManagerCompat.from(this);

        Channel channel= new Channel(this);
        channel.createChannel();


        getSignleProductDetails();
        getFeedback();

    }



    private void getSignleProductDetails() {
        Bundle bundle = getIntent().getExtras();
        productid = bundle.getString("productId");
        name = bundle.getString("productName");
        price = bundle.getString("productPrice");
        brand=bundle.getString("productBrand");
        image = bundle.getString("productImage");
        warrenty = bundle.getString("productWarrenty");
        addedDate = bundle.getString("productAddedDate");
        description = bundle.getString("productDescription");

        txtName.setText(name);
        txtPrice.setText("Rs. " + price);
        txtWarrenty.setText(warrenty + " years");
        txtDesc.setText(description);
        txtAddedDate.setText(addedDate);
        txtBrand.setText(brand);

        Picasso.with(this).load(BASE_URL + "assets/images/" + image).into(imgProductImage);


    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.toggleViewFeedbackDetails) {

            scrollViewproductDetails.setVisibility(View.INVISIBLE);
            bottomButtonsLayout.setVisibility(View.INVISIBLE);
            scrollViewratingandFeedback.setVisibility(View.VISIBLE);
        } else if (view.getId() == R.id.imgToggleVeiewProductDetails) {

            scrollViewratingandFeedback.setVisibility(View.INVISIBLE);
            scrollViewproductDetails.setVisibility(View.VISIBLE);
            bottomButtonsLayout.setVisibility(View.VISIBLE);
        } else if (view.getId() == R.id.btnSubmitRatingFeedback) {
            submitRatingFeedback();
        } else if (view.getId() == R.id.btnAddToCart) {
            addToCart();
        }

    }


    private void submitRatingFeedback() {
        String username = sharedPreferences.getString("LOGGED_IN_USER_FULLNAME", "");
        String rating = etRating.getText().toString();
        String feedback = etFeedback.getText().toString();


        if (rating.isEmpty()) {
            etRating.setError("Enter rating");
            etRating.requestFocus();
        } else if (feedback.isEmpty()) {
            etFeedback.setError("Enter feedback");
            etFeedback.requestFocus();
        } else {
//
            giveFeedback(rating, feedback, username, productid);
        }

    }

    private void giveFeedback(String rating, String feedback, String username, String productid) {
        retrofit = retrofitClient.createInstance();
        fedFeedbackAPI = retrofit.create(FeedbackAPI.class);

        Call<Object> giveFeedback= fedFeedbackAPI.giveFeedback(new FeedbackresponseModel("",Integer.parseInt(rating),feedback,username,productid));

        giveFeedback.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                int statusCode= response.code();

                if (response.isSuccessful()){
                    if (statusCode==200){
                        Toast.makeText(BrowseSingleProduct.this, "Feedback recorded ", Toast.LENGTH_SHORT).show();
                        etFeedback.setText(null);
                        etRating.setText(null);
                        Intent intent= getIntent();
                        finish();
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });

    }

    private void getFeedback(){
        retrofit = retrofitClient.createInstance();
        fedFeedbackAPI = retrofit.create(FeedbackAPI.class);

        Call<List<FeedbackresponseModel>> getFeedbackData= fedFeedbackAPI.getFeedback(productid);

        getFeedbackData.enqueue(new Callback<List<FeedbackresponseModel>>() {
            @Override
            public void onResponse(Call<List<FeedbackresponseModel>> call, Response<List<FeedbackresponseModel>> response) {
                int statuCode= response.code();

                if (response.isSuccessful()){
                    if (statuCode==200){
                        recyclerView.setAdapter(new FeedbackRatingAdapter(response.body(), getApplicationContext()));
                    }
                    else if (statuCode==500){
                        Toast.makeText(BrowseSingleProduct.this, "Cannot fetch feedback data", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<FeedbackresponseModel>> call, Throwable t) {
                Toast.makeText(BrowseSingleProduct.this, "Someting went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }



    private void addToCart() {

        String userid = sharedPreferences.getString("LOGGED_IN_USER_ID", "");

        String email = sharedPreferences.getString("LOGGED_IN_USER_EMAIL", "");
        String mobile = sharedPreferences.getString("LOGGED_IN_USER_MOBILE", "");
        String fullname = sharedPreferences.getString("LOGGED_IN_USER_FULLNAME", "");
        String address = sharedPreferences.getString("LOGGED_IN_USER_ADDRESS", "");

        retrofit = retrofitClient.createInstance();
        cartAPI = retrofit.create(CartAPI.class);
        Call<Object> addToCart = cartAPI.addToCart(new CartResponseModel("",userid,productid,name,image,fullname,email,address,mobile,Integer.parseInt(price)));

        addToCart.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                int statusCode = response.code();

                if (statusCode == 201) {
                    Toast.makeText(BrowseSingleProduct.this, "Added to card", Toast.LENGTH_SHORT).show();
                    Intent intent= new Intent(BrowseSingleProduct.this, Cart.class);
                    startActivity(intent);
                    finish();
                    DisplayNotification();
                    vibrator.vibrate(500);

                } else if (statusCode == 500) {
                    Toast.makeText(BrowseSingleProduct.this, "Cannot add to card" + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(BrowseSingleProduct.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

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

    private void AcceleroInstance(){
        sensorManager= (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        sensor=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        accelistener=new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float[]values=event.values;
                float xAxis =values[0];
                float yAxis=values[1];
                float zAxis=values[2];
                acceLast=acceCurrent;
                acceCurrent= (float) Math.sqrt((double)(xAxis * xAxis +yAxis * yAxis + zAxis * zAxis));
                float delta=acceCurrent-acceLast;
                acce=acce * 0.9f + delta;
                if (acce>11){
                    sensorManager.unregisterListener(accelistener);
                   addToCart();
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        sensorManager.registerListener(accelistener,sensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void DisplayNotification(){
        Notification notification= new NotificationCompat.Builder(this, Channel.CHANNEL_2)
                .setSmallIcon(R.drawable.ic_navcart)
                .setContentTitle("Add to cart")
                .setContentText(name+ "  added to cart")
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManagerCompat.notify(1, notification);

    }




}
