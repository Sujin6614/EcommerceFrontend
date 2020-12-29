package com.example.eshoppingecommerce.ACTIVITY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.eshoppingecommerce.ADAPTER.BrowseProductAdapter;
import com.example.eshoppingecommerce.API.ProductsAPI;
import com.example.eshoppingecommerce.MODEL.ProductModel;
import com.example.eshoppingecommerce.R;
import com.example.eshoppingecommerce.RETROFIT_OBJECT.RetrofitClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BrowseFood extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private BrowseProductAdapter browseProductAdapter;
    private RecyclerView recyclerView;
    private RetrofitClient retrofitClient = new RetrofitClient();
    private ProductsAPI productsApi;
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_food);

        getSupportActionBar().setTitle("Browse phone");
        setupBottomNav();


        recyclerView = findViewById(R.id.browseProductRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        getProductsDataFromAPI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tool_bar_menu, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint("Type product name ...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                browseProductAdapter.getFilter().filter(newText);
                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    private void setupBottomNav() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_car);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_bike:
                        startActivity(new Intent(getApplicationContext(), BrowsePhone.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.navigation_bus:
                        startActivity(new Intent(getApplicationContext(), BrowseCloth.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.navigation_truck:
                        startActivity(new Intent(getApplicationContext(), BrowseFurniture.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.navigation_dashboard:
                        startActivity(new Intent(getApplicationContext(), UserDashboard.class));
                        overridePendingTransition(0, 0);
                        return true;

                }
                return false;
            }
        });
    }


    private void getProductsDataFromAPI() {

        String category="car";

        retrofit = retrofitClient.createInstance();
        productsApi = retrofit.create(ProductsAPI.class);

        Call<List<ProductModel>> productsData = productsApi.getProductsBycategory(category);

        productsData.enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(Call<List<ProductModel>> call, Response<List<ProductModel>> response) {
                int statusCode = response.code();
                if (response.isSuccessful()) {
                    if (statusCode == 200) {
                        browseProductAdapter = new BrowseProductAdapter(response.body(), getApplicationContext());
                        recyclerView.setAdapter(browseProductAdapter);
                    } else if (statusCode == 500) {
                        Toast.makeText(getApplicationContext(), "Cannot fetch data please try again " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ProductModel>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Something went wrong " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
