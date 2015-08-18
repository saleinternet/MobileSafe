package com.csw.ms.mobilesafe;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chensiwen on 15/8/18.
 */
public class SelectContactActivity extends Activity {

    private static final String TAG = "SelectContactActivity";
    /**
     * 联系人显示控件
     */
    private ListView list_select_contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contacts);

        list_select_contact = (ListView) findViewById(R.id.list_select_contact);
        final List<Map<String, String>> data = getContactInfo();
        list_select_contact.setAdapter(
                    new SimpleAdapter(this, data,
                            R.layout.contact_item_view,
                            new String[] {"name","phone"},
                            new int[] {R.id.tv_name, R.id.tv_phone}));
        //点击,选中安全号码
        list_select_contact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String phone = data.get(position).get("phone");
                Log.i(TAG, "选择安全号码:"+phone);
                //以意图的形式跳转到防盗页面
                Intent intent = new Intent();
                intent.putExtra("phone", phone);
                setResult(200, intent);

                finish();
            }
        });
    }

    /**
     * 读取手机联系人
     */
    private List<Map<String, String>> getContactInfo() {

        List<Map<String, String>> list = new ArrayList<Map<String, String>>();

        //得到一个内容解析器
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        int contactIdIndex = 0;
        int nameIndex = 0;
        if (cursor.getCount() > 0) {
            contactIdIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        }

        while (cursor.moveToNext()) {
            Map<String, String> map = new HashMap<String, String>();
            String contactId = cursor.getString(contactIdIndex);
            String name = cursor.getString(nameIndex);
            map.put("name", name);

            /*
             * 查找该联系人的phone信息
             */
            Cursor phones = resolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId,
                    null, null);
            int phoneIndex = 0;
            if(phones.getCount() > 0) {
                phoneIndex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            }
            while(phones.moveToNext()) {
                String phoneNumber = phones.getString(phoneIndex);
                map.put("phone", phoneNumber);
            }

            phones.close();

            list.add(map);
        }

        cursor.close();

        return list;
    }
}
