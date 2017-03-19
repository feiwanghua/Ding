package com.feiwanghua.ding;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.albert.firebase.UserInfo;

/**
 * Created by feiwanghua on 2017/3/20.
 */

public class LoginInfo {
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_PHOTO = "photo";


    public static void setUserInfo(Context context,UserInfo userInfo){
        SharedPreferences mSharedPreferences = context.getSharedPreferences("LoginInfo",Context.MODE_PRIVATE);
        if(!TextUtils.isEmpty(KEY_ID)) {
            mSharedPreferences.edit().putString(KEY_ID, userInfo.id).apply();
        }
        if(!TextUtils.isEmpty(KEY_NAME)) {
            mSharedPreferences.edit().putString(KEY_NAME, userInfo.name).apply();
        }
        if(!TextUtils.isEmpty(KEY_PASSWORD)) {
            mSharedPreferences.edit().putString(KEY_PASSWORD, userInfo.password).apply();
        }
        if(!TextUtils.isEmpty(KEY_EMAIL)) {
            mSharedPreferences.edit().putString(KEY_EMAIL, userInfo.email).apply();
        }
        if(!TextUtils.isEmpty(KEY_PHONE)) {
            mSharedPreferences.edit().putString(KEY_PHONE, userInfo.phone).apply();
        }
        if(!TextUtils.isEmpty(KEY_PHOTO)) {
            mSharedPreferences.edit().putString(KEY_PHOTO, userInfo.photo).apply();
        }
    }

    public static void initLoginInfo(Context context){
        SharedPreferences mSharedPreferences = context.getSharedPreferences("LoginInfo",Context.MODE_PRIVATE);
        mSharedPreferences.edit().clear();
    }

    public static String getID(Context context){
        SharedPreferences mSharedPreferences = context.getSharedPreferences("LoginInfo",Context.MODE_PRIVATE);
        return mSharedPreferences.getString(KEY_ID,"");
    }

    public static String getName(Context context){
        SharedPreferences mSharedPreferences = context.getSharedPreferences("LoginInfo",Context.MODE_PRIVATE);
        return mSharedPreferences.getString(KEY_NAME,"");
    }

    public static String getPhoto(Context context){
        SharedPreferences mSharedPreferences = context.getSharedPreferences("LoginInfo",Context.MODE_PRIVATE);
        return mSharedPreferences.getString(KEY_PHOTO,"");
    }

    public static String getEmail(Context context){
        SharedPreferences mSharedPreferences = context.getSharedPreferences("LoginInfo",Context.MODE_PRIVATE);
        return mSharedPreferences.getString(KEY_EMAIL,"");
    }
}
