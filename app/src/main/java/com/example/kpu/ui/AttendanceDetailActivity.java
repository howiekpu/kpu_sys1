package com.example.kpu.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kpu.App;
import com.example.kpu.R;
import com.example.kpu.adapter.AttendanceDetailAdapter;
import com.example.kpu.bean.Attendance;
import com.example.kpu.db.UserDbHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
public class AttendanceDetailActivity extends AppCompatActivity {
    private TextView tvLeft, tv1, tv2, tv3;
    private RecyclerView recyclerView;
    private AttendanceDetailAdapter attendanceDetailAdapter;
    private TextView tvDate;
    private String[] months = {
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
    };
    private UserDbHelper dbHelper;
    private List<Attendance> attendanceList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_detail);
        dbHelper = new UserDbHelper(this);
        recyclerView = findViewById(R.id.recyclerView);
        tvDate = findViewById(R.id.tvDate);
        tvLeft = findViewById(R.id.tvLeft);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tvLeft.setText("Attendance");  // Set text for left button

        // Set a click listener for the left button to finish the activity (go back)
        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Finish the current activity and return to the previous one
            }
        });

        // Set the layout manager for the RecyclerView to display 7 dates per row (week from Monday to Sunday)
        recyclerView.setLayoutManager(new GridLayoutManager(this, 7));

        // Retrieve data passed from the previous activity
        int index = getIntent().getExtras().getInt("index");
        int year = Integer.parseInt(getIntent().getExtras().getString("year")); // Get the year
        int month = index + 1;   // Get the month (index+1 to match month number)
        String monthString = (month < 10 ? "0" + month : month + ""); // Format month to "01", "02", ...
        tvDate.setText(months[index] + " " + year);  // Set the date display (e.g., May 2025)

        // Retrieve attendance records for the specified month
        attendanceList.addAll(dbHelper.getAttendanceForMonth(App.getLoginUser().getId(), year + "-" + monthString));

        // Set the adapter for the RecyclerView to display attendance details
        attendanceDetailAdapter = new AttendanceDetailAdapter(attendanceList, new AttendanceDetailAdapter.OnDateClickListener() {
            @Override
            public void onDateClick(Attendance attendance, int position) {
                String dayTime = attendance.getDate();
                // If attendance status is -1 (not yet marked), show dialog to select attendance status
                if (attendance.getStatus() == -1) {
                    showAttendanceDialog(AttendanceDetailActivity.this, new OnAttendanceSelectedListener() {
                        @Override
                        public void onAttendanceSelected(int status) {
                            if (status != -1) {
                                // Record the selected attendance status in the database
                                dbHelper.recordAttendance(App.getLoginUser().getId(), year + "-" + monthString + "-" + (Integer.parseInt(dayTime) < 10 ? "0" + dayTime : dayTime), status);
                                attendanceList.get(position).setStatus(status); // Update the status of the selected attendance item
                                attendanceDetailAdapter.notifyDataSetChanged(); // Notify the adapter of data changes
                                setStatusData(); // Update the displayed attendance status data
                            }
                        }
                    });
                }
            }
        });

        recyclerView.setAdapter(attendanceDetailAdapter); // Set the adapter to the RecyclerView
        setStatusData(); // Set the initial attendance status data
    }

    // Set attendance status data (Present, Absent, Leave counts)
    private void setStatusData() {
        int status[] = countAttendanceStatus();
        tv1.setText(status[0] + "\nPresent"); // Display the number of "Present" records
        tv2.setText(status[1] + "\nAbsent");  // Display the number of "Absent" records
        tv3.setText(status[2] + "\nLeave");   // Display the number of "Leave" records
    }

    // Get all the dates for a given month (including empty dates at the beginning of the month to align with the weekdays)
    private List<String> getMonthDates(int year, int month) {
        List<String> dateList = new ArrayList<>();

        // Get the first day and total number of days in the given month
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1); // Set the month (month is 0-based)
        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK); // Get the weekday of the first day of the month
        int totalDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH); // Get the total days in the month

        // Adjust firstDayOfWeek for alignment (Sunday is 1, Monday is 2, etc.)
        int emptyDays = (firstDayOfWeek == 1) ? 6 : firstDayOfWeek - 2; // Add empty slots for weekdays before the 1st
        for (int i = 0; i < emptyDays; i++) {
            dateList.add("");  // Add empty strings for empty dates
        }

        // Add actual dates for the month
        for (int day = 1; day <= totalDaysInMonth; day++) {
            dateList.add(String.valueOf(day));  // Add actual dates (1, 2, 3,...)
        }

        // Return the list of dates
        return dateList;
    }

    // Count the number of occurrences of each attendance status (Present, Absent, Leave)
    private int[] countAttendanceStatus() {
        int countStatus1 = 0; // Present count
        int countStatus2 = 0; // Absent count
        int countStatus3 = 0; // Leave count

        // Loop through attendance records and count each status
        for (Attendance attendance : attendanceList) {
            switch (attendance.getStatus()) {
                case 1:
                    countStatus1++; // Increment present count
                    break;
                case 2:
                    countStatus2++; // Increment absent count
                    break;
                case 3:
                    countStatus3++; // Increment leave count
                    break;
                default:
                    break;  // Do nothing for unrecognized statuses
            }
        }

        // Return the count of Present, Absent, and Leave statuses
        return new int[]{countStatus1, countStatus2, countStatus3};
    }

    // Show the dialog to select attendance status (Present, Absent, Leave)
    private void showAttendanceDialog(Context context, OnAttendanceSelectedListener listener) {
        // Inflate the custom dialog layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_attendance, null);

        // Create the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);

        // Get references to RadioGroup and RadioButtons for attendance status options
        RadioGroup radioGroup = dialogView.findViewById(R.id.radioGroup);
        RadioButton presentButton = dialogView.findViewById(R.id.radioPresent);
        RadioButton absentButton = dialogView.findViewById(R.id.radioAbsent);
        RadioButton leaveButton = dialogView.findViewById(R.id.radioLeave);

        // Get reference to the confirm button
        Button confirmButton = dialogView.findViewById(R.id.btnConfirm);

        // Create the dialog
        AlertDialog dialog = builder.create();

        // Set the click listener for the confirm button
        confirmButton.setOnClickListener(v -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();  // Get the selected RadioButton ID

            int status = -1;  // Default to no selection
            if (selectedId == presentButton.getId()) {
                status = 1; // Present
            } else if (selectedId == absentButton.getId()) {
                status = 2; // Absent
            } else if (selectedId == leaveButton.getId()) {
                status = 3; // Leave
            }

            // Call the listener to pass the selected status
            if (listener != null) {
                listener.onAttendanceSelected(status); // Trigger the callback to pass the selected status
            }

            // Dismiss the dialog
            dialog.dismiss();
        });

        // Show the dialog
        dialog.show();
    }

    // Interface to handle attendance status selection callback
    public interface OnAttendanceSelectedListener {
        void onAttendanceSelected(int status); // 1 = Present, 2 = Absent, 3 = Leave
    }
}
