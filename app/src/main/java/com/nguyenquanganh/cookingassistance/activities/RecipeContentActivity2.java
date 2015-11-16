package com.nguyenquanganh.cookingassistance.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import com.nguyenquanganh.cookingassistance.R;
import com.nguyenquanganh.cookingassistance.adapters.CookingContentAdapter;
import com.nguyenquanganh.cookingassistance.application.MyApplication;
import com.nguyenquanganh.cookingassistance.database.CookingDatabase;
import com.nguyenquanganh.cookingassistance.entities.CookingStep;
import com.nguyenquanganh.cookingassistance.entities.LoadedDataStorage;
import com.nguyenquanganh.cookingassistance.entities.Recipe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by nguyenquanganh on 9/7/15.
 */
public class RecipeContentActivity2 extends Activity{
    Recipe currentRecipe;
    CookingDatabase db;
    boolean wasStoped = false;

    private ProgressDialog dialog = null;
    List<String> ingredientList = null;
    List<String> preCooking = null;
    List<CookingStep> cookingStepList = null;
    List<CookingStep> stepWithTimer = null;
    ListView listContent = null;
    int currentStepEnable = 0;

    AudioManager audioManager;
    MediaPlayer mediaPlayer;

    private void setUpAlarm() {
        try {
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(this, alert);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playSound() {
        try {
            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0){
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mediaPlayer.setLooping(true);
                mediaPlayer.prepare();
                mediaPlayer.start();}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopSound() {
        mediaPlayer.stop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = MyApplication.getCookingDatabase();
        setUpAlarm();


        Intent intent = getIntent();
        currentRecipe = (Recipe) intent.getSerializableExtra("recipe");
        loadIngredient();

        setContentView(R.layout.activity_recipe_content_2);
        listContent = (ListView) findViewById(R.id.list_content);

        if (preCooking == null && cookingStepList == null) {
            dialog = ProgressDialog.show(this, "Please wait", "Loading", true, false);
            new DownloadRecipeContent().execute(currentRecipe);
        }

        listContent.setOnTouchListener(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (wasStoped)
            ((CookingContentAdapter)listContent.getAdapter()).notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        wasStoped = true;
        super.onStop();
    }

    public void enableNextStep(){
        if (stepWithTimer != null && stepWithTimer.size() > 0 && currentStepEnable < stepWithTimer.size()){
            stepWithTimer.get(currentStepEnable).setEnableTurn(false);
            if (currentStepEnable + 1 < stepWithTimer.size()){
                currentStepEnable++;
                stepWithTimer.get(currentStepEnable).setEnableTurn(true);
            }
            ((CookingContentAdapter)listContent.getAdapter()).notifyDataSetChanged();
        }
    }

    private void loadIngredient(){
        if (ingredientList == null)
            ingredientList = new ArrayList<String>();
        loadMainIngredient();
        loadVegetable();
        loadSeasonings();
        loadDailyIngredient();
    }

    private void loadVegetable(){
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

    private void loadMainIngredient(){
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

    private void loadSeasonings(){
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

    private void loadDailyIngredient() {
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

    public class CaptionTitle {
        String content;
        public CaptionTitle (String s){
            content = s;
        }

        public String getContent(){
            return content;
        }
    }


    private class DownloadRecipeContent extends AsyncTask<Recipe, Void, List[]>{

        @Override
        protected List[] doInBackground(Recipe... params) {
            String preCookingSteps = db.getPreCookingSteps(params[0]);
            List<CookingStep> myStepList = db.getCookingStep(params[0]);
            List[] result = new ArrayList[2];
            if (preCookingSteps != null && !preCookingSteps.equals("")){
                String[] splits = preCookingSteps.split("\\|");
                result[0] = new ArrayList(Arrays.asList(splits));
            }
            result[1] = myStepList;
            return result;
        }

        @Override
        protected void onPostExecute(List[] list) {
            preCooking = list[0];
            cookingStepList = list[1];
            List allItems = new ArrayList();

            if (stepWithTimer == null){
                stepWithTimer = new ArrayList<CookingStep>();
                for (int i=0; i<cookingStepList.size(); i++){
                    if (cookingStepList.get(i).getMinute() != null && cookingStepList.get(i).getMinute() > 0){
                        stepWithTimer.add(cookingStepList.get(i));
                    }
                    //Log.v("COOKINGSTEP :", String.valueOf(cookingStepList.get(i).getMinute()));
                }
                if (stepWithTimer != null && stepWithTimer.size() > 0)
                    stepWithTimer.get(0).setEnableTurn(true);
            }

            CaptionTitle c1 = new CaptionTitle(currentRecipe.getName() + "  ( source:" + currentRecipe.getSource() + " )");
            CaptionTitle c2 = new CaptionTitle(getResources().getString(R.string.precooking_title));
            CaptionTitle c3 = new CaptionTitle(getResources().getString(R.string.startcooking_title));

            int ingredientSize = 0;
            if (ingredientList != null && ingredientList.size() > 0) {
                allItems.add(c1);
                allItems.addAll(ingredientList);
                ingredientSize = ingredientList.size();
            }

            int preCookingSize = 0;
            if (preCooking != null && preCooking.size() > 0) {
                allItems.add(c2);
                allItems.addAll(preCooking);
                preCookingSize = preCooking.size();
            }

            allItems.add(c3);
            allItems.addAll(cookingStepList);

            if (RecipeContentActivity2.this.dialog != null)
                RecipeContentActivity2.this.dialog.dismiss();

            CookingContentAdapter adapter = new CookingContentAdapter(RecipeContentActivity2.this,
                    allItems, ingredientSize, preCookingSize);

            if (listContent == null)
                listContent = (ListView) findViewById(R.id.list_content);
            listContent.setAdapter(adapter);

            super.onPostExecute(list);
        }
    }
}
