package com.nguyenquanganh.cookingassistance.uiassistance;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import com.nguyenquanganh.cookingassistance.R;

/**
 * Created by nguyenquanganh on 9/25/15.
 */
public class TimerButtonControl extends Button{
    private static final int[] STATE_RUN = {R.attr.timer_run};

    private boolean running = false;

    public TimerButtonControl(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setRun(boolean r){
        running = r;
        refreshDrawableState();
    }
    public boolean isRunning() {return running; }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState =  super.onCreateDrawableState(extraSpace + 1);
        if (running) {
            mergeDrawableStates(drawableState, STATE_RUN);
        }
        return drawableState;
    }
}
