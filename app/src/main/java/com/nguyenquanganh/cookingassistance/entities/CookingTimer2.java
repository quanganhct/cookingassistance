package com.nguyenquanganh.cookingassistance.entities;

import android.media.AudioManager;
import android.os.Handler;
import android.util.Log;

import java.util.logging.LogRecord;

/**
 * Created by nguyenquanganh on 9/24/15.
 */
public abstract class CookingTimer2 {
    int hour;
    int min;
    int sec;
    int secondLeft;
    boolean timerEnd;
    boolean isTicking = false;
    Handler myHandler = new Handler();

    Runnable timer = new Runnable() {
        @Override
        public void run() {
            tick();
            upgradeUI();
            myHandler.postDelayed(timer, 1000);
        }
    };

    public abstract void upgradeUI();
    public abstract void notifyAlarm();

    public CookingTimer2 (int min){
        int total = min * 60;
        secondLeft = min * 60;
        hour = total / 3600;
        this.min = (total - hour*3600) / 60;
        sec = total - hour*3600 - this.min*60;
        timerEnd = false;
    }

    public void doStart(){
        if (!isTicking) {
            isTicking = true;
            myHandler.postDelayed(timer, 0);
        }
    }

    public void doStop(){
        if (isTicking) {
            isTicking = false;
            myHandler.removeCallbacks(timer);
        }
    }

    public boolean isTicking(){
        return isTicking;
    }

    public boolean isEnd(){
        return timerEnd;
    }

    public String toString(){
        String result = "" + hour + ":" + min + ":" + sec;

        return result;
    }

    public void tick(){
        if (sec > 0){
            sec -= 1;
        }else{
            if (min > 0){
                min -= 1;
                sec = 59;
            }else{
                if (hour > 0){
                    hour -= 1;
                    min = 59;
                    sec = 59;
                } else {
                    timerEnd = true;
                }
            }
        }

        if (secondLeft > 0){
            secondLeft--;
        }
        if (secondLeft == 180) {
            Log.v("NOTIFY", "TIME UP");
            notifyAlarm();
        }
    }
}
