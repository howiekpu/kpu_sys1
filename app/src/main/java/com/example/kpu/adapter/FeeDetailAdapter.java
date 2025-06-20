package com.example.kpu.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kpu.R;
import com.example.kpu.bean.FreeRecord;

import java.util.List;

public class FeeDetailAdapter extends RecyclerView.Adapter<FeeDetailAdapter.DateViewHolder> {

    private List<FreeRecord> dateList;  // List of free records to display

    // Constructor to initialize the adapter with a list of free records
    public FeeDetailAdapter(List<FreeRecord> dateList) {
        this.dateList = dateList;
    }

    @NonNull
    @Override
    public DateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fee_detail, parent, false);
        return new DateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DateViewHolder holder, int position) {
        FreeRecord freeRecord = dateList.get(position);  // Get the FreeRecord for the given position

        // Set text values for the various TextViews
        holder.tvDate.setText(freeRecord.getTimeDate());
        holder.tvTitle.setText(freeRecord.getTitle());
        holder.tvPaidFee.setText(freeRecord.getPaidFee());
        holder.tvPaidFee2.setText(freeRecord.getPaidFee());
        holder.tvTotalFee.setText(freeRecord.getTotalFee());
        holder.tvExtraFee.setText(freeRecord.getExtraFee());
        holder.tvLateCharges.setText(freeRecord.getLateCharges());
        holder.tvDiscountFree.setText(freeRecord.getDiscountFee());
        holder.tvDiscount.setText("Discount(" + freeRecord.getDiscount() + ")");

        // Set up the click listener for the down/up icon to toggle visibility of the hidden details
        holder.rlDownUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toggle the visibility of the additional details (llHide)
                if (holder.llHide.getVisibility() == View.GONE) {
                    holder.llHide.setVisibility(View.VISIBLE);  // Show the hidden details
                    holder.ivDownUp.setImageResource(R.mipmap.ic_sjt);  // Change icon to indicate it's open
                } else {
                    holder.llHide.setVisibility(View.GONE);  // Hide the additional details
                    holder.ivDownUp.setImageResource(R.mipmap.ic_xjt);  // Change icon to indicate it's closed
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dateList.size();  // Return the number of items in the list
    }

    public static class DateViewHolder extends RecyclerView.ViewHolder {
        // Declare UI components for the item layout
        RelativeLayout rlDownUp;
        LinearLayout llHide;
        ImageView ivDownUp;
        TextView tvDate, tvTitle, tvPaidFee, tvTotalFee, tvExtraFee, tvLateCharges, tvDiscount, tvDiscountFree, tvPaidFee2;

        // Constructor to initialize UI components
        public DateViewHolder(View itemView) {
            super(itemView);
            rlDownUp = itemView.findViewById(R.id.rlDownUp);  // RelativeLayout to toggle visibility
            llHide = itemView.findViewById(R.id.llHide);  // LinearLayout containing hidden details
            ivDownUp = itemView.findViewById(R.id.ivDownUp);  // ImageView for toggle icon
            tvDate = itemView.findViewById(R.id.tvDate);  // TextView for date
            tvTitle = itemView.findViewById(R.id.tvTitle);  // TextView for title
            tvPaidFee = itemView.findViewById(R.id.tvPaidFee);  // TextView for paid fee
            tvTotalFee = itemView.findViewById(R.id.tvTotalFee);  // TextView for total fee
            tvExtraFee = itemView.findViewById(R.id.tvExtraFee);  // TextView for extra fee
            tvLateCharges = itemView.findViewById(R.id.tvLateCharges);  // TextView for late charges
            tvDiscount = itemView.findViewById(R.id.tvDiscount);  // TextView for discount
            tvDiscountFree = itemView.findViewById(R.id.tvDiscountFree);  // TextView for discount fee
            tvPaidFee2 = itemView.findViewById(R.id.tvPaidFee2);  // TextView for paid fee 2
        }
    }
}
