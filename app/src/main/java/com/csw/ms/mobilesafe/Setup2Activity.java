package com.csw.ms.mobilesafe;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.csw.ms.mobilesafe.ui.SettingItemView;

/**
 * 手机防盗设置向导2
 * @author chensiwen
 * @date 2015/08/15
 */
public class Setup2Activity extends BaseActivity {

    /**
     * 绑定SIM卡
     */
    private SettingItemView siv_setup2_sim;

    /**
     * 读取手机的SIM的信息
     */
    private TelephonyManager tm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);

        siv_setup2_sim = (SettingItemView) findViewById(R.id.siv_setup2_sim);
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        //判断是否选中sim卡信息
        String sim = sp.getString("sim", null);
        if (TextUtils.isEmpty(sim)) {
            //没有绑定sim
            siv_setup2_sim.setChecked(false);
        } else {
            siv_setup2_sim.setChecked(true);
        }
        siv_setup2_sim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editor editor = sp.edit();

                if (siv_setup2_sim.isChecked()) {
                    siv_setup2_sim.setChecked(false);
                    editor.putString("sim", "");
                } else {
                    siv_setup2_sim.setChecked(true);
                    //保存SIM卡的序列号
                    String sim = tm.getSimSerialNumber();
                    editor.putString("sim", sim);

                }
                editor.commit();

            }
        });

    }

    public void showPrev() {
        Intent intent = new Intent(this, Setup1Activity.class);
        startActivity(intent);
        finish();

        //要求在finish方法或者startActivity后面执行
        overridePendingTransition(R.anim.tran_prev_in, R.anim.tran_prev_out);
    }

    public void showNext() {
        //取出是否绑定SIM卡
        String sim = sp.getString("sim", null);
        if (TextUtils.isEmpty(sim)) {
            //没有绑定SIM
            Toast.makeText(this, "SIM卡没有绑定", Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent(this, Setup3Activity.class);
        startActivity(intent);
        finish();

        //要求在finish方法或者startActivity后面执行
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
    }

}
