package com.example.eshoppingecommerce.ACTIVITY;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eshoppingecommerce.API.UserAPI;
import com.example.eshoppingecommerce.MODEL.UserModel;
import com.example.eshoppingecommerce.R;
import com.example.eshoppingecommerce.RETROFIT_OBJECT.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class SignUp extends AppCompatActivity implements View.OnClickListener {

    boolean IsUserLoggedIn = false;
    private EditText etfullname, etemail, etmobile, etaddress, etpassword, etcpassword;
    private Button btnSignup;
    private TextView txtGoToLogin;
    private UserModel userModel;
    private Vibrator vibrator;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private RetrofitClient retrofitClient = new RetrofitClient();
    private UserAPI userAPI;
    private Retrofit retrofit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        etfullname = findViewById(R.id.etFfullname);
        etemail = findViewById(R.id.etEmail);
        etmobile = findViewById(R.id.etMobile);
        etaddress = findViewById(R.id.etAddress);
        etpassword = findViewById(R.id.etPassword);
        etcpassword = findViewById(R.id.etCPassword);


        btnSignup = findViewById(R.id.btnSignup);
        btnSignup.setOnClickListener(this);

        txtGoToLogin = findViewById(R.id.gotoLogin);
        txtGoToLogin.setOnClickListener(this);

//        Shared preference
        sharedPreferences = getSharedPreferences("LOGGED_IN_USER_DATA", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        ;

//        Vibrator
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        IsUserLoggedIn = sharedPreferences.getBoolean("IS_LOGGED_IN", false);

        if (IsUserLoggedIn) {
            Intent UserProfileIntent = new Intent(SignUp.this, UserDashboard.class);
            startActivity(UserProfileIntent);
            finish();
        }

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSignup) {
            SignUpUserValidation();


        } else if (view.getId() == R.id.gotoLogin) {
            Intent intent = new Intent(SignUp.this, Login.class);
            startActivity(intent);
        }
    }

    private void SignUpUserValidation() {
        String fullname, email, mobile, address, password, cpassword;

        fullname = etfullname.getText().toString();
        email = etemail.getText().toString().toLowerCase();
        mobile = etmobile.getText().toString();
        address = etaddress.getText().toString();
        password = etpassword.getText().toString();
        cpassword = etcpassword.getText().toString();


        if (fullname.isEmpty()) {
            etfullname.setError("Please enter your name");
            etfullname.requestFocus();
        } else if (!fullname.isEmpty() && !fullname.matches("[a-z a-z A-Z A-Z]+")) {
            etfullname.setError("Please enter a valid name");
            etmobile.requestFocus();
        } else if (email.isEmpty()) {
            etemail.setError("Please enter your email");
            etemail.requestFocus();
        } else if (!email.isEmpty() && !email.matches("^[0-9a-zA-Z!#$%&;'*+\\-/\\=\\?\\^_`\\.{|}~]{1,64}@[0-9a-zA-Z]{1,255}\\.[a-zA-Z]{1,10}")) {
            etemail.setError("Email is invalid, please match the email format");
            etemail.requestFocus();
        } else if (mobile.isEmpty()) {
            etmobile.setError("Please enter your mobile number");
            etmobile.requestFocus();
        } else if (!mobile.isEmpty() && !mobile.matches("[+ 0-9].{6,15}")) {
            etmobile.setError("Phone number can include + and must be minimum character up to 7 and maximum up to 10 character only");
            etmobile.requestFocus();
        } else if (address.isEmpty()) {
            etaddress.setError("Please enter your addresss");
            etaddress.requestFocus();
        } else if (password.isEmpty()) {
            etpassword.setError("Please enter your password");
            etpassword.requestFocus();
        } else if (!password.isEmpty() && !password.matches("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,}")) {
            etpassword.setError("Must contain at least one number and one uppercase and lowercase letter, and include at least 6 or more characters");
            etpassword.requestFocus();
        } else if (cpassword.isEmpty()) {
            etcpassword.setError("Please re-enter your password");
            etcpassword.requestFocus();
        } else if (!cpassword.isEmpty() && !cpassword.equals(password)) {
            etcpassword.setError("Password didnot match, re-enter same password");
            etcpassword.requestFocus();
        } else {

            userModel = new UserModel("",fullname, email, mobile, address, password, "");
            addUser(userModel);

        }

    }


    private boolean addUser(UserModel userModel) {


        retrofit = retrofitClient.createInstance();
        userAPI = retrofit.create(UserAPI.class);

        Call<Object> addUser = userAPI.addUser(userModel);

        addUser.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                int code = response.code();
                if (response.isSuccessful()) {
                    if (code == 200) {
                        Toast.makeText(SignUp.this, "Username already taken, choose different one ", Toast.LENGTH_SHORT).show();
                    }

                     if (code == 201) {
                        Toast.makeText(SignUp.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                         etfullname.setText(null);
                         etemail.setText(null);
                         etaddress.setText(null);
                         etmobile.setText(null);
                         etpassword.setText(null);
                         etcpassword.setText(null);
                         vibrator.vibrate(500);
                         goToLoginAfterSignUP();
                    }

                    if (code == 500) {
                        Toast.makeText(SignUp.this, "Internal server error", Toast.LENGTH_SHORT).show();
                        vibrator.vibrate(1000);
                    }

                }
            }


            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(SignUp.this, "Something went wrong " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return false;
    }

    private void goToLoginAfterSignUP(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Signed up").
                setMessage("User created go to login?").

                setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(SignUp.this, Login.class);
                        startActivity(intent);
                        finish();
                    }
                })

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })

                .show();

    }
}
