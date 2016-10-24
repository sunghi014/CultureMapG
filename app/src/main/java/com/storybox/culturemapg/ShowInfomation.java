package com.storybox.culturemapg;

import java.sql.Time;
import java.sql.Date;

/**
 * Created by sunghee on 2016-10-24.
 */

public class ShowInfomation {
    public int id, place_id;
    public String title, category, introduce, poster_path;
    public Date date;
    public Time start_time, end_time;

    private ShowInfomation(){

    }

    public ShowInfomation(int id){

    }
}
