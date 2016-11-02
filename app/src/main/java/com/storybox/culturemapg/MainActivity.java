package com.storybox.culturemapg;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.calendardatepicker.MonthAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sunghee on 2016-10-14.
 */

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void callMapActivity(View v){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void callCalenderActivity(View v){
        /*
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
        */

        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy",java.util.Locale.getDefault());

        MonthAdapter.CalendarDay minDate = new MonthAdapter.CalendarDay(Integer.parseInt(dateFormat.format(date))-1,0,1);
        MonthAdapter.CalendarDay maxDate = new MonthAdapter.CalendarDay(Integer.parseInt(dateFormat.format(date))+5,11,31);

        CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                .setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
                    @Override
                    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
                        Intent intent = new Intent(getApplicationContext(), DailyShowScheduleActivity.class);
                        String m,d;

                        d = (dayOfMonth >= 10 ? "" : "0") + Integer.toString(dayOfMonth);
                        m = (monthOfYear+1 >= 10 ? "" : "0") + Integer.toString(monthOfYear+1);
                        intent.putExtra("full_date", year + "-" + m + "-" + d);
                        intent.putExtra("year", Integer.toString(year));
                        intent.putExtra("monthOfYear", m);
                        intent.putExtra("dayOfMonth", d);
                        startActivity(intent);
                    }
                })
                .setDoneText("확인")
                .setCancelText("취소")
                .setDateRange(minDate, maxDate);
        cdp.show(getSupportFragmentManager(), "TEST");
    }
}
