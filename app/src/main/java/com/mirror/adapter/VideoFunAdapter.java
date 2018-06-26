//package com.mirror.adapter;
//
//import android.content.Context;
//import android.support.v4.view.ViewCompat;
//import android.support.v4.view.ViewPropertyAnimatorCompat;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.RecyclerView.Adapter;
//import android.support.v7.widget.RecyclerView.ViewHolder;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.View.OnFocusChangeListener;
//import android.view.View.OnHoverListener;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.mirror.R;
//import com.mirror.entity.InnerAppEntity;
//
//import java.util.List;
//
//public class VideoFunAdapter extends RecyclerView.Adapter<VideoFunAdapter.ViewHolder> {
//    private LayoutInflater inflater;
//    private List<InnerAppEntity> list_curren;
//    VideoFunItemClickListener listener;
//    private Context mContext;
//
//    public VideoFunAdapter(Context mContext, List<InnerAppEntity> paramList) {
//        this.mContext = mContext;
//        this.list_curren = paramList;
//        this.inflater = LayoutInflater.from(mContext);
//    }
//
//    public int getItemCount() {
//        return this.list_curren.size();
//    }
//
//    public void onBindViewHolder(ViewHolder holder, final int position) {
//        holder.lin_video_item_layout.setFocusable(true);
//        if (holder == null) {
//            return;
//        }
//        InnerAppEntity entity = list_curren.get(position);
//        int imageUrl = entity.getImgeUrl();
//        final String liveName = entity.getLiveName();
//        holder.ib_videofun_icon.setImageResource(imageUrl);
//        holder.tv_videofun_appname.setText(liveName);
//        holder.lin_video_item_layout.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View paramAnonymousView) {
//                if (listener != null) {
//                    listener.clickItem(position);
//                }
//            }
//        });
//        holder.lin_video_item_layout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            public void onFocusChange(View view, boolean isFocus) {
//                setChangeAnimal(view, isFocus, position);
//            }
//        });
//    }
//
//    public ViewHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt) {
//        return new ViewHolder(inflater.inflate(R.layout.item_video_fun, paramViewGroup, false));
//    }
//
//    public void setChangeAnimal(View view, boolean isTrue, int position) {
//        if (isTrue) {
//            if (listener != null) {
//                listener.selectionPosition(position);
//            }
//            view.setBackgroundResource(R.drawable.rect_circle_app);
//            ViewCompat.animate(view).scaleX(1.2F).scaleY(1.2F).translationZ(1.0F).setDuration(100L).start();
//        } else {
//            view.setBackgroundResource(R.drawable.rect_circle_trans);
//            ViewCompat.animate(view).scaleX(1.0F).scaleY(1.0F).translationZ(1.0F).setDuration(100L).start();
//        }
//    }
//
//    public void setListCurrent(List<InnerAppEntity> paramList) {
//        this.list_curren = paramList;
//        notifyDataSetChanged();
//    }
//
//    public void setVideoFunItemClickListener(VideoFunItemClickListener paramVideoFunItemClickListener) {
//        this.listener = paramVideoFunItemClickListener;
//    }
//
//    public interface VideoFunItemClickListener {
//        void clickItem(int paramInt);
//
//        void selectionPosition(int paramInt);
//    }
//
//    class ViewHolder extends RecyclerView.ViewHolder {
//        ImageView ib_videofun_icon;
//        LinearLayout lin_video_item_layout;
//        TextView tv_videofun_appname;
//
//        public ViewHolder(View paramView) {
//            super(paramView);
//            lin_video_item_layout = ((LinearLayout) paramView.findViewById(R.id.lin_video_item_layout));
//            tv_videofun_appname = ((TextView) paramView.findViewById(R.id.tv_videofun_appname));
//            ib_videofun_icon = ((ImageView) paramView.findViewById(R.id.ib_videofun_icon));
//        }
//    }
//}
