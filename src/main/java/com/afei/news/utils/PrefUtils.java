package com.afei.news.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by fei on 2016/11/12.
 */

public class PrefUtils {
    public static  void putBoolean(String key, Boolean values, Context context){
        SharedPreferences sp =  context.getSharedPreferences("config",Context.MODE_PRIVATE);
        sp.edit().putBoolean(key,values).commit();
    }
    public static  Boolean getBoolean(String key, Boolean values, Context context){
        SharedPreferences sp =  context.getSharedPreferences("config",Context.MODE_PRIVATE);
        return sp.getBoolean(key,values);
    }
    public static  void putString(String key, String values, Context context){
        SharedPreferences sp =  context.getSharedPreferences("config",Context.MODE_PRIVATE);
        sp.edit().putString(key,values).commit();
    }
    public static  String getString(String key, String values, Context context){
        SharedPreferences sp =  context.getSharedPreferences("config",Context.MODE_PRIVATE);
        return sp.getString(key,values);
    }

    public static void putInt(String key, int values, Context context) {
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        sp.edit().putInt(key, values).commit();
    }
    public static  int getInt(String key, int values, Context context){
        SharedPreferences sp =  context.getSharedPreferences("config",Context.MODE_PRIVATE);
        return sp.getInt(key,values);
    }
}
