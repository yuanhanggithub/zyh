package com.mirror.http;

/**
 * Created by jsjm on 2018/1/9.
 */

public interface RequestBackListener {

    void requestSuccess(String json);

    void requestFailed(String errorDesc);

}
