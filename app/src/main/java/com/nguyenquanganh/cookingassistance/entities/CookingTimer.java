package com.nguyenquanganh.cookingassistance.entities;

import android.os.CountDownTimer;
import android.os.Looper;
import android.util.Log;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by nguyenquanganh on 9/2/15.
 */


public class CookingTimer extends Observable {
    int hour;
    int min;
    int sec;
    boolean end = false;
    boolean ticking = false;
    int second;
    CountDownTimer _timer;

    public CookingTimer (int min){
        int total = min * 60;
        second = min * 60;
        hour = total / 3600;
        this.min = (total - hour*3600) / 60;
        sec = total - hour*3600 - this.min*60;
        _timer = new CountDownTimer(min * 60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tick();
                setChanged();
                notifyObservers();
                clearChanged();
                Log.v("THREAD TIMER : ", "" + Looper.myLooper().getThread().getName());
            }

            @Override
            public void onFinish() {
                end = true;
                setChanged();
                notifyObservers();
                clearChanged();
            }
        };
    }

    public void start(){
        if (!ticking){
            ticking = true;
            _timer.start();
        }
    }

    public void stop(){
        ticking = false;
        _timer.cancel();
    }

    public boolean isEnd(){
        return end;
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
                }else{
                    end = true;
                }
            }
        }

        if (second > 0){
            second--;
        }
        if (second == 180) {
            
        }
    }
}
