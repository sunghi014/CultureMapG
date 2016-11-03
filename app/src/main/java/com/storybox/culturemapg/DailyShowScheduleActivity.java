package com.storybox.culturemapg;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.w3c.dom.Text;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by sunghee on 2016-11-02.
 */

public class DailyShowScheduleActivity extends AppCompatActivity {
    private AQuery aq = new AQuery( this );
    private String full_date;

    private LayoutInflater inflater;
    private GridLayout inflatedLayout;

    private int call_counter;
    private String serverLocation,serverQueryLocation;

    int realWidth;
    int realHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_show_schedule);

        Intent intent = getIntent();
        full_date = intent.getStringExtra("full_date");

        getDisplaySize();

        TextView dayOfWeek,monthOfYear,dayOfMonth,year;
        dayOfWeek = (TextView) findViewById(R.id.daily_show_schedule_activity_day_of_week);
        monthOfYear = (TextView) findViewById(R.id.daily_show_schedule_activity_month_of_year);
        dayOfMonth = (TextView) findViewById(R.id.daily_show_schedule_activity_day_of_month);
        year = (TextView) findViewById(R.id.daily_show_schedule_activity_year);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(intent.getStringExtra("year")),Integer.parseInt(intent.getStringExtra("monthOfYear"))-1,Integer.parseInt(intent.getStringExtra("dayOfMonth")));

        int val = calendar.get(Calendar.DAY_OF_WEEK);
        String vamp = "";
        switch (val) {
            case Calendar.SUNDAY:
                vamp = "일";
                break;
            case Calendar.MONDAY:
                vamp = "월";
                break;
            case Calendar.TUESDAY:
                vamp = "화";
                break;
            case Calendar.WEDNESDAY:
                vamp = "수";
                break;
            case Calendar.THURSDAY:
                vamp = "목";
                break;
            case Calendar.FRIDAY:
                vamp = "금";
                break;
            case Calendar.SATURDAY:
                vamp = "토";
                break;
        }

        dayOfWeek.setText(vamp+"요일");
        monthOfYear.setText(intent.getStringExtra("monthOfYear")+"월");
        dayOfMonth.setText(intent.getStringExtra("dayOfMonth"));
        year.setText(intent.getStringExtra("year"));



        serverLocation = "http://chansh2013.cafe24.com";
        serverQueryLocation = serverLocation+"/test.php";

        Toast.makeText(this, full_date+"의 공연 및 전시정보입니다.", Toast.LENGTH_SHORT).show();

        inflatedLayout = (GridLayout) findViewById(R.id.daily_show_schedule_activity_layout);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        getShowInfoFromHttpServer(full_date);
    }

    private void getShowInfoFromHttpServer(String date) {
        //전송할 data(json형식과 비슷한 키-값 쌍) 구성하는 구문
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", "getDailyShowInfo");
        map.put("isAjax", "1");
        map.put("date", date);
        map.put("offset", call_counter*20);

        //ajax요청 및 결과값 가져오기(callback으로)
        aq.ajax(serverQueryLocation, map, JSONArray.class, new AjaxCallback<JSONArray>() {
            @Override
            public void callback(String url, JSONArray object, AjaxStatus status) {
                if (object != null) {
                    try {
                        String successCode = "success";
                        if (object.getJSONObject(0).get("result").toString().equals(successCode)) {
                            int now_num_rows = object.getJSONObject(0).getInt("now_num_rows");  //현재 받아온 (표시할) 쿼리의 갯수를 받아야함
                            call_counter++;
                            writeSimpleShowInfo(object, now_num_rows);
                        } else {
                            Toast.makeText(getApplicationContext(), "정보가 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "error in ajax callback method : " + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "관리자 정보가 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void writeSimpleShowInfo(JSONArray object, int now_num_rows){
        int length = now_num_rows;
        final ArrayList<SimpleShowInfo> simpleShowInfo = new ArrayList<SimpleShowInfo>();

        for(int i = 1; i <= length; i++){
            try {
                final View table_row_show_info = inflater.inflate(R.layout.table_row_simple_show_info, null);
                TextView simple_show_date = (TextView)table_row_show_info.findViewById(R.id.simple_show_date),
                        simple_show_title = (TextView)table_row_show_info.findViewById(R.id.simple_show_title),
                        simple_show_category = (TextView)table_row_show_info.findViewById(R.id.simple_show_category),
                        simple_show_price = (TextView)table_row_show_info.findViewById(R.id.simple_show_price);

                simpleShowInfo.add(new SimpleShowInfo(object.getJSONObject(i)));

                simple_show_date.setText(simpleShowInfo.get(i-1).start_date + "\n~\n" + simpleShowInfo.get(i-1).end_date);
                simple_show_title.setText(simpleShowInfo.get(i-1).title);
                simple_show_price.setText(simpleShowInfo.get(i-1).price);
                simple_show_category.setText(simpleShowInfo.get(i-1).category);
                final int tmp_i = i-1;
                table_row_show_info.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callShowInfoActivity(v, Integer.toString(simpleShowInfo.get(tmp_i).id));
                    }
                });

                inflatedLayout.addView(table_row_show_info);
            }catch (Exception e){
                //Toast.makeText(this, "err:"+e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        try {
            int total_num_rows = object.getJSONObject(0).getInt("total_num_rows");
            if(total_num_rows-((call_counter+1)*20) > -20) {
                final View bt = inflater.inflate(R.layout.button_more_show, null);
                Button more_button = (Button) bt.findViewById(R.id.more_show_bt);
                more_button.setWidth(Resources.getSystem().getDisplayMetrics().widthPixels);
                more_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        inflatedLayout.removeView(v);
                        getMoreShowInfo(v);
                    }
                });
                inflatedLayout.addView(bt);
            }
        }catch(Exception e){

        }
    }

    public void callShowInfoActivity(View view, String id)
    {
        //Toast.makeText(this, "clicked id=" + id, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, ShowInfoActivity.class);
        intent.putExtra("show_id", id);
        startActivity(intent);
    }

    public void getMoreShowInfo(View v){
        getShowInfoFromHttpServer(full_date);

    }

    public void getDisplaySize(){
        Display display = DailyShowScheduleActivity.this.getWindowManager().getDefaultDisplay();

        if (Build.VERSION.SDK_INT >= 17){
            //new pleasant way to get real metrics
            DisplayMetrics realMetrics = new DisplayMetrics();
            display.getRealMetrics(realMetrics);
            realWidth = realMetrics.widthPixels;
            realHeight = realMetrics.heightPixels;

        } else if (Build.VERSION.SDK_INT >= 14) {
            //reflection for this weird in-between time
            try {
                Method mGetRawH = Display.class.getMethod("getRawHeight");
                Method mGetRawW = Display.class.getMethod("getRawWidth");
                realWidth = (Integer) mGetRawW.invoke(display);
                realHeight = (Integer) mGetRawH.invoke(display);
            } catch (Exception e) {
                //this may not be 100% accurate, but it's all we've got
                realWidth = display.getWidth();
                realHeight = display.getHeight();
                Log.e("Display Info", "Couldn't use reflection to get the real display metrics.");
            }

        } else {
            //This should be close, as lower API devices should not have window navigation bars
            realWidth = display.getWidth();
            realHeight = display.getHeight();
        }
    }
}
