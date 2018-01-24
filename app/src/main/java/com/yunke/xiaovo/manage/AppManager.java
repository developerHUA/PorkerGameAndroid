package com.yunke.xiaovo.manage;

import android.app.Activity;

import com.yunke.xiaovo.base.BaseApplication;
import com.lzy.okgo.OkGo;

import java.util.Stack;

/**
 *
 */
public class AppManager extends BaseApplication {

    private static AppManager appManager;
    private static Stack<Activity> activityStack;


    public static AppManager getInstance() {
        return appManager;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        appManager = this;
        initOkGo();
        UserManager.getInstance().init();


    }

    /**
     * 添加Activity到堆栈
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 删除特定类名的Activity
     *
     * @param cls
     */
    public void removeActivity(Class cls) {
        if (activityStack == null) {
            return;
        }
        Activity activity = getActivity(cls);
        activityStack.remove(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     *
     * @return
     */
    public Activity currentActivity() {
        if (activityStack == null) return null;

        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        finishActivity(currentActivity());
    }

    /**
     * 结束指定的Activity
     *
     * @param activity
     */
    public void finishActivity(Activity activity) {
        if (activityStack == null) return;

        if (activity != null && !activity.isFinishing()) {
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     *
     * @param cls
     */
    public void finishActivity(Class<?> cls) {
        if (activityStack == null) return;

        finishActivity(getActivity(cls));
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        if (activityStack == null) return;

        for (int size = activityStack.size(), i = 0; i < size; i++) {
            if (null != activityStack.get(i)) {
                finishActivity(activityStack.get(i));
            }
        }
        activityStack.clear();
    }

    /**
     * 获取指定的Activity
     *
     * @param cls
     * @return
     */
    public static Activity getActivity(Class<?> cls) {
        if (activityStack == null) return null;

        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                return activity;
            }
        }
        return null;
    }


    private void initOkGo() {
        OkGo.getInstance().init(this);
    }
}
