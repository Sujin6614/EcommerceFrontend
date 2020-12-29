package com.example.eshoppingecommerce.ADAPTER;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eshoppingecommerce.ACTIVITY.Orders;
import com.example.eshoppingecommerce.API.OrderAPI;
import com.example.eshoppingecommerce.RETROFIT_OBJECT.RetrofitClient;
import com.example.eshoppingecommerce.ResponsesModel.OrdersResponseModel;
import com.example.eshoppingecommerce.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ItemsViewHolder>  {


    private final static RetrofitClient retrofitClient = new RetrofitClient();
    private final static String BASE_URL = retrofitClient.BASE_URL();

    private Retrofit retrofit;
    private OrderAPI orderAPI;

    private List<OrdersResponseModel> ordersResponseModelList;

    private Context mContext;

    public OrdersAdapter(List<OrdersResponseModel> ordersResponseModelList, Context mContext) {
        this.ordersResponseModelList = ordersResponseModelList;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public OrdersAdapter.ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.order_recycler_view_sample_layout, viewGroup, false);

        return new ItemsViewHolder(itemView);

    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull OrdersAdapter.ItemsViewHolder itemsViewHolder, int i) {
        final OrdersResponseModel ordersResponseModel = ordersResponseModelList.get(i);

        itemsViewHolder.orderProductPrice.setText(" "+ ordersResponseModel.getPrice());
        itemsViewHolder.orderProductName.setText(ordersResponseModel.getProductname());

        Picasso.with(mContext).load(BASE_URL + "assets/images/" + ordersResponseModel.getImage()).into(itemsViewHolder.orderProudctImage);


        boolean ispaid= ordersResponseModel.isPayment();
        if (ispaid==true){
            itemsViewHolder.paymentStatus.setText("Paid");
            itemsViewHolder.paymentStatus.setTextColor(Color.parseColor("#1F8139"));
        }
        else if (ispaid==false){
            itemsViewHolder.paymentStatus.setText("Unpaid");
            itemsViewHolder.paymentStatus.setTextColor(Color.parseColor("#CC0B00"));
        }

        itemsViewHolder.imgCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id= ordersResponseModel.get_id();

                CancelOrder(id);
            }
        });




    }

    @Override
    public int getItemCount() {
        return ordersResponseModelList.size();
    }


    public class ItemsViewHolder extends RecyclerView.ViewHolder {

        public TextView orderProductPrice, orderProductName, paymentStatus;
        public ImageView orderProudctImage,  imgCancelOrder;


        public ItemsViewHolder(@NonNull View itemView) {
            super(itemView);

            orderProductName=itemView.findViewById(R.id.orderProductname);
            orderProductPrice=itemView.findViewById(R.id.orderProductprice);
            orderProudctImage=itemView.findViewById(R.id.imgOrderedImage);
            paymentStatus=itemView.findViewById(R.id.paymentStatus);

            imgCancelOrder=  itemView.findViewById(R.id.imgCancelOrder);

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
