package com.nguyenquanganh.cookingassistance.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.nguyenquanganh.cookingassistance.R;
import com.nguyenquanganh.cookingassistance.adapters.ListRecipeAdapter;
import com.nguyenquanganh.cookingassistance.application.MyApplication;
import com.nguyenquanganh.cookingassistance.database.CookingDatabase;
import com.nguyenquanganh.cookingassistance.entities.LoadedDataStorage;
import com.nguyenquanganh.cookingassistance.entities.Recipe;

public class MainActivity extends ListActivity {
    CookingDatabase db;

    private void loadDatabase(){
        db.loadSeasonings();
        db.loadRecipes();
        db.loadMainIngredient();
        db.loadVegetable();
        db.loadDailyIngredient();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = MyApplication.getCookingDatabase();

        loadDatabase();

        //Cursor c = db.getCursorSeasonings();
        //RecipeCursorAdapter adapter = new RecipeCursorAdapter(this, c, 0);

        ArrayAdapter<Recipe> adapter = new ListRecipeAdapter(this, LoadedDataStorage.getListRecipe());
        setListAdapter(adapter);

        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        //String item = (String) getListAdapter().getItem(position);
        Recipe item = (Recipe) getListAdapter().getItem(position);
        Intent intent = new Intent(this, RecipeContentActivity2.class);
        intent.putExtra("recipe", item);

        this.startActivity(intent);
    }

    /******  Menu on Action Bar  ******/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    /****** End Menu on Action Bar ******/
}
