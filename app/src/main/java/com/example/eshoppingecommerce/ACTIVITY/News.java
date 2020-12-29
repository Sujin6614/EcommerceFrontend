package com.example.eshoppingecommerce.ACTIVITY;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eshoppingecommerce.ADAPTER.NewsAdapter;
import com.example.eshoppingecommerce.API.NewsAPI;
import com.example.eshoppingecommerce.MODEL.NewsModel;
import com.example.eshoppingecommerce.R;
import com.example.eshoppingecommerce.RETROFIT_OBJECT.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class News extends AppCompatActivity {

    private RetrofitClient retrofitClient = new RetrofitClient();
    private String BASE_URL = retrofitClient.BASE_URL();
    private Retrofit retrofit;
    private NewsAPI newsAPI;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        getSupportActionBar().setTitle("News");

        recyclerView = findViewById(R.id.newsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        getNewDataFromAPI();
    }


    private void getNewDataFromAPI() {

        retrofit = retrofitClient.createInstance();
        newsAPI = retrofit.create(NewsAPI.class);

        Call<List<NewsModel>> newsData = newsAPI.getAllNews();

        newsData.enqueue(new Callback<List<NewsModel>>() {
            @Override
            public void onResponse(Call<List<NewsModel>> call, Response<List<NewsModel>> response) {
                int statusCode = response.code();

                if (statusCode == 200) {
                    recyclerView.setAdapter(new NewsAdapter(response.body(), getApplicationContext()));
                } else if (statusCode == 500) {
                    Toast.makeText(News.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<NewsModel>> call, Throwable t) {
                Toast.makeText(News.this, "Something went wrong", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
