package com.example.kpu.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kpu.R;
import com.example.kpu.bean.Attendance;

import java.util.List;
public class AttendanceDetailAdapter extends RecyclerView.Adapter<AttendanceDetailAdapter.DateViewHolder> {

    private List<Attendance> dateList;
    private OnDateClickListener onDateClickListener;  // Add a listener to handle the click event

    public AttendanceDetailAdapter(List<Attendance> dateList, OnDateClickListener onDateClickListener) {
        this.dateList = dateList;
        this.onDateClickListener = onDateClickListener;  // Initialize the listener
    }

    @NonNull
    @Override
    public DateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attendance_detail, parent, false);
        return new DateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DateViewHolder holder, int position) {
        Attendance attendance = dateList.get(position);
        holder.dateText.setText(attendance.getDate());

        // Handle visibility and color settings based on the attendance status
        if (attendance.getDate().equals("")) {
            holder.dateText.setVisibility(View.GONE);  // Hide if date is empty
        } else {
            holder.dateText.setVisibility(View.VISIBLE);  // Show if date is available
            holder.dateText.setText(attendance.getDate());
            if (attendance.getStatus() == -1) {
                holder.dateText.setTextColor(Color.parseColor("#c5c7ce"));
            } else if (attendance.getStatus() == 1) {
                holder.dateText.setTextColor(Color.parseColor("#000000"));
                holder.dateText.setBackgroundResource(R.drawable.shape_present_10dp);
            } else if (attendance.getStatus() == 2) {
                holder.dateText.setTextColor(Color.parseColor("#000000"));
                holder.dateText.setBackgroundResource(R.drawable.shape_absent_10dp);
            } else if (attendance.getStatus() == 3) {
                holder.dateText.setTextColor(Color.parseColor("#000000"));
                holder.dateText.setBackgroundResource(R.drawable.shape_leave_10dp);
            }
        }

        // Set a click listener for the dateText
        holder.dateText.setOnClickListener(v -> {
            if (onDateClickListener != null) {
                onDateClickListener.onDateClick(attendance,position);  // Notify the listener when the date is clicked
            }
        });
    }

    @Override
    public int getItemCount() {
        return dateList.size();
    }

    // ViewHolder class
    public static class DateViewHolder extends RecyclerView.ViewHolder {
        TextView dateText;

        public DateViewHolder(View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.tvDate);
        }
    }

    // Interface for handling date click events
    public interface OnDateClickListener {
        void onDateClick(Attendance attendance,int position);  // Method to handle the date click event
    }
}
