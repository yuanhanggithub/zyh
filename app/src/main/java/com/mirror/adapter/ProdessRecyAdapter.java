package com.mirror.adapter;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Debug;
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
import com.cdl.wifi.util.MyLog;
import com.mirror.R;
import com.mirror.entity.ModelMainItem;
import com.mirror.entity.ProcessInfo;
import com.mirror.util.cache.MemoryUtil;
import com.mirror.wifi.ObjectAdapterClickListener;

import java.util.ArrayList;
import java.util.List;

public class ProdessRecyAdapter extends RecyclerView.Adapter<ProdessRecyAdapter.ViewHolder> {
    private Context context;
    ObjectAdapterClickListener listener;
    private List<ProcessInfo> mValues;
    LayoutInflater inflater;
    private ActivityManager mActivityManager;

    public void setProcessList(ArrayList<ProcessInfo> paramList) {
        this.mValues = paramList;
        notifyDataSetChanged();
    }

    public ProdessRecyAdapter(Context context, List<ProcessInfo> paramList) {
        this.mValues = paramList;
        this.context = context;
        mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        inflater = LayoutInflater.from(context);
    }

    public int getItemCount() {
        return mValues.size();
    }

    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.itemView.setFocusable(true);
        changeView(false, holder.itemView);
        ProcessInfo processInfo = mValues.get(position);
        MyLog.i("==", "===ProcessInfo==" + processInfo.toString());
        int[] pids = new int[]{processInfo.pid};
        Debug.MemoryInfo[] memoryInfo = mActivityManager.getProcessMemoryInfo(pids);
        int memorySize = memoryInfo[0].dalvikPrivateDirty;
        holder.ivIcon.setImageBitmap(processInfo.icon);
        holder.tvAppName.setText(processInfo.appName);
        holder.tvRun.setText(processInfo.pgName);
        holder.tvMemorySize.setText(MemoryUtil.byte2FitMemorySize(memorySize * 1024));

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
        } else {
            view.setBackgroundResource(R.drawable.rect_circle_trans);
        }
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int paramInt) {
        View view = inflater.inflate(R.layout.item_process, null);
        return new ViewHolder(view);
    }

    public void setOnRecycleItemClickListener(ObjectAdapterClickListener listener) {
        this.listener = listener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvAppName;
        TextView tvRun;
        TextView tvMemorySize;

        public ViewHolder(View view) {
            super(view);
            ivIcon = (ImageView) view.findViewById(R.id.ivIcon);
            tvAppName = (TextView) view.findViewById(R.id.tvAppName);
            tvRun = (TextView) view.findViewById(R.id.tvRun);
            tvMemorySize = (TextView) view.findViewById(R.id.tvMemorySize);
        }
    }
}
