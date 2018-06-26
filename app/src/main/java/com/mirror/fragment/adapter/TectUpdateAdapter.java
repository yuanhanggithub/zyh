package com.mirror.fragment.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mirror.R;
import com.mirror.entity.TectUpdateEntity;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class TectUpdateAdapter extends BaseAdapter {
    Context context;
    int currentFoufus = 0;
    LayoutInflater inflater;
    List<TectUpdateEntity.TectDetailEntity> lists;


    public void setCurrentFoufus(int paramInt) {
        this.currentFoufus = paramInt;
        notifyDataSetChanged();
    }

    public TectUpdateAdapter(Context paramContext, List<TectUpdateEntity.TectDetailEntity> paramList) {
        this.context = paramContext;
        this.lists = paramList;
        this.inflater = LayoutInflater.from(paramContext);
    }

    public int getCount() {
        return this.lists.size();
    }

    public TectUpdateEntity.TectDetailEntity getItem(int paramInt) {
        return this.lists.get(paramInt);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = this.inflater.inflate(R.layout.item_fation_toword, null);
            viewHolder.tv_videofun_appname = ((TextView) convertView.findViewById(R.id.tv_videofun_appname));
            viewHolder.ib_videofun_icon = ((ImageView) convertView.findViewById(R.id.ib_videofun_icon));
            viewHolder.lin_video_item_layout = ((LinearLayout) convertView.findViewById(R.id.lin_video_item_layout));
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        TectUpdateEntity.TectDetailEntity entity = lists.get(position);
        String title = entity.getTitle();
        if (title.length() > 10) {
            title = title.substring(0, 10) + "...";
        }
        viewHolder.tv_videofun_appname.setText(title);
        Glide.with(context)
                .load(entity.getCover_url())
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .thumbnail(0.5F).priority(Priority.HIGH)
                .placeholder(R.mipmap.ic_launcher)
                .bitmapTransform(new Transformation[]{new RoundedCornersTransformation(this.context, 15, 15, RoundedCornersTransformation.CornerType.ALL)})
                .crossFade(1000)
                .fallback(R.mipmap.ic_launcher)
                .into(viewHolder.ib_videofun_icon);
        Log.e("main", "==============================================");
        if (currentFoufus != position) {
            viewHolder.lin_video_item_layout.setBackgroundResource(R.drawable.rect_circle_trans);
        } else {
            viewHolder.lin_video_item_layout.setBackgroundResource(R.drawable.rect_circle_app);
        }
        viewHolder.lin_video_item_layout.setOnHoverListener(new View.OnHoverListener() {
            public boolean onHover(View view, MotionEvent motionEvent) {
                int what = motionEvent.getAction();
                switch (what) {
                    case MotionEvent.ACTION_HOVER_ENTER:  //鼠标进入view
                        changeView(true, view);
                        break;
                    case MotionEvent.ACTION_HOVER_EXIT:  //鼠标离开view
                        changeView(false, view);
                        break;
                }
                return false;
            }
        });

        return convertView;
    }

    public void changeView(boolean paramBoolean, View view) {
        if (paramBoolean) {
            view.setBackgroundResource(R.drawable.rect_circle_app);
        } else {
            view.setBackgroundResource(R.drawable.rect_circle_trans);
        }
    }

    public void setList(List<TectUpdateEntity.TectDetailEntity> paramList) {
        this.lists = paramList;
        notifyDataSetChanged();
    }

    public class ViewHolder {
        ImageView ib_videofun_icon;
        LinearLayout lin_video_item_layout;
        TextView tv_videofun_appname;
    }
}
