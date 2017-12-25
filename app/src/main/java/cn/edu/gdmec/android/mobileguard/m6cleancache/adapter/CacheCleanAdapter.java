package cn.edu.gdmec.android.mobileguard.m6cleancache.adapter;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.edu.gdmec.android.mobileguard.R;
import cn.edu.gdmec.android.mobileguard.m6cleancache.entity.CacheInfo;

/**
 * Created by Administrator on 2017/11/24.
 */

public class CacheCleanAdapter extends BaseAdapter {
    private Context context;
    private List<CacheInfo> cacheInfos;

    public CacheCleanAdapter(Context context, List<CacheInfo> cacheInfos) {
        super();
        this.context = context;
        this.cacheInfos = cacheInfos;
    }

    @Override
    public int getCount() {
        return cacheInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return cacheInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (view == null){
            viewHolder = new ViewHolder();
            view = View.inflate(context, R.layout.item_cacheclean_list,null);
            viewHolder.mAppIconImgv = (ImageView) view.findViewById(R.id.imgv_appicon_cacheclean);
            viewHolder.mAppNameTV = (TextView) view.findViewById(R.id.tv_appname_cacheclean);
            viewHolder.mCacheSizeTV = (TextView) view.findViewById(R.id.tv_appsize_cacheclean);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        CacheInfo cacheInfo = cacheInfos.get(position);
        viewHolder.mAppIconImgv.setImageDrawable(cacheInfo.appIcon);
        viewHolder.mAppNameTV.setText(cacheInfo.appName);
        viewHolder.mCacheSizeTV.setText(Formatter.formatFileSize(context,cacheInfo.cacheSize));

        return view;
    }
    static class ViewHolder{
        ImageView mAppIconImgv;
        TextView mAppNameTV;
        TextView mCacheSizeTV;
    }
}
