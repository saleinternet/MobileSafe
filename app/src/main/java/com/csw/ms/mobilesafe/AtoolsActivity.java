package com.csw.ms.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * 高级工具:号码归属地查询
 */
public class AtoolsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atools);
    }

    /**
     * 号码归属地查询点击事件,进入号码归属地查询页面
     */
    public void numberQuery(View view) {
        Intent intent = new Intent(this, NumberAddressActivity.class);
        startActivity(intent);
        finish();
    }

}
