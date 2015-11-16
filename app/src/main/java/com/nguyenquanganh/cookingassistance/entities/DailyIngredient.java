package com.nguyenquanganh.cookingassistance.entities;

import android.database.Cursor;

/**
 * Created by nguyenquanganh on 8/31/15.
 */
public class DailyIngredient {
    private int id;
    String daily_name;

    public DailyIngredient(Cursor c){
        id = c.getInt(c.getColumnIndex(Ingredient.ID));
        daily_name = c.getString(c.getColumnIndex(Ingredient.ADDITIVE_NAME));
    }

    public int getId(){
        return id;
    }

    public String getName() {
        return daily_name;
    }
}
