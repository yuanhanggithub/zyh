package com.mirror.fragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mirror.R;
import com.mirror.entity.InnerAppEntity;

import java.util.List;

public class VideoFunAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    List<InnerAppEntity> lists;
    int currentChooicePosition;

    public void updateCurrentPosition(int currentChooicePosition) {
        this.currentChooicePosition = currentChooicePosition;
        notifyDataSetChanged();
    }

    public void setListCurrent(List<InnerAppEntity> list_show) {
        this.lists = list_show;
        notifyDataSetChanged();
    }

    public VideoFunAdapter(Context paramContext, List<InnerAppEntity> lists) {
        this.context = paramContext;
        this.lists = lists;
        this.inflater = LayoutInflater.from(paramContext);
    }

    public void setList(List<InnerAppEntity> paramList) {
        this.lists = paramList;
    }

    public int getCount() {
        return this.lists.size();
    }

    public Object getItem(int paramInt) {
        return this.lists.get(paramInt);
    }

    public long getItemId(int paramInt) {
        return 0L;
    }

    public View getView(int position, View converView, ViewGroup group) {
        ViewHolder holder = null;
        if (converView == null) {
            holder = new ViewHolder();
            converView = inflater.inflate(R.layout.item_video_fun, null);
            holder.lin_video_item_layout = ((LinearLayout) converView.findViewById(R.id.lin_video_item_layout));
            holder.tv_videofun_appname = ((TextView) converView.findViewById(R.id.tv_videofun_appname));
            holder.ib_videofun_icon = ((ImageView) converView.findViewById(R.id.ib_videofun_icon));
            converView.setTag(holder);
        } else {
            holder = (ViewHolder) converView.getTag();
        }
        InnerAppEntity entity = lists.get(position);
        int imageUrl = entity.getImgeUrl();
        final String liveName = entity.getLiveName();
        holder.ib_videofun_icon.setImageResource(imageUrl);
        holder.tv_videofun_appname.setText(liveName);
        if (currentChooicePosition == position) {
            holder.lin_video_item_layout.setBackgroundResource(R.drawable.rect_circle_app);
        } else {
            holder.lin_video_item_layout.setBackgroundResource(R.drawable.rect_circle_trans);
        }
        return converView;
    }


    public class ViewHolder {
        ImageView ib_videofun_icon;
        LinearLayout lin_video_item_layout;
        TextView tv_videofun_appname;
    }
}
