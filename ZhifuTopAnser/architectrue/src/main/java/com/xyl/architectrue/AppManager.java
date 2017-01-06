package com.xyl.architectrue;

import android.app.Activity;
import android.text.TextUtils;

import java.util.Stack;

/**
 * User: ShaudXiao
 * Date: 2017-01-03
 * Time: 15:33
 * Company: zx
 * Description:
 * FIXME
 */


public class AppManager {

    private static AppManager instance;

    private static Stack<Activity> mActivityStack;


    private AppManager() {

    }

    public static AppManager getInstance() {
        if(instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    /*
    * 添加activity到栈
    * */
    public void addActivity(Activity activity) {
        if(null == mActivityStack) {
            mActivityStack = new Stack<>();
        }

        mActivityStack.add(activity);
    }

    /*
    * 包含包名的类
    * */
    public boolean existClass(String className) {
        if(mActivityStack == null || mActivityStack.size() == 0 || TextUtils.isEmpty(className)) {
            return false;
        }

        for(int i = 0; i < mActivityStack.size(); i++) {
            String name = mActivityStack.get(i).getClass().getName();
            if(className.equals(name)) {
                return true;
            }
        }

        return  false;
    }

    /*
    * 获取当前activity
    * */
    public Activity currentActity() {
        Activity activity = mActivityStack.lastElement();
        return activity;
    }

    /*
    * 结束当前activity
    *
    * */
    public void finishActivity() {
        Activity activity = mActivityStack.lastElement();
        activity.finish();
    }

    public void finishActivity(Activity activity) {
        if(null != activity) {
            activity.finish();
        }
    }

    /*
    * 结束指定activity
    * */
    public void finishActivity(Class<?> cls) {
        for (Activity activity: mActivityStack
             ) {
            if(activity.getClass().equals(cls)) {
                activity.finish();
            }
        }
    }

    public void finishAllActivity() {
        if(mActivityStack == null || mActivityStack.size() == 0) {
            return;
        }

        for(Activity activity : mActivityStack) {
            if(activity != null) {
                activity.finish();
            }
        }

        mActivityStack.clear();
    }

    public void removeActivity(Activity activity) {
        if(null != activity) {
            mActivityStack.remove(activity);
        }
    }

    public void exit() {
        try {
            finishActivity();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
