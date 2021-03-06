package com.boyuanitsm.zhetengba.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.boyuanitsm.zhetengba.AppManager;
import com.boyuanitsm.zhetengba.Constant;
import com.boyuanitsm.zhetengba.R;
import com.boyuanitsm.zhetengba.activity.mine.LoginAct;
import com.boyuanitsm.zhetengba.activity.publish.ContractedAct;
import com.boyuanitsm.zhetengba.base.BaseActivity;
import com.boyuanitsm.zhetengba.bean.ChatUserBean;
import com.boyuanitsm.zhetengba.bean.NewCircleMess;
import com.boyuanitsm.zhetengba.bean.ResultBean;
import com.boyuanitsm.zhetengba.bean.UserInfo;
import com.boyuanitsm.zhetengba.chat.DemoHelper;
import com.boyuanitsm.zhetengba.chat.act.ChatActivity;
import com.boyuanitsm.zhetengba.chat.db.InviteMessgeDao;
import com.boyuanitsm.zhetengba.chat.db.UserDao;
import com.boyuanitsm.zhetengba.chat.domain.InviteMessage;
import com.boyuanitsm.zhetengba.db.ActivityMessDao;
import com.boyuanitsm.zhetengba.db.ChatUserDao;
import com.boyuanitsm.zhetengba.db.CircleNewMessDao;
import com.boyuanitsm.zhetengba.db.LabelInterestDao;
import com.boyuanitsm.zhetengba.db.UserInfoDao;
import com.boyuanitsm.zhetengba.fragment.MessFrg;
import com.boyuanitsm.zhetengba.fragment.MineFrg;
import com.boyuanitsm.zhetengba.fragment.calendarFrg.SimpleFrg;
import com.boyuanitsm.zhetengba.fragment.circleFrg.CircleFrg;
import com.boyuanitsm.zhetengba.http.callback.ResultCallback;
import com.boyuanitsm.zhetengba.http.manager.RequestManager;
import com.boyuanitsm.zhetengba.utils.ACache;
import com.boyuanitsm.zhetengba.utils.GeneralUtils;
import com.boyuanitsm.zhetengba.utils.MyLogUtils;
import com.boyuanitsm.zhetengba.utils.MyToastUtils;
import com.boyuanitsm.zhetengba.utils.SpUtils;
import com.boyuanitsm.zhetengba.utils.Uitls;
import com.boyuanitsm.zhetengba.utils.ZtinfoUtils;
import com.boyuanitsm.zhetengba.view.MyRadioButton;
import com.boyuanitsm.zhetengba.view.TipsDrawDialog;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.EMLog;

import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/***
 * 设置首页信息
 */
public class MainAct extends BaseActivity {

    /**
     * 定位SDK的核心类
     */
    public LocationClient mLocClient;
    private String locationCity;//当前城市
    private String citycode;//当前城市编码传后台

    private FragmentManager fragmentManager;
    private SimpleFrg calendarFrg;
    private CircleFrg circleFrg;
    private MessFrg messFrg;
//    private PlaneDialog planeDialog;
    private MyRadioButton rb_mes;
    //    // 未读消息textview
    private TextView unreadLabel;
    private TextView msg_qunzi;//圈子红点

    private UserInfo user;//定位成功后用来给用户设置位置添加这个的时候只是为了用于定位

    protected static final String TAG = "MainActivity";
    // 当前fragment的index
    private int currentTabIndex;
    // 账号在别处登录
    public boolean isConflict = false;
    // 账号被移除
    private boolean isCurrentAccountRemoved = false;
    private MineFrg mineFrg;
    private RelativeLayout rl_ydy;
    private String labelId;//频道标签id
    private boolean isFirst;
    public String getLabelId() {
        return labelId;
    }

    public void setLabelId(String labelId) {
        this.labelId = labelId;
    }

    /**
     * 检查当前用户是否被删除
     */
    public boolean getCurrentAccountRemoved() {
        return isCurrentAccountRemoved;
    }

    /**
     * 按两次退出键时间小于2秒退出
     */
    private final static long WAITTIME = 2000;
    private long touchTime = 0;


    private InviteMessgeDao inviteMessgeDao;
    private UserDao userDao;
    private ACache aCache;
    private int version;
    GeneralUtils generalUtils;
    @Override
    public void setLayout() {

    }

    @Override
    public void init(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        if (savedInstanceState != null && savedInstanceState.getBoolean(Constant.ACCOUNT_REMOVED, false)) {
            // 防止被移除后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
            // 三个fragment里加的判断同理
            DemoHelper.getInstance().logout(false, null);
            finish();
            startActivity(new Intent(this, LoginAct.class));
            return;
        } else if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false)) {
            // 防止被T后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
            // 三个fragment里加的判断同理
            finish();
            startActivity(new Intent(this, LoginAct.class));
            return;
        }
        setContentView(R.layout.act_main_layout);
        AppManager.getAppManager().addActivity(this);
        user=UserInfoDao.getUser();
        position();//定位获取城市
        rb_mes = (MyRadioButton) findViewById(R.id.rb_mes);
        unreadLabel = (TextView) findViewById(R.id.unread_msg_number);
        msg_qunzi= (TextView) findViewById(R.id.msg_qunzi);
        rl_ydy = (RelativeLayout) findViewById(R.id.rl_ydy);
        aCache=ACache.get(MainAct.this);
        //获取frg的管理器
        fragmentManager = getSupportFragmentManager();
        //获取事物
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //默认展示“简约”界面
        defaultShow(transaction);
        //设置RadioButton的点击事件
        RadioGroup rg_button = (RadioGroup) findViewById(R.id.rg_button);
        RadioButton rb_cal = (RadioButton) findViewById(R.id.rb_cal);
        ImageView iv_plane = (ImageView) findViewById(R.id.iv_plane);
//        planeDialog = new PlaneDialog(this);
//        planeDialog.getWindow().setWindowAnimations(R.style.ActionSheetDialogAnimation);
        rb_cal.setChecked(true);
        iv_plane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入活动界面
                Intent intent=new Intent();
                intent.setClass(MainAct.this, ContractedAct.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
//                dismiss();
//                planeDialog.show();
            }
        });
        rg_button.setOnCheckedChangeListener(new OnRadiGrroupCheckedChangeListener());
        inviteMessgeDao = new InviteMessgeDao(this);
        userDao = new UserDao(this);
        //注册local广播接收者，用于接收demohelper中发出的群组联系人的变动通知
        registerBroadcastReceiver();
        EMClient.getInstance().contactManager().setContactListener(new MyContactListener());
        isFirst = SpUtils.getMainIsFirst(MainAct.this);
//        MyLogUtils.info(isFirst + "是否是第一次打开应用");
//        if (isFirst){
//            rl_ydy.setVisibility(View.VISIBLE);
//            SpUtils.setMainIsFirst(MainAct.this,false);
//        }else {
//            rl_ydy.setVisibility(View.GONE);
//        }
//        rl_ydy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                rl_ydy.setVisibility(View.GONE);
//            }
//        });
        version= ZtinfoUtils.getAppVer(MainAct.this);
        generalUtils=new GeneralUtils();
        generalUtils.toVersion(MainAct.this,version,1);
    }

    /**
     * 定位
     */
    private void position() {
        // 实例化定位服务，LocationClient类必须在主线程中声明
        mLocClient = new LocationClient(getApplicationContext());
        mLocClient.registerLocationListener(new BDLocationListenerImpl());// 注册定位监听接口

        /**
         * LocationClientOption 该类用来设置定位SDK的定位方式。
         */
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开GPRS
        option.setAddrType("all");// 返回的定位结果包含地址信息
        option.setCoorType("gcj02");// 返回的定位结果是百度经纬度,默认值gcj02
        option.setPriority(LocationClientOption.GpsFirst); // 设置GPS优先
        option.setScanSpan(0); // 设置发起定位请求的间隔时间为5000ms
        option.disableCache(true);// 禁止启用缓存定位
        mLocClient.setLocOption(option); // 设置定位参数
        mLocClient.start();
    }


    /***
     * 首页默认加载节约界面
     */
    private void defaultShow(FragmentTransaction transaction) {
        hideFragment(transaction);
        calendarFrg= (SimpleFrg) fragmentManager.findFragmentByTag("calendarFrg");
        if (calendarFrg == null) {
            calendarFrg = new SimpleFrg();
            transaction.add(R.id.fl_main, calendarFrg,"calendarFrg");
        } else {
            transaction.show(calendarFrg);
        }
        transaction.commit();

    }

    /***
     * 隐藏所有页面
     *
     * @param transaction
     */
    private void hideFragment(FragmentTransaction transaction) {
        if (calendarFrg != null) {
            transaction.hide(calendarFrg);
        }
        if (messFrg != null) {
            transaction.hide(messFrg);
        }
        if (circleFrg != null) {
            transaction.hide(circleFrg);
        }
        if (mineFrg != null) {
            transaction.hide(mineFrg);
        }

    }

    /***
     * radiobutton点击事件
     */

    private class OnRadiGrroupCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            hideFragment(transaction);
            switch (group.getCheckedRadioButtonId()) {
                case R.id.rb_cal://点击档期显示：简约/档期界面
                    currentTabIndex = 0;
                    calendarFrg= (SimpleFrg) fragmentManager.findFragmentByTag("calendarFrg");
                    if (calendarFrg == null||calendarFrg.isDetached()) {
                        calendarFrg =new SimpleFrg();
                        transaction.add(R.id.fl_main, calendarFrg,"calendarFrg");
                    } else {
                        transaction.show(calendarFrg);
                    }
                    break;
                case R.id.rb_mes://点击显示：消息界面
                    currentTabIndex = 1;
                    messFrg= (MessFrg) fragmentManager.findFragmentByTag("messFrg");
                    if (messFrg == null) {
                        messFrg = new MessFrg();
                        transaction.add(R.id.fl_main, messFrg,"messFrg");
                    } else {
                        transaction.show(messFrg);
                    }
                    break;
                case R.id.rb_cir://点击显示：圈子界面
                    currentTabIndex = 2;
                    circleFrg= (CircleFrg) fragmentManager.findFragmentByTag("circleFrg");
                    if (circleFrg == null) {
                        circleFrg = new CircleFrg();
                        transaction.add(R.id.fl_main, circleFrg,"circleFrg");
                    } else {
                        transaction.show(circleFrg);
                    }
                    if (CircleNewMessDao.getUser()!=null){
                        NewCircleMess circleMess = CircleNewMessDao.getUser();
                        if (circleMess.isMain()==false){
                            msg_qunzi.setVisibility(View.GONE);
                            circleMess.setIsMain(true);
                            CircleNewMessDao.updateMess(circleMess);
                        }else if (circleMess.isMain()==true){
                            msg_qunzi.setVisibility(View.GONE);
                        }
                    }else {
                        msg_qunzi.setVisibility(View.GONE);
                    }
                    break;
                case R.id.rb_my://点击显示：我的界面
                    currentTabIndex = 3;
                    mineFrg= (MineFrg) fragmentManager.findFragmentByTag("mineFrg");
                    if (mineFrg == null) {
                        mineFrg = new MineFrg();
                        transaction.add(R.id.fl_main, mineFrg,"mineFrg");
                    } else {
                        sendBroadcast(new Intent(MineFrg.USER_INFO));
                        transaction.show(mineFrg);
                    }
                    break;
            }
            transaction.commit();
        }
    }


    EMMessageListener messageListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
//            Map<String, EaseUser> contactList = DemoHelper.getInstance().getContactList();
//            Iterator<Map.Entry<String, EaseUser>> iterator = contactList.entrySet().iterator();
//            List<EaseUser> easeUserList=new ArrayList<>();
//            while (iterator.hasNext()) {
//                Map.Entry<String, EaseUser> entry = iterator.next();
//                if (!entry.getKey().equals(Constant.NEW_FRIENDS_USERNAME) && !entry.getKey().equals(Constant.GROUP_USERNAME) && !entry.getKey().equals(Constant.CHAT_ROOM) && !entry.getKey().equals(Constant.CHAT_ROBOT)) {
//                    EaseUser easeUser = entry.getValue();
//                    easeUserList.add(easeUser);
//                }
//            }
            // 提示新消息
            for (EMMessage message : messages) {
                DemoHelper.getInstance().getNotifier().onNewMsg(message);
                ChatUserBean chatUserBean = new ChatUserBean();
                chatUserBean.setUserId(message.getFrom());
                MyLogUtils.info("这个人发送消息来了："+message.getFrom());
                try {
//                    for (int i=0;i<easeUserList.size();i++){
                        EaseUser easeUser = DemoHelper.getInstance().getContactList().get(message.getFrom());
//                    MyLogUtils.info(easeUser.toString()+"easeUser是。。。。。");
                        if (easeUser!=null&&easeUser.getNick().length()!=32){
                            chatUserBean.setNick(easeUser.getNick());
//                            if (!TextUtils.isEmpty(easeUser.getNick())){
//                                MyLogUtils.info("nick不是空的========"+easeUser.getNick());
//                            }
                        }else {
                            chatUserBean.setNick(message.getStringAttribute("nick"));
                        }
//                    }
//                    message.getStringAttribute("userName");
//                    chatUserBean.setNick(message.getStringAttribute("nick"));
                    String userIcon=message.getStringAttribute("icon");
                    if (userIcon.startsWith("http")){
                        chatUserBean.setIcon(message.getStringAttribute("icon"));
                    }else {
                        chatUserBean.setIcon(Uitls.imageFullUrl(userIcon));
                    }
                    MyLogUtils.info("这个头像MainAct："+message.getStringAttribute("nick")+"++昵称："+message.getStringAttribute("icon"));
//                    MyToastUtils.showShortToast(getApplication(),message.getStringAttribute("nick")+":"+message.getStringAttribute("icon"));
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
                MyLogUtils.info("昵称发过来===="+chatUserBean.getNick());
                ChatUserDao.saveUser(chatUserBean);
            }
            refreshUIWithMessage();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
        }

        @Override
        public void onMessageReadAckReceived(List<EMMessage> messages) {
        }

        @Override
        public void onMessageDeliveryAckReceived(List<EMMessage> message) {
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
        }
    };

    private void refreshUIWithMessage() {
        runOnUiThread(new Runnable() {
            public void run() {
                // 刷新bottom bar消息未读数
                updateUnreadLabel();
                if (currentTabIndex == 1) {
                    // 当前页面如果为聊天历史页面，刷新此页面
                    if (messFrg != null) {
                        messFrg.refresh();
                    }
                }
            }
        });
    }


    private void registerBroadcastReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_CONTACT_CHANAGED);
        intentFilter.addAction(Constant.ACTION_GROUP_CHANAGED);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                MyLogUtils.info("删除群聊广播");
                int chat_receiver = intent.getIntExtra("main_receiver", 5);
                String redOut=intent.getStringExtra("redOut");
                String flag=intent.getStringExtra("flag");//这是判断是否是第一次发布活动
                if(!TextUtils.isEmpty(flag)&&flag.equals("2")){
                    TipsDrawDialog tipsDrawDialog=new TipsDrawDialog(MainAct.this).builder();
                    tipsDrawDialog.show();
                    tipsDrawDialog.setCanceledOnTouchOutside(true);
                }
                SharedPreferences sharedPreferences=getSharedPreferences("ztb_cirNews",
                        Activity.MODE_PRIVATE);
                String cir_news = sharedPreferences.getString("cir_news", "");
                int cir_newsCount = sharedPreferences.getInt("cir_NewsCount", 0);
                SharedPreferences sharedPreferences2=getSharedPreferences("sqa_cir",
                        Activity.MODE_PRIVATE);
                int sqa_newsCount = sharedPreferences2.getInt("sqa_NewsCount", 0);
                int newcount=cir_newsCount+sqa_newsCount;
                if (newcount==0){
                    msg_qunzi.setVisibility(View.GONE);
                }else {
                    msg_qunzi.setVisibility(View.VISIBLE);
                    msg_qunzi.setText(newcount+"");
                }
//                if (CircleNewMessDao.getUser()!=null){
//                    NewCircleMess newCircleMess = CircleNewMessDao.getUser();
//                    if (newCircleMess.isMain()==false){
//                        msg_qunzi.setVisibility(View.VISIBLE);
////                        newCircleMess.setIsMain(true);
////                        CircleNewMessDao.updateMess(newCircleMess);
//                    }else if (newCircleMess.isMain()==true){
//                        msg_qunzi.setVisibility(View.GONE);
//                    }
//                }else {
//                    msg_qunzi.setVisibility(View.GONE);
//                }
                 updateUnreadLabel();
                if (TextUtils.equals(redOut,"redOut")){
                    unreadLabel.setVisibility(View.VISIBLE);
                }
                    if (currentTabIndex == 1) {
                        // 当前页面如果为聊天历史页面，刷新此页面
                        if (messFrg != null) {
                            messFrg.refresh();
                        }
                    }
                String action = intent.getAction();
                //刷新群列表
            }
        };
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }


    public class MyContactListener implements EMContactListener {
        @Override
        public void onContactAdded(String username) {
        }

        @Override
        public void onContactDeleted(final String username) {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (ChatActivity.activityInstance != null && ChatActivity.activityInstance.toChatUsername != null &&
                            username.equals(ChatActivity.activityInstance.toChatUsername)) {
                        String st10 = getResources().getString(R.string.have_you_removed);
                        if(EaseUserUtils.getUserInfo(ChatActivity.activityInstance.getToChatUsername())!=null)
                        Toast.makeText(MainAct.this, EaseUserUtils.getUserInfo(ChatActivity.activityInstance.getToChatUsername()).getNick()+st10,Toast.LENGTH_SHORT).show();
                        else
                            MyToastUtils.showShortToast(getApplicationContext(),"对方"+st10);
                        ChatActivity.activityInstance.finish();
                    }
                }
            });
        }

        @Override
        public void onContactInvited(String username, String reason) {
        }

        @Override
        public void onContactAgreed(String username) {
        }

        @Override
        public void onContactRefused(String username) {
        }
    }

    private void unregisterBroadcastReceiver() {
        if (broadcastReceiver!=null){
            broadcastManager.unregisterReceiver(broadcastReceiver);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (conflictBuilder != null) {
            conflictBuilder.create().dismiss();
            conflictBuilder = null;
        }
        unregisterBroadcastReceiver();

        try {
            unregisterReceiver(internalDebugReceiver);
        } catch (Exception e) {
        }

    }

    /**
     * 刷新未读消息数
     */
    public void updateUnreadLabel() {
        int count = getUnreadMsgCountTotal();
        int count2 = getUnreadAddressCountTotal();
        count=count2+count;
        if (count > 0) {
//            unreadLabel.setText(String.valueOf(count));
            unreadLabel.setVisibility(View.VISIBLE);
        } else {
            unreadLabel.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 刷新申请与通知消息数
     */
    public void updateUnreadAddressLable() {
        runOnUiThread(new Runnable() {
            public void run() {
                int count = getUnreadAddressCountTotal();
//                InviteMessgeDao inviteMessgeDao=new InviteMessgeDao(MainAct.this);
//                if (inviteMessgeDao.getUnreadMessagesCount()>0){
//                    sendBroadcast(new Intent(MessFrg.UPDATE_CONTRACT));
//                }
                if (count > 0) {
//					unreadAddressLable.setText(String.valueOf(count));
                    unreadLabel.setVisibility(View.VISIBLE);
                } else {
                    unreadLabel.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    /**
     * 获取未读申请与通知消息
     *
     * @return
     */
    public int getUnreadAddressCountTotal() {
        int unreadAddressCountTotal = 0;
        unreadAddressCountTotal = inviteMessgeDao.getUnreadMessagesCount();
        return unreadAddressCountTotal;
    }

    /**
     * 获取未读消息数
     *
     * @return
     */
    public int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal = 0;
        int chatroomUnreadMsgCount = 0;
        unreadMsgCountTotal = EMClient.getInstance().chatManager().getUnreadMsgsCount();
        if (EMClient.getInstance().chatManager().getAllConversations().size() == 0) {
            return 0;
        } else {
            for (EMConversation conversation : EMClient.getInstance().chatManager().getAllConversations().values()) {
                if (conversation.getType() == EMConversation.EMConversationType.ChatRoom)
                    chatroomUnreadMsgCount = chatroomUnreadMsgCount + conversation.getUnreadMsgCount();
            }
            return unreadMsgCountTotal - chatroomUnreadMsgCount;

        }

    }

    /**
     * 保存提示新消息
     *
     * @param msg
     */
//    private void notifyNewIviteMessage(InviteMessage msg) {
//        saveInviteMsg(msg);
//        // 提示有新消息
//        DemoHelper.getInstance().getNotifier().viberateAndPlayTone(null);
//
//        // 刷新bottom bar消息未读数
//        updateUnreadAddressLable();
//        // 刷新好友页面ui
//        if (currentTabIndex == 1)
//            contactListFragment.refresh();
//    }

    /**
     * 保存邀请等msg
     *
     * @param msg
     */
    private void saveInviteMsg(InviteMessage msg) {
        // 保存msg
        inviteMessgeDao.saveMessage(msg);
        //保存未读数，这里没有精确计算
        inviteMessgeDao.saveUnreadMessageCount(1);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!isConflict && !isCurrentAccountRemoved) {
            updateUnreadLabel();
//            updateUnreadAddressLable();
        }
        if (CircleNewMessDao.getUser()!=null){
            NewCircleMess newCircleMess = CircleNewMessDao.getUser();
            if (newCircleMess.isMain()==false){
                msg_qunzi.setVisibility(View.VISIBLE);
//                newCircleMess.setIsMain(true);
//                CircleNewMessDao.updateMess(newCircleMess);
            }else if (newCircleMess.isMain()==true){
                msg_qunzi.setVisibility(View.GONE);
            }
        }else {
            msg_qunzi.setVisibility(View.GONE);
        }
        // unregister this event listener when this activity enters the
        // background
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.pushActivity(this);

        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    @Override
    protected void onStop() {
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.popActivity(this);

        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isConflict", isConflict);
        outState.putBoolean(Constant.ACCOUNT_REMOVED, isCurrentAccountRemoved);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - touchTime) >= WAITTIME) {
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            touchTime = currentTime;
        } else {
            moveTaskToBack(false);
        }
    }


    private android.app.AlertDialog.Builder conflictBuilder;
    private android.app.AlertDialog.Builder accountRemovedBuilder;
    private boolean isConflictDialogShow;
    private boolean isAccountRemovedDialogShow;
    private BroadcastReceiver internalDebugReceiver;
    private BroadcastReceiver broadcastReceiver;
    private LocalBroadcastManager broadcastManager;

    /**
     * 显示帐号在别处登录dialog
     */
    private void showConflictDialog() {
        isConflictDialogShow = true;
        DemoHelper.getInstance().logout(false, null);
        String st = getResources().getString(R.string.Logoff_notification);
        if (!MainAct.this.isFinishing()) {
            // clear up global variables
            try {
                if (conflictBuilder == null)
                    conflictBuilder = new android.app.AlertDialog.Builder(MainAct.this);
                conflictBuilder.setTitle(st);
                conflictBuilder.setMessage(R.string.connect_conflict);
                conflictBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserInfoDao.deleteUser();
                        ActivityMessDao.delAll();
                        LabelInterestDao.delAll();
                        CircleNewMessDao.deleteUser();
                        aCache.clear();
                        dialog.dismiss();
                        conflictBuilder = null;
                        finish();
                        Intent intent = new Intent(MainAct.this, LoginAct.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
                conflictBuilder.setCancelable(false);
                conflictBuilder.create().show();
                isConflict = true;
            } catch (Exception e) {
                EMLog.e(TAG, "---------color conflictBuilder error" + e.getMessage());
            }

        }

    }


    /**
     * 帐号被移除的dialog
     */
    private void showAccountRemovedDialog() {
        isAccountRemovedDialogShow = true;
        DemoHelper.getInstance().logout(false, null);
        String st5 = getResources().getString(R.string.Remove_the_notification);
        if (!MainAct.this.isFinishing()) {
            // clear up global variables
            try {
                if (accountRemovedBuilder == null)
                    accountRemovedBuilder = new android.app.AlertDialog.Builder(MainAct.this);
                accountRemovedBuilder.setTitle(st5);
                accountRemovedBuilder.setMessage(R.string.em_user_remove);
                accountRemovedBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        accountRemovedBuilder = null;
                        JPushInterface.setAlias(getApplicationContext(), "", new TagAliasCallback() {
                            @Override
                            public void gotResult(int i, String s, Set<String> set) {

                            }
                        });
                        finish();
                        startActivity(new Intent(MainAct.this, LoginAct.class));
                    }
                });
                accountRemovedBuilder.setCancelable(false);
                accountRemovedBuilder.create().show();
                isCurrentAccountRemoved = true;
            } catch (Exception e) {
                EMLog.e(TAG, "---------color userRemovedBuilder error" + e.getMessage());
            }

        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getBooleanExtra(Constant.ACCOUNT_CONFLICT, false) && !isConflictDialogShow) {
            JPushInterface.setAlias(getApplicationContext(), "", new TagAliasCallback() {
                @Override
                public void gotResult(int i, String s, Set<String> set) {

                }
            });
            showConflictDialog();
        } else if (intent.getBooleanExtra(Constant.ACCOUNT_REMOVED, false) && !isAccountRemovedDialogShow) {
            showAccountRemovedDialog();
        }
    }


    /**
     * 百度定位回调
     */
    public class BDLocationListenerImpl implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation == null) {
                return;
            }
//            GlobalParams.m_latitude = bdLocation.getLatitude();
//            GlobalParams.m_longitude = bdLocation.getLongitude();
            locationCity = bdLocation.getCity();//获取定位城市
            citycode=bdLocation.getCityCode();//城市编码 289上海

            if(!TextUtils.isEmpty(locationCity)&&!TextUtils.isEmpty(locationCity)){

                //实例化SharedPreferences对象（第一步）
                SharedPreferences sharedPreferences = getSharedPreferences("ztb_City",
                        Activity.MODE_PRIVATE);
                //实例化SharedPreferences.Editor对象（第二步）
                SharedPreferences.Editor editor = sharedPreferences.edit();
                //用putString的方法保存数据
                    editor.putString("city_location", locationCity);
                editor.putString("cityCode",citycode);
                //提交当前数据
                editor.commit();
                user.setCity(citycode);//保存城市编码
                modifyUser(user);//定位成功把定位城市通过这种接口传给后台

            }else {
                user.setCity("289");
                modifyUser(user);
            }

            mLocClient.stop();
        }

        }

    /**
     * 修改个人资料
     * @param user
     */
    private void modifyUser(final UserInfo user) {
        RequestManager.getUserManager().modifyUserInfo(user, new ResultCallback<ResultBean<String>>() {
            @Override
            public void onError(int status, String errorMsg) {

            }

            @Override
            public void onResponse(ResultBean<String> response) {
                UserInfoDao.updateUser(user);
                Intent intent=new Intent();
                //设置Action
                intent.setAction(SimpleFrg.DATA_CHANGE_KEY);
                //携带数据
                sendBroadcast(intent);
            }
        });
    }
}

