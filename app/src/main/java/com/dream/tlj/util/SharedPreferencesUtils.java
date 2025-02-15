package com.dream.tlj.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import com.dream.tlj.update.XdUpdateBean;

public class SharedPreferencesUtils {
    /**
     * 保存在手机里面的文件名
     */
    private static final String FILE_NAME = "share_data";
    private static final String REWARD = "reward";


    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context
     * @param key
     * @param object
     */
    public static void setParam(Context context, String key, Object object) {

        String type = object.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if ("String".equals(type)) {
            editor.putString(key, (String) object);
        } else if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) object);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) object);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) object);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) object);
        }

        editor.commit();
    }

    public static void saveUpdateBean(Context context, XdUpdateBean bean) {
        if (bean != null) {
            SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            Gson gson = new Gson();
            String s = gson.toJson(bean);
            editor.putString("update_bean", s);
            editor.commit();
        }
    }

    public static XdUpdateBean loadUpdateBean(Context context) {
        XdUpdateBean bean = null;
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        String s = sp.getString("update_bean", "");
        if (s.length() > 0) {
            Gson gson = new Gson();
            bean = gson.fromJson(s, XdUpdateBean.class);
        }
        return bean;
    }

    public static void updateReward(Context context, int count) {
        setParam(context, REWARD, count);
    }

    public static void addReward(Context context, int count) {
        int old = (int) getParam(context, REWARD, 0);
        updateReward(context, old + count);
    }

    public static int getReward(Context context) {
        return (int) getParam(context, REWARD, AdUtil.AD_REWARD_VALUE);
    }


    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object getParam(Context context, String key, Object defaultObject) {
        String type = defaultObject.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        if ("String".equals(type)) {
            return sp.getString(key, (String) defaultObject);
        } else if ("Integer".equals(type)) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if ("Boolean".equals(type)) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if ("Float".equals(type)) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if ("Long".equals(type)) {
            return sp.getLong(key, (Long) defaultObject);
        }

        return null;
    }
}
