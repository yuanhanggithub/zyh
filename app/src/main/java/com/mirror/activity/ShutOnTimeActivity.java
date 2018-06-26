package com.mirror.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mirror.R;
import com.mirror.util.SharedPerManager;
import com.mirror.view.MyToastView;
import com.mirror.view.dialog.TimerChooiceDialog;
import com.mirror.view.dialog.TimerChooiceDialog.TimerChooiceListener;

public class ShutOnTimeActivity extends BaseActivity implements View.OnClickListener {
    static final String ACTION_POWER_ON_OFF = "com.signway.PowerOnOff";
    Button btn_close_time;
    Button btn_open_close;
    Button btn_open_time;
    CheckBox checkBox_openclose_state;
    TimerChooiceDialog dialog;
    TextView tv_close_time;
    TextView tv_open_close_state;
    TextView tv_open_time;

    protected void onCreate(Bundle paramBundle)  {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_shut_ontime);
        initView();
    }
    private void initView()  {
        this.dialog = new TimerChooiceDialog(this);
        this.btn_open_close = ((Button)findViewById(R.id.btn_open_close));
        this.btn_open_time = ((Button)findViewById(R.id.btn_open_time));
        this.btn_close_time = ((Button)findViewById(R.id.btn_close_time));
        this.btn_open_close.setOnClickListener(this);
        this.btn_open_time.setOnClickListener(this);
        this.btn_close_time.setOnClickListener(this);
        this.tv_open_close_state = ((TextView)findViewById(R.id.tv_open_close_state));
        this.checkBox_openclose_state = ((CheckBox)findViewById(R.id.checkBox_openclose_state));
        updateOpenCloseState();
        this.tv_open_time = ((TextView)findViewById(R.id.tv_open_time));
        this.tv_close_time = ((TextView)findViewById(R.id.tv_close_time));
        int i = SharedPerManager.getOpenEquipHour();
        int j = SharedPerManager.getOpenEquipMin();
        this.tv_open_time.setText(i + ":" + j);
        i = SharedPerManager.getCloseEquipHour();
        j = SharedPerManager.getCloseEqiopMin();
        this.tv_close_time.setText(i + ":" + j);
    }
//private void  {
//    if (SharedPerManager.getOpenEquipHour()>SharedPerManager.getCloseEquipHour()&&(SharedPerManager.getOpenEquipHour()*
//            60+SharedPerManager.getOpenEquipMin())>(SharedPerManager.getCloseEquipHour()*60+SharedPerManager.getCloseEqiopMin())){
//        MyToastView.getInstance().Toast(ShutOnTimeActivity.this,"开机时间不能大于关机时间");
//        SharedPerManager.setOpenPowerNotify(false);
//        updateOpenCloseState();
//    }
//}
    private void sendMyBroadcast() {
        if (SharedPerManager.getOpenEquipHour()>SharedPerManager.getCloseEquipHour()&&(SharedPerManager.getOpenEquipHour()*
                60+SharedPerManager.getOpenEquipMin())>(SharedPerManager.getCloseEquipHour()*60+SharedPerManager.getCloseEqiopMin())){
            MyToastView.getInstance().Toast(ShutOnTimeActivity.this,"开机时间不能大于关机时间");
            SharedPerManager.setOpenPowerNotify(false);
            updateOpenCloseState();
        }else {
            Intent localIntent = new Intent("com.signway.PowerOnOff");
            Bundle localBundle = new Bundle();
            localBundle.putString("group0", timerFormat());
            localIntent.putExtras(localBundle);
            sendBroadcast(localIntent);
            Log.e("broadreceive", "=================发送的广播============" + timerFormat());
        }
    }

    private void updateOpenCloseState()  {
        if (SharedPerManager.isOpenPowerNotify()) {
            tv_open_close_state.setText("打开");
            checkBox_openclose_state.setChecked(true);
        }else {
            tv_open_close_state.setText("关闭");
            checkBox_openclose_state.setChecked(false);
        }
    }

    public void alertCloseEquipTime()
    {
        int i = SharedPerManager.getCloseEquipHour();
        int j = SharedPerManager.getCloseEqiopMin();
        dialog.show(1, i, j);
        dialog.setOnTimerChooiceListener(new TimerChooiceDialog.TimerChooiceListener()
        {
            public void chooiceTimerNum(int paramAnonymousInt1, int paramAnonymousInt2)
            {
                tv_close_time.setText(paramAnonymousInt1 + ":" + paramAnonymousInt2);
               sendMyBroadcast();
            }
        });
    }

    public void alertOpenEquipTime()
    {
        int i = SharedPerManager.getOpenEquipHour();
        int j = SharedPerManager.getOpenEquipMin();
        dialog.show(0, i, j);
        dialog.setOnTimerChooiceListener(new TimerChooiceDialog.TimerChooiceListener()
        {
            public void chooiceTimerNum(int paramAnonymousInt1, int paramAnonymousInt2)
            {
                tv_open_time.setText(paramAnonymousInt1 + ":" + paramAnonymousInt2);
              sendMyBroadcast();
            }
        });
    }

    public String lowTemNum(int paramInt)  {
        if (paramInt < 10) {
            return "0" + paramInt;
        }
        return "" + paramInt;
    }

    public void onClick(View paramView) {
        Log.i("fff","=================="+paramView.getId());
        switch (paramView.getId()) {
            case R.id.btn_open_close:
                if (SharedPerManager.isOpenPowerNotify())  {
                    SharedPerManager.setOpenPowerNotify(false);
                    MyToastView.getInstance().Toast(this, "自动开关机已关闭");
                }else  {
                    SharedPerManager.setOpenPowerNotify(true);
                    MyToastView.getInstance().Toast(this, "自动开关机已打开");
                }
                sendMyBroadcast();
                updateOpenCloseState();
                break;
            case R.id.btn_open_time:
                alertOpenEquipTime();
                break;
            case R.id.btn_close_time:
                alertCloseEquipTime();
                return;
        }
    }

   private static final String OPEN_TIMER = "1";
    private static final String CLOSE_TIMER = "0";
    //open        0/1/08:30/02:30
    //close       0/0/08:30/02:30
    public String timerFormat()  {
        String isOpen = CLOSE_TIMER;
        if (SharedPerManager.isOpenPowerNotify()) {
            isOpen =OPEN_TIMER;
        }
            int hourse = SharedPerManager.getOpenEquipHour();
            int minice = SharedPerManager.getOpenEquipMin();
            String openTime = lowTemNum(hourse) + ":" + lowTemNum(minice);
            hourse = SharedPerManager.getCloseEquipHour();
            minice = SharedPerManager.getCloseEqiopMin();
            String closeTime = lowTemNum(hourse) + ":" + lowTemNum(minice);
            String backJson = "0" + isOpen + openTime + closeTime;
            Log.e("broadreceive", "=============发送的代码=" + "0" + "/" + isOpen + "/" + openTime + "/" + closeTime);
            return backJson;
    }
}
