package com.example.kpu.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kpu.App;
import com.example.kpu.R;

import com.example.kpu.adapter.CalendarAdapter;
import com.example.kpu.adapter.CalendarEventAdapter;
import com.example.kpu.bean.CalendarSchedule;
import com.example.kpu.bean.Schedule;
import com.example.kpu.db.UserDbHelper;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarActivity extends AppCompatActivity {

    private TextView tvLeft;
    private RecyclerView recyclerView,recyclerViewEvent;
    private CalendarAdapter calendarAdapter;
    private CalendarEventAdapter calendarEventAdapter;
    private TextView tvDate;
    private TextView tvRightText;
    private ImageView ivLeft,ivRight;
    private String[] months = {
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
    };
    private int month, year;
    private UserDbHelper dbHelper;
    private List<CalendarSchedule> allList = new ArrayList<>();
    private  List<Schedule> eventDayList = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerViewEvent = findViewById(R.id.recyclerViewEvent);
        tvDate =  findViewById(R.id.tvDate);
        tvLeft = findViewById(R.id.tvLeft);
        tvRightText = findViewById(R.id.tvRightText);
        ivLeft = findViewById(R.id.ivLeft);
        ivRight = findViewById(R.id.ivRight);
        tvLeft.setText("Calendar");  // Customize the text
        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvRightText.setVisibility(View.VISIBLE);
        recyclerViewEvent.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 7)); // 一行显示7个日期（星期一到星期天）
        // 获取当前年份和月份下标
        Calendar calendar = Calendar.getInstance();year = calendar.get(Calendar.YEAR);  // 获取当前年份
        tvRightText.setText(year+"");
        month = calendar.get(Calendar.MONTH);  // 获取当前月份（从0开始，0表示1月，1表示2月，以此类推）
        // 将月份下标转换为实际月份（+1）
        tvDate.setText(months[month] + " " + year);  // 显示当前月份和年份
        // 获取当前月份的所有日期
        dbHelper = new UserDbHelper(this);
        String monthString = (month + 1)<10?"0"+(month+1):(month + 1)+"";
        allList.addAll(dbHelper.getSchedulesForMonth(App.getLoginUser().getId(),year+"-"+monthString));
        calendarAdapter = new CalendarAdapter(allList, new CalendarAdapter.OnDateClickListener() {
            @Override
            public void onDateClick(int position) {
                eventListUpdate(position);
                showScheduleDialog(CalendarActivity.this, null, new OnScheduleConfirmedListener() {
                    @Override
                    public void onScheduleConfirmed(String title, String content) {
                        Schedule schedule = new Schedule();
                        String dayString = allList.get(position).getDate();
                        schedule.setCreateTime(dayString);
                        schedule.setTitle(title);
                        schedule.setContent(content);
                        schedule.setUserId(App.getLoginUser().getId());
                        dbHelper.addSchedule(schedule);
                        allList.clear();
                        String monthString = (month + 1)<10?"0"+(month+1):(month + 1)+"";
                        allList.addAll(dbHelper.getSchedulesForMonth(App.getLoginUser().getId(),year+"-"+monthString));
                        calendarAdapter.notifyDataSetChanged();
                        eventListUpdate(position);
                    }
                });
            }
        });
        recyclerView.setAdapter(calendarAdapter);

        calendarEventAdapter = new CalendarEventAdapter(eventDayList, new CalendarEventAdapter.OnItemClickListener() {
            @Override
            public void onUpdateClick(int position) {
                showScheduleDialog(CalendarActivity.this, eventDayList.get(position), new OnScheduleConfirmedListener() {
                    @Override
                    public void onScheduleConfirmed(String title, String content) {
                        Schedule schedule = eventDayList.get(position);
                        schedule.setTitle(title);
                        schedule.setContent(content);
                        dbHelper.updateSchedule(schedule);
                        eventDayList.get(position).setContent(content);
                        eventDayList.get(position).setTitle(title);
                        calendarEventAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onDeleteClick(int position) {
                new AlertDialog.Builder(CalendarActivity.this)
                        .setTitle("Confirm Deletion")
                        .setMessage("Are you sure you want to delete this event?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(dbHelper.deleteSchedule(eventDayList.get(position).getScheduleId())!= -1){
                                    eventDayList.remove(position);
                                    calendarEventAdapter.notifyItemRemoved(position);
                                    allList.clear();
                                    String monthString = (month + 1)<10?"0"+(month+1):(month + 1)+"";
                                    allList.addAll(dbHelper.getSchedulesForMonth(App.getLoginUser().getId(),year+"-"+monthString));
                                    calendarAdapter.notifyDataSetChanged();
                                    Toast.makeText(CalendarActivity.this, "success", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(CalendarActivity.this, "error", Toast.LENGTH_SHORT).show();
                                }

                            }
                        })
                        .setNegativeButton("No", null)  // If the user clicks "No", just dismiss the dialog
                        .show();
            }
        });
        recyclerViewEvent.setAdapter(calendarEventAdapter);
        setCurrentDate();
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(month == 0){
                    month = 11;
                }else{
                    month--;
                }
                String monthString = (month + 1)<10?"0"+(month+1):(month + 1)+"";
                allList.clear();
                allList.addAll(dbHelper.getSchedulesForMonth(App.getLoginUser().getId(),year+"-"+monthString));
                calendarAdapter.notifyDataSetChanged();
                eventDayList.clear();
                calendarEventAdapter.notifyDataSetChanged();
                setCurrentDate();
                tvDate.setText(months[month] + " " + year);  // 显示当前月份和年份
            }
        });
        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(month == 11){
                    month = 0;
                }else{
                    month++;
                }
                String monthString = (month + 1)<10?"0"+(month+1):(month + 1)+"";
                allList.clear();
                allList.addAll(dbHelper.getSchedulesForMonth(App.getLoginUser().getId(),year+"-"+monthString));
                calendarAdapter.notifyDataSetChanged();
                eventDayList.clear();
                calendarEventAdapter.notifyDataSetChanged();
                setCurrentDate();
                tvDate.setText(months[month] + " " + year);  // 显示当前月份和年份
            }
        });
    }

    private void setCurrentDate(){
        // Get the current date
        Date currentDate = new Date();
        // Create a SimpleDateFormat object to define the date format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(currentDate);
        for(int i = 0; i < allList.size(); i++){
            if(formattedDate.equals(allList.get(i).getDate())){
                eventListUpdate(i);
            }
        }
    }

    private void eventListUpdate(int position){
        eventDayList.clear();
        eventDayList.addAll(allList.get(position).getScheduleList());
        calendarEventAdapter.notifyDataSetChanged();
    }

    private List<String> getMonthDates(int year, int month) {
        List<String> dateList = new ArrayList<>();

        // Get the first day and total number of days of the month
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1); // Month starts from 0
        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK); // Get the first weekday of the month
        int totalDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH); // Get the total days in the month

        // Adjust firstDayOfWeek to Monday-Sunday order
        int emptyDays = (firstDayOfWeek == 1) ? 6 : firstDayOfWeek - 2; // If Sunday is 1, fill 6 empty days before
        for (int i = 0; i < emptyDays; i++) {
            dateList.add("");  // Fill with empty spaces
        }

        // Fill in actual dates
        for (int day = 1; day <= totalDaysInMonth; day++) {
            dateList.add(String.valueOf(day));
        }

        // Return the date list
        return dateList;
    }

    private interface OnScheduleConfirmedListener {
        void onScheduleConfirmed(String title, String content);
    }

    private void showScheduleDialog(Context context, Schedule schedule, final OnScheduleConfirmedListener listener) {
        // Get the layout file
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_schedule, null);

        // Get EditText and Button controls
        final EditText editTextTitle = dialogView.findViewById(R.id.editTextTitle);
        final EditText editTextContent = dialogView.findViewById(R.id.editTextContent);
        Button btnConfirm = dialogView.findViewById(R.id.btnConfirm);

        // If the passed Schedule object is not null, set default values for EditText
        if (schedule != null) {
            editTextTitle.setText(schedule.getTitle());
            editTextContent.setText(schedule.getContent());
        }

        // Create AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        // Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
        // Set the confirm button click event
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the title and content entered by the user
                String title = editTextTitle.getText().toString();
                String content = editTextContent.getText().toString();
                if(title.isEmpty() || content.isEmpty()){
                    return;
                }
                // Call the callback function to return the modified Schedule
                if (listener != null) {
                    listener.onScheduleConfirmed(title, content);
                }
                dialog.dismiss();
            }
        });
    }

}
