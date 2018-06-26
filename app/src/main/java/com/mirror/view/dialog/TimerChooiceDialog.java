package com.mirror.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.mirror.R;
import com.mirror.util.SharedPerManager;

public class TimerChooiceDialog implements View.OnClickListener {
    public static final int TAG_CLOSE_EQUIP = 1;
    public static final int TAG_OPEN_EQUIP = 0;
    Button btn_cacel;
    Button btn_hour_add;
    Button btn_min_add;
    Button btn_ok;
    Button btn_reduce_hour;
    Button btn_reduce_min;
    private Context context;
    int currentHour;
    int currentMin;
    private Dialog dialog;
    TimerChooiceListener listener;
    int tag;
    TextView tv_hour;
    TextView tv_min;

    public TimerChooiceDialog(Context context) {
      this.context = context;
        dialog = new Dialog(context, R.style.MyDialog);
        dialog.requestWindowFeature(1);
        View view = View.inflate(context, R.layout.dialog_timer_show, null);
        ViewGroup.LayoutParams localLayoutParams = new ViewGroup.LayoutParams(SharedPerManager.getScreenWidth(), SharedPerManager.getScreenHeight());
        dialog.setContentView(view, localLayoutParams);
        dialog.setCancelable(false);
        tv_hour = ((TextView)view.findViewById(R.id.tv_hour));
        tv_min = ((TextView)view.findViewById(R.id.tv_min));
        btn_hour_add = ((Button)view.findViewById(R.id.btn_hour_add));
        btn_min_add = ((Button)view.findViewById(R.id.btn_min_add));
        btn_reduce_hour = ((Button)view.findViewById(R.id.btn_reduce_hour));
        btn_reduce_min = ((Button)view.findViewById(R.id.btn_reduce_min));
        btn_ok = ((Button)view.findViewById(R.id.btn_ok));
        btn_cacel = ((Button)view.findViewById(R.id.btn_cacel));
        btn_hour_add.setOnClickListener(this);
        btn_min_add.setOnClickListener(this);
        btn_reduce_hour.setOnClickListener(this);
        btn_reduce_min.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
        btn_cacel.setOnClickListener(this);
    }

    private void updateShareOpenTime(int paramInt1, int paramInt2) {
        if (this.tag == 0)  {
            SharedPerManager.setOpenEquipHour(paramInt1);
            SharedPerManager.setOpenEquipMin(paramInt2);
        }
        while (this.tag != 1) {
            return;
        }
        SharedPerManager.setCloseEquipHour(paramInt1);
        SharedPerManager.setCloseEqiopMin(paramInt2);
    }

    private void updateTimer(int paramInt1, int paramInt2)
    {
        tv_hour.setText(paramInt1 + "");
        tv_min.setText(paramInt2 + "");
    }

    public void dissmiss() {
        if ((this.dialog != null) && (this.dialog.isShowing())) {
            dialog.dismiss();
        }
    }

    public void onClick(View paramView) {
        switch (paramView.getId()) {
            case R.id.btn_hour_add:
                currentHour += 1;
                if (this.currentHour > 23) {
                    currentHour = 0;
                }
                tv_hour.setText(this.currentHour + "");
                return;
            case R.id.btn_min_add:
                currentMin += 1;
                if (this.currentMin > 59) {
                    currentMin = 0;
                }
                tv_min.setText(this.currentMin + "");
                return;
            case R.id.btn_reduce_hour:
                currentHour -= 1;
                if (this.currentHour < 0) {
                    currentHour = 23;
                }
                tv_hour.setText(this.currentHour + "");
                return;
            case R.id.btn_reduce_min:
                currentMin -= 1;
                if (this.currentMin < 0) {
                    currentMin = 59;
                }
                tv_min.setText(this.currentMin + "");
                return;
            case R.id.btn_ok:
                updateShareOpenTime(this.currentHour,currentMin);
                if (this.listener != null) {
                    listener.chooiceTimerNum(this.currentHour,currentMin);
                }
                dissmiss();
                return;
            case R.id.btn_cacel:
                dissmiss();
        }
    }

    public void setOnTimerChooiceListener(TimerChooiceListener paramTimerChooiceListener)
    {
        listener = paramTimerChooiceListener;
    }

    public void show(int paramInt1, int paramInt2, int paramInt3)
    {
        tag = paramInt1;
        currentHour = paramInt2;
        currentMin = paramInt3;
        Log.e("haha", "=================获取的年月日==" +currentHour + "  /" +currentMin);
        updateTimer(this.currentHour,currentMin);
        dialog.show();
    }

    public interface TimerChooiceListener
    {
       void chooiceTimerNum(int paramInt1, int paramInt2);
    }
}
