package com.nguyenquanganh.cookingassistance.entities;

import android.database.Cursor;

import java.io.Serializable;

/**
 * Created by nguyenquanganh on 8/29/15.
 */
public class Recipe implements Serializable{

    int id;
    String recipe_name;
    String source;
    String image_uri;
    String list_meat;
    String list_vegetable;
    String list_dailyIngredient;
    String list_seasoning;

    public Recipe (String name, int id) {
        this.recipe_name = name;
        this.id = id;
    }

    public Recipe (Cursor c) {
        this.recipe_name = c.getString(c.getColumnIndex(Ingredient.RECIPE_NAME));
        this.id = c.getInt(c.getColumnIndex(Ingredient.ID));
        this.source = c.getString(c.getColumnIndex(Ingredient.RECIPE_SOURCE));
        this.image_uri = c.getString(c.getColumnIndex(Ingredient.RECIPE_IMAGE));
        this.list_meat = c.getString(c.getColumnIndex(Ingredient.LIST_RECIPE_MAIN_INGREDIENT));
        this.list_vegetable = c.getString(c.getColumnIndex(Ingredient.LIST_RECIPE_VEGETABLE));
        this.list_dailyIngredient = c.getString(c.getColumnIndex(Ingredient.LIST_RECIPE_ADDITIVE_ID));
        this.list_seasoning = c.getString(c.getColumnIndex(Ingredient.LIST_RECIPE_SEASONING));
    }

    public String getList_meat(){
        return this.list_meat;
    }

    public String getList_seasoning(){
        return this.list_seasoning;
    }

    public String getList_vegetable(){
        return this.list_vegetable;
    }

    public String getList_dailyIngredient(){
        return this.list_dailyIngredient;
    }

    public String getName() {
        return this.recipe_name;
    }
    public String getSource() { return this.source; }
    public String getImage_uri() {
        return this.image_uri;
    }
    public int getId(){
        return id;
    }

}
