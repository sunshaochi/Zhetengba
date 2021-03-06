package com.boyuanitsm.zhetengba.activity.mine;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.boyuanitsm.zhetengba.MyApplication;
import com.boyuanitsm.zhetengba.R;
import com.boyuanitsm.zhetengba.base.BaseActivity;
import com.boyuanitsm.zhetengba.bean.ResultBean;
import com.boyuanitsm.zhetengba.http.IZtbUrl;
import com.boyuanitsm.zhetengba.http.callback.ResultCallback;
import com.boyuanitsm.zhetengba.http.manager.RequestManager;
import com.boyuanitsm.zhetengba.utils.MyToastUtils;
import com.boyuanitsm.zhetengba.utils.SpUtils;
import com.boyuanitsm.zhetengba.utils.ZhetebaUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 忘记密码界面
 * Created by bitch-1 on 2016/4/28.
 */
public class FrogetpwdAct extends BaseActivity {

    @ViewInject(R.id.et_phone)
    private EditText et_phone;

    @ViewInject(R.id.code_tv)
    private TextView tv_code;//验证码按钮

    @ViewInject(R.id.et_pwd)//密码
    private EditText et_pwd;
    @ViewInject(R.id.et_cpwd)
    private EditText et_cpwd;//重复密码
    @ViewInject(R.id.et_yzm)
    private EditText et_yzm;//验证码

    @ViewInject(R.id.et_aqm)
    private EditText et_aqm;//安全码

    @ViewInject(R.id.iv_aqm)
    private ImageView iv_aqm;//图片安全码

    private String phone,yzm,pwd,cpwd,imgcatchurl,aqm;//手机号，验证码，密码，确认密码,图片验证码,安全码
    private ProgressDialog pd;

    private Bitmap bitmap;

    private boolean ispress;


    private int i = 60;
    private Timer timer;
    private MyTimerTask myTask;

    private int type;//用来handle来区分,1表示处理图片验证码，2表示处理倒计时

    private DisplayImageOptions optionsImag = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.mipmap.yzmjiazai)
            .showImageOnFail(R.mipmap.yzmjiazai).cacheInMemory(true).cacheOnDisk(true)
            .considerExifParams(true).imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565).build();
    @Override
    public void setLayout() {
        setContentView(R.layout.act_frogetpwd);

    }

    @Override
    public void init(Bundle savedInstanceState) {
        setTopTitle("忘记密码");
        getImaCatch();//获取图像验证码
        pd=new ProgressDialog(FrogetpwdAct.this);
        pd.setCanceledOnTouchOutside(false);

    }

    /**
     * 获取图片验证码
     */
    private void getImaCatch() {
        new Thread() {
            @Override
            public void run() {
                type = 1;
                super.run();
                try {
                    URL url = new URL(IZtbUrl.FINDIMGCAPTCHA_URL);
                    //打开URL对应的资源输入流
                    HttpURLConnection conn = null;
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setRequestMethod("POST");
                    conn.connect();
                    String cookie = conn.getHeaderField("set-cookie");
                    InputStream in = conn.getInputStream();
                    if (!TextUtils.isEmpty(cookie)){
                        SpUtils.setCooike(MyApplication.getInstance(), cookie);
                    }
                    //从InputStream流中解析出图片
                    bitmap = BitmapFactory.decodeStream(in);
                    //  imageview.setImageBitmap(bitmap);
                    //发送消息，通知UI组件显示图片\
                    Message msg = Message.obtain();
                    msg.what = 0x9527;
//                    handleryzm.sendMessage(msg);
                    handler.sendMessage(msg);
                    //关闭输入流
                    in.close();
                } catch (Exception e) {
                    iv_aqm.setImageResource(R.mipmap.yzmjiazai);
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @OnClick({R.id.code_tv,R.id.tv_frogettj,R.id.iv_aqm})
    public void OnClick(View v){
        switch (v.getId()){
            case R.id.code_tv://获取验证码
                phone=et_phone.getText().toString().trim();
                aqm=et_aqm.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    MyToastUtils.showShortToast(getApplicationContext(), "请输入手机号");
                    return;
                }
                if (phone.length() != 11) {
                    MyToastUtils.showShortToast(getApplicationContext(), "请输入11的手机号");
                    et_phone.requestFocus();
                    et_phone.setSelection(et_phone.length());
                    return;
                }
                if(!ZhetebaUtils.checkCellPhone(phone)){
                    MyToastUtils.showShortToast(getApplicationContext(), "请输入正确的手机号码");
                    return;
                }
//                if(TextUtils.isEmpty(aqm)){
//                    MyToastUtils.showShortToast(getApplicationContext(), "请输入安全码");
//                    et_aqm.requestFocus();
//                    return;
//                }

                   sendSms(phone, "false");
                break;


            case R.id.tv_frogettj://提交
                 if(isValidate()){
                     pd.setMessage("提交中...");
                     pd.show();
                     frogetpwd(phone,yzm, pwd);
                 }

                break;
            case R.id.iv_aqm:
                getImaCatch();//获取图像验证码
                break;
        }
    }

    /**
     * 是否可用
     *
     * @return
     */
    private boolean isValidate() {
        phone = et_phone.getText().toString().trim();
        yzm = et_yzm.getText().toString().trim();
        pwd = et_pwd.getText().toString();//.trim();
        cpwd=et_cpwd.getText().toString();//.trim();
        aqm=et_aqm.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            MyToastUtils.showShortToast(getApplicationContext(), "请输入手机号");
            et_phone.requestFocus();
            return false;
        }
        if(phone.length()!=11){
            MyToastUtils.showShortToast(getApplicationContext(), "请输入11位的手机号");
            et_phone.requestFocus();
            et_phone.setSelection(et_phone.length());
            return false;
        }
        if(!ZhetebaUtils.checkCellPhone(phone)){
            MyToastUtils.showShortToast(getApplicationContext(), "请输入正确的手机号码");
            return false;
        }

        if (TextUtils.isEmpty(yzm)) {
            MyToastUtils.showShortToast(getApplicationContext(), "请输入验证码");
            et_yzm.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(pwd)) {
            MyToastUtils.showShortToast(getApplicationContext(), "请输入密码");
            et_pwd.requestFocus();
            return false;
        }
        if(pwd.length()<4){
            MyToastUtils.showShortToast(getApplicationContext(),"请输入至少4位密码");
            et_pwd.requestFocus();
            et_pwd.setSelection(pwd.length());
            return false;
        }
        if(pwd.length()>24){
            MyToastUtils.showShortToast(getApplicationContext(), "请输入不超过24位密码");
            et_pwd.requestFocus();
            et_pwd.setSelection(pwd.length());
            return false;
        }
        if(TextUtils.isEmpty(cpwd)){
            MyToastUtils.showShortToast(getApplicationContext(), "请确认密码");
            et_cpwd.requestFocus();
            return false;
        }

        if(!(cpwd.equals(pwd))){
            MyToastUtils.showShortToast(getApplicationContext(), "确认密码输入错误");
            et_cpwd.requestFocus();
            return false;
        }

//        if(!ZhetebaUtils.checkPwd(pwd)){
//            MyToastUtils.showShortToast(getApplicationContext(), "请输入4-24位字母和数字");
//            return false;
//        }
//        if (TextUtils.isEmpty(aqm)) {
//            MyToastUtils.showShortToast(getApplicationContext(), "请输入安全码");
//            et_aqm.requestFocus();
//            return false;
//        }
        return true;
    }

    /**
     * 倒计时
     *
     * @author wangbin
     *
     */
    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            type=2;
            handler.sendEmptyMessage(i--);
        }

    }

    /**
     * 这个用来处理倒计时的handler;
     */
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(type==1){
                if (msg.what==0x9527) {
                    //显示从网上下载的图片
                    iv_aqm.setImageBitmap(bitmap);
                }
            }
            if(type==2) {
                if (msg.what == 0 || msg.what < 0) {
                    tv_code.setEnabled(true);
                    tv_code.setText("重新发送");
                    timer.cancel();
                    myTask.cancel();
                } else {
                    tv_code.setText(msg.what + "秒");
                }
            }
        }

    };

    /**
     * 这个用来处理图片验证码的handler
     */
//    private Handler handleryzm=new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if (msg.what==0x9527) {
//                //显示从网上下载的图片
//                iv_aqm.setImageBitmap(bitmap);
//            }
//        }
//    };


    /**
     * 忘记密码
     * @param captcha
     * @param newPassword
     */
    public void frogetpwd(String phoneNumber,String captcha,String newPassword){
        RequestManager.getUserManager().forgetPassword(phoneNumber,captcha, newPassword, new ResultCallback<ResultBean<String>>() {
            @Override
            public void onError(int status, String errorMsg) {
                MyToastUtils.showShortToast(FrogetpwdAct.this,errorMsg);
                pd.dismiss();

            }

            @Override
            public void onResponse(ResultBean<String> response) {
                pd.dismiss();
                MyToastUtils.showShortToast(FrogetpwdAct.this,"密码修改成功，请重新登录！");
                openActivity(LoginAct.class);
            }
        });

    }


    /**
     * 发送验证码接口
     * @param phoneNumber
     * @param isRegister
     */
    public void sendSms(String phoneNumber,String isRegister){
        tv_code.setEnabled(false);
        RequestManager.getUserManager().getSms2(phoneNumber, isRegister, new ResultCallback<ResultBean<String>>() {
            @Override
            public void onError(int status, String errorMsg) {
                tv_code.setEnabled(true);
                MyToastUtils.showShortToast(getApplicationContext(), errorMsg);
            }

            @Override
            public void onResponse(ResultBean<String> response) {
                i = 60;
                timer = new Timer();
                myTask = new MyTimerTask();
                timer.schedule(myTask, 0, 1000);
                MyToastUtils.showShortToast(getApplicationContext(), "验证码发送成功");

            }
        });


    }


}
