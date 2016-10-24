package com.storybox.culturemapg;

import android.content.Context;

import org.json.JSONObject;

/**
 * Created by sunghee on 2016-10-24.
 */

public class Place {
    public String name, address, icon_path, category, show_ids, contact, introduce, photo_path;
    public int id, manager_id;
    public float coordinateX, coordinateY;
    public Manager manager;
    public ShowInfomation[] showInfomation;

    public Place(JSONObject object, Context context){
        //매니저 id로 php에 접근하여 매니저 정보를 받아와 manager 변수에 넣어야함.
        //manager = new Manager(manager_id);
        //show_ids로 php에 접근하여 공연정보들을 받아와 showInfomation 배열변수에 넣어야함.
        try{
            name = object.getString("name");
            address = object.getString("address");
            icon_path = object.getString("icon_path");
            category = object.getString("category");
            show_ids = object.getString("show_ids");
            contact = object.getString("contact");
            introduce = object.getString("introduce");
            photo_path = object.getString("photo_path");
            id = object.getInt("id");
            manager_id = object.getInt("manager_id");
            coordinateX = (float)object.getDouble("coordinateX");
            coordinateY = (float)object.getDouble("coordinateY");
            manager = new Manager(manager_id, context);  //생성자에서 php접근 필요
            String[] show_id = show_ids.split(",");
            showInfomation = new ShowInfomation[show_id.length];
            for(int i=0; i < show_id.length; i++){
                showInfomation[i] = new ShowInfomation(Integer.parseInt(show_id[i]));  //생성자에서 php접근 필요
            }

        }catch(Exception e){

        }
    }
}
