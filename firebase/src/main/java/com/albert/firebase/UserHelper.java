package com.albert.firebase;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

/**
 * Created by feiwh on 2017/3/18.
 */

public class UserHelper {
    private static UserHelper mUserHelper;
    private DatabaseReference mDatabaseReference;
    private UserHelper(){
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("userinfo");
    }

    /*
    * user register
    * */
    public void register(UserInfo userInfo,Callback callback){
        mDatabaseReference.child(userInfo.id).setValue(userInfo);
        // Read from the database
        login(userInfo,callback);
    }

    public static UserHelper getInstance(){
        synchronized (UserHelper.class) {
            if (mUserHelper == null) {
                synchronized (UserHelper.class) {
                    mUserHelper = new UserHelper();
                }
            }
        }
        return mUserHelper;
    }

    /*
    * check user information
    * */
    public void checkUserRegisterStatus(final UserInfo userInfo , final Callback callback){
        ValueEventListener postListener = new ValueEventListener() {
            boolean status = false;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!status) {
                    Log.w("albert", "checkUserRegisterStatus onDataChange");
                    status = true;
                    // Get Post object and use the values to update the UI
                    Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                    while (iterator.hasNext()) {
                        DataSnapshot ds = iterator.next();
                        if (userInfo.id.equals(ds.child("id").getValue())) {
                            callback.failed();
                            return;
                        }
                    }
                    callback.succeed(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                if(!status) {
                    Log.w("albert", "checkUserRegisterStatus loadPost:onCancelled", databaseError.toException());
                    status = true;
                    callback.failed();
                }
            }
        };
        mDatabaseReference.addValueEventListener(postListener);
    }

    /*
    *  login in with id password
    * */
    public void login(final UserInfo userInfo , final Callback callback){
        ValueEventListener postListener = new ValueEventListener() {
            boolean status = false;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!status) {
                    Log.w("albert", "checkUserRegisterStatus onDataChange");
                    status = true;
                    // Get Post object and use the values to update the UI
                    Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                    while (iterator.hasNext()) {
                        DataSnapshot ds = iterator.next();
                        if (userInfo.id.equals(ds.child("id").getValue()) && userInfo.password.equals(ds.child("password").getValue())) {
                            String id = null,name = null,password = null,email = null,phone = null,photo = null;
                            if(ds.hasChild("id")){
                                id=ds.child("id").getValue().toString();
                            }
                            if(ds.hasChild("name")){
                                name=ds.child("name").getValue().toString();
                            }
                            if(ds.hasChild("password")){
                                password=ds.child("password").getValue().toString();
                            }
                            if(ds.hasChild("email")){
                                email=ds.child("email").getValue().toString();
                            }
                            if(ds.hasChild("phone")){
                                phone=ds.child("phone").getValue().toString();
                            }
                            if(ds.hasChild("photo")){
                                photo=ds.child("photo").getValue().toString();
                            }
                            callback.succeed(new UserInfo(id,name,password,email,phone,photo));
                            return;
                        }
                    }
                    callback.failed();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if(!status) {
                    // Getting Post failed, log a message
                    status = true;
                    Log.w("albert", "checkUserRegisterStatus loadPost:onCancelled", databaseError.toException());
                    callback.failed();
                }
            }
        };
        mDatabaseReference.addValueEventListener(postListener);
    }

    public interface Callback {
        void succeed(UserInfo userInfo);
        void failed();
    }
}
