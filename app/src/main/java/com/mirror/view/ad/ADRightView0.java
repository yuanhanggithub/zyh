//package com.mirror.view.ad;
//
//import android.content.Context;
//import android.util.Log;
//import android.view.View;
//import android.widget.ImageView;
//
//import com.bumptech.glide.Glide;
//import com.mirror.service.MirrorService;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import cn.cdl.library.CycleViewPager;
//import cn.cdl.library.ViewFactory;
//import cn.cdl.library.util.AdView;
//import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
//
//public class ADRightView0 implements CycleViewPager.ImageCycleViewListener {
//    private static final String TAG = "adv";
//    Context context;
//    List<Pos1Bean> infos = null;
//    private List<ImageView> views = new ArrayList();
//    private List<String> picUrls = new ArrayList();
//    AdView adView;
//
//    public ADRightView0(Context context) {
//        this.context = context;
//        adView = new AdView(context);
//    }
//
//    public void setAdInfo(CycleViewPager paramCycleViewPager, List<Pos1Bean> infos) {
//        Log.e("image", "=========准备显示的图片数量==" + infos.size());
//        try {
//            this.infos = infos;
//            views.clear();
//            picUrls.clear();
//            for (int i = 0; i < infos.size(); i++) {
//                views.add(ViewFactory.getImageView(this.context, infos.get(i).getPicpath()));
//                picUrls.add(infos.get(i).getPicpath());
//            }
//            paramCycleViewPager.setCycle(true);
//            paramCycleViewPager.setData(views, picUrls, this);
//            paramCycleViewPager.setWheel(true);
//            paramCycleViewPager.setTime(30 * 1000);
//            paramCycleViewPager.setIndicatorCenter();
//        } catch (Exception e) {
//        }
//    }
//
//
//    @Override
//    public void onImageClick(String info, int postion, View imageView) {
//
//    }
//
//    @Override
//    public void onItemSecet(int position) {
//        try {
//            Pos1Bean pos1Bean = infos.get(position);
//            MirrorService.getInstance().addPicCountRquest(pos1Bean);
//            String picUrl = pos1Bean.getPicpath();
//            Log.e("glideImage", "======文件存在侧边栏图片的位置==" + picUrl);
//            Glide.with(context)
//                    .load(picUrl)
//                    .bitmapTransform(new RoundedCornersTransformation(context, 15, 0, RoundedCornersTransformation.CornerType.ALL))
//                    .into(views.get(position));
//        } catch (Exception e) {
//            Log.e("glideImage", "catch异常捕捉==" + e.toString());
//        }
//    }
//
//    public void onImageClick(int paramInt, View paramView) {
//        Log.i("adv", "glideImage=====" + paramInt);
//    }
//}