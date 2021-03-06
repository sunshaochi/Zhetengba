package com.boyuanitsm.zhetengba.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.boyuanitsm.zhetengba.Constant;
import com.boyuanitsm.zhetengba.IsShow;
import com.boyuanitsm.zhetengba.activity.MainAct;
import com.boyuanitsm.zhetengba.activity.circle.CirMessAct;
import com.boyuanitsm.zhetengba.activity.circle.CircleAct;
import com.boyuanitsm.zhetengba.activity.circle.SerchCirAct;
import com.boyuanitsm.zhetengba.activity.circle.SquareAct;
import com.boyuanitsm.zhetengba.activity.mess.DqMesAct;
import com.boyuanitsm.zhetengba.activity.mess.SquareMessAct;
import com.boyuanitsm.zhetengba.bean.ActivityMess;
import com.boyuanitsm.zhetengba.bean.CircleInfo;
import com.boyuanitsm.zhetengba.bean.NewCircleMess;
import com.boyuanitsm.zhetengba.bean.SquareInfo;
import com.boyuanitsm.zhetengba.db.ActivityMessDao;
import com.boyuanitsm.zhetengba.db.CircleMessDao;
import com.boyuanitsm.zhetengba.db.CircleNewMessDao;
import com.boyuanitsm.zhetengba.db.SquareMessDao;
import com.boyuanitsm.zhetengba.db.UserInfoDao;
import com.boyuanitsm.zhetengba.fragment.MessFrg;
import com.boyuanitsm.zhetengba.fragment.circleFrg.CircleFrg;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * <p/>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";
    private LocalBroadcastManager broadcastManager;
    private String flag;
    int newCount = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//        	processCustomMessage(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
            String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的内容: " + extra);
            try {
                JSONObject json = new JSONObject(extra);
                String type = json.getString("type");//解析单个
//                    String comment=json.getString("commentTalk");
//                    Log.d(TAG, "json.getString(\"commentContent\"); 接收到推送下来的commentContent的内容: " + comment);
                if (TextUtils.equals(type, "2")) {
                    if (IsShow.getA().equals("2")) {//表示在cirmessAct中
                        context.sendBroadcast(new Intent(CirMessAct.REDGONG));
                    } else {

                        if (CircleNewMessDao.getUser() != null) {
                            CircleNewMessDao.deleteUser();
                        }
                        if (UserInfoDao.getUser() != null) {
                            NewCircleMess newCircleMess = new NewCircleMess();
                            newCircleMess.setId(UserInfoDao.getUser().getId());
                            newCircleMess.setIsMain(false);//false表示未读
                            newCircleMess.setIsCircle(false);
                            newCircleMess.setIsMess(false);
                            CircleNewMessDao.saveUser(newCircleMess);
                        }

                    }

                    Gson gson = new Gson();
                    flag = 2 + "";
                    CircleInfo circleInfo = gson.fromJson(json.toString(), CircleInfo.class);//解析成对象
                    //实例化SharedPreferences对象（第一步）
                    SharedPreferences sharedPreferences = context.getSharedPreferences("ztb_cirNews",
                            Activity.MODE_PRIVATE);
                    //实例化SharedPreferences.Editor对象（第二步）
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    //用putString的方法保存数据
                    if (!TextUtils.isEmpty(circleInfo.getUserIcon())) {
                        editor.putString("cir_news", circleInfo.getUserIcon());
                    }
                    int cir_newsCount = sharedPreferences.getInt("cir_NewsCount", 0);
                    cir_newsCount++;
                    editor.putInt("cir_NewsCount", cir_newsCount);
                    //提交当前数据
                    editor.commit();
                    broadcastManager = LocalBroadcastManager.getInstance(context);
                    Intent intentPointGone = new Intent(context, MainAct.class);
                    intentPointGone.setAction(Constant.ACTION_CONTACT_CHANAGED);
                    broadcastManager.sendBroadcast(intentPointGone);//发广播到主界面红点显示
                    context.sendBroadcast(new Intent(CircleFrg.UPDATE));//发广播到圈子frg红点显示
                    context.sendBroadcast(new Intent(CircleAct.ALLTALKS));
                    context.sendBroadcast(new Intent(SerchCirAct.CIRSERCH));
                    if (!TextUtils.isEmpty(circleInfo.getType())) {
                        if (TextUtils.equals(circleInfo.getType(), 2 + "")) {
                            if (!TextUtils.isEmpty(circleInfo.getMesstype())) {
                                if (TextUtils.equals(circleInfo.getMesstype(), 1 + "")) {
                                    return;
                                }
                            }
                        }
                    }
                    circleInfo.setIsAgree(0);
                    CircleMessDao.saveCircleMess(circleInfo);

                } else if (TextUtils.equals(type, "3")){
                    //吐槽评论，赞
                    Gson gson = new Gson();
                    SquareInfo circleInfo = gson.fromJson(json.toString(), SquareInfo.class);//解析成对象
                    //实例化SharedPreferences对象（第一步）
                    SharedPreferences sharedPreferences = context.getSharedPreferences("sqa_cir",
                            Activity.MODE_PRIVATE);
                    //实例化SharedPreferences.Editor对象（第二步）
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    //用putString的方法保存数据
                    if (!TextUtils.isEmpty(circleInfo.getUserIcon())) {
                        editor.putString("sqa_news", circleInfo.getUserIcon());
                    }
                    int cir_newsCount = sharedPreferences.getInt("sqa_NewsCount", 0);
                    cir_newsCount++;
                    editor.putInt("sqa_NewsCount", cir_newsCount);
                    //提交当前数据
                    editor.commit();
                    SquareMessDao.saveCircleMess(circleInfo);
                    broadcastManager = LocalBroadcastManager.getInstance(context);
                    Intent intentPointGone = new Intent(context, MainAct.class);
                    intentPointGone.setAction(Constant.ACTION_CONTACT_CHANAGED);
                    broadcastManager.sendBroadcast(intentPointGone);//发广播到主界面红点显示
                    context.sendBroadcast(new Intent(CircleFrg.UPDATE));//发广播到圈子frg红点显示
                    context.sendBroadcast(new Intent(SquareAct.TALK_LIST));//发广播到吐槽界面，红点显示
                }else {
                    Gson gson = new Gson();
                    Intent intent2 = new Intent(MessFrg.UPDATE_CONTRACT);
                    broadcastManager = LocalBroadcastManager.getInstance(context);
                    Intent intentPointGone = new Intent(context, MainAct.class);
                    intentPointGone.setAction(Constant.ACTION_CONTACT_CHANAGED);
                    intentPointGone.putExtra("redOut", "redOut");
                    broadcastManager.sendBroadcast(intentPointGone);//发广播到主界面红点显示
                    ActivityMess activityMess = gson.fromJson(json.toString(), ActivityMess.class);
                    activityMess.setIsAgree(0);
                    intent2.putExtra("chat_receiver", 3);//3表示有档期消息进入
                    if (activityMess != null) {
                        if (!TextUtils.isEmpty(activityMess.getMessage())) {
                            intent2.putExtra("chat_message", activityMess.getMessage());
                        }
                    }
                    //实例化SharedPreferences对象（第一步）
                    SharedPreferences sharedPreferences = context.getSharedPreferences("ztb_cir",
                            Activity.MODE_PRIVATE);
                    //实例化SharedPreferences.Editor对象（第二步）
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    //用putString的方法保存数据
                    if (!TextUtils.isEmpty(activityMess.getMessage())) {
                        editor.putString("dq_news", activityMess.getMessage());
                    }
                    //提交当前数据
                    editor.commit();
                    context.sendBroadcast(intent2);
                    if (!TextUtils.isEmpty(activityMess.getType())) {
                        if (TextUtils.equals(activityMess.getType(), 1 + "")) {
                            if (!TextUtils.isEmpty(activityMess.getMesstype())) {
                                if (TextUtils.equals(activityMess.getMesstype(), 1 + "")) {
                                    return;
                                }
                            }
                        }
                    }
                    ActivityMessDao.saveCircleMess(activityMess);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
            String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
            try {
                JSONObject json = new JSONObject(extra);
                String type = json.getString("type");//解析单个
                Intent i = new Intent();
                if (TextUtils.equals(type, "2")) {
                    i.setClass(context, CirMessAct.class);
                } else if (TextUtils.equals(type,"3")) {
                        i.setClass(context, SquareMessAct.class);
                }else {
                        i.setClass(context, DqMesAct.class);
                    }
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
                JPushInterface.clearAllNotifications(context);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();
//					Gson gson = new Gson();
//					CircleInfo circleInfo = gson.fromJson(json.toString(),CircleInfo.class);
                    Log.e(TAG, json.toString());

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    //send msg to MainActivity
//	private void processCustomMessage(Context context, Bundle bundle) {
//		if (MainActivity.isForeground) {
//			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//			Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
//			msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
//			if (!ExampleUtil.isEmpty(extras)) {
//				try {
//					JSONObject extraJson = new JSONObject(extras);
//					if (null != extraJson && extraJson.length() > 0) {
//						msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
//					}
//				} catch (JSONException e) {
//
//				}
//
//			}
//			context.sendBroadcast(msgIntent);
//		}
//	}
}
