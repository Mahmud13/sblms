package com.example.kaziorin.sblms;

/**
 * Created by Kazi Orin on 12/16/2017.
 */

public class ShopModel {
   String name,ownerName,address,phone,email,website,latitude,longitude,image,createdAt,id;

    int districtId;
    ShopModel( String name,String ownerName,String address,String phone,String email,String website,int districtId,String latitude,String longitude,String image)
    {
        this.name = name;
        this.ownerName = ownerName;
        this.address =address;
        this.phone = phone;
        this.email = email;
        this.website = website;
        this.districtId = districtId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image=image;
    }
}
