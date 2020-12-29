package com.example.eshoppingecommerce.ADAPTER;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eshoppingecommerce.ACTIVITY.BrowseSingleProduct;
import com.example.eshoppingecommerce.MODEL.ProductModel;
import com.example.eshoppingecommerce.R;
import com.example.eshoppingecommerce.RETROFIT_OBJECT.RetrofitClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BrowseProductAdapter extends RecyclerView.Adapter<BrowseProductAdapter.ItemsViewHolder> implements Filterable {

    private List<ProductModel> productModelList;
    private List<ProductModel> productModelListFull;
    private final static RetrofitClient retrofit = new RetrofitClient();
    private final static String BASE_URL = retrofit.BASE_URL();

    private Context mContext;
    private Filter productListFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<ProductModel> filterList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filterList.addAll(productModelListFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (ProductModel productModelitem : productModelListFull) {
                    if (productModelitem.getProductname().toLowerCase().contains(filterPattern)) {
                        filterList.add(productModelitem);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filterList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            productModelList.clear();
            productModelList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public BrowseProductAdapter(List<ProductModel> productModelList, Context mContext) {
        this.productModelList = productModelList;
        this.productModelListFull = new ArrayList<>(productModelList);
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public BrowseProductAdapter.ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.products_recycler_view_sample_layout, viewGroup, false);

        return new ItemsViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull BrowseProductAdapter.ItemsViewHolder itemsViewHolder, int i) {
        final ProductModel productModel = productModelList.get(i);

        itemsViewHolder.productName.setText(productModel.getProductname());
        itemsViewHolder.productPrice.setText("Rs. " + productModel.getPrice());

//        Picasso.with(mContext).load("https://www.gstatic.com/webp/gallery3/1.png").into(itemsViewHolder.productImage);
        Picasso.with(mContext).load(BASE_URL + "assets/images/" + productModel.getImage()).into(itemsViewHolder.productImage);


        itemsViewHolder.productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.imgProduct) {
                    Intent intent = new Intent(mContext, BrowseSingleProduct.class);
                    intent.putExtra("productId", productModel.get_id());
                    intent.putExtra("productName", productModel.getProductname());
                    intent.putExtra("productPrice", String.valueOf(productModel.getPrice()));
                    intent.putExtra("productBrand", String.valueOf(productModel.getBrand()));
                    intent.putExtra("productImage", productModel.getImage());
                    intent.putExtra("productWarrenty", String.valueOf(productModel.getWarrenty()));
                    intent.putExtra("productBrand", productModel.getBrand());
                    intent.putExtra("productAddedDate", productModel.getDate());
                    intent.putExtra("productDescription", productModel.getDescription());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return productModelList.size();
    }

    @Override
    public Filter getFilter() {
        return productListFilter;
    }

    public class ItemsViewHolder extends RecyclerView.ViewHolder {
        public ImageView productImage;
        public TextView productName, productPrice;

        public ItemsViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.imgProduct);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);

        }
    }
}
