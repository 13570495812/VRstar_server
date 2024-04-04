package com.jamma.vr_star.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 文件存储工厂
 * @author user
 *
 */
public class SharedPreferencesUtil {

	private SharedPreferences config;

	static SharedPreferencesUtil util;
	
	public static SharedPreferencesUtil getInstance(Context context) {
		if(util == null){
			util = new SharedPreferencesUtil(context, context.getPackageName());
		}
		return util;
	}

	public SharedPreferencesUtil(Context context, String name) {
		config = context.getSharedPreferences(name, Context.MODE_PRIVATE);// demo演示,允许其他应用读取
	}

	// 各种基本类型
	public void setBoolean(String key, Boolean flag) {
		config.edit().putBoolean(key, flag).commit();
	}

	public boolean getBoolean(String key) {
		return config.getBoolean(key, false);
	}

	public boolean getBoolean(String key,boolean defaultValue){
		return config.getBoolean(key, defaultValue);
	}

	public void setFloat(String key, float flag) {
		config.edit().putFloat(key, flag).commit();
	}

	public float getFloat(String key) {
		return config.getFloat(key, 0f);
	}

	public void setInt(String key, int flag) {
		config.edit().putInt(key, flag).commit();
	}

	public int getInt(String key) {
		return config.getInt(key, 0);
	}

	public void setLong(String key, long flag) {
		config.edit().putLong(key, flag).commit();
	}

	public long getLong(String key) {
		return config.getLong(key, 0);
	}

	public void setString(String key, String flag) {
		config.edit().putString(key, flag).commit();
	}

	public String getString(String key) {
		return config.getString(key, "");
	}

}
