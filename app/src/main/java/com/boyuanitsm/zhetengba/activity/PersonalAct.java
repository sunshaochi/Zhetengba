package com.boyuanitsm.zhetengba.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.boyuanitsm.zhetengba.R;
import com.boyuanitsm.zhetengba.activity.circle.CircleglAct;
import com.boyuanitsm.zhetengba.activity.mess.MessVerifyAct;
import com.boyuanitsm.zhetengba.activity.mine.EditAct;
import com.boyuanitsm.zhetengba.activity.mine.LabelMangerAct;
import com.boyuanitsm.zhetengba.adapter.HlvppAdapter;
import com.boyuanitsm.zhetengba.adapter.TestAdapter;
import com.boyuanitsm.zhetengba.base.BaseActivity;
import com.boyuanitsm.zhetengba.bean.ChatUserBean;
import com.boyuanitsm.zhetengba.bean.CircleEntity;
import com.boyuanitsm.zhetengba.bean.DataBean;
import com.boyuanitsm.zhetengba.bean.ImageInfo;
import com.boyuanitsm.zhetengba.bean.PersonalMain;
import com.boyuanitsm.zhetengba.bean.ResultBean;
import com.boyuanitsm.zhetengba.bean.SimpleInfo;
import com.boyuanitsm.zhetengba.bean.UserInfo;
import com.boyuanitsm.zhetengba.bean.UserInterestInfo;
import com.boyuanitsm.zhetengba.chat.DemoHelper;
import com.boyuanitsm.zhetengba.chat.act.ChatActivity;
import com.boyuanitsm.zhetengba.chat.db.UserDao;
import com.boyuanitsm.zhetengba.db.ChatUserDao;
import com.boyuanitsm.zhetengba.db.UserInfoDao;
import com.boyuanitsm.zhetengba.fragment.ContractsFrg;
import com.boyuanitsm.zhetengba.fragment.calendarFrg.SimpleFrg;
import com.boyuanitsm.zhetengba.http.callback.ResultCallback;
import com.boyuanitsm.zhetengba.http.manager.RequestManager;
import com.boyuanitsm.zhetengba.utils.CharacterParserUtils;
import com.boyuanitsm.zhetengba.utils.LayoutHelperUtil;
import com.boyuanitsm.zhetengba.utils.MyToastUtils;
import com.boyuanitsm.zhetengba.utils.Uitls;
import com.boyuanitsm.zhetengba.utils.ZtinfoUtils;
import com.boyuanitsm.zhetengba.view.CircleImageView;
import com.boyuanitsm.zhetengba.view.HorizontalListView;
import com.boyuanitsm.zhetengba.view.LoadingView;
import com.boyuanitsm.zhetengba.view.MyAlertDialog;
import com.boyuanitsm.zhetengba.view.PicShowDialog;
import com.boyuanitsm.zhetengba.view.PicSmShowDialog;
import com.boyuanitsm.zhetengba.view.refresh.PullToRefreshBase;
import com.boyuanitsm.zhetengba.view.refresh.PullToRefreshListView;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试listview
 * Created by xiaoke on 2016/5/26.
 */
public class PersonalAct extends BaseActivity{
    @ViewInject(R.id.ld_view)
    private LoadingView lv_view;
    private String userId;
    private List<CircleEntity> circleEntity = new ArrayList<>();
    private List<UserInfo> userEntity = new ArrayList<>();
    private List<UserInterestInfo> userInterestEntity = new ArrayList<>();
    private List<SimpleInfo> scheduleEntity = new ArrayList<>();
    private List<CircleEntity> circleTalkEntity=new ArrayList<>();
    private List<CircleEntity> datas2=new ArrayList<>();
    //    private List<SimpleInfo> list;//活动对象集合
    private List<SimpleInfo> datas = new ArrayList<>();
    private PersonalMain personalMain;
    private Boolean flag;
    private LinearLayout ll_add_riend;
    private ImageView iv_set;
    private Button bt_message;
    private HorizontalListView hlv_perpage;//水平listview
    private RelativeLayout rl_dangqi,rl_dangqi1;
    private RelativeLayout rl_dongtai,rl_dongtai1;
    private TextView tv_dangqi,tv_dangqi1;
    private TextView tv_dongtai,tv_dongtai1;
    private View view_dangqi,view_dangqi1;
    private View view_dongtai,view_dongtai1;
    private CircleImageView cv_photo;
    private ScrollView msv_scroll;
    private LinearLayout ll_tab;
    private TextView tv_tab1;
    private TextView tv_tab2;
    private TextView tv_tab3;
    private TextView tv_tab4;
    private TextView tv_tab5;
    private TextView tv_niName;//昵称
    private TextView tv_cir;
    private int state = 1, state1 = 1;//1,增加
    private RelativeLayout ll_scorl;
    private PullToRefreshListView test_lv;
    private TestAdapter adapter;
    private String PAGEFRG_KEY = "perpage_to_pagecalFrg";
    private ProgressDialog progressDialog;
    private int chat=-1;
    private String groupname;
    private int page=1;
    private int rows=10;
    private int page2=1;
    private View inflate,float1;
    private int tag;
    private int clickPos=-1;
    private  String[] urlList;
    private LinearLayout ll_float;
    // 图片缓存 默认 等
    private DisplayImageOptions optionsImag = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.mipmap.userhead)
            .showImageOnFail(R.mipmap.userhead).cacheInMemory(true).cacheOnDisk(true)
            .considerExifParams(true).imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565).build();
    private List<List<ImageInfo>> datalist=new ArrayList<>();
    @Override
    public void setLayout() {
        setContentView(R.layout.test_perpage);
    }

    @Override
    public void init(Bundle savedInstanceState) {
//        progressDialog=new ProgressDialog(PersonalAct.this);
//        progressDialog.setMessage("数据加载中...");
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.show();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        userId = bundle.getString("userId");
        chat = intent.getIntExtra("chat_type", 5);
        groupname=intent.getStringExtra("groupName");
         inflate = getLayoutInflater().inflate(R.layout.test_item_header, null);
        float1 = getLayoutInflater().inflate(R.layout.act_float_personal, null);
        setInflateListener(this.inflate,this.float1);
        getPersonalMain(userId);
        getCirclTalk(page2, rows, userId);
        test_lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                page2 = 1;
                if (clickPos == 0) {
                    getActivityList(page, rows, userId);
                } else {
                    getCirclTalk(page2, rows, userId);
                }

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (clickPos == 0) {
                    page++;
                    getActivityList(page, rows, userId);
                } else {
                    page2++;
                    getCirclTalk(page2, rows, userId);

                }
            }
        });
        test_lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem >= 1) {
                    ll_float.setVisibility(View.VISIBLE);
                } else {
                    ll_float.setVisibility(View.GONE);


                }
            }
        });
    }

    private void setInflateListener(View inflate, View view) {
        test_lv= (PullToRefreshListView) findViewById(R.id.test_lv);
        ll_add_riend= (LinearLayout) view.findViewById(R.id.ll_add_friend);
        iv_set= (ImageView) inflate.findViewById(R.id.iv_set);
        tv_tab1= (TextView) inflate.findViewById(R.id.tv_tab1);
        tv_tab2= (TextView) inflate.findViewById(R.id.tv_tab2);
        tv_tab3= (TextView) inflate.findViewById(R.id.tv_tab3);
        tv_tab4= (TextView) inflate.findViewById(R.id.tv_tab4);
        tv_tab5= (TextView) inflate.findViewById(R.id.tv_tab5);
        tv_cir= (TextView) inflate.findViewById(R.id.tv_cir);
        tv_niName= (TextView) inflate.findViewById(R.id.tv_Name);
        cv_photo= (CircleImageView) inflate.findViewById(R.id.cv_photo);
        ll_tab= (LinearLayout) inflate.findViewById(R.id.ll_tab);
        tv_dangqi= (TextView) view.findViewById(R.id.tv_dangqi);
        tv_dongtai= (TextView) view.findViewById(R.id.tv_dongtai);
        view_dangqi= (View) view.findViewById(R.id.view_dangqi);
        view_dongtai= (View) view.findViewById(R.id.view_dongtai);
        tv_dangqi1= (TextView) findViewById(R.id.tv_dangqi);
        tv_dongtai1= (TextView) findViewById(R.id.tv_dongtai);
        view_dangqi1= (View) findViewById(R.id.view_dangqi);
        view_dongtai1= (View) findViewById(R.id.view_dongtai);
        hlv_perpage= (HorizontalListView) inflate.findViewById(R.id.hlv_perpage);
        rl_dangqi= (RelativeLayout) view.findViewById(R.id.rl_dangqi);
        rl_dongtai= (RelativeLayout) view.findViewById(R.id.rl_dongtai);
        rl_dangqi1= (RelativeLayout) findViewById(R.id.rl_dangqi);
        rl_dongtai1= (RelativeLayout) findViewById(R.id.rl_dongtai);
        LayoutHelperUtil.freshInit(test_lv);
        test_lv.getRefreshableView().addHeaderView(inflate);
        test_lv.getRefreshableView().addHeaderView(view);
        ll_scorl= (RelativeLayout) findViewById(R.id.ll_scorl);
        ll_float = (LinearLayout) findViewById(R.id.ll_float);
        bt_message= (Button) findViewById(R.id.bt_message);
        rl_dangqi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTabBtn();
                clickPos=0;
                setSelect(0, userEntity, datas, datas2);
            }
        });
        rl_dongtai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTabBtn();
                clickPos=1;
                setSelect(1, userEntity, datas, datas2);
            }
        });
        rl_dangqi1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTabBtn();
                clickPos=0;
                setSelect(0,userEntity,datas,datas2);
            }
        });
        rl_dongtai1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTabBtn();
                clickPos=1;
                setSelect(1, userEntity, datas, datas2);
            }
        });
        iv_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = findViewById(R.id.iv_set);
                showPopupWindow(view);
            }
        });
        bt_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bt_message.getText().equals("加为好友")) {
                    isValidation(userId);
                } else if (bt_message.getText().equals("发送消息")) {
                    final Intent intent = new Intent(PersonalAct.this, ChatActivity.class);
                    final EaseUser easeUser = EaseUserUtils.getUserInfo(userEntity.get(0).getId());
                    if (getIntent().getIntExtra("chat_type", 0) == 1) {
                        finish();
                    } else {
                        if (easeUser != null && easeUser.getNick().length() != 32) {
                            Bundle bundle = new Bundle();
                            bundle.putString("userId", userEntity.get(0).getId());
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else {
                            RequestManager.getMessManager().findUserByHId(userEntity.get(0).getId(), new ResultCallback<ResultBean<UserInfo>>() {
                                @Override
                                public void onError(int status, String errorMsg) {
                                    MyToastUtils.showShortToast(getApplicationContext(), errorMsg);
                                }

                                @Override
                                public void onResponse(ResultBean<UserInfo> response) {
                                    UserInfo userInfo = response.getData();
                                    EaseUser easeUser1 = new EaseUser(userInfo.getId());
                                    easeUser1.setNick(userInfo.getPetName());
                                    easeUser1.setInitialLetter(CharacterParserUtils.getInstance().getSelling(userInfo.getPetName()).substring(0, 1));
                                    easeUser1.setAvatar(Uitls.imageFullUrl(userInfo.getIcon()));
                                    DemoHelper.getInstance().saveContact(easeUser1);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("userId", userEntity.get(0).getId());
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });
                        }

                    }

                }
            }
        });

        lv_view.setOnRetryListener(new LoadingView.OnRetryListener() {
            @Override
            public void OnRetry() {
                getPersonalMain(userId);
            }
        });


    }


    private int instalData() {
        if (userEntity != null) {
            flag = userEntity.get(0).isFriend();
        }
        if (UserInfoDao.getUser().getId().equals(userId)) {
            ll_add_riend.setVisibility(View.VISIBLE);//档期frg
//            test_lv.setVisibility(View.VISIBLE);
            iv_set.setVisibility(View.GONE);
            bt_message.setVisibility(View.GONE);
            test_lv.getRefreshableView().setPadding(0, 0, 0, 0);
            return 0;
        } else if (flag) {
            test_lv.getRefreshableView().setPadding(0, 0, 0, 80);
//            test_lv.setVisibility(View.VISIBLE);
            bt_message.setVisibility(View.VISIBLE);
            ll_add_riend.setVisibility(View.VISIBLE);
            iv_set.setVisibility(View.VISIBLE);
            bt_message.setText("发送消息");
//            cv_photo.setEnabled(true);
            return 0;

        } else {
//            test_lv.setVisibility(View.GONE);
            test_lv.getRefreshableView().setPadding(0, 0, 0, 80);
            bt_message.setVisibility(View.VISIBLE);
            bt_message.setText("加为好友");
            iv_set.setVisibility(View.GONE);
            ll_add_riend.setVisibility(View.GONE);
//            cv_photo.setEnabled(false);
            return 2;
        }
    }
    /**
     * 点击加为好友按钮时候通过此接口返回获取字段判断是否需要验证
     * 是否有需要验证
     */
    private void isValidation(String personId) {
        RequestManager.getMessManager().findUserIcon(personId, new ResultCallback<ResultBean<UserInfo>>() {
            @Override
            public void onError(int status, String errorMsg) {

            }

            @Override
            public void onResponse(ResultBean<UserInfo> response) {
                UserInfo userinfo = response.getData();
                if (userinfo != null) {
                    if (userinfo.getUserType() != null) {
                        String usertype = userinfo.getUserType();
                        if (usertype.equals("1")) {
                            Intent intent = new Intent(PersonalAct.this, MessVerifyAct.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("userName", userEntity.get(0).getPetName());
                            bundle.putString("userId", userEntity.get(0).getId());
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else {
                            addfrend(userEntity.get(0).getId());

                        }
                    }
                }
            }
        });

    }

    /**
     *
     */
    private void addfrend(String friendId) {
        RequestManager.getMessManager().aggreeFriend(friendId, new ResultCallback<ResultBean<String>>() {
            @Override
            public void onError(int status, String errorMsg) {

            }

            @Override
            public void onResponse(ResultBean<String> response) {
//                MyToastUtils.showShortToast(PerpageAct.this,"添加成功");
                getPersonalMain(userId);

            }
        });
    }
    /**
     * 初识化btn颜色
     */
    private void resetTabBtn() {
        tv_dangqi.setTextColor(Color.parseColor("#cdcdcd"));
        tv_dongtai.setTextColor(Color.parseColor("#cdcdcd"));
        view_dangqi.setBackgroundColor(Color.parseColor("#cdcdcd"));
        view_dongtai.setBackgroundColor(Color.parseColor("#cdcdcd"));
        tv_dangqi1.setTextColor(Color.parseColor("#cdcdcd"));
        tv_dongtai1.setTextColor(Color.parseColor("#cdcdcd"));
        view_dangqi1.setBackgroundColor(Color.parseColor("#cdcdcd"));
        view_dongtai1.setBackgroundColor(Color.parseColor("#cdcdcd"));
    }


    /**
     * 获取数据
     *
     * @param id
     */
    private void getPersonalMain(String id) {
        RequestManager.getScheduleManager().getPersonalMain(id, new ResultCallback<ResultBean<PersonalMain>>() {
            @Override
            public void onError(int status, String errorMsg) {
//                progressDialog.dismiss();
                lv_view.loadError();
                MyToastUtils.showShortToast(getApplicationContext(), errorMsg);
            }

            @Override
            public void onResponse(ResultBean<PersonalMain> response) {
//                progressDialog.dismiss();
                lv_view.loadComplete();
                personalMain = response.getData();
                circleEntity = personalMain.getCircleEntity();
                userEntity = personalMain.getUserEntity();
                tag = instalData();
                getActivityList(page, rows, userId);
                userInterestEntity = personalMain.getUserInterestEntity();
                initUserData(userEntity);
                iniTab(userInterestEntity, userEntity.get(0).getId());
                setSelect(tag, userEntity, scheduleEntity, circleTalkEntity);
                clickPos=0;
                setOnclikListener();
                hlv_perpage.setAdapter(new HlvppAdapter(PersonalAct.this, circleEntity));//她的圈子下面水平view适配器
                hlv_perpage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(PersonalAct.this, CircleglAct.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("PPuserId", userEntity.get(0).getId());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    /**
     * 获取我的会有
     */
    private void getActivityList(final int page,int rows,String userId){
        RequestManager.getScheduleManager().activityList(page, rows, userId, new ResultCallback<ResultBean<DataBean<SimpleInfo>>>() {
            @Override
            public void onError(int status, String errorMsg) {

            }

            @Override
            public void onResponse(ResultBean<DataBean<SimpleInfo>> response) {
                test_lv.onPullDownRefreshComplete();
                test_lv.onPullUpRefreshComplete();
                scheduleEntity = response.getData().getRows();
                if (scheduleEntity.size() == 0) {
                    if (page == 1) {
                    } else {
                        test_lv.setHasMoreData(false);
                    }
                }
                if (page == 1) {
                    datas.clear();
                }
                datas.addAll(scheduleEntity);
                resetTabBtn();
                setSelect(tag, userEntity, datas, circleTalkEntity);
                setOnclikListener();
            }
        });
    }

    /**
     * 获取圈子列表
     * @param page
     * @param rows
     * @param userId
     */
    private void getCirclTalk(final int page,int rows,String userId){
        RequestManager.getTalkManager().myTalksOut(page, rows, userId, new ResultCallback<ResultBean<DataBean<CircleEntity>>>() {
            @Override
            public void onError(int status, String errorMsg) {

            }

            @Override
            public void onResponse(ResultBean<DataBean<CircleEntity>> response) {
                test_lv.onPullDownRefreshComplete();
                test_lv.onPullUpRefreshComplete();
                circleTalkEntity=response.getData().getRows();
                if (circleTalkEntity.size() == 0) {
                    if (page2 == 1) {

                    } else {
                        test_lv.setHasMoreData(false);
                    }
                    return;
                }
                if (page2 == 1) {
                    datas2.clear();
                }
                datas2.addAll(circleTalkEntity);
                resetTabBtn();
                setSelect(tag, userEntity, datas, datas2);
            }
        });
    }
    /**
     * 点击button后选择显示的frg
     */
    private void setSelect(int position, List<UserInfo> userEntity, List<SimpleInfo> scheduleEntity, List<CircleEntity> circleTalkEntity) {
        switch (position) {
            case 0://档期frg
                setTab(0);
                    if (adapter==null){
                        adapter=new TestAdapter(PersonalAct.this,userEntity,scheduleEntity,circleTalkEntity,0);
                        test_lv.getRefreshableView().setAdapter(adapter);
                    }else {
                        adapter.updata(userEntity,scheduleEntity,circleTalkEntity,0);
                    }

                break;
            case 1://圈子动态frg
                setTab(1);
                if (adapter==null){
                    adapter=new TestAdapter(PersonalAct.this,userEntity,scheduleEntity,circleTalkEntity,1);
                    test_lv.getRefreshableView().setAdapter(adapter);
                }else {
                    adapter.updata(userEntity,scheduleEntity,circleTalkEntity,1);
                }
                break;
            case 2:
                setTab(0);
                if (adapter==null){
                    adapter=new TestAdapter(PersonalAct.this,userEntity,scheduleEntity,circleTalkEntity,2);
                    test_lv.getRefreshableView().setAdapter(adapter);
                }else {
                    adapter.updata(userEntity,scheduleEntity,circleTalkEntity,2);
                }

        }
    }
    /**
     * 选中 选中btn颜色
     *
     * @param i
     */
    private void setTab(int i) {
        switch (i) {
            case 0:
                tv_dangqi.setTextColor(Color.parseColor("#52c791"));
                view_dangqi.setBackgroundColor(Color.parseColor("#52c791"));
                tv_dangqi1.setTextColor(Color.parseColor("#52c791"));
                view_dangqi1.setBackgroundColor(Color.parseColor("#52c791"));
                break;
            case 1:
                tv_dongtai.setTextColor(Color.parseColor("#52c791"));
                view_dongtai.setBackgroundColor(Color.parseColor("#52c791"));
                tv_dongtai1.setTextColor(Color.parseColor("#52c791"));
                view_dongtai1.setBackgroundColor(Color.parseColor("#52c791"));
                break;
        }
    }

    /**
     * 点击头像跳转个人资料
     */
    private void setOnclikListener() {
        if (!TextUtils.isEmpty(userEntity.get(0).getIcon())){
            urlList = ZtinfoUtils.convertStrToArray(userEntity.get(0).getIcon());
            cv_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PicSmShowDialog dialog=new PicSmShowDialog(PersonalAct.this,urlList,0);
                    dialog.show();
                }
            });
        }
    }
    /**
     * 用户信息初始化
     *
     * @param userEntity
     */
    private void initUserData(List<UserInfo> userEntity) {
        if (!TextUtils.isEmpty(userEntity.get(0).getPetName())) {
            tv_niName.setText(userEntity.get(0).getPetName());
        } else {
            tv_niName.setText("暂无昵称");
        }
        if (!TextUtils.isEmpty(userEntity.get(0).getSex())) {
            if (userEntity.get(0).getSex().equals(1 + "")) {
                tv_cir.setText("他的圈子");
            } else if (userEntity.get(0).getSex().equals(0 + "")) {
                tv_cir.setText("她的圈子");
            }
        }
        ImageLoader.getInstance().displayImage(Uitls.imageFullUrl(userEntity.get(0).getIcon()), cv_photo, optionsImag);
        ChatUserBean chatUserBean = new ChatUserBean();
        chatUserBean.setUserId(userEntity.get(0).getId());
        chatUserBean.setNick(userEntity.get(0).getPetName());
        chatUserBean.setIcon(Uitls.imageFullUrl(userEntity.get(0).getIcon()));
        ChatUserDao.saveUser(chatUserBean);

    }
    /**
     * 兴趣标签初始化
     *
     * @param str
     */
    private void iniTab(List<UserInterestInfo> str, final String useId) {
        if (str.size() == 0) {
            ll_tab.setVisibility(View.GONE);
        } else if (str.size() == 1) {
            ll_tab.setVisibility(View.VISIBLE);
            tv_tab1.setVisibility(View.VISIBLE);
            tv_tab1.setText(str.get(0).getDictName());
            tv_tab2.setVisibility(View.GONE);
            tv_tab3.setVisibility(View.GONE);
            tv_tab4.setVisibility(View.GONE);
            tv_tab5.setVisibility(View.GONE);
        } else if (str.size() == 2) {
            ll_tab.setVisibility(View.VISIBLE);
            tv_tab1.setVisibility(View.VISIBLE);
            tv_tab2.setVisibility(View.VISIBLE);
            tv_tab1.setText(str.get(0).getDictName());
            tv_tab2.setText(str.get(1).getDictName());
            tv_tab3.setVisibility(View.GONE);
            tv_tab4.setVisibility(View.GONE);
            tv_tab5.setVisibility(View.GONE);
        } else if (str.size() == 3) {
            ll_tab.setVisibility(View.VISIBLE);
            tv_tab1.setVisibility(View.VISIBLE);
            tv_tab2.setVisibility(View.VISIBLE);
            tv_tab3.setVisibility(View.VISIBLE);
            tv_tab1.setText(str.get(0).getDictName());
            tv_tab2.setText(str.get(1).getDictName());
            tv_tab3.setText(str.get(2).getDictName());
            tv_tab4.setVisibility(View.GONE);
            tv_tab5.setVisibility(View.GONE);
        } else if (str.size() == 4) {
            ll_tab.setVisibility(View.VISIBLE);
            tv_tab1.setVisibility(View.VISIBLE);
            tv_tab2.setVisibility(View.VISIBLE);
            tv_tab3.setVisibility(View.VISIBLE);
            tv_tab4.setVisibility(View.VISIBLE);
            tv_tab5.setVisibility(View.GONE);
            tv_tab1.setText(str.get(0).getDictName());
            tv_tab2.setText(str.get(1).getDictName());
            tv_tab3.setText(str.get(2).getDictName());
            tv_tab4.setText(str.get(3).getDictName());
        } else if (str.size() > 4) {
            ll_tab.setVisibility(View.VISIBLE);
            tv_tab1.setVisibility(View.VISIBLE);
            tv_tab2.setVisibility(View.VISIBLE);
            tv_tab3.setVisibility(View.VISIBLE);
            tv_tab4.setVisibility(View.VISIBLE);
            tv_tab5.setVisibility(View.VISIBLE);
            tv_tab1.setText(str.get(0).getDictName());
            tv_tab2.setText(str.get(1).getDictName());
            tv_tab3.setText(str.get(2).getDictName());
            tv_tab4.setText(str.get(3).getDictName());
            tv_tab5.setText("更多");
            tv_tab5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("userId", useId);
                    intent.setClass(PersonalAct.this, LabelMangerAct.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }
    }
    private void showPopupWindow(View parent) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.popuwindows_dialog, null);
        // 实例化popupWindow
        final PopupWindow popupWindow = new PopupWindow(layout, AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT);
        //控制键盘是否可以获得焦点
        popupWindow.setFocusable(true);
        //设置popupWindow弹出窗体的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable(null, ""));
        WindowManager manager = (WindowManager) getSystemService(PersonalAct.this.WINDOW_SERVICE);
        @SuppressWarnings("deprecation")
        //获取xoff
                int xpos = manager.getDefaultDisplay().getWidth() / 2 - popupWindow.getWidth() / 2;
        //xoff,yoff基于anchor的左下角进行偏移。
        popupWindow.showAsDropDown(parent, xpos, 0);
        LinearLayout ll_schy = (LinearLayout) layout.findViewById(R.id.ll_schy);//删除好友
        LinearLayout ll_xiugai = (LinearLayout) layout.findViewById(R.id.ll_xiugai);//修改备注
        LinearLayout ll_hei=(LinearLayout) layout.findViewById(R.id.ll_hei);//加入黑名单
        ll_schy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MyAlertDialog(PersonalAct.this).builder().setTitle("提示").setMsg("确定删除该好友").setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteFriendPer(userEntity.get(0).getId());
                    }
                }).setNegativeButton("取消", null).show();

            }
        });
        ll_hei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MyAlertDialog(PersonalAct.this).builder().setTitle("提示").setMsg("确定加入黑名单").setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //加入黑名单接口
                        addBlack(userEntity.get(0).getId());



                    }
                }).setNegativeButton("取消",null).show();
            }
        });
        ll_xiugai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditAct.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(EditAct.USER_TYPE, 8);
                intent.putExtra("friendId", userEntity.get(0).getId());
                intent.putExtra("remark",userEntity.get(0).getPetName());
                intent.putExtra("chat_type", chat);
                if (!TextUtils.isEmpty(groupname)){
                    intent.putExtra("groupName",groupname);
                }
                startActivity(intent);
                popupWindow.dismiss();

            }
        });
    }
    /**
     * 删除好友接口
     *
     * @param friendId
     */
    private void deleteFriendPer(final String friendId) {
        RequestManager.getMessManager().deleteFriend(friendId, new ResultCallback<ResultBean<String>>() {
            @Override
            public void onError(int status, String errorMsg) {

            }

            @Override
            public void onResponse(ResultBean<String> response) {
                UserDao userDao=new UserDao(getApplicationContext());
                userDao.deleteContact(friendId);
                sendBroadcast(new Intent(SimpleFrg.DATA_CHANGE_KEY));
                sendBroadcast(new Intent(ContractsFrg.UPDATE_CONTRACT));
                finish();
            }
        });
    }
    /**
     * 添加到黑名单
     * @param friendId
     */
    private void addBlack(final String friendId) {
        RequestManager.getScheduleManager().addblackList(friendId, new ResultCallback<ResultBean<String>>() {
            @Override
            public void onError(int status, String errorMsg) {

            }

            @Override
            public void onResponse(ResultBean<String> response) {
                UserDao userDao=new UserDao(getApplicationContext());
                userDao.deleteContact(friendId);
                sendBroadcast(new Intent(ContractsFrg.UPDATE_CONTRACT));
                sendBroadcast(new Intent(SimpleFrg.DATA_CHANGE_KEY));
                finish();

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (receiverTalk == null) {
            receiverTalk = new MyBroadCastReceiverTalk();
            registerReceiver(receiverTalk, new IntentFilter(PPLABELS));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiverTalk != null) {
            unregisterReceiver(receiverTalk);
            receiverTalk = null;
        }
    }

    private MyBroadCastReceiverTalk receiverTalk;
    public static final String PPLABELS = "test_update";

    private class MyBroadCastReceiverTalk extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            getPersonalMain(userId);
            page2=1;
            getCirclTalk(page2,rows,userId);
        }
    }
}
