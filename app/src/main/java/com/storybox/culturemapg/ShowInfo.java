package com.storybox.culturemapg;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Time;
import java.sql.Date;

/**
 * Created by sunghee on 2016-10-24.
 */

public class ShowInfo {
    public int id, place_id;
    public String title, category, introduce, poster_path, price, detail_location_info, telephone_inquiry;
    public String start_date, end_date;
    public String start_time, end_time;

    private ShowInfo(){

    }

    public ShowInfo(JSONObject object){
        try {
            id = object.getInt("id");
            place_id = object.getInt("place_id");
            title = object.getString("title");
            category = object.getString("category");
            price = object.getString("price");
            start_date = object.getString("start_date");
            end_date = object.getString("end_date");
            introduce = object.getString("introduce");
            poster_path = object.getString("poster_path");
            detail_location_info = object.getString("detail_location_info");
            telephone_inquiry = object.getString("telephone_inquiry");
            start_time = object.getString("start_time");
            end_time = object.getString("end_time");
        }catch(Exception e){

        }
    }
}
