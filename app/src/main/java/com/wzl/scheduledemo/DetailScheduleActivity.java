package com.wzl.scheduledemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class DetailScheduleActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Schedule schedule;
    private int REQUEST_CODE = 1;
    private TextView tvTeacher;
    private TextView tvLocation;
    private TextView tvStartEndWeek;
    private TextView tvCurrentWeek;
    private TextView tvStartEndSection;
    private TextView tvWhichWeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_schedule);

        initView();

        Intent intent = getIntent();
        schedule = (Schedule) intent.getSerializableExtra("schedule");

        initData();
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(2);
                finish();
            }
        });
    }

    public void initView(){
        toolbar = findViewById(R.id.detail_toolbar);

        tvTeacher = findViewById(R.id.dt_teacher);
        tvLocation = findViewById(R.id.dt_location);
        tvStartEndWeek = findViewById(R.id.dt_start_end_week);
        tvCurrentWeek = findViewById(R.id.dt_current_week);
        tvStartEndSection = findViewById(R.id.dt_start_end_section);
        tvWhichWeek = findViewById(R.id.dt_which_week);
    }

    public void initData(){

        //将课程名字显示到ToolBar上
        toolbar.setTitle(schedule.getCourse());
        tvTeacher.setText(schedule.getTeacher());
        tvLocation.setText(schedule.getLocation());
        tvStartEndWeek.setText(schedule.getStartWeek() + " - " + schedule.getEndWeek() + " 周");
        tvCurrentWeek.setText("当前为第 " + schedule.getCurrentWeek() + " 周");
        tvStartEndSection.setText(schedule.getStartSection() + " - " + schedule.getEndSection() + " 节");
        tvWhichWeek.setText(ScheduleUtils.numberToWeek(schedule.getWhichWeek()));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.edit:
                Intent intent = new Intent(this, AddScheduleActivity.class);
                intent.putExtra("editSchedule", schedule);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.delete:
                showDelDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //显示删除对话框
    public void showDelDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("是否删除？");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int result = ScheduleUtils.delSchedule(schedule);
                if (result > 0){
                    Toast.makeText(DetailScheduleActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                    setResult(2);
                    finish();
                }else {
                    Toast.makeText(DetailScheduleActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == 1){
            if (data != null){
                schedule = (Schedule) data.getSerializableExtra("updateSchedule");
                initData();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        setResult(2);
        super.onBackPressed();
    }
}
