package com.nguyenquanganh.cookingassistance.application;

import android.app.Application;

import com.nguyenquanganh.cookingassistance.database.CookingDatabase;
import com.nguyenquanganh.cookingassistance.fonts.FontManager;

/**
 * Created by nguyenquanganh on 8/27/15.
 */
public class MyApplication extends Application{
    public static CookingDatabase db;

    @Override
    public void onCreate() {
        super.onCreate();
        FontManager.setDefaultFont(this, "MONOSPACE", "fonts/VNF-Comic Sans.ttf");
        db = new CookingDatabase(this);
    }

    public static CookingDatabase getCookingDatabase(){
        return db;
    }
}
