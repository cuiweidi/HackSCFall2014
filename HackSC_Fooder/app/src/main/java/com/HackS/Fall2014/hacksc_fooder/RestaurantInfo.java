package com.HackS.Fall2014.hacksc_fooder;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Eli on 14/11/8.
 */
public class RestaurantInfo extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_info);
        TextView tv;
        tv=(TextView)this.findViewById(R.id.name);
        tv.setText(main.current.name);
        tv=(TextView)this.findViewById(R.id.address1);
        tv.setText(main.current.address);
        tv=(TextView)this.findViewById(R.id.address2);
        tv.setText(main.current.cityState+", "+main.current.country+", "+main.current.zipCode);
        tv=(TextView)this.findViewById(R.id.phone);
        tv.setText(main.current.displayPhone);
        tv=(TextView)this.findViewById(R.id.price);
        tv.setText(String.valueOf(main.current.rating));
        tv=(TextView)this.findViewById(R.id.hours);
        ImageView iv=(ImageView)this.findViewById(R.id.imageView5);
        try {
            new ImageDownloader(iv).execute(main.current.mapUrl);
        }  catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void returnClicked(View view){
        this.finish();
    }
   /* public void mapClicked(View view){
        Intent intent=new Intent(this,MapDemoActivity.class);
        this.startActivity(intent);
    }*/
}