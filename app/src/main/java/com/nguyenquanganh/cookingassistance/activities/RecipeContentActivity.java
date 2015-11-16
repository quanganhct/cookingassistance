package com.nguyenquanganh.cookingassistance.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Looper;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nguyenquanganh.cookingassistance.R;
import com.nguyenquanganh.cookingassistance.adapters.CookingStepAdapter;
import com.nguyenquanganh.cookingassistance.application.MyApplication;
import com.nguyenquanganh.cookingassistance.database.CookingDatabase;
import com.nguyenquanganh.cookingassistance.entities.CookingStep;
import com.nguyenquanganh.cookingassistance.entities.LoadedDataStorage;
import com.nguyenquanganh.cookingassistance.entities.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeContentActivity extends Activity {
    Recipe currentRecipe;
    CookingDatabase db;
    List<String> ingredientList;
    List<String> preCooking;
    List<CookingStep> myStepList;
    ListView lv1, lv2, lv3;

    int currentSpecialStep = 0;
    List<CookingStep> specialStep = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = MyApplication.getCookingDatabase();

        Intent intent = getIntent();
        currentRecipe = (Recipe) intent.getSerializableExtra("recipe");

        ingredientList = new ArrayList<String>();
        preCooking = new ArrayList<String>();
        myStepList = db.getCookingStep(currentRecipe);

        setContentView(R.layout.activity_recipe_content);

        Log.v("MAIN THREAD ", "" + Looper.myLooper().getThread().getName() + "--" + Looper.getMainLooper().getThread().getName());

        TextView title = (TextView) findViewById(R.id.recipe_name);
        lv1 = (ListView) findViewById(R.id.list_ingredient);

        displayIngredients();

        title.setText(currentRecipe.getName() + "  ( source:" + currentRecipe.getSource() + " )");
        lv1.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ingredientList));

        int parent_width = 0;

        displayPreCookingSteps(parent_width);
        displayCookingSteps(parent_width);

        int pad = (int) getResources().getDimension(R.dimen.padding_value);

        //ListUtils.setDynamicHeight(lv1, parent_width);
        getLayoutWidth(lv1, pad);
        getLayoutWidth(lv2, pad);
        getLayoutWidth(lv3, pad);
    }

    private void displayIngredients(){
        displayMeat();
        displayVegetable();
        displaySeasonings();
        displayDailyIngredient();
    }

    private void displayPreCookingSteps(int width){
        String steps = db.getPreCookingSteps(currentRecipe);
        if (steps != null && !steps.equals("")){
            String[] ss = steps.split("\\|");
            ss[ss.length-1] += "\n";
            TextView tv2 = (TextView) findViewById(R.id.precooking_title);
            tv2.setText(R.string.precooking_title);
            lv2 = (ListView) findViewById(R.id.list_pre_cooking);
            lv2.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ss));
            //ListUtils.setDynamicHeight(lv2, width);
        }
    }

    private void displayCookingSteps(int width){

        if (myStepList != null && !myStepList.isEmpty()){
            for (int i=0; i<myStepList.size(); i++){
                if (myStepList.get(i).getMinute()!= null && myStepList.get(i).getMinute() > 0){
                    if (specialStep == null)
                        specialStep = new ArrayList<CookingStep>();
                    specialStep.add(myStepList.get(i));
                    specialStep.get(0).setEnableTurn(true);
                }
            }

            TextView tv3 = (TextView) findViewById(R.id.startcooking_title);
            tv3.setText(R.string.startcooking_title);
            lv3 = (ListView) findViewById(R.id.list_cooking_step);
            CookingStepAdapter adapter = new CookingStepAdapter(this, myStepList);
            lv3.setAdapter(adapter);
            //ListUtils.setDynamicHeight(lv3, width);
        }
    }

    public void nextStepEnable(){
        specialStep.get(currentSpecialStep).setEnableTurn(false);
        if (currentSpecialStep+1 < specialStep.size()){
            currentSpecialStep++;
            specialStep.get(currentSpecialStep).setEnableTurn(true);
        }
        ((CookingStepAdapter)lv3.getAdapter()).notifyDataSetChanged();
    }

    private void getLayoutWidth(final ListView lv, final int pad){
        //final ArrayList<Integer> width = new ArrayList<Integer>();

        ViewTreeObserver vto = lv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                lv.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                //width.add(layout.getMeasuredWidth());
                int width = lv.getMeasuredWidth();
                ListUtils.setDynamicHeight(lv, width, pad);
            }
        });
    }



    private void displayVegetable(){
        String _veg = currentRecipe.getList_vegetable();
        if (_veg != null && !_veg.equals("")){
            String[] ss = _veg.split(",");
            for (String s : ss){
                if (!s.equals("")){
                    Integer vid = Integer.parseInt(s);
                    ingredientList.add(LoadedDataStorage.vegetableMap.get(vid).getName());
                }
            }
        }
    }

    private void displayMeat(){
        String _meats = currentRecipe.getList_meat();
        if (_meats != null && !_meats.equals("")){
            String[] ss = _meats.split(",");
            for (String s : ss){
                if (!s.equals("")){
                    Integer mid = Integer.parseInt(s);
                    ingredientList.add(LoadedDataStorage.meatMap.get(mid).getName());
                }
            }
        }
    }

    private void displaySeasonings(){
        String _season = currentRecipe.getList_seasoning();
        if (_season != null && !_season.equals("")){
            String text = "Gia vị: ";
            boolean first = true;

            String[] ss = _season.split(",");
            for (String s : ss){
                if (!s.equals("")){
                    if (!first)
                        text += ", ";
                    else
                        first = false;
                    Integer sid = Integer.parseInt(s);
                    text += LoadedDataStorage.seasoningMap.get(sid).getName();
                }
            }
            ingredientList.add(text);
        }
    }

    private void displayDailyIngredient() {
        String _daily = currentRecipe.getList_dailyIngredient();
        if (_daily != null && !_daily.equals("")){
            String text = "";
            String[] ss = _daily.split(",");
            int count = 0;
            boolean headLine = true;

            for (String s : ss){
                if (!s.equals("")){
                    count++;
                    Integer did = Integer.parseInt(s);

                    if(!headLine)
                        text += ", ";
                    else
                        headLine = false;

                    text += LoadedDataStorage.dailyIngredientMap.get(did).getName();
                    if (count == 3){
                        ingredientList.add(text);
                        text = "";
                        count = 0;
                        headLine = true;
                    }
                }
            }
            if (count > 0)
                ingredientList.add(text);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_recipe_content, menu);
        return false;
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

    public static class ListUtils {
        //private static final int UNBOUNDED = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        public static void setDynamicHeight(ListView mListView, int width, int pad) {
            ListAdapter mListAdapter = mListView.getAdapter();
            mListView.getParent();
            if (mListAdapter == null) {
                // when adapter is null
                return;
            }
            int height = 0;


            int desiredWidth = View.MeasureSpec.makeMeasureSpec(width - 2*pad, View.MeasureSpec.EXACTLY);
            for (int i = 0; i < mListAdapter.getCount(); i++) {
                View listItem = mListAdapter.getView(i, null, mListView);

                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                height += listItem.getMeasuredHeight() + 2*pad;
                Log.v("ViewHeight :", mListAdapter.getClass().toString() + " " + listItem.getMeasuredHeight() + "--" + listItem.getMeasuredWidth());
            }
            ViewGroup.LayoutParams params = mListView.getLayoutParams();
            params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
            mListView.setLayoutParams(params);
            mListView.requestLayout();
        }
    }
}
