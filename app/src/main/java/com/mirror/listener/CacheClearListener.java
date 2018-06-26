package com.mirror.listener;

import com.mirror.entity.ProcessInfo;

import java.util.ArrayList;

/**
 * 清理缓存。返回接口
 */
public interface CacheClearListener {
    void cacheBack(ArrayList<ProcessInfo> paramArrayList);
}
