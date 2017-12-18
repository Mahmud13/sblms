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

            this.salePrice = salePrice;

            this.purchasePrice = purchasePrice;

            this.comment= comment;


    }
    BrandModel( String name,Double purchasePrice ,Double salePrice)
    {
        this.name = name;
        this.purchasePrice = purchasePrice;
        this.salePrice = salePrice;
    }
}
