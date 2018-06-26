package com.mirror.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mirror.R;

import java.util.ArrayList;
import java.util.List;

public class TitleAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    List<String> lists = new ArrayList();
    int selection_position;

    public void updateSelectionPoaition(int position) {
        this.selection_position = position;
        notifyDataSetChanged();
    }

    public TitleAdapter(Context paramContext, List<String> paramList) {
        this.context = paramContext;
        this.lists = paramList;
        inflater = LayoutInflater.from(paramContext);
    }

    public int getCount() {
        return lists.size();
    }

    public String getItem(int position) {
        return lists.get(position);
    }

    public long getItemId(int paramInt) {
        return 0;
    }

    public View getView(int position, View converView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (converView == null) {
            holder = new ViewHolder();
            converView = inflater.inflate(R.layout.item_text_desc, null);
            holder.tv_item_video = ((TextView) converView.findViewById(R.id.tv_item_video));
            holder.rela_video_title_bgg = (ImageView) converView.findViewById(R.id.rela_video_title_bgg);
            converView.setTag(holder);
        } else {
            holder = (ViewHolder) converView.getTag();
        }
        String titleDesc = lists.get(position).toString();
        holder.tv_item_video.setText(titleDesc);
        if (selection_position == position) {
            holder.rela_video_title_bgg.setBackgroundResource(R.drawable.grid_chooice);
        } else {
            holder.rela_video_title_bgg.setBackgroundResource(R.drawable.grid_chooice_trans);
        }
        return converView;
    }

    class ViewHolder {
        ImageView rela_video_title_bgg;
        TextView tv_item_video;
    }
}
