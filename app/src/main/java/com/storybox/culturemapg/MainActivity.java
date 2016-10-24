package com.storybox.culturemapg;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.androidquery.AQuery;

/**
 * Created by sunghee on 2016-10-14.
 */

public class MainActivity extends AppCompatActivity{
    private AQuery aq = new AQuery( this );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toast.makeText(this, "main activity created", Toast.LENGTH_LONG).show();
        //test123
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void callMapActivity(View v){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}
