package com.example.kpu.adapter;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kpu.R;
import com.example.kpu.bean.Schedule;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CalendarEventAdapter extends RecyclerView.Adapter<CalendarEventAdapter.DateViewHolder> {

    private List<Schedule> dateList;  // List of schedules (events)
    private OnItemClickListener onItemClickListener;  // Listener for item click events

    // Constructor to initialize the adapter with the event list and item click listener
    public CalendarEventAdapter(List<Schedule> dateList, OnItemClickListener onItemClickListener) {
        this.dateList = dateList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public DateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_calendar_event, parent, false);
        return new DateViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull DateViewHolder holder, int position) {
        Schedule schedule = dateList.get(position);  // Get the schedule at the given position
        holder.tvContent.setText(schedule.getContent());  // Set content text
        holder.tvTitle.setText(schedule.getTitle());  // Set title text
        String date = schedule.getCreateTime();  // Get the date of the event
        holder.dateText.setText(getMonthAbbreviation(date));  // Set the abbreviated month for the date

        // Set click listeners for update and delete buttons
        holder.tvUpdate.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onUpdateClick(position);  // Trigger the update click listener
            }
        });

        holder.tvDelete.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onDeleteClick(position);  // Trigger the delete click listener
            }
        });
    }

    @Override
    public int getItemCount() {
        return dateList.size();  // Return the size of the event list
    }

    public static class DateViewHolder extends RecyclerView.ViewHolder {
        TextView dateText, tvTitle, tvContent, tvUpdate, tvDelete;

        public DateViewHolder(View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.tvDate);  // Initialize the date TextView
            tvTitle = itemView.findViewById(R.id.tvTitle);  // Initialize the title TextView
            tvContent = itemView.findViewById(R.id.tvContent);  // Initialize the content TextView
            tvUpdate = itemView.findViewById(R.id.tvUpdate);  // Initialize the update button
            tvDelete = itemView.findViewById(R.id.tvDelete);  // Initialize the delete button
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getMonthAbbreviation(String date) {
        // Define the date formatter to parse the date string
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Parse the date string into a LocalDate object
        LocalDate localDate = LocalDate.parse(date, formatter);

        // Get the month number from the date
        int month = localDate.getMonthValue();

        // Define month abbreviations
        String monthAbbreviation = "JAN,FEB,MAR,APR,MAY,JUN,JUL,AUG,SEP,OCT,NOV,DEC";

        // Split the month abbreviations and return the corresponding month abbreviation
        String[] months = monthAbbreviation.split(",");
        return months[month - 1];  // Subtract 1 because array indices start from 0
    }

    // Interface to handle update and delete item actions
    public interface OnItemClickListener {
        void onUpdateClick(int position);  // Handle update action
        void onDeleteClick(int position);  // Handle delete action
    }
}
