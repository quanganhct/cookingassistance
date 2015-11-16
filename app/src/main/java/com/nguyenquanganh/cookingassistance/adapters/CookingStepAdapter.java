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
import com.nguyenquanganh.cookingassistance.activities.RecipeContentActivity;
import com.nguyenquanganh.cookingassistance.entities.CookingStep;

import java.util.List;

/**
 * Created by nguyenquanganh on 9/1/15.
 */
public class CookingStepAdapter extends ArrayAdapter<CookingStep> {
    Context context;
    List<CookingStep> list;

    //CountDownTimer countdown;
    //CookingTimer myTimer;

    public CookingStepAdapter(Context context, List<CookingStep> list){
        super(context, R.layout.cooking_steps_and_timer, list);
        this.context = context;
        this.list = list;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.v("GET VIEW", "" + " ITEM accordingly " + position);

        View rowView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.cooking_steps_and_timer, parent, false);
        }else{
            rowView = convertView;
        }

        final Button button = (Button) rowView.findViewById(R.id.button_timer);
        final TextView timerTextView = (TextView) rowView.findViewById(R.id.cooking_timer);
        final Button skipButton = (Button) rowView.findViewById(R.id.button_skip);
        TextView stepContent = (TextView) rowView.findViewById(R.id.cooking_step_content);

        final CookingStep step = list.get(position);
        String stepOrder = (String) context.getResources().getText(R.string.step_order) + " " + step.getOrder();
        String content = "<b>" + stepOrder + ":</b>" + " " + step.getContent() + "\n";
        stepContent.setText(Html.fromHtml(content));

        if (step.getMinute() == null || step.getMinute() == 0){
            timerTextView.setVisibility(View.GONE);
            button.setVisibility(View.GONE);
            skipButton.setVisibility(View.GONE);
        } else {

            //myTimer = new CookingTimer(step.getMinute());
            timerTextView.setText(step.getMyTimer().toString());
            timerTextView.post(new Runnable() {
                @Override
                public void run() {
                    timerTextView.setText(step.getMyTimer().toString());
                }
            });
//            if (step.getMyTimer().countObservers() == 0){
//                Observer ob = new Observer() {
//                    @Override
//                    public void update(Observable observable, Object data) {
//                        timerTextView.setText(step.getMyTimer().toString());
//
//                        Log.v("THREAD UPDATE OBSERVER ", ""+Looper.myLooper().getThread().getName() + "--" + step.getMyTimer().toString());
//                    }
//                };
//
//                step.getMyTimer().addObserver(ob);
//                Log.v("THREAD ADD OBSERVER :", "" + Looper.myLooper().getThread().getName());
//            }

            button.setText(R.string.button_available);
            skipButton.setText(R.string.skip_button_content);
            button.setEnabled(step.isTurnEnable());
            skipButton.setEnabled(step.isTurnEnable());

            button.requestFocusFromTouch();
            button.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {

                        if (!button.isPressed()) {
                            button.setPressed(true);
                            button.setText(R.string.button_pressed);
                            //step.getMyTimer().start();
                        } else {
                            button.setPressed(false);
                            button.setText(R.string.button_available);
                            //step.getMyTimer().stop();

                        }
                    }
                    return true;
                }
            });

            skipButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        //step.getMyTimer().stop();
                        RecipeContentActivity ca = (RecipeContentActivity) context;
                        ca.nextStepEnable();
                    }

                    return true;
                }
            });
        }
        return rowView;
    }

}
