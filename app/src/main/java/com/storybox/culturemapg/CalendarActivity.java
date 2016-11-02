package com.storybox.culturemapg;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;
import android.widget.Toast;


import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;

/**
 * Created by sunghee on 2016-11-02.
 */

public class CalendarActivity extends AppCompatActivity {
    private CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);



        calendarView = (CalendarView) findViewById(R.id.calendarView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                //Toast.makeText(CalendarActivity.this, "Y:"+year+"/M:"+month+"/D:"+dayOfMonth, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), DailyShowScheduleActivity.class);
                String m,d;

                d = (dayOfMonth >= 10 ? "" : "0") + Integer.toString(dayOfMonth);
                m = (month+1 >= 10 ? "" : "0") + Integer.toString(month+1);
                intent.putExtra("date", year + "-" + m + "-" + d);
                startActivity(intent);
            }
        });
    }
}
