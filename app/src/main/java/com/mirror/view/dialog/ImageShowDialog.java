package com.mirror.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.mirror.R;
import com.mirror.util.SharedPerManager;
import com.mirror.util.image.GlideImageUtil;

public class ImageShowDialog {
    private Context context;
    private Dialog dialog;
    private final ImageView image_show_dialog;

    public ImageShowDialog(Context context) {
        this.context = context;
        dialog = new Dialog(context, R.style.MyDialog);
        dialog.requestWindowFeature(1);
        View view = View.inflate(context, R.layout.dialog_image_show, null);
        ViewGroup.LayoutParams localLayoutParams = new ViewGroup.LayoutParams(SharedPerManager.getScreenWidth(), SharedPerManager.getScreenHeight());
        dialog.setContentView(view, localLayoutParams);
        dialog.setCancelable(true);
        image_show_dialog = (ImageView) view.findViewById(R.id.image_show_dialog);
    }

    public void dissmiss() {
        if ((dialog != null) && (dialog.isShowing())) {
            dialog.dismiss();
        }
    }

    public void show(String paramString) {
        GlideImageUtil.loadImageWidthCahe(context, paramString, image_show_dialog);
        dialog.show();
    }
}
