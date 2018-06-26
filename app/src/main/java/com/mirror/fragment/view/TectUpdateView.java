package com.mirror.fragment.view;


import com.mirror.entity.TectUpdateEntity;

import java.util.List;

public interface TectUpdateView {
    void backTectRequestBack(List<TectUpdateEntity.TectDetailEntity> paramList);

    void showToast(String paramString);

    void showWaitDialog(boolean paramBoolean);
}
