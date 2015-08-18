package com.csw.ms.mobilesafe;

import android.content.Intent;
import android.os.Bundle;

/**
 * 手机防盗设置向导3
 * @author chensiwen
 * @date 2015/08/15
 */
public class Setup3Activity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
    }

    public void showPrev() {
        Intent intent = new Intent(this, Setup2Activity.class);
        startActivity(intent);
        finish();

        //要求在finish方法或者startActivity后面执行
        overridePendingTransition(R.anim.tran_prev_in, R.anim.tran_prev_out);
    }

    public void showNext() {
        Intent intent = new Intent(this, Setup4Activity.class);
        startActivity(intent);
        finish();

        //要求在finish方法或者startActivity后面执行
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
    }

}
