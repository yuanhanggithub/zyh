package com.mirror.config;

import android.net.wifi.aware.PublishConfig;
import android.os.Environment;

import com.mirror.R;

/**
 * Created by jsjm on 2018/5/5.
 */

public class AppInfo {

    public static final String ADV_PICCOUNT = "http://api.magicmirrormedia.cn/mirr/apiv1/adv/viewcount/";
    public static final int AD_TAG_BOTTOM_MENU = 4;
    public static final int AD_TAG_POP_TOP = 13;

    public static final String BASE_LOCAL_URL = Environment.getExternalStorageDirectory().getPath() + "/mirror";
    public static final String BASE_APK_PATH = BASE_LOCAL_URL + "/apk";
    public static final String BASE_MUSIC_PATH = BASE_LOCAL_URL + "/music";
    public static final String BASE_QR_PATH = BASE_LOCAL_URL + "/qr";
    public static final String BASE_VIDEO_CACHE = BASE_LOCAL_URL + "/cache";
    public static final String BASE_VIDEO_PATH = BASE_LOCAL_URL + "/video";
    public static final String VIDEO_SPLASH_PATH = BASE_LOCAL_URL + "/splash";
    public static final String FILE_RECEIVER_PATH = BASE_LOCAL_URL + "/filereceive";
    public static final String FILE_RECEIVE_IMAGE = FILE_RECEIVER_PATH + "/image";
    public static final String AD_IMAGE_RIGHT = FILE_RECEIVE_IMAGE + "/right";
    public static final String AD_IMAGE_BOTTOM = FILE_RECEIVE_IMAGE + "/bottom";

    public static final String FIREWARE_DOWNLOAD_SAVE_APK = BASE_APK_PATH + "/update.img";
    public static final String DOWNLOAD_SAVE_APK = BASE_APK_PATH + "/mirror.apk";
    //绑定设备二维码保存地址
    public static final String EQUIP_BIND_CODE = BASE_QR_PATH + "/EQUIP_BIND.jpg";
    //投屏二维码保存地址
    public static final String TOU_PING_ER_CODE_PATH = BASE_QR_PATH + "/eshare.jpg";

    //*======================================网络请求相关==========================================================
    public static final String REQUEST_BASE_URL = "http://api.magicmirrormedia.cn/mirr/apiv1/";
    //技能提升
    public static final String TECH_UPDATE_URL = REQUEST_BASE_URL + "teachingVideo/search";
    //添加设备到服务器
    public static final String ADD_EQUIP_TO_WEB = REQUEST_BASE_URL + "device/add";
    //查询设备服务器
    public static final String SEARCH_EQUIP_SHOP = REQUEST_BASE_URL + "device/search";
    //增加统计次数的
    public static final String ADD_COUNT_PIC_VODIO = REQUEST_BASE_URL + "adv/viewcount/";
    //获取侧边栏广告
    public static final String GET_RIGHT_AD_IMAGE = REQUEST_BASE_URL + "adv/list/position/1,3/token/";
    //获取弹屏广告
    public static final String GET_POP_URL = REQUEST_BASE_URL + "adv/list/position/13/token/";
    //获取底部栏广告
    public static final String GET_BOTTOM_URL = REQUEST_BASE_URL + "adv/list/position/4/token/";

    //  * 获取潮流趋势Url
    public static String getFationUrl(String token) {
        String url = REQUEST_BASE_URL + "channel/videolist/subchannel_id/33/token/" + token + "/orderby/new/start_pos/1/list_num/200";
        return url;
    }

    // *  获取登陆URL
    public static String getLoginUrl(String userName, String password) {
        String loginUrl = REQUEST_BASE_URL + "user/login/username/" + userName + "/password/" + password + "/usertype/shop";
        return loginUrl;
    }

    //* 默认广告请求地址
    public static final String DEFAULT_AD_REQUEST = "http://cdn.magicmirrormedia.cn/mirrorprojector/interface/tvadinfo.txt";
    // * 默认显示图片url
    public static final String DEFAULT_BOTTOM_IMAGE_DIAONLINE = AD_IMAGE_BOTTOM + "/menu_bottom_default.png";
    public static final String DEFAULT_RIGHT_IMAGE_DISONLINE = AD_IMAGE_RIGHT + "/ad_default.png";
    public static final String DEFAULT_BOTTOM_IMAGE = "http://cdn.magicmirrormedia.cn/img/menu_bottom_default.png";
    public static final String DEFAULT_RIGHT_IMAGE = "http://cdn.magicmirrormedia.cn/img/ad_default.png";
    public static final String DEFAULT_TOP_IMAGE = "http://cdn.magicmirrormedia.cn/default/default_ad_pic_pos_13.png";

    //老设备升级地址
    public static final String CHECK_APP_UPDARE_OLD = "http://cdn.magicmirrormedia.cn/mirrorprojector/mirrormagic_old/updatemorror.txt";
    //软件升级检测地址
    public static final String CHECK_APP_UPDARE = "http://cdn.magicmirrormedia.cn/mirrorprojector/update/updatemorror.txt";
    //系统升级检测地址
    public static final String CHECK_SYSTEM_URL = "http://cdn.magicmirrormedia.cn/mirrorprojector/systemupdate/update_fireware_info.txt";

    //=========================================================================================================================
    //ImIRROR
    public static final String ESHARE_I_MIRRIR_PACKAGE = "com.ecloud.eairplay";
    //EshareServer
    public static final String ESHARE_SERVER_PACKAGE = "com.ecloud.eshare.server";

    //旧设备包名
    public static final String APP_PACKAGE_OLD = "com.magicmirrormedia.mirrorclient.activity";
    //新设备包名
    public static final String APP_PACKAGE_NEW = "com.example.gsyvideoplayer";


    /***
     *  W文件传输下载地址
     */
    public static final String SOCKET_FILE_DOWN_URL = "http://cdn.magicmirrormedia.cn/mirrorprojector/socketUpdateFile/app-debug.apk";
    public static final String SOCKET_SAVE_URL = BASE_APK_PATH + "/SOCK_FILE.apk";
    public static final String SOCKET_APK_PACKAGENAME = "com.cdl.socket";


    //GSY播放器
    public static final String GSY_DOWN_NEW = "http://cdn.magicmirrormedia.cn/mirrorprojector/gsyplayer/GsyPlayer.apk";
    public static final String GSY_DOWN_OLD = "http://cdn.magicmirrormedia.cn/mirrorprojector/gsyplayer_old/GsyPlayer.apk";
    public static final String GSY_PLAYER_PACKAGENAME = "com.mirror.videoplayer";
    public static final String GSY_SAVE = BASE_APK_PATH + "/GSYplay.apk";

    /***
     * 蜂蜜直播
     */
    public static final int BEE_ICON = R.mipmap.icon_fengmi;
    public static final String BEE_NAME_APP = "枫蜜直播";
    public static final String BEE_PACKAGE = "com.fengmizhibo.live";
    public static final String BEE_DOWNURL = "http://cdn.magicmirrormedia.cn/mirrorprojector/apkdown/fengmi.apk";

    //bilibili
    public static final int BILIBILI_ICON = R.mipmap.icon_bilibili;
    public static final String BILIBILI_NAME = "BiliBili";
    public static final String BILIBILI_PACKAGE = "com.bilibili.tv";
    public static final String BILIBILI_DOWNURL = "http://cdn.magicmirrormedia.cn/mirrorprojector/apkdown/bilibili.apk";
    //斗鱼
//    public static final int DOUYU_ICON = R.mipmap.icon_douyu;
//    public static final String DOUYU_NAME_APP = "斗鱼直播端";
//    public static final String DOUYU_PACKAGE = "air.tv.douyu.android";
//    public static final String DOUYU_DOWNURL = "http://cdn.magicmirrormedia.cn/mirrorprojector/apkdown/douyuclient.apk";
    //斗鱼观看端
    public static final int DOUYU_TV_ICON = R.mipmap.icon_douyu;
    public static final String DOUYU_TV_NAME_APP = "斗鱼TV观看端";
    public static final String DOUYU_TV_PACKAGE = "com.douyu.xl.douyutv";
    public static final String DOUYU_TV_DOWNURL = "http://cdn.magicmirrormedia.cn/mirrorprojector/apkdown/douyutv.apk";
    //hdp直播
    public static final int HDP_ICON = R.mipmap.icon_hdp;
    public static final String HDP_NAME = "HDP电视直播";
    public static final String HDP_PACKAGE = "hdpfans.com";
    public static final String HDP_HOME_DOWNURL = "http://cdn.magicmirrormedia.cn/mirrorprojector/apkdown/hdpzb.apk";
    //虎牙直播
    public static final int HUYA_TV_ICON = R.mipmap.icon_huya;
    public static final String HUYA_TV_NAME_APP = "虎牙直播";
    public static final String HUYA_TV_PACKAGE = "com.duowan.kiwitv";
    public static final String HUYA_TV_DOWNURL = "http://cdn.magicmirrormedia.cn/mirrorprojector/apkdown/hyzb.apk";
    //乐看动画
    public static final int LEKAN_ICON = R.mipmap.icon_lekan;
    public static final String LEKAN_NAME = "乐看卡通";
    public static final String LEKAN_PACKAGE = "com.lekan.tv.kids.activity";
    public static final String LEKAN_DOWNURL = "http://cdn.magicmirrormedia.cn/mirrorprojector/apkdown/lekancarton.apk";
    //六一卡通
    public static final int LIUYI_ICON = R.mipmap.icon_liuyi;
    public static final String LIUYI_NAME = "六一卡通乐园";
    public static final String LIUYI_PACKAGE = "com.hoaix.childplayer.tv";
    public static final String LIUYI_DOWNURL = "http://cdn.magicmirrormedia.cn/mirrorprojector/apkdown/liuyi.apk";
    //熊猫直播
    public static final int PANDER_TV_ICON = R.mipmap.icon_xiongmao;
    public static final String PANDER_TV_NAME_APP = "熊猫直播";
    public static final String PANDER_TV_PACKAGE = "com.panda.videolivetv";
    public static final String PANDER_TV_DOWNURL = "http://cdn.magicmirrormedia.cn/mirrorprojector/apkdown/pandalive.apk";
    //爱奇艺动画
    public static final int TV_ANIMAL_ICON = R.mipmap.icon_aibabu;
    public static final String TV_ANIMAL_NAME = "奇巴布动画";
    public static final String TV_ANIMAL_PACKAGE = "com.happy.wonderland";
    public static final String TV_ANIMAL_DOWNURL = "http://cdn.magicmirrormedia.cn/mirrorprojector/apkdown/aiqiyi.apk";
    //电视家
//    public static final int TV_HOME_ICON = R.mipmap.icon_dianshijia;
//    public static final String TV_HOME_NAME = "电视家TV";
//    public static final String TV_HOME_PACKAGE = "com.dianshijia.newlive";
//    public static final String TV_HOME_PACKAGE_OLD = "com.elinkway.tvlive2";
//    public static final String TV_HOME_DOWNURL = "http://cdn.magicmirrormedia.cn/mirrorprojector/apkdown/tv_home.apk";

    public static final int    TV_KW_MUSIC_ICON = R.mipmap.icon_kwmusic;
    public static final String TV_KW_MUSIC_NAME = "酷我音乐";
    public static final String TV_KW_MUSIC_PACKAGE = "cn.kuwo.music.tv";
    public static final String TV_KW_MUSIC_DOWNURL = "http://cdn.magicmirrormedia.cn/mirrorprojector/apkdown/kuwomusic.apk";
    //==============广播================================================================
    //* 网络连接成功
    public static final String NET_ONLINE = "com.reeman.receiver.NET_ONLINE";
    // * 网络连接断开
    public static final String NET_DISONLINE = "com.reeman.receiver.NET_DISONLINE";


    public static final String POWER_OPEN_CLOSE_BROAD = "com.ys.keyevent_power";
    public static final String VOICE_TEST_ADD = "com.ys.keyevent_volume_up";
    public static final String VOICE_TEST_REDUCE = "com.ys.keyevent_volume_down";
    //eshare 点击OK按钮
    public static final String ESAHRE_CLICK_OK = "com.eshare.action.OK_CLICKED_WHEN_DLNAING";
    public static final String ESAHRE_APP_OPEN = "com.eshare.action.PLAYER_STARTED";
    public static final String ESAHRE_APP_CLOSE = "com.eshare.action.PLAYER_STOPPED";

}
