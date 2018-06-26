package com.mirror.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mirror.R;
import com.mirror.entity.ModelMainItem;
import com.mirror.wifi.ObjectAdapterClickListener;

import java.util.List;

public class AdapterRecyclerViewMain extends RecyclerView.Adapter<AdapterRecyclerViewMain.ViewHolder> {
    private Context context;
    ObjectAdapterClickListener listener;
    private List<ModelMainItem> mValues;
    LayoutInflater inflater;

    public AdapterRecyclerViewMain(Context context, List<ModelMainItem> paramList) {
        this.mValues = paramList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public int getItemCount() {
        return mValues.size();
    }

    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.itemView.setFocusable(true);
        ModelMainItem mItem = mValues.get(position);
        holder.tvName.setText(mItem.getName());
        Glide.with(context)
                .load(mItem.getImage())
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .thumbnail(0.5F).priority(Priority.HIGH)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .fallback(R.mipmap.ic_launcher)
                .into(holder.imageView);

        holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View view, boolean isTrue) {
                changeView(isTrue, view);
                Log.i("has", "获取了焦点" + position + "");
            }
        });

        holder.itemView.setOnHoverListener(new View.OnHoverListener() {
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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (listener != null) {
                    listener.clickItem(mValues.get(position), position);
                }
            }
        });
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

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int paramInt) {
        View view = inflater.inflate(R.layout.item_setting, null);
        return new ViewHolder(view);
    }

    public void setOnRecycleItemClickListener(ObjectAdapterClickListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvName;
        LinearLayout lin_setting_item;

        public ViewHolder(View view) {
            super(view);
            lin_setting_item = view.findViewById(R.id.lin_setting_item);
            imageView = view.findViewById(R.id.iv_launcher_item);
            tvName = view.findViewById(R.id.tv_exit_desc);
        }
    }
}
