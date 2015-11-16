package com.nguyenquanganh.cookingassistance.adapters;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nguyenquanganh.cookingassistance.R;
import com.nguyenquanganh.cookingassistance.entities.Recipe;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by nguyenquanganh on 8/26/15.
 */
public class ListRecipeAdapter extends ArrayAdapter<Recipe> {
    private final Context context;
    List<Recipe> recipeList;

    public ListRecipeAdapter(Context context, List<Recipe> list){
        super(context, R.layout.row_layout, list);
        this.context = context;
        this.recipeList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater myInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = myInflater.inflate(R.layout.row_layout, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        //imageView.getLayoutParams().width = imageView.getLayoutParams().height;

        Recipe item = recipeList.get(position);
        textView.setText(item.getName());
        AssetManager assetManager = context.getAssets();

        InputStream is;
        if (item.getImage_uri() != null && !item.getImage_uri().equals("")) {
            try {
                is = assetManager.open("images/" + item.getImage_uri());
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                imageView.setImageBitmap(bitmap);
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return rowView;
    }
}
