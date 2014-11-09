package com.HackS.Fall2014.hacksc_fooder;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.HackS.Fall2014.hacksc_fooder.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;


public class main extends Activity {
    protected int index;
    protected ArrayList<Restaurant> list;
    protected ImageView iv;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Initial("ramen"," Los Angeles, CA");
        try {
            iv = (ImageView) this.findViewById(R.id.picture);
            System.out.println(list.size());
            new ImageDownloader(iv).execute(list.get(index).imageUrl);
            index++;
        }  catch (Exception e){
            System.out.println(e.getMessage());
        }

        /*



        iv=(ImageView)this.findViewById(R.id.imageView3);
        try {
            String url="http://s16.sinaimg.cn/orignal/89429f6dhb99b4903ebcf&690";
            System.out.println(url);
            URL u=new URL(url);
            System.out.println("abaciajfiaejfjaijfao");
            InputStream in=u.openConnection().getInputStream();
            System.out.println("Hello");
           Bitmap bitmap = BitmapFactory.decodeStream(in);
            if(bitmap==null){
                System.out.println("Mark");
            }else{
                System.out.println("NotNULL");
            }
            iv.setImageBitmap(bitmap);
        }catch(Exception E){
            System.out.println(E.getMessage());
            Toast.makeText(getApplicationContext(),E.getMessage(),Toast.LENGTH_LONG).show();
        }*/
    }
    public void Initial(String kind, String location){
        index=0;
        YelpAPI yelpAPI=new YelpAPI(kind,location,10);
        list=yelpAPI.queryAPI(yelpAPI);
            System.out.println("Hello6");
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
    public void dislikeClicked(View view){
        if(index==list.size())
            Initial("ramen","Los Angeles, CA");
        try {
            //System.out.println(list.size());
            new ImageDownloader(iv).execute(list.get(index).imageUrl);
            index++;
        }  catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void likeClicked(View view){
        Intent intent=new Intent(this,RestaurantInfo.class);
        this.startActivity(intent);
    }
}

