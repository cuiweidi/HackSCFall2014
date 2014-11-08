package com.HackS.Fall2014.hacksc_fooder;

/**
 * Created by Cuiweidi on 11/8/2014.
 */
public class User {
    private String password;
    private String username;
    private String nickName;
    public User(String username){
        this.username=username;
        password=null;
    }
    public User(String username, String pw){
        this.username=username;
        password=pw;
    }
    protected void setPassword( String pw){
        password=pw;
    }
    protected boolean checkPassword(String pw){
        if(password==pw)
            return true;
        else
            return false;
    }
    public String getUsername(){
        return this.username;
    }
    public String getName(){
        return this.nickName;
    }
}
