package com.wzl.scheduledemo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class TodayScheduleAdapter extends RecyclerView.Adapter<TodayScheduleAdapter.ViewHolder> {

    private List<Schedule> scheduleList;

    public TodayScheduleAdapter(List<Schedule> scheduleList){
        this.scheduleList = scheduleList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.rv_today_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Schedule schedule = scheduleList.get(position);
        viewHolder.tvCourse.setText(schedule.getCourse());
        viewHolder.tvLocation.setText(schedule.getLocation());
        viewHolder.tvTeacher.setText(schedule.getTeacher());
        viewHolder.tvSection.setText(schedule.getStartSection() + "-" + schedule.getEndSection() + " 节");
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvCourse;
        TextView tvLocation;
        TextView tvTeacher;
        TextView tvSection;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCourse = itemView.findViewById(R.id.tv_course);
            tvLocation = itemView.findViewById(R.id.tv_location);
            tvTeacher = itemView.findViewById(R.id.tv_teacher);
            tvSection = itemView.findViewById(R.id.tv_section);
        }
    }
}
