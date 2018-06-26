//package com.mirror.adapter;
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.mirror.R;
//import com.mirror.util.sdcard.FileLocalEntity;
//import com.mirror.wifi.ObjectAdapterClickListener;
//
//import java.util.List;
//
//public class SdcardManagerAdapter extends RecyclerView.Adapter<SdcardManagerAdapter.ViewHolder> {
//    List<FileLocalEntity> appInfos;
//    Context context;
//    ObjectAdapterClickListener listener;
//
//    public SdcardManagerAdapter(Context paramContext, List<FileLocalEntity> paramList) {
//        this.context = paramContext;
//        this.appInfos = paramList;
//    }
//
//    public int getItemCount() {
//        return appInfos.size();
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder holder, final int position) {
//        holder.itemView.setFocusable(true);
//        changeView(holder.itemView, false);
//        FileLocalEntity localFileLocalEntity = (FileLocalEntity) this.appInfos.get(position);
//        holder.tv_sdcard_name.setText("磁盘：" + localFileLocalEntity.getPanName());
//        holder.tv_sdcard_total_size.setText("总空间：" + localFileLocalEntity.getTitalSize());
//        holder.tv_sd_current.setText("可用空间：" + localFileLocalEntity.getLastSize());
//        holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            public void onFocusChange(View view, boolean isTrus) {
//                changeView(view, isTrus);
//            }
//        });
//        holder.itemView.setOnHoverListener(new View.OnHoverListener() {
//            public boolean onHover(View view, MotionEvent event) {
//                int action = event.getAction();
//                switch (action) {
//                    case MotionEvent.ACTION_HOVER_ENTER:
//                        changeView(view, true);
//                        break;
//                    case MotionEvent.ACTION_HOVER_EXIT:
//                        changeView(view, false);
//                        break;
//                }
//                return false;
//            }
//        });
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                if (listener != null) {
//                    listener.clickItem(appInfos.get(position), position);
//                }
//            }
//        });
//    }
//
//
//    public void changeView(View view, boolean isChange) {
//        Log.e("position,", "==============获取焦点==" + isChange);
//        if (isChange) {
//            view.setBackgroundResource(R.drawable.rect_circle_app);
//        } else {
//            view.setBackgroundResource(R.drawable.rect_circle_trans);
//        }
//    }
//
//
//    public ViewHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt) {
//        return new ViewHolder(LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.item_sdcard_info, paramViewGroup, false));
//    }
//
//
//    public void setAppInfos(List<FileLocalEntity> paramList) {
//        this.appInfos = paramList;
//    }
//
//    public void setOnAdapterClickListener(ObjectAdapterClickListener listener) {
//        this.listener = listener;
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        TextView tv_sd_current;
//        TextView tv_sdcard_name;
//        TextView tv_sdcard_total_size;
//
//        public ViewHolder(View paramView) {
//            super(paramView);
//            tv_sdcard_name = ((TextView) paramView.findViewById(R.id.tv_sdcard_name));
//            tv_sdcard_total_size = ((TextView) paramView.findViewById(R.id.tv_sdcard_total_size));
//            tv_sd_current = ((TextView) paramView.findViewById(R.id.tv_sd_current));
//        }
//    }
//}
