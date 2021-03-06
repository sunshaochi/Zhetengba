package com.boyuanitsm.zhetengba.utils;

import android.content.Context;

import com.boyuanitsm.zhetengba.ConstantValue;
import com.boyuanitsm.zhetengba.MyApplication;
import com.boyuanitsm.zhetengba.db.DBOpenHelp;
import com.boyuanitsm.zhetengba.db.SmartDb;
import com.leaf.library.db.SmartDBHelper;


/**
 * 数据库操作辅助类
 * @author wangbin
 *
 */
public class ShUtils {
   
	private static SmartDb dbHelper;
	private static DBOpenHelp dbOpenHelp;
	private static SmartDBHelper labelDbHelper;
	private static DBOpenHelp labelDbOpenHelp;
	private static SmartDBHelper circleDbHelper;
	private static DBOpenHelp circleDbOpenHelper;
	public static Context getApplicationContext(){
		return getApplication().getApplicationContext();
	}
	
	public static SmartDb getDbhelper(){
		if(dbHelper==null){
			dbHelper=new SmartDb(getApplicationContext(),
					ConstantValue.DB_NAME,null,ConstantValue.VERSION,ConstantValue.MODELS);
		}
		return dbHelper;
	}


	public static DBOpenHelp getDbOpenHelp(){
		if (dbOpenHelp==null){
			dbOpenHelp=new DBOpenHelp(getApplicationContext());
		}
		return dbOpenHelp;
	}
	public static MyApplication getApplication(){
		return MyApplication.getInstance();
	}
}
