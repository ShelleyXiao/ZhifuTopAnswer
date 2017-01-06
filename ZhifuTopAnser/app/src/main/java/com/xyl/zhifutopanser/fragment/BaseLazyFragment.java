package com.xyl.zhifutopanser.fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

import com.xyl.architectrue.rxsupport.RxFragment;

/**
 * User: ShaudXiao
 * Date: 2017-01-04
 * Time: 15:56
 * Company: zx
 * Description:
 * FIXME
 */


public abstract class BaseLazyFragment extends RxFragment {

    private boolean isFirstVisible = true;
    private boolean isFirstResume = true;
    private boolean isFirstInvisible = true;
    private boolean isPrepared;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        initPrepare();
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    @Override
    public void onResume() {
        super.onResume();
        if (isFirstResume) {
            isFirstResume = false;
            return;
        }
        if (getUserVisibleHint()) {
            onUserVisible();
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    @Override
    public void onPause() {
        super.onPause();
        if (getUserVisibleHint()) {
            onUserInvisible();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (isFirstVisible) {
                isFirstVisible = false;
                initPrepare();
            } else {
                onUserVisible();
            }
        } else {
            if (isFirstInvisible) {
                isFirstInvisible = false;
                onFirstUserInvisible();
            } else {
                onUserInvisible();
            }
        }
    }

    private synchronized void initPrepare() {
        if (isPrepared) {
            onFirstUserVisible();
        } else {
            isPrepared = true;
        }
    }

    /**
     * 当Fragment第一次可见时调用，可以在此方法做初始化工作或者刷新一次数据
     */
    protected abstract void onFirstUserVisible();

    /**
     * 此方法像Fragment生命周期中的onResume
     */
    protected abstract void onUserVisible();

    /**
     * 当Fragment第一次不可见时调用
     */
    protected abstract void onFirstUserInvisible();

    /**
     * * 此方法像Fragment生命周期中的onPause
     */
    protected abstract void onUserInvisible();
}
