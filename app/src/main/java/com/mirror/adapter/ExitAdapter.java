package com.mirror.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mirror.R;
import com.mirror.entity.ExitEntity;

import java.util.List;

public class ExitAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    List<ExitEntity> lists;

    public ExitAdapter(Context paramContext, List<ExitEntity> paramList) {
        this.context = paramContext;
        this.lists = paramList;
        this.inflater = LayoutInflater.from(paramContext);
    }

    public void setList(List<ExitEntity> paramList) {
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

    public View getView(int paramInt, View converView, ViewGroup group) {
        ViewHolder holder = null;
        if (converView == null) {
            holder = new ViewHolder();
            converView = inflater.inflate(R.layout.item_exit, null);
            holder.iv_launcher_item = (ImageView) converView.findViewById(R.id.iv_launcher_item);
            holder.rela_item_bgg = (LinearLayout) converView.findViewById(R.id.rela_item_bgg);
            holder.tv_exit_desc = (TextView) converView.findViewById(R.id.tv_exit_desc);
            converView.setTag(holder);
        } else {
            holder = (ViewHolder) converView.getTag();
        }
        ExitEntity entity = lists.get(paramInt);
        holder.tv_exit_desc.setText(entity.getDesc());
        holder.iv_launcher_item.setBackgroundResource(entity.getImageId());
        return converView;
    }


    public class ViewHolder {
        ImageView iv_launcher_item;
        LinearLayout rela_item_bgg;
        TextView tv_exit_desc;
    }
}
