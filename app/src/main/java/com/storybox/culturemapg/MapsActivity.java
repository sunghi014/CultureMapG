package com.storybox.culturemapg;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private final int REQ_CODE_PERMISSION_REQUEST = 2;
    private AQuery aq = new AQuery( this );     //php 통신용 객체
    private PlacePosition[] placePositions;     //지도에 위치 표시하기 위한 객체 배열
    public int numMarkers;
    private String serverLocation, serverQueryLocation;
    public SimplePlaceInfo simplePlaceInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        serverLocation = "http://chansh2013.cafe24.com";
        serverQueryLocation = serverLocation+"/test.php";

        hideInfoBox();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // 사용자의 현재위치 접근 권한이 없을 때 진입
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            // 권한이 필요한 이유를 설명해야하는가?(한 번 이상 권한 거부했을 때)
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)) {
                // 다이얼로그로 사용자에게 해당 권한이 필요한 이유에 대해 설명
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setIcon(R.drawable.ic_my_location_black_24dp);
                alertDialog.setTitle(R.string.alertDialog_permission_title);
                alertDialog.setMessage(R.string.alertDialog_permission_message);
                alertDialog.setNegativeButton(R.string.bt_close, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        requestPermission(new String[]{Manifest.permission.ACCESS_FINE_LOCATION});
                    }
                }); //닫기버튼 누르면, 권한요청 메소드 호출하도록 콜백 등록
                alertDialog.show();

            } else {
                requestPermission(new String[]{Manifest.permission.ACCESS_FINE_LOCATION});
            }
        }
        else{
            mMap.setMyLocationEnabled(true);
        }

        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);

        //위치좌표 셋팅
        LatLng acc = new LatLng(35.147, 126.920); //아시아문화전당
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(acc, 16));
        getCoordinatesFromHttpServer();


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                getSimplePlaceInfoFromHttpServer(marker.getTag().toString()); //place 셋팅
                mMap.getUiSettings().setZoomControlsEnabled(false);
                return false;
            }
        });

        /* 마커를 클릭하면 나타나는 작은 infoWindow를 클릭하면 발생하는 이벤트
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String text = "[Info Window Click Event]" + " info TITLE : " + marker.getTitle().toString();
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG)
                        .show();
                showInfoBox();
            }
        });
        */
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //맵의 아무곳이나 클릭을하면 infoBox를 숨기고 줌컨트롤 다시 표시
                hideInfoBox();
                mMap.getUiSettings().setZoomControlsEnabled(true);
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String text = null;

        switch (item.getItemId()){
            /*case R.id.action_search:
                text = "click the search button";
                break;*/
            case R.id.action_settings:
                text = "click the setting button";
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        return true;
    }

    private void hideInfoBox()
    {
        View infoBox = findViewById(R.id.infoBox);
        if(infoBox.getVisibility() == View.VISIBLE)
        {
            infoBox.setVisibility(View.GONE);
        }
    }

    private void showInfoBox()
    {
        View infoBox = findViewById(R.id.infoBox);

        if(infoBox.getVisibility() == View.GONE)
        {
            infoBox.setVisibility(View.VISIBLE);
        }
    }

    //infoBox 클릭 시 PlaceInfoActivity를 실행시키는 메소드
    public void callPlaceInfoActivity(View view)
    {
        if(simplePlaceInfo != null) {
            //Toast.makeText(this, "fragment clicked", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, PlaceInfoActivity.class);
            intent.putExtra("place_id", simplePlaceInfo.id);
            startActivity(intent);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        //어떤 권한이든, 권한요청 이후 사용자가 응답했을 때 그 결과값에따른 처리를 하는 메소드
        switch (requestCode) {
            case REQ_CODE_PERMISSION_REQUEST:

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 권한 허가
                    try {
                        mMap.setMyLocationEnabled(true);
                    }catch (SecurityException e){
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // 권한 거부
                    Toast.makeText(this, "권한이 거부되었습니다.", Toast.LENGTH_LONG).show();
                }
                return;
        }
    }

    public void requestPermission(String[] needPermissions) {
        ActivityCompat.requestPermissions(this, needPermissions, REQ_CODE_PERMISSION_REQUEST);
    }

    private void getCoordinatesFromHttpServer(){
        //전송할 data(json형식과 비슷한 키-값 쌍) 구성하는 구문
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", "getCoordinates");
        map.put("isAjax", "1");

        //ajax요청 및 결과값 가져오기(callback으로)
        aq.ajax(serverQueryLocation, map, JSONArray.class, new AjaxCallback<JSONArray>(){
            @Override
            public void callback(String url, JSONArray object, AjaxStatus status){
                if(object != null){
                    try{
                        String successCode = "success";
                        if(object.getJSONObject(0).get("result").toString().equals(successCode)){
                            numMarkers = object.getJSONObject(0).getInt("num_rows");
                            placePositions = new PlacePosition[numMarkers];
                            for(int i=0; i < numMarkers; i++){
                                placePositions[i] = new PlacePosition(object.getJSONObject(i+1));
                            }
                            setMarkers();
                        }
                        else{
                            Toast.makeText(MapsActivity.this, "error : " + object.getJSONObject(0).get("result").toString(), Toast.LENGTH_SHORT).show();
                        }
                    }catch(Exception e){
                        Toast.makeText(MapsActivity.this, "error in ajax callback method : " + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(MapsActivity.this, "서버로부터 받은 결과값이 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setMarkers(){
        for(int i=0; i < numMarkers; i++){
            mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.pointer))
                    .anchor(0.0f, 1.0f)
                    .position(placePositions[i].latLng)
                    .title(placePositions[i].name))
                    .setTag(placePositions[i].id);
        }
    }

    private void getSimplePlaceInfoFromHttpServer(String id){
        //전송할 data(json형식과 비슷한 키-값 쌍) 구성하는 구문
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", "getSimplePlaceInfo");
        map.put("isAjax", "1");
        map.put("id", id);

        //ajax요청 및 결과값 가져오기(callback으로)
        aq.ajax(serverQueryLocation, map, JSONObject.class, new AjaxCallback<JSONObject>(){
            @Override
            public void callback(String url, JSONObject object, AjaxStatus status){
                if(object != null){
                    try{
                        String successCode = "success";
                        if(object.get("result").toString().equals(successCode)){
                            simplePlaceInfo = new SimplePlaceInfo(object);
                            setInfoBox();
                        }
                        else{
                            Toast.makeText(MapsActivity.this, "error : " + object.get("result").toString(), Toast.LENGTH_SHORT).show();
                        }
                    }catch(Exception e){
                        Toast.makeText(MapsActivity.this, "error in ajax callback method : " + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(MapsActivity.this, "서버로부터 받은 결과값이 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setInfoBox(){
        //infoBox의 ImageView를 id를통해 찾아서, 이미지를 셋팅함
        ImageView iconImageView = (ImageView) findViewById(R.id.infoBox_icon);
        //infoBox의 TextView를 id를 통해 찾아서, 텍스트값을 새로 셋팅함
        TextView nameTextView = (TextView) findViewById(R.id.infoBox_name);
        TextView addressTextView = (TextView) findViewById(R.id.infoBox_address);
        TextView contactTextView = (TextView) findViewById(R.id.infoBox_contact);
        TextView managerTextView = (TextView) findViewById(R.id.infoBox_category);

        aq.id(iconImageView).image(serverLocation+ simplePlaceInfo.icon_path);
        nameTextView.setText(simplePlaceInfo.name);
        addressTextView.setText(simplePlaceInfo.address);
        contactTextView.setText(simplePlaceInfo.contact);
        managerTextView.setText(simplePlaceInfo.category);

        //infoBox 나타냄
        showInfoBox();
    }

}
