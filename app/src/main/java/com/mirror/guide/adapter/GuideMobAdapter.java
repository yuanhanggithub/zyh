package com.mirror.guide.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.mirror.R;
import com.mirror.guide.entity.GuideMobEntity;


import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class GuideMobAdapter extends RecyclerView.Adapter<GuideMobAdapter.Holder> {
    private static final String TAG = GuideMobAdapter.class.getSimpleName();
    private List<GuideMobEntity> data;
    private Context mContext;

    public GuideMobAdapter(Context paramContext, List<GuideMobEntity> paramList) {
        this.mContext = paramContext;
        this.data = paramList;
    }

    public void changeViewSize(boolean paramBoolean, View view, int paramInt) {
        if (paramBoolean) {
            view.setBackgroundResource(R.drawable.rect_circle_app);
        } else {
            view.setBackgroundResource(R.drawable.rect_circle_trans);
        }
    }

    public int getItemCount() {
        if (this.data == null) {
            return 0;
        }
        return this.data.size();
    }

    public void onBindViewHolder(Holder paramHolder, final int position) {
        GuideMobEntity localGuideMobEntity = (GuideMobEntity) this.data.get(position);
        String str2 = localGuideMobEntity.getDesc();
        String str1 = str2;
        if (str2.length() > 6) {
            str1 = str2.substring(0, 6);
        }
        paramHolder.tv_video_desc.setText(str1);
        paramHolder.itemView.setFocusable(true);
        paramHolder.itemView.setTag(Integer.valueOf(position));
        changeViewSize(false, paramHolder.itemView, position);
        Glide.with(this.mContext)
                .load(Integer.valueOf(localGuideMobEntity.getImageId()))
                .placeholder(R.mipmap.ic_launcher).centerCrop()
                .bitmapTransform(new Transformation[]{new RoundedCornersTransformation(this.mContext, 10, 0, RoundedCornersTransformation.CornerType.ALL)})
                .crossFade(1000).into(paramHolder.iv_video_pic);
        paramHolder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View view, boolean isChhice) {
                changeViewSize(isChhice, view, position);
            }
        });
    }

    public Holder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt) {
        return new Holder(LayoutInflater.from(mContext).inflate(R.layout.item_guide_mob, paramViewGroup, false));
    }

    public void setData(List<GuideMobEntity> paramList) {
        this.data = paramList;
    }

    public void setList(List<GuideMobEntity> paramList) {
        this.data = paramList;
        notifyDataSetChanged();
    }

    class Holder extends RecyclerView.ViewHolder {
        ImageView iv_video_pic;
        TextView tv_video_desc;

        Holder(View paramView) {
            super(paramView);
            this.tv_video_desc = ((TextView) paramView.findViewById(R.id.tv_huide_desc));
            this.iv_video_pic = ((ImageView) paramView.findViewById(R.id.iv_guide_image));
        }
    }
}
