package com.ict.test;

import android.net.Uri;

import java.net.URI;
import java.net.URL;

public class Message {
    public static String SENT_BY_ME = "me";
    public static String SENT_BY_BOT="bot";

    public static  String SENT_BY_URI="URI";

    String message;
    String sentBy;

    Uri imageUri;



    public String getMessage() {
        return message;
    }

    public Uri getImageUri(){return imageUri;}

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSentBy() {
        return sentBy;
    }

    public void setSentBy(String sentBy) {
        this.sentBy = sentBy;
    }

    public Message(String message, String sentBy,Uri uri) {
        this.message = message;
        this.sentBy = sentBy;
        this.imageUri =uri;
    }


}

