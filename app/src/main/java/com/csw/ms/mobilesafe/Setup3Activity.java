package com.csw.ms.mobilesafe;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 手机防盗设置向导3
 * @author chensiwen
 * @date 2015/08/15
 */
public class Setup3Activity extends BaseActivity {

    /**
     * 安全号码
     */
    private EditText et_setup3_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);

        et_setup3_phone = (EditText) findViewById(R.id.et_setup3_phone);

        String safenumber = sp.getString("safenumber", "");
        et_setup3_phone.setText(safenumber);
    }

    public void showPrev() {
        Intent intent = new Intent(this, Setup2Activity.class);
        startActivity(intent);
        finish();

        //要求在finish方法或者startActivity后面执行
        overridePendingTransition(R.anim.tran_prev_in, R.anim.tran_prev_out);
    }

    public void showNext() {
        String phone = et_setup3_phone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "请设置安全号码", Toast.LENGTH_SHORT).show();
            return;
        }

        //保存安全号码
        Editor editor = sp.edit();
        editor.putString("safenumber", phone);
        editor.commit();

        Intent intent = new Intent(this, Setup4Activity.class);
        startActivity(intent);
        finish();

        //要求在finish方法或者startActivity后面执行
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
    }

    /**
     * 选择联系人的点击事件
     * */
    public void selectContact(View view) {
        Intent intent = new Intent(this, SelectContactActivity.class);
        startActivityForResult(intent, 200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) return;

        if (resultCode == 200) {
            String phone = data.getStringExtra("phone").replace("-", "");
            et_setup3_phone.setText(phone);
        }
    }
}
