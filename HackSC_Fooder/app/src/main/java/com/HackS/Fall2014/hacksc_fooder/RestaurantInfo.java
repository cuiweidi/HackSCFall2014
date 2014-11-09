package com.HackS.Fall2014.hacksc_fooder;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Eli on 14/11/8.
 */
public class RestaurantInfo extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_info);

    }
    public void returnClicked(View view){
        this.finish();
    }
    public void mapClicked(View view){
        Intent intent=new Intent(this,MapDemoActivity.class);
        this.startActivity(intent);
    }
}