package cn.edu.gdmec.android.mobileguard.m2theftguard.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.edu.gdmec.android.mobileguard.m2theftguard.entity.ContactInfo;

/**
 * Created by Administrator on 2017/10/21.
 */

public class ContactInfoParser {
    public static List<ContactInfo> getSystemContact(Context context){
        //获取内容解析者
        ContentResolver resolver = context.getContentResolver();
        //查询raw_contacts表 把联系人的id取出来
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri datauri = Uri.parse("content://com.android.contacts/data");
        List<ContactInfo> infos = new ArrayList<ContactInfo>();
        Cursor cursor = resolver.query(uri,new String[]{"contact_id"},null,null,null);
        while (cursor.moveToNext()){
            String id = cursor.getString(0);
            if (id!=null){
                Log.d("", "getSystemContact: 联系人id："+id);
                ContactInfo info = new ContactInfo();
                info.id=id;
                //根据联系人的id 查询data表 把id 的数据取出来
                //系统api 查询data表的时候，不是真正的查询data表，而是查询他的data表的视图
                Cursor dataCursor = resolver.query(datauri,new String[]{"data1","mimetype"},"raw_contact_id=?",new String[]{id},null);
                while(dataCursor.moveToNext()){
                    String data1 = dataCursor.getString(0);
                    String mimetype = dataCursor.getString(1);
                    if ("vnd.android.cursor.item/name".equals(mimetype)){
                        Log.i("", "getSystemContact: 姓名="+data1);
                        info.name=data1;
                    }else if ("vnd.android.cursor.item/phone_v2".equals(mimetype)){
                        Log.i("", "getSystemContact: 电话="+data1);
                        info.phone = data1;
                    }

                }
                //如果姓名和手机都为空 则跳过该条数据
                if(TextUtils.isEmpty(info.name) && TextUtils.isEmpty(info.phone))
                    continue;
                infos.add(info);
                dataCursor.close();
            }

        }
        cursor.close();
        return infos;
    }
    public static List<ContactInfo> getSimContacts(Context context){
        Uri uri = Uri.parse("content://icc/adn");
        List<ContactInfo> infos = new ArrayList<ContactInfo>();
        Cursor mCursor = context.getContentResolver().query(uri,null,null,null,null);
        if (mCursor != null){
            while (mCursor.moveToNext()){
                ContactInfo info = new ContactInfo();
                //得到联系人名字
                int nameFieldColumnIndex = mCursor.getColumnIndex("name");
                info.name = mCursor.getString(nameFieldColumnIndex);
                //取得电话号码
                int numberFieldColumnIndex = mCursor.getColumnIndex("number");
                info.phone = mCursor.getString(numberFieldColumnIndex);
                infos.add(info);
            }
        }
        mCursor.close();
        return infos;
    }
}
