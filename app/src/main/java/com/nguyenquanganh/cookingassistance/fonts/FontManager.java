package com.nguyenquanganh.cookingassistance.fonts;

import android.content.Context;
import android.graphics.Typeface;

import java.lang.reflect.Field;

/**
 * Created by nguyenquanganh on 8/27/15.
 */
public final class FontManager {
    public static void setDefaultFont(Context context, String oldFont, String newFont){
        final Typeface tf = Typeface.createFromAsset(context.getAssets(), newFont);
        replaceFont(oldFont, tf);
    }

    protected static void replaceFont(String oldFont, Typeface newTF){
        try {
            final Field staticField = Typeface.class.getDeclaredField(oldFont);
            staticField.setAccessible(true);
            staticField.set(null, newTF);
        } catch (NoSuchFieldException e){
            e.printStackTrace();
        } catch (IllegalAccessException e){
            e.printStackTrace();
        }
    }
}
