package com.example.kaziorin.sblms;

/**
 * Created by Kazi Orin on 12/15/2017.
 */

public class UserModel {
    public String type,token,name,email,address,phone,password,id,latitude,longitude;

    UserModel()
    {}
     UserModel(String type,String name,String email,String address,String phone ,String password )
     {
         this.type = type;
         this.name = name;
         this.email = email;
         this.address = address;
         this.phone = phone;
         this.password = password;

     }
    UserModel(String type,String name,String email,String address,String phone)
    {
        this.type = type;
        this.name = name;
        this.email = email;
        this.address = address;
        this.phone = phone;
    }
    public void setPass(String password) {
        this.password = password;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }
    public String getType() {
        return type;
    }
    public String getEmail() {
        return email;
    }
    public String getAddress() {
        return address;
    }
    public String getPhone() {
        return phone;
    }
    public String getId() {
        return this.id;
    }

}
