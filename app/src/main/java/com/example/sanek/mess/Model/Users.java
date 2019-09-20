package com.example.sanek.mess.Model;

import com.google.firebase.database.IgnoreExtraProperties;


    @IgnoreExtraProperties
    public class Users {
        public String uid;
        public String nickname;

        public Users(){
        }

        public Users(String nickname, String uid){
            this.nickname=nickname;
            this.uid=uid;

        }

    }

