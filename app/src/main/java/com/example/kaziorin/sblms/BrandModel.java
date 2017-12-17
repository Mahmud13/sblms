package com.example.kaziorin.sblms;

/**
 * Created by Kazi Orin on 12/17/2017.
 */

public class BrandModel {
    String name,comment;
    double purchasePrice,salePrice;
    BrandModel( String name,String comment,Double purchasePrice ,Double salePrice)
    {
        this.name = name;
        this.comment= comment;
        this.purchasePrice = purchasePrice;
        this.salePrice = salePrice;
    }
}
