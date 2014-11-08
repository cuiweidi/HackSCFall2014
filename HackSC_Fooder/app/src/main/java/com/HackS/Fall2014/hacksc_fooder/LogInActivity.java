package com.HackS.Fall2014.hacksc_fooder;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Path;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Scanner;


public class LogInActivity extends Activity {
    public static HashMap<String, User> user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_log_in);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        user=new HashMap<String, User>();
        try
        {
            File fl=new File(this.getApplicationContext().getFilesDir().getPath()+"/user.txt");
            //System.out.println(fl.getAbsolutePath());

            Scanner sc=new Scanner(new FileReader(fl));

            while (sc.hasNextLine())
            {
                String username=sc.nextLine();
                String password=sc.nextLine();
                LogInActivity.user.put(username, new User(username, password));
                //System.out.println("Mark");
            }
            System.out.println(user.size());
        }
        catch (IOException e)
        {
            Toast.makeText(getApplicationContext(), "IOE error: "+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_log_in, menu);
        return true;
    }
    public void registerClicked(View view){
        Intent intent=new Intent(this,RegisterActivity.class);
        this.startActivity(intent);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_log_in, container, false);
            return rootView;
        }
    }
}
