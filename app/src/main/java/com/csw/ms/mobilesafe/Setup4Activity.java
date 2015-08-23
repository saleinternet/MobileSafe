package com.csw.ms.mobilesafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

/**
 * 手机防盗设置向导4
 * @author chensiwen
 * @date 2015/08/15
 */
public class Setup4Activity extends BaseActivity {

    private SharedPreferences sp;

    private CheckBox cb_protecting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        cb_protecting = (CheckBox) findViewById(R.id.cb_protecting);

        boolean protecting = sp.getBoolean("protecting", false);
        if (protecting) {
            //手机防盗已经开启
            cb_protecting.setChecked(true);
            cb_protecting.setText("手机防盗已经开启");
        } else {
            //手机防盗没有开启
            cb_protecting.setChecked(false);
            cb_protecting.setText("手机防盗没有开启");
        }

        cb_protecting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //判断是否勾选
                if (isChecked) {
                    cb_protecting.setText("手机防盗已经开启");
                } else {
                    cb_protecting.setText("手机防盗没有开启");
                }

                //保存选中的状态
                Editor editor = sp.edit();
                editor.putBoolean("protecting", isChecked);
                editor.commit();
            }
        });
    }

    public void showPrev() {
        Intent intent = new Intent(this, Setup3Activity.class);
        startActivity(intent);
        finish();

        //要求在finish方法或者startActivity后面执行
        overridePendingTransition(R.anim.tran_prev_in, R.anim.tran_prev_out);
    }

    public void showNext() {
        //进入手机防盗页面
        Editor editor = sp.edit();
        editor.putBoolean("configed", true);
        editor.commit();

        Intent intent = new Intent(this, LostFindActivity.class);
        startActivity(intent);
        finish();

        //要求在finish方法或者startActivity后面执行
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
    }

}
