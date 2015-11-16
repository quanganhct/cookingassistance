package com.nguyenquanganh.cookingassistance.entities;

import android.database.Cursor;


/**
 * Created by nguyenquanganh on 8/31/15.
 */
public class CookingStep {
    private int id;
    int recipe_id;
    String content;
    Integer minute = null;
    int step_order;
    CookingTimer2 myTimer = null;
    boolean enableTurn = false;

    public CookingStep (Cursor c){
        id = c.getInt(c.getColumnIndex(Ingredient.ID));
        recipe_id = c.getInt(c.getColumnIndex(Ingredient.RECIPE_ID));
        content = c.getString(c.getColumnIndex(Ingredient.COOKING_STEP_CONTENT));
        minute = c.getInt(c.getColumnIndex(Ingredient.COOKING_TIMER));
        step_order = c.getInt(c.getColumnIndex(Ingredient.STEP_ORDER));
    }

    public void setEnableTurn(boolean b){
        enableTurn = b;
    }

    public boolean isTurnEnable(){
        return enableTurn;
    }

    public CookingTimer2 getMyTimer(){
        return myTimer;
    }
    public void setTimer(CookingTimer2 t){
        myTimer = t;
    }

    public String getContent() {
        return content;
    }

    public Integer getMinute() {
        return minute;
    }

    public int getOrder() {
        return step_order;
    }

    public int getRecipe_id() {
        return recipe_id;
    }

    public int getId() {
        return id;
    }
}
