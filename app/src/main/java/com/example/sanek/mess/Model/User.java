package com.example.sanek.mess.Model;

/**
 * Created by sanek on 29.01.2018.
 */
import com.google.firebase.database.IgnoreExtraProperties;

    @IgnoreExtraProperties
    public class User {
    public static String uid;
    public static String email;
    public static String nickname;

    public User(){
        }

    public User(String email,String nickname, String uid){
        User.email = email;
        User.nickname =nickname;
        User.uid = uid;

    }

    }