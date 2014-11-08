package com.HackS.Fall2014.hacksc_fooder;

import android.app.Activity;
import android.app.Fragment;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.HackS.Fall2014.hacksc_fooder.R.menu.menu_register;


public class RegisterActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }
    public void cancelClicked(View view){
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(menu_register, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private View rootView;
        public PlaceholderFragment() {
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.fragment_register, container, false);

            Button registerButt=(Button) rootView.findViewById(R.id.register_button);
            registerButt.setOnClickListener(new View.OnClickListener()
            {

                @Override
                public void onClick(View v) {
                    String email = ((EditText)rootView.findViewById(R.id.text_email)).getText().toString();
                    String password = ((EditText)rootView.findViewById(R.id.text_password)).getText().toString();

                    if (email.length()==0)
                    {
                       Toast.makeText(PlaceholderFragment.this.getActivity().getApplicationContext(), "Please input a valid email name", Toast.LENGTH_LONG).show();
                    }
                    else if (password.length()==0)
                    {
                       Toast.makeText(PlaceholderFragment.this.getActivity().getApplicationContext(), "Please input a valid password", Toast.LENGTH_LONG).show();
                    }
                    else if( LogInActivity.user.containsKey(email)){
                        Toast.makeText(PlaceholderFragment.this.getActivity().getApplicationContext(), "Your username has been used!", Toast.LENGTH_LONG).show();
                    }
                    else if (email.length()!=0&&password.length()!=0)
                    {
                        try
                        {
                            File fl=new File(PlaceholderFragment.this.getActivity().getFilesDir().getPath()+"/user.txt");
                            System.out.println(fl.getAbsolutePath());
                            FileWriter fw = new FileWriter(fl);
                            PrintWriter pw = new PrintWriter(fw);
                            LogInActivity.user.put(email, new User(email, password));
                            Iterator it=LogInActivity.user.entrySet().iterator();
                            while(it.hasNext()){
                                Map.Entry pairs=(Map.Entry)it.next();
                                pw.println(pairs.getKey());
                                pw.println(pairs.getValue());
                            }
                            Toast.makeText(PlaceholderFragment.this.getActivity().getApplicationContext(),"You have created an account!", Toast.LENGTH_LONG).show();
                            PlaceholderFragment.this.getActivity().finish();

                        } catch (IOException e) {
                            Toast.makeText(PlaceholderFragment.this.getActivity().getApplicationContext(), "IOE Exception: "+e.getMessage(), Toast.LENGTH_LONG).show();
                        }


                    }

                }
            });
            return rootView;
        }
    }
}
