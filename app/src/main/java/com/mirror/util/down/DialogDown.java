package com.mirror.util.down;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.mirror.R;


/***
 * 通用dialog 两个按钮
 */
public class DialogDown {
    private Dialog dialog;
    Context context;


    public DialogDown(Context context) {
        this.context = context;
        if (dialog == null) {
            dialog = new Dialog(context, R.style.MyDialog);
        }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        int width = 1280;
        int height = 800;
        LayoutParams params = new LayoutParams(width, height);
        View dialog_view = View.inflate(context, R.layout.dialog_down_file, null);
        dialog.setContentView(dialog_view, params);
        dialog.setCancelable(false); // true点击屏幕以外关闭dialog
        initView(dialog_view);
    }


    Button btn_ok;
    Button btn_cancle;
    TextView tv_title, tv_dialog_content;
    TextView tv_speed, tv_down_progress;
    ProgressBar progress_down;
    RelativeLayout rela_progress;

    private void initView(View dialog_view) {
        if (dialog_view == null) {
            return;
        }
        rela_progress = (RelativeLayout) dialog_view.findViewById(R.id.rela_progress);
        progress_down = (ProgressBar) dialog_view.findViewById(R.id.progress_down);
        btn_cancle = (Button) dialog_view.findViewById(R.id.btn_dialog_no);
        btn_ok = (Button) dialog_view.findViewById(R.id.btn_dialog_yes);
        tv_title = (TextView) dialog_view.findViewById(R.id.tv_title);
        tv_speed = (TextView) dialog_view.findViewById(R.id.tv_down_speed);
        tv_down_progress = (TextView) dialog_view.findViewById(R.id.tv_down_progress);
        tv_dialog_content = (TextView) dialog_view.findViewById(R.id.tv_dialog_content);
        btn_ok.setOnClickListener(clickListener);
        btn_cancle.setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {

        public void onClick(View v) {
            if (v.getId() == R.id.btn_dialog_yes) {
                listener.clickOk();
            } else if (v.getId() == R.id.btn_dialog_no) {
                dismiss();
                listener.clickCancle();
            }
        }
    };

    public void showDialog(String desc) {
        tv_title.setText("App Down");
        tv_dialog_content.setVisibility(View.VISIBLE);   //初始化布局
        rela_progress.setVisibility(View.GONE);           //默认隐藏
        btn_ok.setVisibility(View.VISIBLE);               //控件默认显示状态
        tv_dialog_content.setText(desc);
        dialog.show();
    }

    public void dismiss() {
        if (dialog == null) {
            return;
        }
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    DownDialogListener listener;

    public void setOnDownDialogListener(DownDialogListener listener) {
        this.listener = listener;
    }

    int COLOR_RED = 0xffF0001D;
    int COLOR_BLUE = 0xff5cabfa;

    public void updateView(DownFileEntity entity) {
        String desc = entity.getDesc();
        int progress = entity.getProgress();
        int downSpeed = entity.getDownSpeed();
        int downState = entity.getDownState();
        Log.i("dialog", "progress==" + progress);
        tv_dialog_content.setVisibility(View.GONE);
        rela_progress.setVisibility(View.VISIBLE);
        progress_down.setProgress(progress);
        tv_down_progress.setText(progress + "%");
        tv_speed.setText(downSpeed + "");

        boolean isDown = entity.isDown();
        if (isDown) {
            btn_ok.setVisibility(View.GONE);
        } else {
            btn_ok.setVisibility(View.VISIBLE);
        }
        updateState(downState, desc);
        if (progress >= 100) {
            dismiss();
        }
    }

    /***
     * 更新标题文字
     * @param downState
     * @param desc
     */
    private void updateState(int downState, String desc) {
        if (downState == DownFileEntity.DOWN_STATE_FAIED) {
            tv_title.setTextColor(COLOR_RED);
        } else {
            tv_title.setTextColor(COLOR_BLUE);
        }
        tv_title.setText(desc);
    }

    public interface DownDialogListener {
        void clickOk();

        void clickCancle();
    }

}
