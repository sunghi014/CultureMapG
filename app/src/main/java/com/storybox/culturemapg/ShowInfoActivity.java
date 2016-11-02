package com.storybox.culturemapg;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by sunghee on 2016-11-01.
 */

public class ShowInfoActivity extends AppCompatActivity {
    private String serverLocation, serverQueryLocation;
    private AQuery aq = new AQuery(this);
    private ImageView posterView;
    private ShowInfo showInfo;


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_info);
        //user code
        serverLocation = "http://chansh2013.cafe24.com";
        serverQueryLocation = serverLocation+"/test.php";
        posterView = (ImageView) findViewById(R.id.showInfoActivity_Poster);
        Intent intent = getIntent();
        String show_id = intent.getStringExtra("show_id");
        getBigShowInfoFromHttpServer(show_id);
    }

    private void getBigShowInfoFromHttpServer(String show_id){
        //전송할 data(json형식과 비슷한 키-값 쌍) 구성하는 구문
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", "getShowInfo");
        map.put("isAjax", "1");
        map.put("show_id", show_id);

        //ajax요청 및 결과값 가져오기(callback으로)
        aq.ajax(serverQueryLocation, map, JSONObject.class, new AjaxCallback<JSONObject>(){
            @Override
            public void callback(String url, JSONObject object, AjaxStatus status){
                if(object != null){
                    try{
                        String successCode = "success";
                        if(object.get("result").toString().equals(successCode)){
                            showInfo = new ShowInfo(object);
                            if(showInfo.poster_path != null) {
                                aq.id(posterView).image(serverLocation + showInfo.poster_path);
                            }
                            writeShowInformation();
                        }
                        else{
                            Toast.makeText(ShowInfoActivity.this, "error : " + object.get("result").toString(), Toast.LENGTH_SHORT).show();
                        }
                    }catch(Exception e){
                        Toast.makeText(ShowInfoActivity.this, "error in ajax callback method : " + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(ShowInfoActivity.this, "서버로부터 받은 결과값이 없습니다.\nin getShowInfo", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void writeShowInformation(){
        TextView title, category, introduce, price, detail_location, telephone_inquiry, date, time;
        title = (TextView) findViewById(R.id.showInfoActivity_ShowTitle);
        category = (TextView) findViewById(R.id.showInfoActivity_ShowCategory);
        introduce = (TextView) findViewById(R.id.showInfoActivity_ShowIntroduce);
        price = (TextView) findViewById(R.id.showInfoActivity_ShowPrice);
        detail_location = (TextView) findViewById(R.id.showInfoActivity_ShowDetailLocationInfo);
        telephone_inquiry = (TextView) findViewById(R.id.showInfoActivity_ShowTelephoneInquiry);
        date = (TextView) findViewById(R.id.showInfoActivity_ShowDate);
        time = (TextView) findViewById(R.id.showInfoActivity_ShowTime);

        title.setText(showInfo.title);
        category.setText(showInfo.category);
        introduce.setText(showInfo.introduce);
        price.setText(showInfo.price);
        detail_location.setText(showInfo.detail_location_info);
        telephone_inquiry.setText(showInfo.telephone_inquiry);
        date.setText(showInfo.start_date + " ~ " + showInfo.end_date);
        time.setText(showInfo.start_time + " ~ " + showInfo.end_time);
    }
}
