package com.example.kpu.adapter;

import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kpu.R;
import com.example.kpu.bean.CalendarSchedule;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.DateViewHolder> {

    private List<CalendarSchedule> dateList;
    private OnDateClickListener onDateClickListener;  // Add a listener to handle the click event

    // Constructor accepting the list of CalendarSchedule and a listener for date clicks
    public CalendarAdapter(List<CalendarSchedule> dateList, OnDateClickListener onDateClickListener) {
        this.dateList = dateList;
        this.onDateClickListener = onDateClickListener;
    }

    @NonNull
    @Override
    public DateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_calendar, parent, false);
        return new DateViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull DateViewHolder holder, int position) {
        CalendarSchedule calendarSchedule = dateList.get(position);

        // Check if the date is not empty and set the day part of the date
        if(!TextUtils.isEmpty(calendarSchedule.getDate())){
            holder.dateText.setText(calendarSchedule.getDate().split("-")[2]);
        }

        // Set the default text color to black
        holder.dateText.setTextColor(Color.parseColor("#000000"));

        // If the date is empty, hide the TextView
        if (calendarSchedule.getDate().equals("")) {
            holder.dateText.setVisibility(View.GONE);
        } else {
            holder.dateText.setVisibility(View.VISIBLE);

            // If the date is today, change its appearance
            if(isToday(calendarSchedule.getDate())){
                holder.dateText.setTextColor(Color.WHITE);
                holder.dateText.setBackgroundResource(R.drawable.shape_date_month);
            }

            // Check the number of schedules for the day and set the visibility of the oval indicators
            if(calendarSchedule.getScheduleList().size() == 0){
                holder.tvOval1.setVisibility(View.GONE);
                holder.tvOval2.setVisibility(View.GONE);
                holder.tvOval3.setVisibility(View.GONE);
            } else if(calendarSchedule.getScheduleList().size() == 1){
                holder.tvOval1.setVisibility(View.VISIBLE);
                holder.tvOval2.setVisibility(View.GONE);
                holder.tvOval3.setVisibility(View.GONE);
            } else if(calendarSchedule.getScheduleList().size() == 2){
                holder.tvOval1.setVisibility(View.VISIBLE);
                holder.tvOval2.setVisibility(View.VISIBLE);
                holder.tvOval3.setVisibility(View.GONE);
            } else {
                holder.tvOval1.setVisibility(View.VISIBLE);
                holder.tvOval2.setVisibility(View.VISIBLE);
                holder.tvOval3.setVisibility(View.VISIBLE);
            }
        }

        // Set a click listener for the date TextView
        holder.dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null != onDateClickListener){
                    onDateClickListener.onDateClick(position); // Trigger the callback when the date is clicked
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        // Return the total number of dates
        return dateList.size();
    }

    public static class DateViewHolder extends RecyclerView.ViewHolder {
        TextView dateText, tvOval1, tvOval2, tvOval3;

        public DateViewHolder(View itemView) {
            super(itemView);
            // Initialize the TextViews for displaying the date and schedule indicators
            dateText = itemView.findViewById(R.id.tvDate);
            tvOval1 = itemView.findViewById(R.id.tvOval1);
            tvOval2 = itemView.findViewById(R.id.tvOval2);
            tvOval3 = itemView.findViewById(R.id.tvOval3);
        }
    }

    // Interface for handling date click events
    public interface OnDateClickListener {
        void onDateClick(int position);  // Method to handle the date click event
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean isToday(String dateString) {
        // Get today's date
        LocalDate today = LocalDate.now();

        // Create a date formatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Convert the date string to LocalDate
        LocalDate calendarDate = LocalDate.parse(dateString, formatter);

        // Check if the date is today
        return calendarDate.isEqual(today);
    }
}
