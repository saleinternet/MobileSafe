package com.csw.ms.mobilesafe.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络连接检查：网络是否连接
 * Created by chensiwen on 15/8/24.
 */
public class ConnectionDetector {

    private Context _context;

    public ConnectionDetector(Context context) {
        this._context = context;
    }

    /**
     * 检查网络是否连接
     */
    public boolean isConnectingToInternet() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager)_context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) return true;
                }
            }
        }

        return false;
    }

}
