package cn.edu.gdmec.android.mobileguard.m2theftguard.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.edu.gdmec.android.mobileguard.R;
import cn.edu.gdmec.android.mobileguard.m2theftguard.entity.ContactInfo;

/**
 * Created by Administrator on 2017/10/21.
 */

public class ContactAdapter extends BaseAdapter {
    private List<ContactInfo> contactInfos;
    static int count = 1;
    private Context context;
//    private TextView mNameTV;
//    private TextView mPhoneTV;
    public ContactAdapter(List<ContactInfo> contactInfos,Context context){
        super();
        this.contactInfos = contactInfos;
        this.context = context;

    }
    @Override
    public int getCount() {
        Log.d("Tag", "getItem: ----------------系统调用getitem 集合长度为："+contactInfos.size());
        return contactInfos.size();
    }

    @Override
    public Object getItem(int position) {

        return contactInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Log.d("Tag", "getView:  -----------------系统调用 "+(count++)+"次  然后集合长度为"+contactInfos.size());
        ViewHolder holder = null;
        if (view == null){
            view = View.inflate(context, R.layout.item_list_contact_select,null);
            holder = new ViewHolder();
            holder.mNameTV = (TextView) view.findViewById(R.id.tv_name1);
            holder.mPhoneTV = (TextView) view.findViewById(R.id.tv_phone);
//            mNameTV = (TextView) view.findViewById(R.id.tv_name1);
//            mPhoneTV = (TextView) view.findViewById(R.id.tv_phone);
            view.setTag(holder);
        }else{
            holder= (ViewHolder) view.getTag();

        }
        Log.d("Tag",  holder.mNameTV == null?"true":"false");
        Log.d("Tag",  holder.mPhoneTV == null?"true":"false");
//        mNameTV.setText(contactInfos.get(position).name);
//        mPhoneTV.setText(contactInfos.get(position).phone);
        holder.mNameTV.setText(contactInfos.get(position).name);
        holder.mPhoneTV.setText(contactInfos.get(position).phone);
        Log.d("Tag", "getView:  holder.mNameTV.setText(contactInfos.get(position).name); end ");
        return view;
    }
    static class ViewHolder {
        TextView mNameTV;
        TextView mPhoneTV;
    }
}
