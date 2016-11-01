package com.storybox.culturemapg;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sunghee on 2016-10-24.
 */

public class PlaceInfoActivity extends AppCompatActivity {
    AQuery aq = new AQuery(this);
    private String serverLocation, serverQueryLocation;
    private PlaceInfo placeInfo;
    private Manager manager;
    ImageView photoView;
    private int call_counter = 0;
    private LayoutInflater inflater;
    private GridLayout inflatedLayout;
    final int SHOW_NUMBER_PER_ONE_PAGE = 5;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_info);
        //user code
        serverLocation = "http://chansh2013.cafe24.com";
        serverQueryLocation = serverLocation+"/test.php";
        photoView = (ImageView) findViewById(R.id.infoActivity_Photo);
        Intent intent = getIntent();
        int place_id = intent.getIntExtra("place_id",0);
        getBigPlaceInfoFromHttpServer(Integer.toString(place_id));

        inflatedLayout = (GridLayout) findViewById(R.id.layout_place_info);
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private void getBigPlaceInfoFromHttpServer(String id){
        //전송할 data(json형식과 비슷한 키-값 쌍) 구성하는 구문
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", "getBigPlaceInfo");
        map.put("isAjax", "1");
        map.put("id", id);
        //ajax요청 및 결과값 가져오기(callback으로)
        aq.ajax(serverQueryLocation, map, JSONObject.class, new AjaxCallback<JSONObject>(){
            @Override
            public void callback(String url, JSONObject object, AjaxStatus status){
                if(object != null){
                    try{
                        String successCode = "success";
                        if(object.get("result").toString().equals(successCode)){
                            placeInfo = new PlaceInfo(object);
                            aq.id(photoView).image(serverLocation + placeInfo.photo_path);
                            getManagerInfoFromHttpServer(Integer.toString(placeInfo.manager_id));
                            getShowInfoFromHttpServer(Integer.toString(placeInfo.id));
                        }
                        else{
                            Toast.makeText(PlaceInfoActivity.this, "error : " + object.get("result").toString(), Toast.LENGTH_SHORT).show();
                        }
                    }catch(Exception e){
                        Toast.makeText(PlaceInfoActivity.this, "error in ajax callback method : " + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(PlaceInfoActivity.this, "서버로부터 받은 결과값이 없습니다.\nin getBigPlaceInfo", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getManagerInfoFromHttpServer(String id){
        //전송할 data(json형식과 비슷한 키-값 쌍) 구성하는 구문
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", "getManagerInfo");
        map.put("isAjax", "1");
        map.put("id", id);

        //ajax요청 및 결과값 가져오기(callback으로)
        aq.ajax(serverQueryLocation, map, JSONObject.class, new AjaxCallback<JSONObject>(){
            @Override
            public void callback(String url, JSONObject object, AjaxStatus status){
                if(object != null){
                    try{
                        String successCode = "success";
                        if(object.get("result").toString().equals(successCode)){
                            manager = new Manager(object);
                            setInfomations(true, true);
                        }
                        else{
                            Toast.makeText(PlaceInfoActivity.this, "error : " + object.get("result").toString(), Toast.LENGTH_SHORT).show();
                            setInfomations(true, false);
                        }
                    }catch(Exception e){
                        Toast.makeText(PlaceInfoActivity.this, "error in ajax callback method : " + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(PlaceInfoActivity.this, "관리자 정보가 없습니다.", Toast.LENGTH_SHORT).show();
                    setInfomations(true, false);
                }
            }
        });
    }

    private void setInfomations(boolean placeFlag, boolean managerFlag){
        TextView place_name, place_category, place_address, place_contact, place_introduce, manager_name, manager_contact, manager_email;

        place_name = (TextView) findViewById(R.id.infoActivity_PlaceName);
        place_category = (TextView) findViewById(R.id.infoActivity_PlaceCategory);
        place_address = (TextView) findViewById(R.id.infoActivity_PlaceAddress);
        place_contact = (TextView) findViewById(R.id.infoActivity_PlaceContact);
        place_introduce = (TextView) findViewById(R.id.infoActivity_PlaceIntroduce);
        manager_name = (TextView) findViewById(R.id.infoActivity_ManagerName);
        manager_contact = (TextView) findViewById(R.id.infoActivity_ManagerContact);
        manager_email = (TextView) findViewById(R.id.infoActivity_ManagerEmail);

        if(placeFlag){
            place_name.setText(placeInfo.name);
            place_category.setText(placeInfo.category);
            place_address.setText(placeInfo.address);
            place_contact.setText(placeInfo.contact);
            place_introduce.setText(placeInfo.introduce);
        }else{
            place_name.setText("정보없음");
            place_category.setText("정보없음");
            place_address.setText("정보없음");
            place_contact.setText("정보없음");
            place_introduce.setText("정보없음");
        }

        if(managerFlag){
            manager_name.setText(manager.name);
            manager_contact.setText(manager.contact);
            manager_email.setText(manager.email);
        }else {
            manager_name.setText("정보없음");
            manager_contact.setText("정보없음");
            manager_email.setText("정보없음");
        }
    }

    private void getShowInfoFromHttpServer(String place_id) {
        //전송할 data(json형식과 비슷한 키-값 쌍) 구성하는 구문
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", "getSimpleShowInfo");
        map.put("isAjax", "1");
        map.put("place_id", place_id);
        map.put("offset", call_counter*5);

        //ajax요청 및 결과값 가져오기(callback으로)
        aq.ajax(serverQueryLocation, map, JSONArray.class, new AjaxCallback<JSONArray>() {
            @Override
            public void callback(String url, JSONArray object, AjaxStatus status) {
                if (object != null) {
                    try {
                        String successCode = "success";
                        if (object.getJSONObject(0).get("result").toString().equals(successCode)) {
                            int total_num_rows = object.getJSONObject(0).getInt("total_num_rows");//쿼리의 총 갯수를 받아야함
                            int now_num_rows = object.getJSONObject(0).getInt("now_num_rows");  //현재 받아온 (표시할) 쿼리의 갯수를 받아야함
                            call_counter++;
                            writeSimpleShowInfo(object, now_num_rows);
                        } else {
                            Toast.makeText(PlaceInfoActivity.this, "error : " + object.getJSONObject(0).get("result").toString(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(PlaceInfoActivity.this, "error in ajax callback method : " + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PlaceInfoActivity.this, "관리자 정보가 없습니다.", Toast.LENGTH_SHORT).show();
                    setInfomations(true, false);
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
            if(total_num_rows-((call_counter+1)*5) > -5) {
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

    //infoBox 클릭 시 PlaceInfoActivity를 실행시키는 메소드
    public void callShowInfoActivity(View view, String id)
    {
        Toast.makeText(this, "clicked id=" + id, Toast.LENGTH_LONG).show();
        /*
        Intent intent = new Intent(this, ShowInfoActivity.class);
        intent.putExtra("show_id", id);
        startActivity(intent);
        */
    }

    public void getMoreShowInfo(View v){
        getShowInfoFromHttpServer(Integer.toString(placeInfo.id));

    }
}
