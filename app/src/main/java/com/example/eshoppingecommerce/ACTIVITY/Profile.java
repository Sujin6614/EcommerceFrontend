package com.example.eshoppingecommerce.ACTIVITY;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.eshoppingecommerce.API.UserAPI;
import com.example.eshoppingecommerce.MODEL.USERIMAGEMODEL;
import com.example.eshoppingecommerce.MODEL.UserModel;
import com.example.eshoppingecommerce.R;
import com.example.eshoppingecommerce.RETROFIT_OBJECT.RetrofitClient;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Profile extends AppCompatActivity implements View.OnClickListener {

    private static final int PICK_IMAGE = 1;
    private ImageView imgOpenCamera, imgOpenGallery;
    private EditText etFullname, etEmail, etMobile, etAddress;
    private Button btnUdpdateProfile;
    private CircleImageView imgUserImage;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String LoggedInUserid, LoggedInUserfullname, LoggedInUseremail, LoggedInUsermobile, LoggedInUseraddress, LoggedInUserimage;
    private RetrofitClient retrofitClient = new RetrofitClient();
    private UserAPI userAPI;
    private Retrofit retrofit;
    private String getImageNameFromServer;
    private Bitmap bitmap;
    private Uri imageUri;
    private Handler handler;
    private String BASE_URL = retrofitClient.BASE_URL();
    private boolean isImageSelected=false, isImageClicked=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        Images
        imgOpenCamera = findViewById(R.id.imgOpenCamera);
        imgOpenCamera.setOnClickListener(this);
        imgOpenGallery = findViewById(R.id.imgOpenGallery);
        imgOpenGallery.setOnClickListener(this);
        imgUserImage = findViewById(R.id.imgUserProfileImage);

//        TextBox
        etFullname = findViewById(R.id.etFfullname);
        etEmail = findViewById(R.id.etEmail);
        etMobile = findViewById(R.id.etMobile);
        etAddress = findViewById(R.id.etAddress);

//        buttons
        btnUdpdateProfile = findViewById(R.id.btnUpdateProfile);
        btnUdpdateProfile.setOnClickListener(this);

//        Shared preference

        sharedPreferences = getSharedPreferences("LOGGED_IN_USER_DATA", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        LoggedInUseremail = sharedPreferences.getString("LOGGED_IN_USER_EMAIL", "");
        LoggedInUsermobile = sharedPreferences.getString("LOGGED_IN_USER_MOBILE", "");
        LoggedInUserfullname = sharedPreferences.getString("LOGGED_IN_USER_FULLNAME", "");
        LoggedInUserimage = sharedPreferences.getString("LOGGED_IN_USER_IMAGE", "");
        LoggedInUseraddress = sharedPreferences.getString("LOGGED_IN_USER_ADDRESS", "");
        LoggedInUserid = sharedPreferences.getString("LOGGED_IN_USER_ID", "");

        etFullname.setText(LoggedInUserfullname);
        etMobile.setText(LoggedInUsermobile);
        etAddress.setText(LoggedInUseraddress);
        etEmail.setText(LoggedInUseremail);


//        handler
        handler = new Handler();

        Picasso.with(this).load(BASE_URL + "assets/images/" + LoggedInUserimage).into(imgUserImage);


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imgOpenCamera) {
            Intent imageCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(imageCapture, 2);

        } else if (view.getId() == R.id.imgOpenGallery) {
            OpenGallery();
        } else if (view.getId() == R.id.btnUpdateProfile) {
            validateUserDetails();
        }
    }

    private void validateUserDetails() {

        final String fullname = etFullname.getText().toString();
        final String email = etEmail.getText().toString();
        final String mobile = etMobile.getText().toString();
        final String address = etAddress.getText().toString();

        if (fullname.isEmpty()) {
            etFullname.setError("Please enter your name");
            etFullname.requestFocus();
        } else if (!fullname.isEmpty() && !fullname.matches("[a-z a-z A-Z A-Z]+")) {
            etFullname.setError("Please enter a valid name");
            etFullname.requestFocus();
        } else if (email.isEmpty()) {
            etEmail.setError("Please enter your email");
            etEmail.requestFocus();
        } else if (!email.isEmpty() && !email.matches("^[0-9a-zA-Z!#$%&;'*+\\-/\\=\\?\\^_`\\.{|}~]{1,64}@[0-9a-zA-Z]{1,255}\\.[a-zA-Z]{1,10}")) {
            etEmail.setError("Email is invalid, please match the email format");
            etEmail.requestFocus();
        } else if (mobile.isEmpty()) {
            etMobile.setError("Please enter your mobile number");
            etMobile.requestFocus();
        } else if (!mobile.isEmpty() && !mobile.matches("[+ 0-9].{6,15}")) {
            etMobile.setError("Phone number can include + and must be minimum character up to 7 and maximum up to 10 character only");
            etMobile.requestFocus();
        } else if (address.isEmpty()) {
            etAddress.setError("Please enter your addresss");
            etAddress.requestFocus();
        } else {

            if (imageUri == null) {
                UpdateProfile(fullname, email, address, mobile);
            } else {
                addImage(bitmap);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        UpdateProfile(fullname, email, address, mobile);
                    }
                }, 2000);
            }

        }
    }


    private void OpenGallery() {
        Intent gallery = new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(gallery, "Choose image"), PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            imageUri = data.getData();


//            pick image from camera

            try {
                bitmap = MediaStore.Images.Media
                        .getBitmap(getContentResolver(), imageUri);
                imgUserImage.setImageBitmap(bitmap);


            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            getImagePath(data);
            isImageClicked=true;
            addImage(bitmap);
        }
    }

    private void getImagePath(Intent data) {
        bitmap = (Bitmap) data.getExtras().get("data");
        imgUserImage.setImageBitmap(bitmap);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        isImageSelected=true;
        addImage(bitmap);
    }


    private void addImage(Bitmap bm) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bytes = stream.toByteArray();


        try {

            File file = new File(this.getCacheDir(), "image.jpeg");
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.flush();
            fos.close();

            RequestBody rb = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), rb);

            UserAPI img_api = retrofit.create(UserAPI.class);

            Call<USERIMAGEMODEL> call = img_api.UploadUserImage(body);


            call.enqueue(new Callback<USERIMAGEMODEL>() {
                @Override
                public void onResponse(Call<USERIMAGEMODEL> call, Response<USERIMAGEMODEL> response) {
                    String TotalImagepath = response.body().getImage();
                    if (TotalImagepath.isEmpty()) {
                        Toast.makeText(Profile.this, "Failed to upload image please try again", Toast.LENGTH_SHORT).show();
                    }

                    if (response.isSuccessful()) {
                        getImageNameFromServer = TotalImagepath;

                    } else {
                        if (TotalImagepath.isEmpty()) {
                            Toast.makeText(Profile.this, "Failed to upload image please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<USERIMAGEMODEL> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setUpdatedUserDataToSharedPreference(String Email, String Mobile, String FullName, String address, String image, String id) {
        editor.putBoolean("IS_LOGGED_IN", true);
        editor.putString("LOGGED_IN_USER_EMAIL", Email);
        editor.putString("LOGGED_IN_USER_MOBILE", Mobile);
        editor.putString("LOGGED_IN_USER_FULLNAME", FullName);
        editor.putString("LOGGED_IN_USER_IMAGE", image);
        editor.putString("LOGGED_IN_USER_ADDRESS", address);
        editor.putString("LOGGED_IN_USER_ID", id);
        editor.commit();


    }


    private void UpdateProfile(final String fullname, final String email, final String address, final String mobile) {

        final String userid = LoggedInUserid;



        if (getImageNameFromServer == null) {
            getImageNameFromServer = LoggedInUserimage;
        }

       if (isImageClicked && getImageNameFromServer==LoggedInUserimage){
           Toast.makeText(this, "Failed to upload image try again", Toast.LENGTH_SHORT).show();
       }

       if (isImageSelected && getImageNameFromServer==LoggedInUserimage){
           Toast.makeText(this, "Failed to upload image try again", Toast.LENGTH_SHORT).show();
       }

        retrofit = retrofitClient.createInstance();
        userAPI = retrofit.create(UserAPI.class);

        Call<Object> updateUser = userAPI.updateUser(new UserModel(userid, fullname, email, mobile, address, "", getImageNameFromServer));

        updateUser.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                int statusCode = response.code();
                if (response.isSuccessful()) {
                    if (statusCode == 200) {
                        setUpdatedUserDataToSharedPreference(email, mobile, fullname, address, getImageNameFromServer, userid);
                        Toast.makeText(getApplicationContext(), "Profile updated", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Profile.this, "Cannot update profile", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(Profile.this, "Something went wrong", Toast.LENGTH_SHORT).show();

            }
        });

    }
}
