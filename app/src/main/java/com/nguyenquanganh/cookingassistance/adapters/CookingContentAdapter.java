package com.nguyenquanganh.cookingassistance.adapters;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.nguyenquanganh.cookingassistance.R;
import com.nguyenquanganh.cookingassistance.activities.RecipeContentActivity2;
import com.nguyenquanganh.cookingassistance.entities.CookingStep;
import com.nguyenquanganh.cookingassistance.entities.CookingTimer2;
import com.nguyenquanganh.cookingassistance.uiassistance.TimerButtonControl;

import java.util.List;

/**
 * Created by nguyenquanganh on 9/23/15.
 */
public class CookingContentAdapter extends ArrayAdapter<Object> {
    int CAPTION_TITLE = 0;
    int INGREDIENTS = 1;
    int COOKING = 3;


    Context context;
    List allItems = null;

    public CookingContentAdapter(Context context, List list, int ingredients, int precooking) {
        super(context, R.layout.activity_recipe_content_2, list);
        this.context = context;
        this.allItems = list;
    }

    @Override
    public int getItemViewType(int position) {
        int type = 0;
        if (allItems != null){
            if (allItems.get(position) instanceof RecipeContentActivity2.CaptionTitle)
                type = CAPTION_TITLE;
            else if (allItems.get(position) instanceof CookingStep)
                type = COOKING;
            else {
                type = INGREDIENTS;
            }
        }

        return type;
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ContentViewHolder.CaptionTitleHolder captionHolder = null;
        ContentViewHolder.PreCookingViewHolder preCookingHolder = null;
        ContentViewHolder.CookingViewHolder cookingHolder = null;
        int type = getItemViewType(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (type == CAPTION_TITLE){
            if (convertView == null){
                convertView = inflater.inflate(R.layout.caption_title_view, parent, false);
                captionHolder = new ContentViewHolder.CaptionTitleHolder();
                captionHolder.text = (TextView) convertView.findViewById(R.id.caption_title_textview);
                convertView.setTag(captionHolder);
            } else {
                captionHolder = (ContentViewHolder.CaptionTitleHolder) convertView.getTag();
            }
            captionHolder.text.setText(((RecipeContentActivity2.CaptionTitle) allItems.get(position)).getContent());

        } else if (type == COOKING){
            if (convertView == null){
                convertView = inflater.inflate(R.layout.cooking_steps_and_timer, parent, false);
                cookingHolder = new ContentViewHolder.CookingViewHolder();
                cookingHolder.text = (TextView) convertView.findViewById(R.id.cooking_step_content);
                cookingHolder.time = (TextView) convertView.findViewById(R.id.cooking_timer);
                cookingHolder.skipButton = (Button) convertView.findViewById(R.id.button_skip);
                cookingHolder.startButton = (TimerButtonControl) convertView.findViewById(R.id.button_timer);

                convertView.setTag(cookingHolder);
            } else {
                cookingHolder = (ContentViewHolder.CookingViewHolder) convertView.getTag();
            }
            CookingStep myStep = (CookingStep) allItems.get(position);
            setUpCookingView(convertView, cookingHolder, myStep);
        } else {
            if (convertView == null){
                convertView = inflater.inflate(R.layout.pre_cooking_view, parent, false);
                preCookingHolder = new ContentViewHolder.PreCookingViewHolder();
                preCookingHolder.text = (TextView) convertView.findViewById(R.id.pre_cooking_textview);
                convertView.setTag(preCookingHolder);
            } else {
                preCookingHolder = (ContentViewHolder.PreCookingViewHolder) convertView.getTag();
            }
            preCookingHolder.text.setText((String) allItems.get(position));
        }

        return convertView;
    }

    private void setUpCookingView(final View c, final ContentViewHolder.CookingViewHolder view, final CookingStep step){
        String stepOrder = context.getResources().getString(R.string.step_order) + " " + step.getOrder();
        String content = "<b>" + stepOrder + ":</b> " + step.getContent() + "\n";
        view.text.setText(Html.fromHtml(content));

        if (step.getMinute() != null && step.getMinute() > 0){
            if (step.getMyTimer() == null) {
                CookingTimer2 timer = new CookingTimer2(step.getMinute()) {

                    @Override
                    public void upgradeUI() {
                        view.time.setText(toString());
                    }

                    @Override
                    public void notifyAlarm() {
                        ((RecipeContentActivity2)context).playSound();
                    }
                };
                step.setTimer(timer);
            }
            view.time.setVisibility(View.VISIBLE);
            view.startButton.setVisibility(View.VISIBLE);
            view.skipButton.setVisibility(View.VISIBLE);



            view.startButton.setText(R.string.button_available);
            view.skipButton.setText(R.string.skip_button_content);
            view.startButton.setEnabled(step.isTurnEnable());
            view.skipButton.setEnabled(step.isTurnEnable());
            view.time.setText(step.getMyTimer().toString());

            if (step.getMyTimer().isTicking()){
                view.startButton.setRun(true);
                view.startButton.setText(R.string.button_pressed);
            }

            view.startButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (!view.startButton.isRunning()) {
                            view.startButton.setRun(true);
                            view.startButton.setText(R.string.button_pressed);
                            step.getMyTimer().doStart();
                        } else {
                            view.startButton.setRun(false);
                            view.startButton.setText(R.string.button_available);
                            step.getMyTimer().doStop();
                            ((RecipeContentActivity2)context).stopSound();
                        }
                    }
                    return true;
                }
            });

            view.skipButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        step.getMyTimer().doStop();
                        ((RecipeContentActivity2)context).stopSound();
                        ((RecipeContentActivity2) context).enableNextStep();
                    }
                    return true;
                }
            });
        } else {
            view.time.setVisibility(View.GONE);
            view.startButton.setVisibility(View.GONE);
            view.skipButton.setVisibility(View.GONE);
        }

    }
    //TODO : Using AsyncTask to do timer's work here

    private static class ContentViewHolder {
        private static class CaptionTitleHolder {
            TextView text;
        }

        private static class PreCookingViewHolder {
            TextView text;
        }

        private static class CookingViewHolder {
            TextView text;
            TextView time;
            Button skipButton;
            TimerButtonControl startButton;
        }
    }
}
