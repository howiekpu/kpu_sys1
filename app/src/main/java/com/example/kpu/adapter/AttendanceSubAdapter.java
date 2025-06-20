package com.example.kpu.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kpu.R;
import com.example.kpu.bean.AttendanceSubItem;

import java.util.List;

public class AttendanceSubAdapter extends RecyclerView.Adapter<AttendanceSubAdapter.AttendanceSubViewHolder> {

    private List<AttendanceSubItem> subItems;
    private OnItemClickListener onItemClickListener; // Interface member variable for item click listener

    // Constructor that accepts a callback interface parameter
    public AttendanceSubAdapter(List<AttendanceSubItem> subItems, OnItemClickListener onItemClickListener) {
        this.subItems = subItems;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public AttendanceSubViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attendance_second, parent, false);
        return new AttendanceSubViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceSubViewHolder holder, int position) {
        // Get the AttendanceSubItem for the current position
        AttendanceSubItem subItem = subItems.get(position);

        // Set the data for the TextViews
        holder.dateTextView.setText(subItem.getDate());
        holder.tv1.setText(subItem.getPresentNum() + "");
        holder.tv2.setText(subItem.getAbsentNum() + "");
        holder.tv3.setText(subItem.getLeaveNum() + "");

        // Set an OnClickListener on the itemView to trigger the callback when clicked
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(position);  // Call the callback method when an item is clicked
            }
        });
    }

    @Override
    public int getItemCount() {
        // Return the total number of sub items in the list
        return subItems.size();
    }

    public static class AttendanceSubViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView;
        TextView tv1, tv2, tv3;

        public AttendanceSubViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize the TextViews for displaying data
            dateTextView = itemView.findViewById(R.id.tvDate);
            tv1 = itemView.findViewById(R.id.tv1);
            tv2 = itemView.findViewById(R.id.tv2);
            tv3 = itemView.findViewById(R.id.tv3);
        }
    }

    // Interface to handle item click events
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
