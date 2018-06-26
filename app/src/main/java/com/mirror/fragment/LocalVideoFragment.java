package com.mirror.fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.mirror.MirrorApplication;
import com.mirror.R;
import com.mirror.config.AppConfig;
import com.mirror.config.AppInfo;
import com.mirror.fragment.adapter.LocalPlayAdapter;
import com.mirror.util.APKUtil;
import com.mirror.util.CursorMediaUtil;
import com.mirror.util.DisPlayUtil;
import com.mirror.util.FileUtil;
import com.mirror.util.SharedPerManager;
import com.mirror.util.down.AppDownInstallUtil;
import com.mirror.util.down.DownFileEntity;
import com.mirror.util.down.DownStateListener;
import com.mirror.util.down.MirrorDownUtil;
import com.mirror.view.MyToastView;
import com.mirror.view.WaitDialogUtil;
import com.mirror.view.ad.VideoLocaEntity;
import com.mirror.view.dialog.ImageShowDialog;
import com.mirror.view.dialog.OridinryDialog;

import java.util.ArrayList;
import java.util.List;

public class LocalVideoFragment extends Fragment {

    LocalPlayAdapter adapter;
    CursorMediaUtil cursorMediaUtil;
    AppDownInstallUtil appDownInstallUtil;
    GridView grid_content;
    ImageShowDialog imageDialog;
    private int lastSelectGridPosition = 0;
    List<VideoLocaEntity> listsMedia = new ArrayList();
    OridinryDialog oridinryDialog;
    WaitDialogUtil waitDialogUtil;
    private TextView tv_fragment_desc;

    public Animation onCreateAnimation(int paramInt1, boolean paramBoolean, int paramInt2) {
        if (paramBoolean) {
            return AnimationUtils.loadAnimation(getActivity(), R.anim.anim_enter);
        }
        return AnimationUtils.loadAnimation(getActivity(), R.anim.anim_exit);
    }

    public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
        View view = View.inflate(getActivity(), R.layout.fragment_fathion, null);
        initView(view);
        initListener();
        return view;
    }

    private void initView(View paramView) {
        appDownInstallUtil = new AppDownInstallUtil(getActivity());
        tv_fragment_desc = (TextView) paramView.findViewById(R.id.tv_fragment_desc);
        tv_fragment_desc.setText("请点击<菜单>按钮，进行作品传输");
        oridinryDialog = new OridinryDialog(getActivity(), SharedPerManager.getScreenWidth(), SharedPerManager.getScreenHeight());
        waitDialogUtil = new WaitDialogUtil(getActivity());
        grid_content = ((GridView) paramView.findViewById(R.id.grid_fathion));
        grid_content.setNumColumns(5);
        adapter = new LocalPlayAdapter(getActivity(), listsMedia);
        grid_content.setAdapter(this.adapter);
        grid_content.requestFocus();
    }

    private void getVideoImageData() {
        showWaitDialog(true);
        listsMedia.clear();
        cursorMediaUtil = new CursorMediaUtil(getActivity());
        cursorMediaUtil.refreshFileList(AppInfo.FILE_RECEIVER_PATH, new CursorMediaUtil.FileSearchBackListener() {
            public void backError(String paramAnonymousString) {
                showWaitDialog(false);
                MyToastView.getInstance().Toast(getActivity(), paramAnonymousString);
            }

            public void backFile(List<VideoLocaEntity> paramAnonymousList) {
                showWaitDialog(false);
                List<VideoLocaEntity> lists = MirrorApplication.getInstance().getListsMedia();
                if (lists.size() > 0) {
                    listsMedia.addAll(lists);
                }
                Log.e("====", "==================获取的本地视屏的数量 :" + paramAnonymousList.size() + "  //" + listsMedia.size());
                adapter.setList(listsMedia);
                if (listsMedia.size() > 0) {
                    grid_content.requestFocus();
                    return;
                }
            }
        });
    }

    private void initListener() {
        grid_content.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> paramAnonymousAdapterView, View viw, int position, long paramAnonymousLong) {
                lastSelectGridPosition = position;
                adapter.setCurrentFoufus(position);
            }

            public void onNothingSelected(AdapterView<?> paramAnonymousAdapterView) {
            }
        });
        grid_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long paramAnonymousLong) {
                lastSelectGridPosition = position;
                adapter.setCurrentFoufus(position);
                VideoLocaEntity entity = listsMedia.get(position);
                Log.e("cdl", "=====点击的文件的Path==" + entity.getFilePath());
                int fileType = entity.getFileType();
                if (fileType == VideoLocaEntity.TYPE_IMAGE) {
                    opImageView(entity.getFilePath());
                } else if (fileType == VideoLocaEntity.TYPE_VIDEO) {
                    String playPackageName = AppInfo.GSY_PLAYER_PACKAGENAME;
                    if (APKUtil.ApkState(getActivity(), playPackageName)) {
                        openVideo(entity, position);
                    } else {
                        appDownInstallUtil.downAppInstall(AppInfo.GSY_PLAYER_PACKAGENAME, true);
                    }
                }
            }
        });
        grid_content.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                try {
                    VideoLocaEntity entity = listsMedia.get(position);
                    final String filePath = entity.getFilePath();
                    String fileName = entity.getFileName();
                    oridinryDialog.show("是否删除 <" + fileName + "> 文件", "删除", "在想想");
                    oridinryDialog.setOnDialogClickListener(new OridinryDialog.OridinryDialogClick() {
                        public void noSure() {
                        }

                        public void sure() {
                            if (FileUtil.deleteDirOrFile(filePath)) {
                                MyToastView.getInstance().Toast(getActivity(), "删除文件成功");
                                getVideoImageData();
                            } else {
                                MyToastView.getInstance().Toast(getActivity(), "删除文件失败，请重启设备刷新\n或者去文件管理器/sdcard/mirror/filereceiver中删除文件");
                            }
                        }
                    });
                } catch (Exception e) {
                }
                return true;
            }
        });
    }

    private void jujleSocketExiet() {
        String socketPackageName = AppInfo.SOCKET_APK_PACKAGENAME;
        if (APKUtil.ApkState(getActivity(), socketPackageName)) {
            DisPlayUtil.startApp(getActivity(), socketPackageName);
        } else {
            appDownInstallUtil.downAppInstall(AppInfo.SOCKET_APK_PACKAGENAME, true);
        }
    }

    private void opImageView(String paramString) {
        if (imageDialog == null) {
            imageDialog = new ImageShowDialog(getActivity());
        }
        imageDialog.show(paramString);
    }

    private void openVideo(VideoLocaEntity paramVideoLocaEntity, int paramInt) {
        try {
            String action = "com.example.gsyvideoplayer.activity.PlayLocalVideoActivity";
            ComponentName localComponentName = new ComponentName("com.mirror.videoplayer", action);
            Intent localIntent = new Intent();
            localIntent.setComponent(localComponentName);
            localIntent.setAction(action);
            localIntent.putExtra("LOCAL_PLAY_URL", paramVideoLocaEntity.getFilePath());
            localIntent.putExtra("LOCAL_PLAY_TITLE", paramVideoLocaEntity.getFileName());
            localIntent.putExtra("LOCAL_CLIICK_POSITION", paramInt);
            startActivity(localIntent);
            return;
        } catch (Exception e) {
            Log.e("cdl", "====跳转异常===" + e.toString());
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            showSendDialog();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            if (grid_content.isFocused()) {
                return true;
            }
            grid_content.requestFocus();
            if (listsMedia.size() > 2) {
                adapter.setCurrentFoufus(lastSelectGridPosition);
            }
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            if (grid_content.isFocused()) {
                adapter.setCurrentFoufus(-1);
            }
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            if (grid_content.isFocused()) {
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            if (grid_content.isFocused()) {
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            getActivity().finish();
            return true;
        }
        return false;
    }

    private void showSendDialog() {
        oridinryDialog.show("是否进行去传输作品 ？ ", "去传输", "算了");
        oridinryDialog.setOnDialogClickListener(new OridinryDialog.OridinryDialogClick() {
            @Override
            public void sure() {
                jujleSocketExiet();
            }

            @Override
            public void noSure() {

            }
        });
    }

    public void onResume() {
        super.onResume();
        getVideoImageData();
    }

    public void showWaitDialog(boolean paramBoolean) {
        if (paramBoolean) {
            waitDialogUtil.show("加载中");
            return;
        }
        waitDialogUtil.dismiss();
    }
}
