package com.csw.ms.mobilesafe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.csw.ms.mobilesafe.ui.SettingItemView;

/**
 * 设置中心
 * @author chensiwen
 * @date 2015/08/15
 */
public class SettingActivity extends Activity {

    private static final String TAG = "SettingActivity";
    /**
     * 升级更新
     * */
    private SettingItemView siv_udpate;

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        sp = getSharedPreferences("config", MODE_PRIVATE);
        siv_udpate = (SettingItemView) findViewById(R.id.siv_update);

        //自动升级是否开启
        boolean update = sp.getBoolean("config",false);
        Log.i(TAG, "update : "+update);
        if (update) {
            //自动升级已经开启
            siv_udpate.setChecked(true);
        } else {
            //自动升级已经关闭
            siv_udpate.setChecked(false);
        }


        siv_udpate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sp.edit();
                //判断是否选中
                if (siv_udpate.isChecked()) {
                    //已经打开自动升级
                    siv_udpate.setChecked(false);
                    editor.putBoolean("update", false);
                } else {
                    //没有打开自动升级
                    siv_udpate.setChecked(true);
                    editor.putBoolean("update", true);
                }

                editor.commit();
            }
        });
    }

}
