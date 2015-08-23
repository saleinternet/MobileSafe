package com.csw.ms.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 手机防盗
 * @author chensiwen
 * @date 2015/08/15
 */
public class LostFindActivity extends Activity {

    private SharedPreferences sp;

    /**
     * 安全号码
     */
    private TextView tv_safenumber;

    /**
     * 开启防盗保护
     */
    private ImageView iv_protecting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //判断是否做过设置向导，如果没有做过，则跳转到设置向导进行设置，否则留在当前页面
        sp = getSharedPreferences("config", MODE_PRIVATE);
        boolean configed = sp.getBoolean("configed", false);
        if (configed) {
            //做过设置 向导，留在当前页面
            setContentView(R.layout.activity_lost_find);

            //设置安全号码
            tv_safenumber = (TextView) findViewById(R.id.tv_safenumber);
            String safenumber = sp.getString("safenumber", "");
            tv_safenumber.setText(safenumber);

            //是否开启防盗保护
            iv_protecting = (ImageView) findViewById(R.id.iv_protecting);
            boolean protecting = sp.getBoolean("protecting", false);
            iv_protecting.setImageResource(protecting?R.drawable.lock:R.drawable.unlock);
        } else {
            //没有做过设置向导，跳转到设置向导
            Intent intent = new Intent(this, Setup1Activity.class);
            startActivity(intent);

            //关闭当前页面
            finish();
        }

    }

    /**
     * 重新进入手机防盗设置向导页面
     */
    public void reEnterSetup(View view) {
        Intent intent = new Intent(this, Setup1Activity.class);
        startActivity(intent);

        //关闭当前页面
        finish();
    }

}
