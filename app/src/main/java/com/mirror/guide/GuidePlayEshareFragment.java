//package com.mirror.guide;
//
//import android.graphics.BitmapFactory;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.animation.Animation;
//import android.view.animation.AnimationUtils;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.mirror.config.AppInfo;
//import com.mirror.util.FileUtil;
//import com.mirror.util.QRCodeUtil;
//
//import java.io.File;
//
//public class GuidePlayEshareFragment extends Fragment {
//    private static final int CREATE_FAILED = 1;
//    private static final int CREATE_SUCCESS = 0;
//    private Handler handler = new Handler() {
//        public void handleMessage(Message paramAnonymousMessage) {
//            super.handleMessage(paramAnonymousMessage);
//            switch (paramAnonymousMessage.what) {
//                default:
//                    return;
//                case 0:
//                    paramAnonymousMessage = (String) paramAnonymousMessage.obj;
//                    if (new File(paramAnonymousMessage).exists()) {
//                        paramAnonymousMessage = BitmapFactory.decodeFile(paramAnonymousMessage);
//                        GuidePlayEshareFragment.this.iv_bind_er_code.setImageBitmap(paramAnonymousMessage);
//                        return;
//                    }
//                    MyToastView.getInstance().Toast(GuidePlayEshareFragment.this.getActivity(), "二维码不存在");
//                    return;
//            }
//            paramAnonymousMessage = (String) paramAnonymousMessage.obj;
//            MyToastView.getInstance().Toast(GuidePlayEshareFragment.this.getActivity(), paramAnonymousMessage);
//        }
//    };
//    ImageView iv_bind_er_code;
//    TextView tv_ercode_desc;
//
//    private void initView(View paramView) {
//        this.tv_ercode_desc = ((TextView) paramView.findViewById(2131624215));
//        this.tv_ercode_desc.setText("请使用 <店长助手> 扫描二维码，绑定设备");
//        this.iv_bind_er_code = ((ImageView) paramView.findViewById(2131624214));
//        paramView = CodeUtil.getBlueToothCode();
//        QRCodeUtil.createErCode("{\"bind_code\":" + paramView + "}", AppInfo.EQUIP_BIND_CODE, new QRCodeUtil.ErCodeListener() {
//            public void createFailed(String paramAnonymousString) {
//                Message localMessage = new Message();
//                localMessage.what = 1;
//                localMessage.obj = paramAnonymousString;
//                GuidePlayEshareFragment.this.handler.sendMessage(localMessage);
//            }
//
//            public void createSuccess(String paramAnonymousString) {
//                Message localMessage = new Message();
//                localMessage.what = 0;
//                localMessage.obj = paramAnonymousString;
//                GuidePlayEshareFragment.this.handler.sendMessage(localMessage);
//            }
//        });
//    }
//
//    public Animation onCreateAnimation(int paramInt1, boolean paramBoolean, int paramInt2) {
//        if (paramBoolean) {
//            return AnimationUtils.loadAnimation(getActivity(), 2131034125);
//        }
//        return AnimationUtils.loadAnimation(getActivity(), 2131034126);
//    }
//
//    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
//        paramLayoutInflater = View.inflate(getActivity(), 2130968669, null);
//        FileUtil.creatDirPathNoExists();
//        initView(paramLayoutInflater);
//        return paramLayoutInflater;
//    }
//}
