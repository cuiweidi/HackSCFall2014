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
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Scanner;


public class LogInActivity extends Activity {
    protected static HashMap<String, User> user;
    protected static User currentUser;

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
        currentUser=null;
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

    public void signIn(View view){
        String username=((EditText)this.findViewById(R.id.text_email)).getText().toString();
        String pw=((EditText)this.findViewById(R.id.text_password)).getText().toString();
        if(!user.containsKey(username)){
            Toast.makeText(getApplicationContext(),"username does not exist!", Toast.LENGTH_LONG).show();
            return;
        }else if(!user.get(username).checkPassword(pw)){
            Toast.makeText(getApplicationContext(), "password does not match your username!",Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent=new Intent(this, main.class);
        this.startActivity(intent);
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
