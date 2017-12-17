package com.example.kaziorin.sblms;

/**
 * Created by Kazi Orin on 12/16/2017.
 */

public class ZoneModel {

    String id,name ;

    ZoneModel( String id,String name)
    {
        this.id = id;
        this.name = name;
    }

    public String getId()
    {
        return this.id;
    }
    public String getName()
    {
        return this.name;
    }
}
