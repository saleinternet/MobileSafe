package com.csw.ms.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

import java.io.IOException;
import java.io.InputStream;


/**
 * GPS定位服务
 * Created by chensiwen on 15/8/19.
 */
public class GPSService extends Service {

    /**
     * 位置服务
     */
    private LocationManager lm;

    private MyLocatioinListener listener;


    @Override
    public void onCreate() {
        super.onCreate();

        lm = (LocationManager) getSystemService(LOCATION_SERVICE);

        listener = new MyLocatioinListener();
        //注册监听位置服务
        //给位置提供者设置条件
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);//设置为最大精度
        //设置参数细化
        criteria.setAltitudeRequired(false);//不要求海拔信息
        criteria.setBearingRequired(false);//不要求方位信息
        criteria.setCostAllowed(false); //是否允许付费
        criteria.setPowerRequirement(Criteria.POWER_LOW);//对电量要求

        String provider = lm.getBestProvider(criteria, true);
        lm.requestLocationUpdates(provider, 0, 0, listener);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //取消监听服务
        lm.removeUpdates(listener);
        listener = null;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class MyLocatioinListener implements LocationListener {

        /**
         * 当位置发生变化的时候回调
         */
        @Override
        public void onLocationChanged(Location location) {
            String longitude = "j:" + location.getLongitude();
            String altitude = "w:" + location.getAltitude();
            String accuracy = "a:" + location.getAccuracy();

            //发送短信给安全号码

            //把标准的GPS坐标转换为火星坐标
            InputStream is = null;
            try {
                is = getAssets().open("axisoffset.dat");
                ModifyOffset offset = ModifyOffset.getInstance(is);
                PointDouble pd =
                        offset.s2c(
                                new PointDouble(location.getLongitude(), location.getAltitude()));
                //转换后的精度、纬度
                longitude = "j:" + pd.x;
                altitude =  "w:" + pd.y;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (is != null) try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //保存位置
            SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
            Editor editor = sp.edit();
            editor.putString("lastlocation", longitude + altitude + accuracy);
            editor.commit();
        }

        /**
         * 当状态发生变化的时候回调(关闭，打开)
         */
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        /**
         * 当某一个位置提供者可以使用了
         */
        @Override
        public void onProviderEnabled(String provider) {

        }

        /**
         * 当某一个位置提供者不可以使用了
         */
        @Override
        public void onProviderDisabled(String provider) {

        }
    }
}
