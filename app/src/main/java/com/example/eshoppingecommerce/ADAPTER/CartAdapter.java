package com.example.eshoppingecommerce.ADAPTER;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eshoppingecommerce.ACTIVITY.Cart;
import com.example.eshoppingecommerce.ACTIVITY.ConfirmOrder;
import com.example.eshoppingecommerce.API.CartAPI;
import com.example.eshoppingecommerce.R;
import com.example.eshoppingecommerce.RETROFIT_OBJECT.RetrofitClient;
import com.example.eshoppingecommerce.ResponsesModel.CartResponseModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ItemsViewHolder> {

    private RetrofitClient retrofitClient = new RetrofitClient();
    private String BASE_URL = retrofitClient.BASE_URL();
    private Retrofit retrofit;
    private CartAPI cartAPI;

    private List<CartResponseModel> cartResponseModelList;

    private Context mContext;


    public CartAdapter(List<CartResponseModel> cartResponseModelList, Context mContext) {
        this.cartResponseModelList = cartResponseModelList;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public CartAdapter.ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cart_recycler_view_sample_layout, viewGroup, false);

        return new ItemsViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ItemsViewHolder itemsViewHolder, int i) {
        final CartResponseModel cartResponseModel = cartResponseModelList.get(i);


        itemsViewHolder.cartProductName.setText(cartResponseModel.getName());
        itemsViewHolder.cartProductPrice.setText("Rs. " + cartResponseModel.getPrice());

        Picasso.with(mContext).load(BASE_URL + "assets/images/" + cartResponseModel.getImage()).into(itemsViewHolder.cartProudctImage);


        itemsViewHolder.imgRemoveFromCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (view.getId() == R.id.imgRemoveFromCart) {
                    String id = cartResponseModel.getId();
                    removeFromCart(id);
                }
            }
        });

        itemsViewHolder.imgProceedOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.imgProceedOrder) {
                    Intent intent = new Intent(mContext, ConfirmOrder.class);
                    intent.putExtra("id", cartResponseModel.getId());
                    intent.putExtra("userid", cartResponseModel.getUserid());
                    intent.putExtra("productId", cartResponseModel.getProductid());
                    intent.putExtra("productName", cartResponseModel.getName());
                    intent.putExtra("productPrice", String.valueOf(cartResponseModel.getPrice()));
                    intent.putExtra("productImage", cartResponseModel.getImage());
                    intent.putExtra("username", cartResponseModel.getUsername());
                    intent.putExtra("email", cartResponseModel.getEmail());
                    intent.putExtra("mobile", cartResponseModel.getMobile());
                    intent.putExtra("address", cartResponseModel.getAddress());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return cartResponseModelList.size();
    }

    private void removeFromCart(String id) {


        retrofit = retrofitClient.createInstance();
        cartAPI = retrofit.create(CartAPI.class);
        Call<Object> getCartData = cartAPI.removeFromCart(id);

        getCartData.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
               if (response.isSuccessful()){
                   Intent intent = new Intent(mContext, Cart.class);
                   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                   mContext.startActivity(intent);

               }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });


    }

    public class ItemsViewHolder extends RecyclerView.ViewHolder {

        public TextView cartProductPrice, cartProductName;
        public ImageView cartProudctImage, imgProceedOrder, imgRemoveFromCart;


        public ItemsViewHolder(@NonNull View itemView) {
            super(itemView);

            cartProductName = itemView.findViewById(R.id.cartProductname);
            cartProductPrice = itemView.findViewById(R.id.cartProductprice);
            cartProudctImage = itemView.findViewById(R.id.cartProductImage);

            imgProceedOrder = itemView.findViewById(R.id.imgProceedOrder);
            imgRemoveFromCart = itemView.findViewById(R.id.imgRemoveFromCart);

        }
    }

}
