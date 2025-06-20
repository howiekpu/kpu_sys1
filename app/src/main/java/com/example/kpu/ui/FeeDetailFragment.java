package com.example.kpu.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kpu.R;
import com.example.kpu.adapter.FeeDetailAdapter;
import com.example.kpu.bean.FreeRecord;
import com.example.kpu.db.UserDbHelper;

import java.util.ArrayList;
import java.util.List;

public class FeeDetailFragment extends Fragment {
    private RecyclerView recyclerView;
    private FeeDetailAdapter feeDetailAdapter;
    private UserDbHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fee_detail, container, false);
        recyclerView  = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<FreeRecord> dataList = new ArrayList<>();
        dbHelper = new UserDbHelper(getActivity());
        int type = getArguments().getInt("type");
        dataList.addAll(dbHelper.getFreeRecordsByType(type));
        feeDetailAdapter = new FeeDetailAdapter(dataList);
        recyclerView.setAdapter(feeDetailAdapter);
        return view;
    }
}
