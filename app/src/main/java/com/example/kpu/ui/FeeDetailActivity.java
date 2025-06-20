package com.example.kpu.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;


import com.example.kpu.R;
import com.example.kpu.view.FragmentViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


public class FeeDetailActivity extends AppCompatActivity {

    private String[] mTitlesArrays = {"School Fee","Exam Fee","Activity Fee","Other"};
    private ArrayList<Fragment> fragments;
    private ViewPager viewPager;
    private TabLayout tableLayout;
    private TextView tvLeft;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_detail);
        initView();
    }
    private void initView() {
        viewPager = findViewById(R.id.viewPager);
        tableLayout = findViewById(R.id.tableLayout);
        tvLeft = findViewById(R.id.tvLeft);
        tvLeft.setText("Fee Details");  // Customize the text
        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fragments = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        for(int i = 0;i<mTitlesArrays.length;i++){
            titles.add(mTitlesArrays[i]);
            Bundle bundle = new Bundle();
            bundle.putInt("type",i);
            FeeDetailFragment feeDetailFragment = new FeeDetailFragment();
            feeDetailFragment.setArguments(bundle);
            fragments.add( feeDetailFragment);
        }
        FragmentViewPagerAdapter adapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(fragments.size());
        viewPager.setCurrentItem(0, false);
        tableLayout.setupWithViewPager(viewPager, false);
    }

}
