package com.nguyenquanganh.cookingassistance.entities;

import android.database.Cursor;

import com.nguyenquanganh.cookingassistance.enums.TypeMainIngredient;

/**
 * Created by nguyenquanganh on 8/31/15.
 */
public class MainIngredient {
    int id;
    String meat_name;
    TypeMainIngredient type;
    String animal;

    public MainIngredient(Cursor c){
        id = c.getInt(c.getColumnIndex(Ingredient.ID));
        meat_name = c.getString(c.getColumnIndex(Ingredient.MAIN_INGREDIENT_NAME));
        String mt = c.getString(c.getColumnIndex(Ingredient.MAIN_INGREDIENT_TYPE));
        animal = c.getString(c.getColumnIndex(Ingredient.FROM));
        type = getTypeMainIngredient(mt);
    }

    private TypeMainIngredient getTypeMainIngredient(String mt){
        switch (mt){
            case "REDMEAT": return TypeMainIngredient.REDMEAT;
            case "FISH" : return TypeMainIngredient.FISH;
        }
        return null;
    }

    public int getId(){
        return id;
    }

    public String getName() {
        return meat_name;
    }
}
