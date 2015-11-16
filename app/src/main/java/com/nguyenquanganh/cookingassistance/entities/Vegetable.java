package com.nguyenquanganh.cookingassistance.entities;

import android.database.Cursor;

/**
 * Created by nguyenquanganh on 8/31/15.
 */
public class Vegetable {
    private int id;
    String vegetable_name;

    public Vegetable (Cursor c){
        id = c.getInt(c.getColumnIndex(Ingredient.ID));
        vegetable_name = c.getString(c.getColumnIndex(Ingredient.VEGETABLE_NAME));
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return vegetable_name;
    }
}
