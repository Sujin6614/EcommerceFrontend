package com.example.eshoppingecommerce.ACTIVITY;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eshoppingecommerce.API.UserAPI;
import com.example.eshoppingecommerce.MODEL.UserModel;
import com.example.eshoppingecommerce.R;
import com.example.eshoppingecommerce.RETROFIT_OBJECT.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Login extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private TextView etEmail, etPassword, goToSignUp;
    private Button btnLogin;
    private Vibrator vibrator;
    private RetrofitClient retrofitClient = new RetrofitClient();
    private UserAPI userAPI;
    private Retrofit retrofit;
    private boolean IsUserLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        etEmail = findViewById(R.id.etLEmail);
        etPassword = findViewById(R.id.etLPassword);

        goToSignUp = findViewById(R.id.gotToSignUP);
        goToSignUp.setOnClickListener(this);

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);


//        Shared preference object initialization
        sharedPreferences = getSharedPreferences("LOGGED_IN_USER_DATA", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

//        Vibrator sensor
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        IsUserLoggedIn = sharedPreferences.getBoolean("IS_LOGGED_IN", false);

        if (IsUserLoggedIn) {
            Intent UserProfileIntent = new Intent(Login.this, UserDashboard.class);
            startActivity(UserProfileIntent);
            finish();
        }

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.gotToSignUP) {
            Intent intent = new Intent(Login.this, SignUp.class);
            startActivity(intent);
        }

        if (view.getId() == R.id.btnLogin) {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            if (email.isEmpty()) {
                etEmail.setError("Please enter email");
                etEmail.requestFocus();
            } else if (password.isEmpty()) {
                etPassword.setError("Please enter password");
                etPassword.requestFocus();
            } else {
                checkUser(email, password);
            }
        }
    }


    public void checkUser(String email, String password) {


        retrofit = retrofitClient.createInstance();
        userAPI = retrofit.create(UserAPI.class);


        Call<UserModel> checkLogin = userAPI.loginUser(new UserModel("", "", email, "", "", password, ""));

        checkLogin.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {

                int statusCode = response.code();

                if (statusCode == 201) {



                        UserModel userModel= response.body();

                        String id = userModel.get_id();
                        String email = userModel.getEmail();
                        String mobile = userModel.getMobile();
                        String address = userModel.getAddress();
                        String fullname = userModel.getFullname();
                        String image = userModel.getImage();


                        editor.putBoolean("IS_LOGGED_IN", true);
                        editor.putString("LOGGED_IN_USER_ID", id);
                        editor.putString("LOGGED_IN_USER_EMAIL", email);
                        editor.putString("LOGGED_IN_USER_MOBILE", mobile);
                        editor.putString("LOGGED_IN_USER_FULLNAME", fullname);
                        editor.putString("LOGGED_IN_USER_ADDRESS", address);
                        editor.putString("LOGGED_IN_USER_IMAGE", image);
                        editor.commit();

                        vibrator.vibrate(200);

                        Intent intent = new Intent(Login.this, UserDashboard.class);
                        startActivity(intent);
                        finish();


                }

                if (statusCode == 500) {
                    Toast.makeText(Login.this, "Invalid login try again", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {

            }
        });

    }

}
