package com.example.kaziorin.sblms;

import java.util.ArrayList;

/**
 * Created by Kazi Orin on 12/17/2017.
 */

public class ProductModel {
    String name;
    ArrayList<BrandModel> brands;
    ProductModel(String name)
    {
        this.name= name;
        this.brands = new ArrayList<BrandModel>();
    }
}
