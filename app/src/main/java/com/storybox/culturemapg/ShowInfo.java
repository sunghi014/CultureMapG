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
    public Date start_date, end_date;
    public Time start_time, end_time;

    private ShowInfo(){

    }

    public ShowInfo(JSONObject object){

    }
}
