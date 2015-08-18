package com.csw.ms.mobilesafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.csw.ms.mobilesafe.utils.StringUtils;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 启动程序界面
 * @author chensiwen
 * @date 2015/08/15
 */
public class SplashActivity extends Activity {

    private static final String TAG = "SplashActivity";

    /**
     * 消息机制
     * */
    private static final int ENTER_HOME = 0;
    private static final int SHOW_UPDATE_DIALOG = 1;
    private static final int URL_ERROR = 2;
    private static final int NETWORK_ERROR = 3;
    private static final int JSON_ERROR = 4;

    /**
     * app版本信息
     * */
    private TextView tv_splash_version;

    /**
     * 描述信息
     * */
    private String description;

    /**
     * apk下载地址
     * */
    private String apkurl;

    /**
     * 更新进度信息
     * */
    private TextView tv_splash_udpateinfo;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ENTER_HOME:            //进入主页面
                    Log.i(TAG, "进入主页面");
                    enterHome();
                    break;
                case SHOW_UPDATE_DIALOG:    //显示升级对话框
                    Log.i(TAG, "显示升级对话框");
                    showUpdateDialog();
                    break;
                case URL_ERROR:             //URL错误
                    Log.i(TAG, "URL错误");
                    Toast.makeText(SplashActivity.this, "URL错误", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case NETWORK_ERROR:         //网络错误
                    Log.i(TAG, "网络异常");
                    Toast.makeText(SplashActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                case JSON_ERROR:            //JSON解析出错
                    Log.i(TAG, "SON解析出错");
                            Toast.makeText(getApplicationContext(), "SON解析出错", Toast.LENGTH_SHORT).show();
                    enterHome();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        tv_splash_udpateinfo = (TextView) findViewById(R.id.tv_splash_udpateinfo);

        tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
        tv_splash_version.setText("版本号：" + getVersionName());
        tv_splash_udpateinfo.setVisibility(View.INVISIBLE);

        //检查版本升级
        checkUpdate();

        //动画 Splash->Home(透明度动画变化)
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.2f, 1.0f);
        alphaAnimation.setDuration(500);
        findViewById(R.id.rl_root_splash).startAnimation(alphaAnimation);
    }

    /**
     * 升级对话框
     * */
    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
        builder.setTitle("升级提升");
//        builder.setCancelable(false);   //点击返回时,不起作用(强制升级)
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                //进入主页面
                enterHome();
                dialog.dismiss();
            }
        });
        builder.setMessage(description);

        builder.setPositiveButton("立刻升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //下载APK,并且替换安转
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    //显示升级进度信息
                    tv_splash_udpateinfo.setVisibility(View.VISIBLE);

                    //sdcard卡存在
                    //afinal
                    FinalHttp finalHttp = new FinalHttp();
                    finalHttp.download(
                            apkurl,
                            Environment.getExternalStorageDirectory().getAbsolutePath() + "/mobilesafe2.0.apk",
                            new AjaxCallBack<File>() {

                                @Override
                                public void onLoading(long count, long current) {
                                    super.onLoading(count, current);
                                    //当前下载百分比
                                    int progress = (int)(current * 100 / count);

                                    tv_splash_udpateinfo.setText("下载进度："+progress + "%");
                                }

                                //下载成功,安装APK
                                @Override
                                public void onSuccess(File file) {
                                    super.onSuccess(file);
                                    installAPK(file);
                                }

                                //下载出错
                                @Override
                                public void onFailure(Throwable t, int errorNo, String strMsg) {
                                    t.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "下载失败", Toast.LENGTH_SHORT);
                                    super.onFailure(t, errorNo, strMsg);
                                }

                                @Override
                                public boolean isProgress() {
                                    return super.isProgress();
                                }
                            });

                } else {
                    Toast.makeText(getApplication(),
                            "没有sdcard,请安装后再试!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        builder.setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //对话框小说,进入主页面
                dialog.dismiss();
                enterHome();
            }
        });

        builder.show();
    }

    /**
     * 安装下载的APK
     * @param file
     * */
    private void installAPK(File file) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
    }

    /**
     * 检查是否有心版本，如果有就升级
     * */
    private void checkUpdate() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                Message message = Message.obtain();
                long startTime = System.currentTimeMillis();
                HttpURLConnection conn = null;
                try {

                    String address = getString(R.string.serverurl);
                    Log.i(TAG, "address: "+address);
                    URL url = new URL(address);
                    //联网
                    conn = (HttpURLConnection) url.openConnection();
                    //设置请求方式
                    conn.setRequestMethod("GET");
                    //设置连接超时时间
                    conn.setConnectTimeout(8000);
                    //conn.setReadTimeout(8000);
                    conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:19.0) Gecko/20100101 Firefox/19.0");
                    //返回码
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        //联网成功
                        InputStream is = conn.getInputStream();
                        //将输入流转换成字符串
                        String result = StringUtils.readFromStream(is);
                        Log.i(TAG, result);
                        //JSON解析
                        JSONObject obj = new JSONObject(result);
                        //得到服务器的版本信息
                        String version = obj.optString("version");
                        description = obj.optString("description");
                        apkurl = obj.optString("apkurl");

                        //校验是否有新版本
                        if (getVersionName().equals(version)) {
                            //版本一致,没有新版本,进入主页面
                            message.what = ENTER_HOME;
                        } else {
                            //有新版本,弹出一个升级对话框
                            message.what = SHOW_UPDATE_DIALOG;
                        }
                    }
                } catch (MalformedURLException e) {
                    message.what = URL_ERROR;
                    e.printStackTrace();
                } catch (IOException e) {
                    message.what = NETWORK_ERROR;
                    e.printStackTrace();
                } catch (JSONException e) {
                    message.what = JSON_ERROR;
                    e.printStackTrace();
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }

                    long endTime = System.currentTimeMillis();
                    //耗时
                    long wTime = endTime - startTime;
                    if (wTime < 2000) {
                        //休息
                        SystemClock.sleep(2000-wTime);
                    }
                    handler.sendMessage(message);
                }
            }
        }).start();
    }

    /**
     * 进入主页面
     * */
    private void enterHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
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
