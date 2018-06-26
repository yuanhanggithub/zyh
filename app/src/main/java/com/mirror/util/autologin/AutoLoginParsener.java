package com.mirror.util.autologin;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.mirror.config.AppInfo;
import com.mirror.http.GetHttpRequestRunnable;
import com.mirror.http.PostRequestRunnable;
import com.mirror.http.RequestBackListener;
import com.mirror.util.CodeUtil;
import com.mirror.util.NetWorkUtils;
import com.mirror.util.SharedPerManager;
import com.mirror.util.SimpleDateUtil;
import com.mirror.wifi.ApMgr;
import com.mirror.wifi.WifiMgr;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class AutoLoginParsener {
    private static final int AUTOLOGIN_TIME = 2541;
    private static final String TAG = "login";
    AutoLoginView autoLoginView;
    Context context;

    public AutoLoginParsener(Context paramContext, AutoLoginView paramAutoLoginView) {
        this.context = paramContext;
        this.autoLoginView = paramAutoLoginView;
    }

    /***
     * 自动登陆入口
     */
    public void autoLoginToWeb() {
        stopLoginWeb();
        this.timer = new Timer(true);
        this.task = new MyTask();
        this.timer.schedule(this.task, 0L, 4000);
    }


    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case AUTOLOGIN_TIME:
                    Log.i("login", "s时间到了，去登陆一次=====" + SharedPerManager.isLogin());
                    if (!SharedPerManager.isLogin()) {
                        addEquipToServer();
                    } else {
                        stopLoginWeb();
                    }
                    break;
            }
        }
    };
    int addEquipNum = 0;
    int netAddNum = 0;  //统计提交的次数

    public void addEquipToServer() {
        boolean isApOpen = ApMgr.isApOn(context);
        Log.w("login", "==========当前ap打开状态==" + isApOpen);
        if (isApOpen) {
            ApMgr.disableAp(context);
            Log.w("login", "==========ap是打开状态去关闭==");
        }
        if (!NetWorkUtils.isNetworkConnected(context)) {
            Log.w("login", "==========当前没有网络去检查==");
            boolean isWifiConnect = WifiMgr.getInstance(context).isWifiEnable();
            Log.w("login", "==========当前WIFI打开状态==" + isWifiConnect);
            if (isWifiConnect) {
                WifiMgr.getInstance(context).openWifi();
                Log.i("login", "wifi没有打开，去打开");
            }
            if (netAddNum > 6) {
                stopLoginWeb();
                netAddNum = 0;
            }
            Log.i("login", "网络未连接，第" + this.netAddNum + "次登录");
            netAddNum += 1;
            return;
        }
        Log.e("login", "======WIFI已经连接");
        autoLoginView.showWaitDialog(true);
        String deviceId = CodeUtil.getBlueToothCode();
        String loginData = SimpleDateUtil.getCurrentDateLogin();
        Log.i("login", "=====提交参数==" + deviceId + " /login_date=" + loginData);
        String requestUrl = AppInfo.ADD_EQUIP_TO_WEB;
        RequestBody requestBodyPost = new FormBody.Builder()
                .add("id", deviceId)
                .add("username", "tam")
                .add("password", "888888")
                .add("login_date", loginData)
                .build();
        PostRequestRunnable runnable = new PostRequestRunnable(requestUrl, requestBodyPost, new RequestBackListener() {
            @Override
            public void requestSuccess(String json) {
                autoLoginView.showWaitDialog(false);
                Log.e("login", "=====添加设备信息成功==" + json);
                parseAddEquip(json);
            }

            @Override
            public void requestFailed(String errorDesc) {
                autoLoginView.showWaitDialog(false);
                if (addEquipNum < 10) {
                    addEquipToServer();
                    addEquipNum++;
                } else {
                    addEquipNum = 0;
                    stopLoginWeb();
                    autoLoginView.showTaost(errorDesc);
                }
            }
        });
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private void parseAddEquip(String json) {
        try {
            JSONObject localObject = new JSONObject(json);
            int ret = ((JSONObject) localObject).getInt("state");
            if (ret == 1) {
                queryEquipToServer();
                return;
            } else if (ret == 0) {
                int code = localObject.getInt("code");
                if (code == 201) { //已经存在的设备直接下一步操作
                    queryEquipToServer();
                } else {
                    String error_msg = localObject.getString("error_msg");
                    String error_text = localObject.getString("error_text");
                    Log.e("login", "====添加设备失败：" + error_msg + "  /" + error_text);
                }
            }
        } catch (Exception paramString) {
            Log.e("login", "====解析异常：" + paramString.toString());
        }
    }


    /***
     * 查询设备信息
     */
    int requestEquipNum = 0;

    public void queryEquipToServer() {
        autoLoginView.showWaitDialog(true);
        String requestUrl = AppInfo.SEARCH_EQUIP_SHOP;
        String deviceId = CodeUtil.getBlueToothCode();
        RequestBody requestBodyPost = new FormBody.Builder()
                .add("id", deviceId)
                .build();
        PostRequestRunnable runnable = new PostRequestRunnable(requestUrl, requestBodyPost, new RequestBackListener() {
            @Override
            public void requestSuccess(String json) {
                autoLoginView.showWaitDialog(false);
                Log.e("login", "=====查询设备信息成功==" + json);
                parsenQueryEquipInfo(json);
            }

            @Override
            public void requestFailed(String errorDesc) {
                autoLoginView.showWaitDialog(false);
                Log.e("login", "=====查询设备信息失败==" + errorDesc);
                if (requestEquipNum < 10) {
                    queryEquipToServer();
                    requestEquipNum++;
                } else {
                    requestEquipNum = 0;
                    stopLoginWeb();
                    autoLoginView.showTaost("请求失败:" + errorDesc);
                }
            }
        });
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private void parsenQueryEquipInfo(String jsoon) {
        try {
            Object localObject1 = new JSONObject(jsoon);
            int ret = ((JSONObject) localObject1).getInt("state");
            Log.i("login", "==========解析查询state = " + ret);
            if (ret == 1) {
                String data = ((JSONObject) localObject1).getString("data");
                data = data.substring(data.indexOf("[") + 1, data.lastIndexOf("]"));
                JSONObject jsonDate = new JSONObject(data);
                String username = jsonDate.getString("username");
                String password = jsonDate.getString("password");
                SharedPerManager.setUserName(username);
                SharedPerManager.setPassword(password);
                autoLogintoWeb();
            } else {
//                String error_msg = localObject.getString("error_msg");
//                String error_text = localObject.getString("error_text");
                autoLoginView.showTaost("获取数据错误:" + jsoon);
            }
        } catch (Exception paramString) {
            Log.e("login", "=====解析异常==" + paramString.toString());
        }
    }

    int loginNum = 0;

    public void autoLogintoWeb() {
        this.autoLoginView.showWaitDialog(true);
        String username = SharedPerManager.getUserName();
        String password = SharedPerManager.getPassword();
        if (TextUtils.isEmpty(username)) {
            username = "tam";
            password = "888888";
        }
        Log.e("login", "=====登录账号：" + username + "  /密码 ：" + password);
        String requestUrl = "http://api.magicmirrormedia.cn/mirr/apiv1/user/login/username/" + username + "/password/" + password + "/usertype/shop";
        GetHttpRequestRunnable runnable = new GetHttpRequestRunnable(requestUrl, new RequestBackListener() {

            @Override
            public void requestSuccess(String json) {
                autoLoginView.showWaitDialog(false);
                Log.i("login", "=====登陆请求成功===" + json);
                parserLoginJson(json);
            }

            @Override
            public void requestFailed(String errorDesc) {
                autoLoginView.showWaitDialog(false);
                if (loginNum < 10) {
                    autoLogintoWeb();
                    loginNum++;
                } else {
                    loginNum = 0;
                    stopLoginWeb();
                    autoLoginView.showTaost("网络请求失败:" + errorDesc);
                }
            }
        });
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private void parserLoginJson(String json) {
        try {
            JSONObject localObject = new JSONObject(json);
            int ret = localObject.getInt("state");
            if (ret == 1) {
                String data = localObject.getString("data");
                Log.e("login", "===========data =" + data);
                JSONObject jsonObjectData = new JSONObject(data);
                String token = jsonObjectData.getString("token");
                Log.e("login", "===========token =" + token);
                SharedPerManager.setToken(token);
                SharedPerManager.setLogin(true);
                stopLoginWeb();
                autoLoginView.autoLoginSuccess();
                Log.e("login", "===========登陆成功了token =" + token);
            } else {
                SharedPerManager.setLogin(false);
                String error_text = localObject.getString("error_text");
                String error_msg = localObject.getString("error_msg");
                this.autoLoginView.showTaost("登录失败:" + error_text + "\n" + error_msg);
            }
        } catch (Exception paramString) {
            Log.i("login", "======解析异常 = " + paramString.toString());
        }
    }
//===================================================================================================

    TimerTask task;
    Timer timer;

    class MyTask extends TimerTask {
        public void run() {
            handler.sendEmptyMessage(AUTOLOGIN_TIME);
        }
    }

    public void stopLoginWeb() {
        if (this.timer != null) {
            this.timer.cancel();
        }
        if (this.task != null) {
            this.task.cancel();
        }
    }

}
