package com.mirror.fragment.adapter;

import android.content.Context;
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
import com.mirror.entity.VideoEntity;
import com.mirror.fragment.FationTowdFragment;
import com.mirror.wifi.ObjectAdapterClickListener;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class FationTowordAdapter extends BaseAdapter {
    Context context;
    ObjectAdapterClickListener listener;
    int currentFoufus = 0;
    LayoutInflater inflater;
    List<VideoEntity> lists;

    public FationTowordAdapter(Context paramContext, List<VideoEntity> paramList) {
        this.context = paramContext;
        this.lists = paramList;
        this.inflater = LayoutInflater.from(paramContext);
    }

    public int getCount() {
        return this.lists.size();
    }

    public VideoEntity getItem(int paramInt) {
        return (VideoEntity) this.lists.get(paramInt);
    }

    public long getItemId(int paramInt) {
        return 0L;
    }

    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_fation_toword, null);
            holder.tv_videofun_appname = ((TextView) convertView.findViewById(R.id.tv_videofun_appname));
            holder.ib_videofun_icon = ((ImageView) convertView.findViewById(R.id.ib_videofun_icon));
            holder.lin_video_item_layout = ((LinearLayout) convertView.findViewById(R.id.lin_video_item_layout));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        VideoEntity localVideoEntity = lists.get(position);
        holder.tv_videofun_appname.setText(localVideoEntity.getTitle() + "");
        Glide.with(context)
                .load(localVideoEntity.getPicpath())
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .thumbnail(0.5F)
                .priority(Priority.HIGH)
                .placeholder(R.mipmap.ic_launcher)
                .bitmapTransform(new Transformation[]{new RoundedCornersTransformation(this.context, 15, 15, RoundedCornersTransformation.CornerType.ALL)})
                .crossFade(1000)
                .fallback(R.mipmap.ic_launcher)
                .into(holder.ib_videofun_icon);
        if (currentFoufus == position) {
            changeView(true, holder.lin_video_item_layout);
        } else {
            changeView(false, holder.lin_video_item_layout);
        }
        holder.lin_video_item_layout.setOnHoverListener(new View.OnHoverListener() {
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
//            ViewCompat.animate(view).scaleX(1.2F).scaleY(1.2F).translationZ(1.0F).setDuration(100L).start();
        } else {
            view.setBackgroundResource(R.drawable.rect_circle_trans);
//            ViewCompat.animate(view).scaleX(1.0F).scaleY(1.0F).translationZ(1.0F).setDuration(100L).start();
        }
    }

    public void setCurrentFoufus(int paramInt) {
        this.currentFoufus = paramInt;
        notifyDataSetChanged();
    }

    public void setList(List<VideoEntity> paramList) {
        this.lists = paramList;
        notifyDataSetChanged();
    }

    public void setOnRecycleItemClickListener(ObjectAdapterClickListener listener) {
        this.listener = listener;
    }

    public class ViewHolder {
        ImageView ib_videofun_icon;
        LinearLayout lin_video_item_layout;
        TextView tv_videofun_appname;
    }
}
