package com.storybox.culturemapg;

import android.content.Context;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by sunghee on 2016-10-24.
 */

public class Manager {
    public int id;
    public String name, contact, email, managing_place_ids_string, managing_place_names_string;
    public String[] managing_place_ids, managing_place_names;
    private AQuery aq;
    private String serverLocation;
    private String serverQueryLocation;

    public Manager(int manager_id, Context context) {
        aq = new AQuery(context);
        serverLocation = "http://chansh2013.cafe24.com";
        serverQueryLocation = serverLocation+"/test.php";
        getManagerInfoFromHttpServer(Integer.toString(manager_id));
    }

    private void getManagerInfoFromHttpServer(String manager_id){
        //전송할 data(json형식과 비슷한 키-값 쌍) 구성하는 구문
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", "getManagerInfo");
        map.put("isAjax", "1");
        map.put("id", manager_id);

        //ajax요청 및 결과값 가져오기(callback으로)
        aq.ajax(serverQueryLocation, map, JSONObject.class, new AjaxCallback<JSONObject>(){
            @Override
            public void callback(String url, JSONObject object, AjaxStatus status){
                if(object != null){
                    try{
                        String successCode = "success";
                        if(object.get("result").toString().equals(successCode)){
                            id = object.getInt("id");
                            name = object.getString("name");
                            contact = object.getString("contact");
                            email = object.getString("email");
                            managing_place_ids_string = object.getString("managing_place_ids");
                            managing_place_ids = managing_place_ids_string.split(",");
                            managing_place_names_string = object.getString("managing_place_names");
                            managing_place_names = managing_place_names_string.split(",");
                        }
                    }catch(Exception e){

                    }
                }
                else{

                }
            }
        });
    }
}
