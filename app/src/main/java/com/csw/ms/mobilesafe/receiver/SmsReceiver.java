package com.csw.ms.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;

import com.csw.ms.mobilesafe.R;
import com.csw.ms.mobilesafe.service.GPSService;

/**
 * 短信指令的广播接收者
 * Created by chensiwen on 15/8/19.
 */
public class SmsReceiver extends BroadcastReceiver {

    private static final String TAG = "SmsReceiver";

    private SharedPreferences sp;

    @Override
    public void onReceive(Context context, Intent intent) {
        sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);

        //接收短信
        Object[] objs = (Object[]) intent.getExtras().get("pdus");
        for (Object obj : objs) {
            //具体的某一条短信
            SmsMessage sms = SmsMessage.createFromPdu((byte[]) obj);
            //发送者
            String sender = sms.getOriginatingAddress();

            //安全号码
            String safenumber = sp.getString("safenumber", "");

            if (sender.contains(safenumber)) {

                //发送者是安全号码
                Log.i(TAG, "发送者是安全号码");

                //短信内容
                String content = sms.getMessageBody();

                //内容判断
                if ("#*location*#".equals(content)) {
                    //手机定位
                    Log.i(TAG, "GPS追踪");

                    //启动GPS服务
                    Intent i = new Intent(context, GPSService.class);
                    context.startService(i);
                    //获取位置信息
                    String location = sp.getString("lastlocation", null);
                    if (TextUtils.isEmpty(location)) {
                        //没有得到位置
                        SmsManager.getDefault().sendTextMessage(sender, null, "正在定位中......", null, null);
                    } else {
                        SmsManager.getDefault().sendTextMessage(sender, null, location, null, null);
                    }

                    abortBroadcast();
                } else if ("#*alaram*#".equals(content)) {
                    //播放报警音乐
                    Log.i(TAG, "播放报警音乐");
                    playMedia(context);
                    abortBroadcast();
                } else if ("#*wipedata*#".equals(content)) {
                    //远程删除数据
                    Log.i(TAG, "远程清除数据");
                    abortBroadcast();
                } else if ("#*lockscreen*#".equals(content)) {
                    //远程锁屏
                    Log.i(TAG, "远程锁屏");
                    abortBroadcast();
                }
            }

        }
    }

    /**
     * 播放报警音乐
     */
    private void playMedia(Context context) {
        MediaPlayer player = MediaPlayer.create(context, R.raw.alarm);
        player.setLooping(false); //是否循环播放:false(不)
        player.setVolume(1.0f, 1.0f); //播放声音设置
        player.start();
    }
}
