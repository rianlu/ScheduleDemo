package com.wzl.scheduledemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TodayScheduleFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today_schedule, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.rv_today);
        //  获得当前星期
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        calendar.setTime(date);
        //  时间从星期日开始到星期一  数字从1 - 7
        int currentWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        //  需要对星期日特殊处理
        if (currentWeek == 0){
            currentWeek = 7;
        }
        List<Schedule> list = ScheduleUtils.getScheduleListByWeek(currentWeek);
        if (list != null && list.size() > 0){
            RecyclerView.LayoutManager manager = new LinearLayoutManager(this.getActivity());
            TodayScheduleAdapter adapter = new TodayScheduleAdapter(list);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(manager);
            recyclerView.addItemDecoration(new TodayItemDecoration(50));
        }
        return view;
    }
}
