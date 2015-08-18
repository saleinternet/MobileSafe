package com.csw.ms.mobilesafe;

import android.content.Intent;
import android.os.Bundle;

/**
 * 手机防盗设置向导2
 * @author chensiwen
 * @date 2015/08/15
 */
public class Setup2Activity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
    }

    public void showPrev() {
        Intent intent = new Intent(this, Setup1Activity.class);
        startActivity(intent);
        finish();

        //要求在finish方法或者startActivity后面执行
        overridePendingTransition(R.anim.tran_prev_in, R.anim.tran_prev_out);
    }

    public void showNext() {
        Intent intent = new Intent(this, Setup3Activity.class);
        startActivity(intent);
        finish();

        //要求在finish方法或者startActivity后面执行
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
    }

}
