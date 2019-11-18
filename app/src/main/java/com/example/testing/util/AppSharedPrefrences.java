package com.example.testing.util;

import android.content.Context;
import android.content.SharedPreferences;



/**
 * Created by Raj on 12/12/17.
 */

public class AppSharedPrefrences {

    static public void setImage(Context c, String value) {
        SharedPreferences settings = c.getSharedPreferences(Constants.IMAGE_NAME, 0);
        settings = c.getSharedPreferences(Constants.IMAGE_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Constants.IMAGE_NAME, value);
        editor.apply();
    }
    static public String getImage(Context c) {
        SharedPreferences settings = c.getSharedPreferences(Constants.IMAGE_NAME, 0);
        settings = c.getSharedPreferences(Constants.IMAGE_NAME, 0);
        String value = settings.getString(Constants.IMAGE_NAME, "");
        return value;
    }


}




