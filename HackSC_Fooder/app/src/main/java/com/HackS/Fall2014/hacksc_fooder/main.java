package com.HackS.Fall2014.hacksc_fooder;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.HackS.Fall2014.hacksc_fooder.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class main extends Activity {
    protected int index;
    protected ArrayList<Restaurant> list;
    protected ImageView iv;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        index=0;
        YelpAPI yelpAPI=new YelpAPI("","Los Angeles, CA",100);
        list=yelpAPI.queryAPI(yelpAPI);
        iv=(ImageView)this.findViewById(R.id.imageView3);
        try {
            String url="http://s16.sinaimg.cn/orignal/89429f6dhb99b4903ebcf&690";
           Bitmap bitmap = BitmapFactory.decodeStream(new URL(url).openConnection().getInputStream());
            if(bitmap!=null){

                Toast.makeText(getApplicationContext(),"NULLLLLLLLLLL!",Toast.LENGTH_LONG).show();
            }
            iv.setImageBitmap(bitmap);
        }catch(Exception E){
            Toast.makeText(getApplicationContext(),E.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
    @Override

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    }

