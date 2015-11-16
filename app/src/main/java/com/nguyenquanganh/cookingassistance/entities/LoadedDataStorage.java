package com.nguyenquanganh.cookingassistance.entities;

import java.util.List;
import java.util.Map;

/**
 * Created by nguyenquanganh on 8/30/15.
 */
public class LoadedDataStorage {
    public static List<Recipe> listRecipe;
    public static Map<Integer, Seasoning> seasoningMap;
    public static Map<Integer, MainIngredient> meatMap;
    public static Map<Integer, Vegetable> vegetableMap;
    public static Map<Integer, DailyIngredient> dailyIngredientMap;

    public static List<Recipe> getListRecipe() {
        return listRecipe;
    }
}
