package com.csw.ms.mobilesafe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by chensiwen on 15/8/18.
 */
public abstract class BaseActivity extends Activity {

    //1.定义手势识别器
    private GestureDetector gestureDetector;

    public SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sp = getSharedPreferences("config", MODE_PRIVATE);

        //2.实例化手势识别器
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {

            /**
             * 当手指在上面滑动的时候回调
             */
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

                //屏蔽在x轴很慢得情形
                if (Math.abs(velocityX) < 200) {
                    Toast.makeText(getApplicationContext(), "滑动太慢", Toast.LENGTH_SHORT).show();
                    return false;
                }

                //屏蔽斜滑这种情况
                if (Math.abs(e2.getRawY() - e1.getRawY()) > 200) {
                    Toast.makeText(getApplicationContext(), "不能这样滑动页面", Toast.LENGTH_SHORT).show();
                    return false;
                }

                if ((e2.getRawX() - e1.getRawX()) > 200) {
                    //显示上一个页面,从左往右滑动
                    System.out.println("显示上一个页面,从左往右滑动");
                    showPrev();
                    return true;
                }

                if ((e1.getRawX() - e2.getRawX()) > 200) {
                    //显示下一个页面,从右往左滑动
                    System.out.println("显示下一个页面,从右往左滑动");
                    showNext();
                    return true;
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    //3.使用手势识别器
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public abstract void showNext();

    public abstract void showPrev();

    /**
     * 上一步的点击事件
     */
    public void prev(View view) {
        showPrev();
    }

    /**
     * 下一步的点击事件
     */
    public void next(View view) {
        showNext();
    }
}
