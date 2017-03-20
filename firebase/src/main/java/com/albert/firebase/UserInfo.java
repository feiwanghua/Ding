package com.albert.firebase;

/**
 * Created by feiwh on 2017/3/18.
 */

public class UserInfo {

    public UserInfo(String id,String name,String password,String email,String phone,String photo){
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.photo = photo;
    }
    public String id;
    public String name ;
    public String password ;
    public String email;
    public String phone;
    public String photo;
}
