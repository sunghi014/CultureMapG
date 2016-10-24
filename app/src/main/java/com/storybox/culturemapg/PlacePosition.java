package com.storybox.culturemapg;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

/**
 * Created by sunghee on 2016-10-24.
 */

public class PlacePosition {
    public int id;
    public String name;
    public float coordinateX, coordinateY;
    public LatLng latLng;

    public PlacePosition(JSONObject obj){
        try{
            id = obj.getInt("id");
            name = obj.getString("name");
            coordinateX = (float)obj.getDouble("coordinateX");
            coordinateY = (float)obj.getDouble("coordinateY");
            latLng = new LatLng(coordinateX, coordinateY);
        }catch (org.json.JSONException e){

        }
    }
}
