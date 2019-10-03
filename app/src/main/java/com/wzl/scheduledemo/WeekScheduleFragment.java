package com.wzl.scheduledemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

public class WeekScheduleFragment extends Fragment {
    private RelativeLayout layout = null;
    private int REQUEST_CODE = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("STATE", "onCreate");
        Log.d("Pixel", getResources().getDimension(R.dimen.week_item_height) + "");
        Log.d("Pixel", getResources().getDimensionPixelOffset(R.dimen.week_item_height) + "");
        Log.d("Pixel", getResources().getDimensionPixelSize(R.dimen.week_item_height) + "");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("STATE", "onCreateView");
        final View view = inflater.inflate(R.layout.fragment_week_schedule, container, false);
        // 默认不加晚上
//        new Thread(){
//            @Override
//            public void run() {
//                createLeftView(view, 180*9);
//                createScheduleView(view, 180);
//            }
//        }.start();
        createLeftView(view, 180*9);
        createScheduleView(view, 180);
        return view;
    }

    // 加载左侧节数
    private void createLeftView(View view, int endHeight){
        RelativeLayout leftLayout = view.findViewById(R.id.left_layout);
        for (int i = 0; i < endHeight/180; i ++){
            View leftView = LayoutInflater.from(getActivity()).inflate(R.layout.left_item, leftLayout, false);
            LinearLayout.LayoutParams leftParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 180);
            leftParams.setMargins(0, i*180, 0, 0);
            leftView.setLayoutParams(leftParams);
            TextView tvNumber = leftView.findViewById(R.id.vertical_number);
            tvNumber.setText(String.valueOf(i + 1));
            leftLayout.addView(leftView);
        }
    }

    //  创建课程视图
    private void createScheduleView(View view, int height) {
        List<Schedule> list = ScheduleUtils.getScheduleList();
        if (list != null && list.size() > 0) {
            RelativeLayout mondayLayout = view.findViewById(R.id.monday_layout);
            RelativeLayout tuesdayLayout = view.findViewById(R.id.tuesday_layout);
            RelativeLayout wednesdayLayout = view.findViewById(R.id.wednesday_layout);
            RelativeLayout thursdayLayout = view.findViewById(R.id.thursday_layout);
            RelativeLayout fridayLayout = view.findViewById(R.id.friday_layout);
//            周六周日
//            RelativeLayout saturdaylayout = view.findViewById(R.id.saturday_layout);
//            RelativeLayout sundaylayout = view.findViewById(R.id.sunday_layout);
            for (final Schedule schedule : list) {
                SharedPreferences sp = Objects.requireNonNull(getActivity()).getSharedPreferences("config", Context.MODE_PRIVATE);
                // 判断课程是否已经结束
                if (sp.getInt("WEEK_NUMBER", 1) >= schedule.getStartWeek() && sp.getInt("WEEK_NUMBER", 1) <= schedule.getEndWeek()){
                    switch (schedule.getWhichWeek()) {
                        case 1:
                            layout = mondayLayout;
                            break;
                        case 2:
                            layout = tuesdayLayout;
                            break;
                        case 3:
                            layout = wednesdayLayout;
                            break;
                        case 4:
                            layout = thursdayLayout;
                            break;
                        case 5:
                            layout = fridayLayout;
                            break;
                    }
                    View rightView = LayoutInflater.from(getActivity()).inflate(R.layout.week_item, layout, false); //加载单个课程布局
                    int startHeight = height * (schedule.getStartSection() - 1);
                    int layoutHeight = (schedule.getEndSection() - schedule.getStartSection() + 1) * height;
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, layoutHeight); //设置布局高度,即跨多少节课
                    //  前提布局类型是RelativeLayout
                    params.setMargins(0, startHeight, 0, 0);
                    rightView.setLayoutParams(params);
                    TextView tvCourse = rightView.findViewById(R.id.tv_course);
                    TextView tvTeacher = rightView.findViewById(R.id.tv_teacher);
                    TextView tvLocation = rightView.findViewById(R.id.tv_location);

                    tvCourse.setText(schedule.getCourse());
                    tvTeacher.setText(schedule.getTeacher());
                    tvLocation.setText(schedule.getLocation());
                    layout.addView(rightView);
                    rightView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), DetailScheduleActivity.class);
                            intent.putExtra("schedule", schedule);
                            //这里的startActivityForResult最后会在Activity中的onActivityResult响应
                            //直接使用startActivityForResult请求码并不匹配
                            getActivity().startActivityForResult(intent, REQUEST_CODE);
                        }
                    });

                    //如果有晚上的课程，则重新绘制左侧的试图
                    if (startHeight >= height*9){
                        createLeftView(view, startHeight + layoutHeight);
                    }
                } else {
                    // 课程已经结束
                    ScheduleUtils.delSchedule(schedule);
                }
            }
        }
    }

}