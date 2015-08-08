package com.csw.ms.mobilesafe;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

public class SplashActivity extends Activity {

    private TextView tv_splash_version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
        tv_splash_version.setText("版本号：" + getVersionName());
    }

    /**
     * 得到应用程序的版本名称
     * */
    private String getVersionName() {
        //  用来管理手机的apk
        PackageManager pm = getPackageManager();

        try {
            //  得到指定apk的功能清单文件
            PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
            Log.i("MobileSafe", info.packageName+"####"+info.versionName);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

}
