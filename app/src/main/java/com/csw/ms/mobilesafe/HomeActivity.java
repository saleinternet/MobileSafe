package com.csw.ms.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeActivity extends Activity {

    /**
     * 应用图标片列表
     * */
    private GridView list_home;

    /**
     * 自定义适配器
     * */
    private MyAdapter adapter;

    /**
     * 功能名称
     * */
    private static String[] names = {
            "手机防盗", "通讯卫生", "软件管理",
            "进程管理", "流量统计", "手机杀毒",
            "缓存清理", "高级工具", "设置中心"
    };

    /**
     * 功能图片id
     * */
    private static int[] ids = {
            R.drawable.safe,R.drawable.callmsgsafe,R.drawable.app,
            R.drawable.taskmanager,R.drawable.netmanager,R.drawable.trojan,
            R.drawable.sysoptimize,R.drawable.atools,R.drawable.settings

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //初始化控件
        list_home = (GridView) findViewById(R.id.list_home);
        adapter = new MyAdapter();
        list_home.setAdapter(adapter);
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

