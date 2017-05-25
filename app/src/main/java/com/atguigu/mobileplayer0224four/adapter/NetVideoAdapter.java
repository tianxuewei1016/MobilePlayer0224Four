package com.atguigu.mobileplayer0224four.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.atguigu.mobileplayer0224four.R;
import com.atguigu.mobileplayer0224four.bean.MediaItem;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 作者：田学伟 on 2017/5/25 10:52
 * QQ：93226539
 * 作用：网络视频的适配器
 */

public class NetVideoAdapter extends BaseAdapter {

    private final Context mContext;
    private final ArrayList<MediaItem> datas;

    public NetVideoAdapter(Context mContext, ArrayList<MediaItem> mediaItems) {
        this.mContext = mContext;
        this.datas = mediaItems;
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
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
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_net_video, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        MediaItem mediaItem = datas.get(position);
        viewHolder.tvName.setText(mediaItem.getName());//设置名称
        //设置文件大小
        viewHolder.tvSize.setText(mediaItem.getDuration() + "秒");
        viewHolder.tvDuration.setText(mediaItem.getDesc());

        Glide.with(mContext)
                .load(mediaItem.getImageUrl())
                .placeholder(R.drawable.video_default)
                .error(R.drawable.video_default)
                .into(viewHolder.ivIcon);

        return convertView;
    }


    static class ViewHolder {
        @InjectView(R.id.iv_icon)
        ImageView ivIcon;
        @InjectView(R.id.tv_name)
        TextView tvName;
        @InjectView(R.id.tv_duration)
        TextView tvDuration;
        @InjectView(R.id.tv_size)
        TextView tvSize;
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
