package com.example.sanek.mess.Model;

/**
 * Created by sanek on 29.01.2018.
 */

public class Chat  {
    public String sender;

    public String senderUid;

    public String message;

    public Object timestamp;

    public Chat(){

    }

    public Chat( String message,String sender, String senderUid,Object timestamp){
        this.sender = sender;

        this.senderUid = senderUid;

        this.message = message;

        this.timestamp=timestamp;
    }





}