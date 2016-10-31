package com.storybox.culturemapg;

import org.json.JSONObject;

/**
 * Created by sunghee on 2016-10-24.
 */

public class Manager {
    public int id;
    public String name, contact, email, managing_place_ids_string, managing_place_names_string;
    public String[] managing_place_ids, managing_place_names;

    public Manager(JSONObject object) {
        try{
            id = object.getInt("id");
            name = object.getString("name");
            contact = object.getString("contact");
            email = object.getString("email");
            managing_place_ids_string = object.getString("managing_place_ids");
            managing_place_names_string = object.getString("managing_place_names");
            managing_place_ids = managing_place_ids_string.split(",");
            managing_place_names = managing_place_names_string.split(",");
        }catch(Exception e){

        }
    }

}
