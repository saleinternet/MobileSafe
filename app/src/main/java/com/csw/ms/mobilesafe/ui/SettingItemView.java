package com.csw.ms.mobilesafe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.csw.ms.mobilesafe.R;

/**
 * 自定义的组合控件,里面有两个TextView,一个CheckBox和View
 * Created by chensiwen on 15/8/10.
 */
public class SettingItemView extends RelativeLayout {

    /**
     * 标题
     */
    private TextView tv_title;

    /**
     * 描述信息
     */
    private TextView tv_desc;

    /**
     * 状态
     */
    private CheckBox cb_status;

    private String desc_on;
    private String desc_off;

    public SettingItemView(Context context) {
        super(context);
        initView(context);
    }

    /**
     * 布局文件使用的时候调用
     * */
    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);

        String title =
                attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "name");
        desc_on =
                attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "desc_on");
        desc_off =
                attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "desc_off");

        setTitle(title);
        setDesc(desc_off);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    /**
     * 初始化布局文件
     * @param context
     * */
    private void initView(Context context) {
        //把一个布局文件-->View 并且加载在SettingItemView中
        View.inflate(context, R.layout.setting_item_view, this);

        tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_desc = (TextView) this.findViewById(R.id.tv_desc);
        cb_status = (CheckBox) this.findViewById(R.id.cb_status);
    }

    /**
     * 校验组合控件是否选中
     * */
    public boolean isChecked() {
        return cb_status.isChecked();
    }

    /**
     * 设置组合控件状态
     * */
    public void setChecked(boolean checked) {
        if (checked) {
            setDesc(desc_on);
        } else {
            setDesc(desc_off);
        }
        cb_status.setChecked(checked);
    }

    /**
     * 设置标题信息
     * */
    public void setTitle(String title) {
        tv_title.setText(title);
    }

    /**
     * 设置描述信息
     * */
    public void setDesc(String desc) {
        tv_desc.setText(desc);
    }

}
