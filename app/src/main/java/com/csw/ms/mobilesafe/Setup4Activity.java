package com.csw.ms.mobilesafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

/**
 * 手机防盗设置向导4
 * @author chensiwen
 * @date 2015/08/15
 */
public class Setup4Activity extends BaseActivity {

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
        sp = getSharedPreferences("config", MODE_PRIVATE);
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
