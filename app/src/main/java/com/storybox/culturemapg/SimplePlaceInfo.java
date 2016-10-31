package com.storybox.culturemapg;

import org.json.JSONObject;

/**
 * Created by sunghee on 2016-10-25.
 */

public class SimplePlaceInfo {
    public String name, address, contact, category, icon_path;
    public int id;

    public SimplePlaceInfo(JSONObject object){
        try {
            name = object.getString("name");
            address = object.getString("address");
            contact = object.getString("contact");
            category = object.getString("category");
            icon_path = object.getString("icon_path");
            id = object.getInt("id");
        }catch (Exception e){

        }
    }
}
