package com.mirror.guide;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.BoolRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.mirror.R;
import com.mirror.config.AppInfo;
import com.mirror.util.CodeUtil;
import com.mirror.util.FileUtil;
import com.mirror.util.QRCodeUtil;
import com.mirror.view.MyToastView;

import java.io.File;

public class GuideBindFragment extends Fragment {

    private static final int CREATE_FAILED = 1;
    private static final int CREATE_SUCCESS = 0;

    public Animation onCreateAnimation(int paramInt1, boolean paramBoolean, int paramInt2) {
        if (paramBoolean) {
            return AnimationUtils.loadAnimation(getActivity(), R.anim.anim_enter);
        }
        return AnimationUtils.loadAnimation(getActivity(), R.anim.anim_exit);
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        View view = View.inflate(getActivity(), R.layout.fragment_guide_bind, null);
        FileUtil.creatDirPathNoExists();
        initView(view);
        return view;
    }


    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CREATE_SUCCESS:
                    String picPath = (String) msg.obj;
                    if (new File(picPath).exists()) {
                        Bitmap bitmap = BitmapFactory.decodeFile(picPath);
                        iv_bind_er_code.setImageBitmap(bitmap);
                    } else {
                        MyToastView.getInstance().Toast(GuideBindFragment.this.getActivity(), "二维码不存在");
                    }
                    return;
                case CREATE_FAILED:
                    String errordesc = (String) msg.obj;
                    MyToastView.getInstance().Toast(GuideBindFragment.this.getActivity(), errordesc);
                    break;
            }
        }
    };
    ImageView iv_bind_er_code;
    TextView tv_ercode_desc;

    private void initView(View view) {
        String bluuthCode = CodeUtil.getBlueToothCode();
        tv_ercode_desc = ((TextView) view.findViewById(R.id.tv_ercode_desc));
        tv_ercode_desc.setText("               设备ID：" + bluuthCode + "\n\n请使用 <尽善镜美> 扫描二维码，绑定设备");
        iv_bind_er_code = ((ImageView) view.findViewById(R.id.iv_bind_er_code));
        String binsCode = "{\"bind_code\":" + bluuthCode + "}";
        QRCodeUtil.createErCode(binsCode, AppInfo.EQUIP_BIND_CODE, new QRCodeUtil.ErCodeListener() {
            @Override
            public void createSuccess(String path) {
                Message localMessage = new Message();
                localMessage.what = CREATE_SUCCESS;
                localMessage.obj = path;
                GuideBindFragment.this.handler.sendMessage(localMessage);
            }

            @Override
            public void createFailed(String error) {
                Message localMessage = new Message();
                localMessage.what = CREATE_FAILED;
                localMessage.obj = error;
                GuideBindFragment.this.handler.sendMessage(localMessage);
            }
        });

    }

}
