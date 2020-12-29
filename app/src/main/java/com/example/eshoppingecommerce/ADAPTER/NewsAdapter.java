package com.example.eshoppingecommerce.ADAPTER;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eshoppingecommerce.ACTIVITY.Orders;
import com.example.eshoppingecommerce.API.OrderAPI;
import com.example.eshoppingecommerce.MODEL.NewsModel;
import com.example.eshoppingecommerce.R;
import com.example.eshoppingecommerce.RETROFIT_OBJECT.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ItemsViewHolder>  {


    private final static RetrofitClient retrofitClient = new RetrofitClient();
    private final static String BASE_URL = retrofitClient.BASE_URL();

    private Retrofit retrofit;
    private OrderAPI orderAPI;

    private List<NewsModel> newsModelList;

    private Context mContext;

    public NewsAdapter(List<NewsModel> newsModelList, Context mContext) {
        this.newsModelList = newsModelList;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public NewsAdapter.ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.news_recycler_view_sample_layout, viewGroup, false);

        return new ItemsViewHolder(itemView);

    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.ItemsViewHolder itemsViewHolder, int i) {
        final NewsModel newsModel = newsModelList.get(i);

        itemsViewHolder.newsDate.setText(" "+ newsModel.getDate());
        itemsViewHolder.newsDescription.setText(" "+ newsModel.getDescription());
        itemsViewHolder.newsHeading.setText(" "+newsModel.getHeading());


    }

    @Override
    public int getItemCount() {
        return newsModelList.size();
    }


    public class ItemsViewHolder extends RecyclerView.ViewHolder {

        public TextView newsHeading, newsDescription, newsDate;



        public ItemsViewHolder(@NonNull View itemView) {
            super(itemView);

           newsHeading=itemView.findViewById(R.id.newsHeading);
            newsDescription=itemView.findViewById(R.id.newsDescription);
            newsDate=itemView.findViewById(R.id.newsDate);

        }
    }


    private void CancelOrder(String id){


        retrofit = retrofitClient.createInstance();
        orderAPI = retrofit.create(OrderAPI.class);
        Call<Object> getCartData = orderAPI.cancelOrder(id);

        getCartData.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()){
                    Intent intent = new Intent(mContext, Orders.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });



    }

}
