package com.example.kpu.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kpu.R;
import com.example.kpu.bean.AttendanceItem;

import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder> {

    private List<AttendanceItem> attendanceItems;

    // Constructor to initialize the attendance items list
    public AttendanceAdapter(List<AttendanceItem> attendanceItems) {
        this.attendanceItems = attendanceItems;
    }

    // Interface to handle the date item click event
    public interface OnItemDateClickListener {
        void onItemDateClick(int position, int subPosition);
    }

    private OnItemDateClickListener onItemDateClickListener;

    // Setter for OnItemDateClickListener
    public void setOnItemDateClickListener(OnItemDateClickListener onItemDateClickListener){
        this.onItemDateClickListener = onItemDateClickListener;
    }

    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attendance, parent, false);
        return new AttendanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // Get the attendance item for the current position
        AttendanceItem item = attendanceItems.get(position);
        holder.yearTextView.setText(item.getYear()); // Set the year text

        // Set the adapter for the child RecyclerView (AttendanceSubAdapter)
        AttendanceSubAdapter subAdapter = new AttendanceSubAdapter(item.getSubItems(), new AttendanceSubAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int subPosition) {
                // Handle the click on a sub item and notify the listener
                if (null != onItemDateClickListener) {
                    onItemDateClickListener.onItemDateClick(position, subPosition);
                }
            }
        });
        holder.secondRecyclerView.setAdapter(subAdapter); // Set the sub-adapter to the child RecyclerView
    }

    @Override
    public int getItemCount() {
        // Return the number of items in the attendance list
        return attendanceItems.size();
    }

    public static class AttendanceViewHolder extends RecyclerView.ViewHolder {
        TextView yearTextView;
        RecyclerView secondRecyclerView;

        public AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views
            yearTextView = itemView.findViewById(R.id.yearTextView);
            secondRecyclerView = itemView.findViewById(R.id.secondRecycleView);
            // Set a linear layout manager for the child RecyclerView
            secondRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        }
    }
}
