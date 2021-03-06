package com.boyuanitsm.zhetengba.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.boyuanitsm.zhetengba.R;
import com.boyuanitsm.zhetengba.activity.mine.LoginAct;
import com.boyuanitsm.zhetengba.activity.mine.LoginFirstAct;
import com.boyuanitsm.zhetengba.activity.mine.RegInfoAct;
import com.boyuanitsm.zhetengba.base.BaseActivity;
import com.boyuanitsm.zhetengba.bean.ResultBean;
import com.boyuanitsm.zhetengba.bean.UserBean;
import com.boyuanitsm.zhetengba.chat.DemoHelper;
import com.boyuanitsm.zhetengba.db.UserInfoDao;
import com.boyuanitsm.zhetengba.http.callback.ResultCallback;
import com.boyuanitsm.zhetengba.http.manager.RequestManager;
import com.boyuanitsm.zhetengba.utils.MyLogUtils;
import com.boyuanitsm.zhetengba.utils.MyToastUtils;
import com.boyuanitsm.zhetengba.utils.SpUtils;
import com.boyuanitsm.zhetengba.utils.ZhetebaUtils;
import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by wangbin on 16/5/10.
 */
public class SplashAct extends BaseActivity {

    private static final int sleepTime = 2000;

    @Override
    public void setLayout() {
        setContentView(R.layout.act_splash);
    }

    @Override
    public void init(Bundle savedInstanceState) {
//       new Handler().postDelayed(new Runnable() {
//           @Override
//           public void run() {
//               openActivity(LoginAct.class);
//           }
//       }, 0);
        SpUtils.setScreenWith(getApplicationContext(), ZhetebaUtils.getScreenWidth(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        new Thread(new Runnable() {
            public void run() {
                if (DemoHelper.getInstance().isLoggedIn()) {
                    // ** 免登陆情况 加载所有本地群和会话
                    //不是必须的，不加sdk也会自动异步去加载(不会重复加载)；
                    //加上的话保证进了主页面会话和群组都已经load完毕
                    long start = System.currentTimeMillis();
                    EMClient.getInstance().groupManager().loadAllGroups();
                    EMClient.getInstance().chatManager().loadAllConversations();
                    long costTime = System.currentTimeMillis() - start;
                    //等待sleeptime时长
//                    if (sleepTime - costTime > 0) {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
//                    }
                    //如果昵称为空说明用户没有走完善信息界面，所以进入app时不能进入主界面要进入完善信息界面
                    if (UserInfoDao.getUser() != null) {
                        if (TextUtils.isEmpty(UserInfoDao.getUser().getSex()) || UserInfoDao.getUser().getSex() == null) {
                            startActivity(new Intent(SplashAct.this, RegInfoAct.class));
                            finish();
                        } else {
                            //进入主页面
                            startActivity(new Intent(SplashAct.this, MainAct.class));
                            //同样，在读取SharedPreferences数据前要实例化出一个SharedPreferences对象
                            SharedPreferences sharedPreferences= getSharedPreferences("ztb",
                                    Activity.MODE_PRIVATE);
                            // 使用getString方法获得value，注意第2个参数是value的默认值
                            String userName =sharedPreferences.getString("userName","");
                            String passWord =sharedPreferences.getString("passWord", "");
                            if (!TextUtils.isEmpty(userName)&&!TextUtils.isEmpty(passWord)){
                                toLogin(userName,passWord);
                            }
                            finish();
                        }
                    }

                } else {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                    }
                    boolean isFirst = SpUtils.getIsFirst(SplashAct.this);
                    Intent intent = null;
                    if (isFirst) {
                        // 第一次进入应用
//                        intent = new Intent(SplashAct.this, GuideAct.class);
                        intent=new Intent(SplashAct.this,LoginFirstAct.class);
                        SpUtils.setIsFirst(SplashAct.this, false);
                    } else {
                        // 主页面
                        intent = new Intent(SplashAct.this, LoginFirstAct.class);
                    }
                    startActivity(intent);
                    finish();
//                    startActivity(new Intent(SplashAct.this, GuideAct.class));
//                    finish();
                }
            }
        }).start();

    }
    private void toLogin(final String username, String password) {
        RequestManager.getUserManager().toLogin(username, password, new ResultCallback<ResultBean<UserBean>>() {
            @Override
            public void onError(int status, String errorMsg) {
            }

            @Override
            public void onResponse(ResultBean<UserBean> response) {
                MyLogUtils.info("二次登录成功！");
            }
        });
    }
}
