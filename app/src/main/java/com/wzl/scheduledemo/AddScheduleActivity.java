package com.wzl.scheduledemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class AddScheduleActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private AutoCompleteTextView actvCourse;
    private EditText etTeacher;
    private EditText etLocation;
    private EditText etStartWeek;
    private EditText etEndWeek;
    private EditText etCurrentWeek;
    private EditText etStartSection;
    private EditText etEndSection;
    private EditText etWhichWeek;
    private boolean IS_INSERT = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);
        toolbar = findViewById(R.id.add_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initView();
    }

    //  初始化布局
    public void initView(){
        actvCourse = findViewById(R.id.et_course);
        etTeacher = findViewById(R.id.et_teacher);
        etLocation = findViewById(R.id.et_location);
        etStartWeek = findViewById(R.id.et_start_week);
        etEndWeek = findViewById(R.id.et_end_week);
        etCurrentWeek = findViewById(R.id.et_current_week);
        etStartSection = findViewById(R.id.et_start_section);
        etEndSection = findViewById(R.id.et_end_section);
        etWhichWeek = findViewById(R.id.et_which_week);

        // 默认当前周即为设置的当前周
        etCurrentWeek.setText(String.valueOf(ScheduleUtils.WEEK_NUMBER));

        //通过查询数据库获得提示的内容
        List<String> list = ScheduleUtils.getAllCourse();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, list
        );
        actvCourse.setAdapter(adapter);
        actvCourse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String course = (String) adapterView.getItemAtPosition(i);
                Schedule schedule = ScheduleUtils.getScheduleByCourse(course);
                if (schedule != null){
                    actvCourse.setText(schedule.getCourse());
                    etTeacher.setText(schedule.getTeacher());
                    etLocation.setText(schedule.getLocation());
                    etStartWeek.setText(String.valueOf(schedule.getStartWeek()));
                    etEndWeek.setText(String.valueOf(schedule.getEndWeek()));
                    etCurrentWeek.setText(String.valueOf(schedule.getCurrentWeek()));
                    etStartSection.setText(String.valueOf(schedule.getStartSection()));
                    etEndSection.setText(String.valueOf(schedule.getEndSection()));
                    etWhichWeek.setText(String.valueOf(schedule.getWhichWeek()));
                    Toast.makeText(AddScheduleActivity.this, "已自动填充", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // 判断是否时通过编辑到达该页面
        Schedule schedule = (Schedule) getIntent().getSerializableExtra("editSchedule");
        if (schedule != null) {
            toolbar.setTitle("修改课程");
            IS_INSERT = false;
            actvCourse.setText(schedule.getCourse());
            etTeacher.setText(schedule.getTeacher());
            etLocation.setText(schedule.getLocation());
            etStartWeek.setText(String.valueOf(schedule.getStartWeek()));
            etEndWeek.setText(String.valueOf(schedule.getEndWeek()));
            etCurrentWeek.setText(String.valueOf(schedule.getCurrentWeek()));
            etStartSection.setText(String.valueOf(schedule.getStartSection()));
            etEndSection.setText(String.valueOf(schedule.getEndSection()));
            etWhichWeek.setText(String.valueOf(schedule.getWhichWeek()));
        } else {
            toolbar.setTitle("添加课程");
        }
    }

    public void insertSchedule(){
        Schedule schedule = new Schedule();
        schedule.setCourse(actvCourse.getText().toString());
        schedule.setTeacher(etTeacher.getText().toString());
        schedule.setLocation(etLocation.getText().toString());
        schedule.setStartWeek(Integer.parseInt(etStartWeek.getText().toString()));
        schedule.setEndWeek(Integer.parseInt(etEndWeek.getText().toString()));
        schedule.setCurrentWeek(Integer.parseInt(etCurrentWeek.getText().toString()));
        schedule.setStartSection(Integer.parseInt(etStartSection.getText().toString()));
        schedule.setEndSection(Integer.parseInt(etEndSection.getText().toString()));
        schedule.setWhichWeek(Integer.parseInt(etWhichWeek.getText().toString()));
        long insert = ScheduleUtils.addSchedule(schedule);
        if (insert > 0) {
            Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
            setResult(1);
            finish();
        }
    }

    public void updateSchedule(){
        Schedule schedule = (Schedule) getIntent().getSerializableExtra("editSchedule");
        Schedule updateSchedule = new Schedule();
        updateSchedule.set_id(schedule.get_id());
        updateSchedule.setCourse(actvCourse.getText().toString().trim());
        updateSchedule.setTeacher(etTeacher.getText().toString().trim());
        updateSchedule.setLocation(etLocation.getText().toString().trim());
        updateSchedule.setStartWeek(Integer.parseInt(etStartWeek.getText().toString()));
        updateSchedule.setEndWeek(Integer.parseInt(etEndWeek.getText().toString()));
        updateSchedule.setCurrentWeek(Integer.parseInt(etCurrentWeek.getText().toString()));
        updateSchedule.setStartSection(Integer.parseInt(etStartSection.getText().toString()));
        updateSchedule.setEndSection(Integer.parseInt(etEndSection.getText().toString()));
        updateSchedule.setWhichWeek(Integer.parseInt(etWhichWeek.getText().toString()));
        int result = ScheduleUtils.updateSchedule(updateSchedule);
        if (result > 0){
            Toast.makeText(this, "修改成功！", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra("updateSchedule", updateSchedule);
            setResult(1, intent);
            finish();
        }else {
            Toast.makeText(this, "修改失败！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.success){
            if (actvCourse.getText().length() > 0 &&
                    etTeacher.getText().length() > 0 &&
                    etLocation.getText().length() > 0 &&
                    etStartWeek.getText().length() > 0 &&
                    etEndWeek.getText().length() > 0 &&
                    etCurrentWeek.getText().length() > 0 &&
                    etStartSection.getText().length() > 0 &&
                    etEndSection.getText().length() > 0 &&
                    etWhichWeek.getText().length() > 0) {
                if (IS_INSERT) {
                    insertSchedule();
                } else {
                    updateSchedule();
                }
            } else {
                Toast.makeText(this, "请完善内容！", Toast.LENGTH_SHORT).show();
            }

        }
        return super.onOptionsItemSelected(item);
    }
}
