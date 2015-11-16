package com.nguyenquanganh.cookingassistance.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.nguyenquanganh.cookingassistance.entities.CookingStep;
import com.nguyenquanganh.cookingassistance.entities.DailyIngredient;
import com.nguyenquanganh.cookingassistance.entities.Ingredient;
import com.nguyenquanganh.cookingassistance.entities.LoadedDataStorage;
import com.nguyenquanganh.cookingassistance.entities.MainIngredient;
import com.nguyenquanganh.cookingassistance.entities.Recipe;
import com.nguyenquanganh.cookingassistance.entities.Seasoning;
import com.nguyenquanganh.cookingassistance.entities.Vegetable;
import com.nguyenquanganh.cookingassistance.sqliteassets.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by nguyenquanganh on 8/29/15.
 */
public class CookingDatabase extends SQLiteAssetHelper{
    private static final String DATABASE_NAME = "cooking.db";
    private static final int DATABASE_VERSION = 1;
    SQLiteDatabase db;

    public CookingDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public Cursor getCursorSeasonings() {
        if (db == null)
            db = getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        builder.setTables(Ingredient.SEASONINGS_TABLE);
        Cursor c = builder.query(db, null, null, null, null, null, null);
        c.moveToFirst();
        return c;
    }

    public Cursor getCursorMainIngredient() {
        if (db == null)
            db = getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        builder.setTables(Ingredient.MAIN_INGREDIENT_TABLE);
        Cursor c = builder.query(db, null, null, null, null, null, null);
        c.moveToFirst();
        return c;
    }

    public Cursor getCursorVegetable() {
        if (db == null)
            db = getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(Ingredient.VEGETABLE_TABLE);
        Cursor c = builder.query(db, null, null, null, null, null, null);
        c.moveToFirst();
        return c;
    }

    public Cursor getCursorDailyIngredient() {
        if (db == null)
            db = getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(Ingredient.ADDITIVE_TABLE);
        Cursor c = builder.query(db, null, null, null, null, null, null);
        c.moveToFirst();
        return c;
    }

    public String getPreCookingSteps(Recipe r){
        String steps = null;
        if (db == null)
            db = getWritableDatabase();
        //SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        //builder.setTables(Ingredient.PRE_COOKING_TABLE);
        String query = "SELECT * FROM "+ Ingredient.PRE_COOKING_TABLE +" WHERE recipe_id = ?";
        //Cursor c = builder.query(db, null, Ingredient.RECIPE_ID + " = ?", new String[]{((Integer.toString(r.getId())))}, null, null, null);

        Cursor c = db.rawQuery(query, new String[]{String.valueOf(r.getId())});
        c.moveToFirst();
        if (c != null & c.getCount() > 0){
            steps = c.getString(c.getColumnIndex(Ingredient.PRE_COOKING_STEPS));
        }
        c.close();

        return steps;
    }

    public List<CookingStep> getCookingStep(Recipe r){
        List<CookingStep> _list = new ArrayList<CookingStep>();

        if (db == null)
            db = getWritableDatabase();
        String query = "SELECT * FROM " + Ingredient.COOKING_STEP_TABLE + " WHERE " + Ingredient.RECIPE_ID +" = ? ORDER BY "
                + Ingredient.STEP_ORDER + " ASC";
        Cursor c = db.rawQuery(query, new String[]{String.valueOf(r.getId())});
        c.moveToFirst();
        if (c != null & c.getCount() > 0){
            while (!c.isAfterLast()){
                CookingStep step = new CookingStep(c);
                _list.add(step);
                c.moveToNext();
            }
        }
        c.close();
        return _list;
    }

    public void loadDailyIngredient(){
        if (LoadedDataStorage.dailyIngredientMap == null)
            LoadedDataStorage.dailyIngredientMap = new HashMap<Integer, DailyIngredient>();
        if (LoadedDataStorage.dailyIngredientMap.isEmpty()){
            Cursor c = getCursorDailyIngredient();
            while (!c.isAfterLast()){
                DailyIngredient di = new DailyIngredient(c);
                LoadedDataStorage.dailyIngredientMap.put(di.getId(), di);
                c.moveToNext();
            }
            c.close();
        }
    }

    public void loadVegetable() {
        if (LoadedDataStorage.vegetableMap == null) {
            LoadedDataStorage.vegetableMap = new HashMap<Integer, Vegetable>();
        }
        if (LoadedDataStorage.vegetableMap.isEmpty()){
            Cursor c = getCursorVegetable();
            while (!c.isAfterLast()) {
                Vegetable v = new Vegetable(c);
                LoadedDataStorage.vegetableMap.put(v.getId(), v);
                c.moveToNext();
            }
            c.close();
        }
    }

    public void loadSeasonings() {
        if (LoadedDataStorage.seasoningMap == null) {
            LoadedDataStorage.seasoningMap = new HashMap<Integer, Seasoning>();
        }
        if (LoadedDataStorage.seasoningMap.isEmpty()){
            Cursor c = getCursorSeasonings();
            while (!c.isAfterLast()) {
                Seasoning s = new Seasoning(c);
                LoadedDataStorage.seasoningMap.put(s.getId(), s);
                c.moveToNext();
            }
            c.close();
        }
    }

    public void loadMainIngredient() {
        if (LoadedDataStorage.meatMap == null){
            LoadedDataStorage.meatMap = new HashMap<Integer, MainIngredient>();
        }
        if (LoadedDataStorage.meatMap.isEmpty()){
            Cursor c = getCursorMainIngredient();
            while (!c.isAfterLast()) {
                MainIngredient m = new MainIngredient(c);
                LoadedDataStorage.meatMap.put(m.getId(), m);
                c.moveToNext();
            }
            c.close();
        }
    }

    public void loadRecipes() {
        if (LoadedDataStorage.listRecipe == null )
            LoadedDataStorage.listRecipe = new ArrayList<Recipe>();
        if (LoadedDataStorage.listRecipe.isEmpty()){
            String query = "SELECT * FROM " + Ingredient.RECIPE_TABLE +
                    " r LEFT JOIN "+ Ingredient.RECIPE_MAIN_INGREDIENT_TABLE + " rm ON r."
                    + Ingredient.ID + "=rm." + Ingredient.RECIPE_ID
                    + " LEFT JOIN " + Ingredient.RECIPE_SEASONING_TABLE + " rs ON r."
                    + Ingredient.ID + "=rs." + Ingredient.RECIPE_ID
                    + " LEFT JOIN " + Ingredient.RECIPE_ADDITIVE_TABLE + " rd ON r."
                    + Ingredient.ID + "=rd." + Ingredient.RECIPE_ID
                    + " LEFT JOIN " + Ingredient.RECIPE_VEGETABLE_TABLE + " rv ON r."
                    + Ingredient.ID + "=rv." + Ingredient.RECIPE_ID;
            Cursor c = db.rawQuery(query, null);
            c.moveToFirst();
            while (!c.isAfterLast()) {
                Recipe r = new Recipe(c);
                LoadedDataStorage.listRecipe.add(r);
                c.moveToNext();
            }
            c.close();
        }
    }
}
