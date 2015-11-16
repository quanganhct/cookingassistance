package com.nguyenquanganh.cookingassistance.entities;

import android.database.Cursor;

import com.nguyenquanganh.cookingassistance.enums.TypeIngredient;

/**
 * Created by nguyenquanganh on 8/29/15.
 */
public class Seasoning extends Ingredient{
    private int id;
    String seasoning_name;

    public Seasoning (String name, int id){
        this.seasoning_name = name;
        this.id = id;
    }

    public Seasoning (Cursor c){
        this.seasoning_name = c.getString(c.getColumnIndex("seasoning_name"));
        this.id = c.getInt(c.getColumnIndex("_id"));
    }

    public String getName(){
        return seasoning_name;
    }

    public int getId() {
        return id;
    }
}
