package com.yunke.xiaovo.manage;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.yunke.xiaovo.bean.User;
import com.yunke.xiaovo.utils.StringUtil;


/**
 * 用户管理类
 */

public class UserManager {

    private static UserManager mUserManager;
    private static final String USER_INFO_FILE = "user_info_file";
    private static final String USER_INFO_JSON_KEY = "user_info_json_key";
    private static final String USER_ID_KEY = "user_id_key";
    private static final String USER_TOKEN_KEY = "user_token_key";
    private SharedPreferences userSP;
    private User mUser;


    private UserManager() {

    }


    public static UserManager getInstance() {

        if (mUserManager == null) {
            synchronized (UserManager.class) {
                if (mUserManager == null) {
                    mUserManager = new UserManager();
                    mUserManager.userSP = AppManager.getInstance().getSharedPreferences(USER_INFO_FILE, Context.MODE_PRIVATE);
                }
            }
        }

        return mUserManager;

    }


    public void init() {
        String userJson = userSP.getString(USER_INFO_JSON_KEY, "");
        mUser = StringUtil.jsonToObject(userJson, User.class);

    }


    public void upDateUser(@NonNull User user) {
        this.mUser = user;
        Gson gson = new Gson();
        SharedPreferences.Editor editor = userSP.edit();
        editor.putString(USER_INFO_JSON_KEY, gson.toJson(user));
        editor.putInt(USER_ID_KEY, user.getUserId());
        editor.putString(USER_TOKEN_KEY, user.getToken());
        editor.apply();
        editor.commit();
    }


    public int getUserId() {
        if (mUser != null) {
            return mUser.getUserId();
        }
        return 0;
    }


    public String getToken() {
        if (mUser != null) {
            return mUser.getToken();
        }
        return null;
    }


}
