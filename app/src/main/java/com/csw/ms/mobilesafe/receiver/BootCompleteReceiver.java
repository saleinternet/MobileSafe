package com.csw.ms.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;

/**
 * 广播接收者
 * Created by chensiwen on 15/8/18.
 */
public class BootCompleteReceiver extends BroadcastReceiver {

    private SharedPreferences sp;
    private TelephonyManager tm;

    @Override
    public void onReceive(Context context, Intent intent) {
        sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        //1.读取之前保存的SIM卡信息
        String saveSim = sp.getString("sim", "");

        //2.读取当前的SIM信息
        String sim = tm.getSimSerialNumber();

        //3.进行比较，不一样，发送短信
        if (!sim.equals(saveSim)) {
            //sim卡变更,发送短信提醒
            System.out.println("sim卡已变更");
        }
    }
}
