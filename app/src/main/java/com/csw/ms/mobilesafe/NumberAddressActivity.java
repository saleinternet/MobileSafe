package com.csw.ms.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.csw.ms.mobilesafe.numberaddress.MobileInfoService;
import com.csw.ms.mobilesafe.utils.ConnectionDetector;

import java.io.InputStream;

/**
 * 号码归属地查询
 */
public class NumberAddressActivity extends Activity {

    private static final String TAG = "NumberAddressActivity";

    /**
     * 系统提供的振动服务
     */
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_address);

        //查询号码
        final EditText et_query_number = (EditText) findViewById(R.id.et_query_number);
        Button bt_query = (Button) findViewById(R.id.bt_query);
        //查询结果
        final TextView tv_query_result = (TextView) findViewById(R.id.tv_query_result);

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        bt_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = et_query_number.getText().toString().trim();
                if (TextUtils.isEmpty(number)) {
                    Toast.makeText(NumberAddressActivity.this, "请输入要查询的手机号码", Toast.LENGTH_SHORT).show();

                    //抖动动画
                    Animation shake = AnimationUtils.loadAnimation(NumberAddressActivity.this, R.anim.shake);
                    et_query_number.startAnimation(shake);
                    //当电话号码为空的时候,就振动手机提醒用户
//                    vibrator.vibrate(2000); //振动2s
                    //振动有规律
                    long[] pattern = {200, 200, 300, 300, 1000, 2000};
                    //-1:不重复 0:循环振动
                    vibrator.vibrate(pattern, -1);


                    return;
                }
                ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
                Boolean isInternetPresent = cd.isConnectingToInternet(); // true or false
                if (isInternetPresent) {
                    InputStream inStream =
                            this.getClass().getClassLoader().getResourceAsStream("mobilesoap.xml");
                    try {
                        tv_query_result.setText(MobileInfoService.getMobileAddress(inStream, number));
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                        Toast.makeText(NumberAddressActivity.this, "查询失败", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(NumberAddressActivity.this, "网络未连接,请打开网络连接", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

}
