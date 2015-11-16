package com.nguyenquanganh.cookingassistance.entities;

import com.nguyenquanganh.cookingassistance.enums.TypeIngredient;

/**
 * Created by nguyenquanganh on 8/29/15.
 */
public class Ingredient {
    public static final String ID = "_id";

    public static final String MAIN_INGREDIENT_TABLE = "main_ingredient";
    public static final String MAIN_INGREDIENT_NAME = "main_ingredient_name";
    public static final String MAIN_INGREDIENT_TYPE = "main_ingredient_type";
    public static final String FROM = "from";

    public static final String RECIPE_TABLE = "recipes";
    public static final String RECIPE_NAME = "recipe_name";
    public static final String RECIPE_SOURCE = "source";
    public static final String RECIPE_IMAGE = "image_uri";

    public static final String SEASONINGS_TABLE = "seasonings";
    public static final String SEASONINGS_NAME = "seasoning_name";

    public static final String VEGETABLE_TABLE = "vegetable";
    public static final String VEGETABLE_NAME = "vegetable_name";

    public static final String ADDITIVE_TABLE = "additive";
    public static final String ADDITIVE_NAME = "additive_name";

    public static final String RECIPE_MAIN_INGREDIENT_TABLE = "recipe_main_ingredient";
    public static final String RECIPE_ID = "recipe_id";
    public static final String LIST_RECIPE_MAIN_INGREDIENT = "list_main_ingredient_id";

    public static final String RECIPE_SEASONING_TABLE = "recipe_seasonings";
    public static final String LIST_RECIPE_SEASONING = "list_seasoning_id";

    public static final String RECIPE_VEGETABLE_TABLE = "recipe_vegetable";
    public static final String LIST_RECIPE_VEGETABLE = "list_vegetable_id";

    public static final String RECIPE_ADDITIVE_TABLE = "recipe_additive";
    public static final String LIST_RECIPE_ADDITIVE_ID = "list_additive_id";

    public static final String PRE_COOKING_TABLE = "pre_cooking";
    public static final String PRE_COOKING_STEPS = "content";

    public static final String COOKING_STEP_TABLE = "cooking_steps";
    public static final String COOKING_STEP_CONTENT = "step_content";
    public static final String COOKING_TIMER = "time";
    public static final String STEP_ORDER = "step_order";

    String ingredient_name;
    TypeIngredient ingredient_type;
}
