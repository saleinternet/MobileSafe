package com.csw.ms.mobilesafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.csw.ms.mobilesafe.utils.Md5Utils;

/**
 * app主界面
 * @author chensiwen
 * @date 2015/08/15
 */
public class HomeActivity extends Activity {

    protected static final String TAG = "HomeActivity";

    /**
     * 应用图标片列表
     */
    private GridView list_home;

    /**
     * 自定义适配器
     */
    private MyAdapter adapter;

    /**
     * 功能名称
     */
    private static String[] names = {
            "手机防盗", "通讯卫生", "软件管理",
            "进程管理", "流量统计", "手机杀毒",
            "缓存清理", "高级工具", "设置中心"
    };

    /**
     * 功能图片id
     */
    private static int[] ids = {
            R.drawable.safe,R.drawable.callmsgsafe,R.drawable.app,
            R.drawable.taskmanager,R.drawable.netmanager,R.drawable.trojan,
            R.drawable.sysoptimize,R.drawable.atools,R.drawable.settings

    };

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sp = getSharedPreferences("config", MODE_PRIVATE);

        //初始化控件
        list_home = (GridView) findViewById(R.id.list_home);
        adapter = new MyAdapter();
        list_home.setAdapter(adapter);

        list_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: //进入手机防盗页面
                        showLostFindDialog();
                        break;
                    case 8: //进入设置中心
                        Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 手机防盗密码设置框
     */
    private void showLostFindDialog() {
        //判断是否设置过密码
        if (isSetupPasswd()) {
            //已经设置过密码，弹出的是输入对话框
            showEnterDialog();
        } else {
            //没哟设置密码，弹出的是设置对话框
            showSetupPasswdDialog();
        }
    }

    /**设置密码对话框组件*/
    private EditText et_passwd;
    private EditText et_confirm_passwd;
    private Button ok;
    private Button cancel;
    private AlertDialog dialog;

    /**
     * 设置密码对话框
     */
    private void showSetupPasswdDialog() {
        AlertDialog.Builder builder = new Builder(HomeActivity.this);
        //自定义一个布局文件，
        View view = View.inflate(HomeActivity.this, R.layout.dialog_setup_password, null);
        et_passwd = (EditText) view.findViewById(R.id.et_setup_passwd);
        et_confirm_passwd = (EditText) view.findViewById(R.id.et_setup_confirm_passwd);
        ok = (Button) view.findViewById(R.id.bt_setup_ok);
        cancel = (Button) view.findViewById(R.id.bt_setup_cancel);

        //取消
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //把这个对话框取消
                dialog.dismiss();
            }
        });

        //确定
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取出输入密码
                String passwd = et_passwd.getText().toString().trim();
                String passwd_confirm = et_confirm_passwd.getText().toString().trim();
                if (TextUtils.isEmpty(passwd) || TextUtils.isEmpty(passwd_confirm)) {
                    Toast.makeText(HomeActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                //判断两次密码是否一致
                if (!passwd.equals(passwd_confirm)) {
                    Toast.makeText(HomeActivity.this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }

                //保存输入密码,去掉对话框,进入手机防盗页面
                Log.i(TAG, "保存输入密码,去掉对话框,进入手机防盗页面");
                SharedPreferences.Editor editor = sp.edit();
                //保存密码，进行MD5加密
                editor.putString("password", Md5Utils.md5(passwd));
                editor.commit();

                dialog.dismiss();

                //进入手机防盗页面
                enterLostFind();
            }
        });

//        builder.setView(view);
        dialog = builder.create();
        dialog.setView(view, 0, 0, 0, 0);
        dialog.show();
    }

    /**
     * 进入手机防盗页面
     */
    private void enterLostFind() {
        Intent intent = new Intent(HomeActivity.this, LostFindActivity.class);
        startActivity(intent);
    }

    /**
     * 输入密码对话框
     */
    private void showEnterDialog() {
        AlertDialog.Builder builder = new Builder(HomeActivity.this);
        //自定义一个布局文件，
        View view = View.inflate(HomeActivity.this, R.layout.dialog_enter_password, null);
        et_passwd = (EditText) view.findViewById(R.id.et_setup_passwd);
        ok = (Button) view.findViewById(R.id.bt_setup_ok);
        cancel = (Button) view.findViewById(R.id.bt_setup_cancel);

        //取消
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //把这个对话框取消
                dialog.dismiss();
            }
        });

        //确定
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取出输入密码
                String passwd = et_passwd.getText().toString().trim();
                if (TextUtils.isEmpty(passwd)) {
                    Toast.makeText(HomeActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                //输入密码,去掉对话框,进入手机防盗页面
                Log.i(TAG, "输入密码成功,去掉对话框,进入手机防盗页面");

                String savePassword = sp.getString("password", null);
                if (!savePassword.equals(Md5Utils.md5(passwd))) {
                    Toast.makeText(HomeActivity.this, "密码不正常,请重新输入", Toast.LENGTH_SHORT).show();
                    et_passwd.setText("");
                    return;
                }

                dialog.dismiss();
                //进入手机防盗页面
                enterLostFind();
            }
        });

//        builder.setView(view);
        dialog = builder.create();
        dialog.setView(view, 0, 0, 0, 0);
        dialog.show();
    }

    /**
     * 判断是否设置过密码
     */
    private boolean isSetupPasswd() {
        String password = sp.getString("password", null);
        return !TextUtils.isEmpty(password);
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return names.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //自定义view
            View view;
            ViewHolder viewHolder;
            if (convertView == null) {
                view = View.inflate(HomeActivity.this, R.layout.list_item_home, null);
                viewHolder = new ViewHolder();
                viewHolder.iv_item = (ImageView) view.findViewById(R.id.iv_item);
                viewHolder.tv_itme = (TextView) view.findViewById(R.id.tv_item);
                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.iv_item.setImageResource(ids[position]);
            viewHolder.tv_itme.setText(names[position]);
            return view;
        }
    }

    class ViewHolder {
        ImageView iv_item;  //图片
        TextView tv_itme;   //名称
    }

}

