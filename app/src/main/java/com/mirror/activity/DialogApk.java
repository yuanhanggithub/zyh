package com.mirror.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mirror.R;
import com.mirror.entity.AppInfomation;
import com.mirror.view.MyToastView;

/***
 * 通用dialog
 */
public class DialogApk extends Activity implements OnClickListener {
    //private Dialog dialog;
    //OriDialogClick dialogClick;
    //Context context;
    private TextView tv_content, tv_title;
    Button btn_ok;
    Button btn_cancle;
    Button btn_delete;
    private ImageView iv_app_icon;
//    private AppInfomation mInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_apk);
        initView();
        initData();
    }

    private void initView() {
        iv_app_icon = (ImageView) findViewById(R.id.iv_app_icon);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_content = (TextView) findViewById(R.id.tv_content);
        btn_cancle = (Button) findViewById(R.id.btn_cancle);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_delete = (Button) findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
        btn_cancle.setOnClickListener(this);
    }

    String packageName;

    public void initData() {
        Intent intent = getIntent();
        String appName = intent.getStringExtra("appName");
        packageName = intent.getStringExtra("packName");
        try {
            byte[] appIcons = intent.getByteArrayExtra("appIcon");
            Bitmap bitmap = BitmapFactory.decodeByteArray(appIcons, 0, appIcons.length);
            iv_app_icon.setImageBitmap(bitmap);
        } catch (Exception e) {
            iv_app_icon.setBackgroundResource(R.mipmap.ic_launcher);
        }
        String content = "请选择操作";
        tv_title.setText(appName);
        tv_content.setText(content);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancle:
                finish();
                break;
            case R.id.btn_ok:
                if (!TextUtils.isEmpty(packageName)) {
                    startAPP(packageName);
                    finish();
                }
                break;
            case R.id.btn_delete:
                if (!TextUtils.isEmpty(packageName)) {
                    unstallApp(packageName);
                    finish();
                }
                break;
            default:
                break;
        }
    }

    /*
     * 启动一个app
     */
    public void startAPP(String appPackageName) {
        try {
            Intent intent = this.getPackageManager().getLaunchIntentForPackage(appPackageName);
            startActivity(intent);
        } catch (Exception e) {
            MyToastView.getInstance().Toast(this, "打开软件失败：" + e.toString());
        }
    }

    // 卸载应用程序
    public void unstallApp(String appPackageName) {
        Intent uninstall_intent = new Intent();
        uninstall_intent.setAction(Intent.ACTION_DELETE);
        uninstall_intent.setData(Uri.parse("package:" + appPackageName));
        try {
            startActivity(uninstall_intent);
        } catch (Exception e) {
            MyToastView.getInstance().Toast(this, "卸载软件失败：" + e.toString());
        }
    }



	/*public void dismiss() {
        if (dialog == null) {
			return;
		}
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
	}

		public DialogApk(final Context context) {
		this.context = context;
		if (dialog == null) {
			dialog = new Dialog(context, R.style.MyDialog);
		}
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		int width = SharedPerManager.getScreenWidth();
		int height = SharedPerManager.getScreenHeight();
		LayoutParams params = new RelativeLayout.LayoutParams(width, height);
		View dialog_view = View.inflate(context, R.layout.dialog_apk, null);
		dialog.setContentView(dialog_view, params);
		dialog.setCancelable(false); // true点击屏幕以外关闭dialog
		initView(dialog_view);
	}

		public void showDialog(AppInfo info) {
		Drawable icon = info.getDrawable();
		String title = info.getAppName();
		String content = "请选择操作";
		packageName = info.getPackageName();
		tv_title.setText(title);
		iv_app_icon.setBackgroundDrawable(icon);
		tv_content.setText(content);
		dialog.show();
	}

		public void setOnDialogClickListener(OriDialogClick dc) {
			dialogClick = dc;
		}


		private OnClickListener clickListener = new OnClickListener() {

		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_cancle:
				if (dialogClick != null) {
					dialogClick.noSure();
					dismiss();
				}
				break;
			case R.id.btn_ok:
				if (dialogClick != null) {
					dialogClick.sure(packageName);
					dismiss();
				}
				break;
			case R.id.btn_delete:
				if (dialogClick != null) {
					dialogClick.dele(packageName);
					dismiss();
				}
				break;
			default:
				break;
			}

		}
	};*/
}
