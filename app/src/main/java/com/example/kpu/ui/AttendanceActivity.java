package com.example.kpu.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kpu.App;
import com.example.kpu.R;
import com.example.kpu.adapter.AttendanceAdapter;
import com.example.kpu.bean.AttendanceItem;
import com.example.kpu.db.UserDbHelper;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AttendanceActivity extends AppCompatActivity {

    private TextView tvLeft;
    private TextView tvRight;
    private RecyclerView recyclerView;
    private AttendanceAdapter attendanceAdapter;
    private int scrollPosition = 1;
    private UserDbHelper dbHelper;
    private  List<AttendanceItem> attendanceItems = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        // Call initView() to initialize the views
        initView();
    }
    private void initView() {
        // Find the views by their IDs
        tvLeft = findViewById(R.id.tvLeft);
        tvRight = findViewById(R.id.tvRight);
        recyclerView  = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 创建数据
        dbHelper = new UserDbHelper(this);
        // 设置适配器
        attendanceAdapter = new AttendanceAdapter(attendanceItems);
        recyclerView.setAdapter(attendanceAdapter);
        attendanceAdapter.setOnItemDateClickListener(new AttendanceAdapter.OnItemDateClickListener() {
            @Override
            public void onItemDateClick(int position,int subPosition) {
                startActivity(new Intent(AttendanceActivity.this,AttendanceDetailActivity.class).
                        putExtra("year",attendanceItems.get(position).getYear())
                        .putExtra("index",subPosition));
            }
        });
        recyclerView.scrollToPosition(scrollPosition);
        tvLeft.setText("Attendance");  // Customize the text
        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Set up the right TextView (tvRight)
        tvRight.setVisibility(View.VISIBLE);  // Make the right TextView visible
        tvRight.setText(getYear()+"");
        // Set an OnClickListener for tvRight if you want to perform actions
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(scrollPosition == 1){
                    scrollPosition = 0;
                    recyclerView.smoothScrollToPosition(scrollPosition);
                    tvRight.setText(attendanceItems.get(0).getYear());
                }else{
                    scrollPosition = 1;
                    recyclerView.smoothScrollToPosition(scrollPosition);
                    tvRight.setText(attendanceItems.get(1).getYear());

                }
            }
        });
    }
    private int getYear() {
        // Get the current year
        return  Calendar.getInstance().get(Calendar.YEAR);
    }

    @Override
    protected void onResume() {
        super.onResume();
        attendanceItems.clear();
        attendanceItems.addAll(dbHelper.getAttendanceForLastAndCurrentYear(App.getLoginUser().getId()));
        attendanceAdapter.notifyDataSetChanged();
    }
}
