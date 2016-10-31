package com.storybox.culturemapg;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Date;
import java.sql.Time;

/**
 * Created by sunghee on 2016-10-28.
 */

public class SimpleShowInfo {
    public int id, place_id;
    public String title, category, price,start_date, end_date;

    private SimpleShowInfo(){

    }

    public SimpleShowInfo(JSONObject object){
        try {
            id = object.getInt("id");
            place_id = object.getInt("place_id");
            title = object.getString("title");
            category = object.getString("category");
            price = object.getString("price");
            start_date = object.getString("start_date");
            end_date = object.getString("end_date");
        }catch(Exception e){

        }
    }
}
