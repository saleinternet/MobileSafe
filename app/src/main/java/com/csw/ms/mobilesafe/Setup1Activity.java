package com.csw.ms.mobilesafe;

import android.content.Intent;
import android.os.Bundle;
/**
 * 手机防盗设置向导1
 * @author chensiwen
 * @date 2015/08/15
 */
public class Setup1Activity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
    }

    public void showNext() {
        Intent intent = new Intent(this, Setup2Activity.class);
        startActivity(intent);
        finish();

        //要求在finish方法或者startActivity后面执行
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
    }

    @Override
    public void showPrev() {
    }

}
