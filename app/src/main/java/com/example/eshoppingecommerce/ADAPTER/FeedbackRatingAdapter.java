package com.example.eshoppingecommerce.ADAPTER;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eshoppingecommerce.ResponsesModel.FeedbackresponseModel;
import com.example.eshoppingecommerce.R;

import java.util.List;

public class FeedbackRatingAdapter extends RecyclerView.Adapter<FeedbackRatingAdapter.ItemsViewHolder>  {

    private List<FeedbackresponseModel> feedbackresponseModelList;

    private Context mContext;

    public FeedbackRatingAdapter(List<FeedbackresponseModel> feedbackresponseModelList, Context mContext) {
        this.feedbackresponseModelList = feedbackresponseModelList;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public FeedbackRatingAdapter.ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.feedback_recycler_view_sample_layout, viewGroup, false);

        return new ItemsViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackRatingAdapter.ItemsViewHolder itemsViewHolder, int i) {
        final FeedbackresponseModel feedbackresponseModel = feedbackresponseModelList.get(i);


            itemsViewHolder.username.setText(feedbackresponseModel.getUsername());
            itemsViewHolder.feedback.setText(feedbackresponseModel.getFeedback());
            itemsViewHolder.rating.setText(" "+feedbackresponseModel.getRating());


    }

    @Override
    public int getItemCount() {
        return feedbackresponseModelList.size();
    }


    public class ItemsViewHolder extends RecyclerView.ViewHolder {

        public TextView username, feedback, rating;

        public ItemsViewHolder(@NonNull View itemView) {
            super(itemView);

                username= itemView.findViewById(R.id.txtusername);
                feedback=itemView.findViewById(R.id.txtuserfeedback);
                rating=itemView.findViewById(R.id.txtuserrating);


        }
    }
}
