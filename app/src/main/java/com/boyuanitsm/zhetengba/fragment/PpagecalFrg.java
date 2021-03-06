package com.boyuanitsm.zhetengba.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.boyuanitsm.zhetengba.R;
import com.boyuanitsm.zhetengba.adapter.PpfrgAdapter;
import com.boyuanitsm.zhetengba.base.BaseFragment;
import com.boyuanitsm.zhetengba.bean.PersonalMain;
import com.boyuanitsm.zhetengba.bean.ResultBean;
import com.boyuanitsm.zhetengba.bean.ScheduleInfo;
import com.boyuanitsm.zhetengba.bean.UserInfo;
import com.boyuanitsm.zhetengba.http.callback.ResultCallback;
import com.boyuanitsm.zhetengba.http.manager.RequestManager;
import com.boyuanitsm.zhetengba.utils.ListViewUtil;
import com.boyuanitsm.zhetengba.utils.MyLogUtils;
import com.boyuanitsm.zhetengba.view.MyListview;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 个人主页里面档期frg
 * Created by bitch-1 on 2016/5/16.
 */
public class PpagecalFrg extends BaseFragment {
    private View view;
    @ViewInject(R.id.lv_ppcal)
    private MyListview lv_ppcal;
    private String userId;
    private List<ScheduleInfo> scheduleEntity = new ArrayList<>();
    private List<UserInfo> userInfoList=new ArrayList<>();
    private PersonalMain personalMain;
//    private IntentFilter intentFilter;
    private String PAGEFRG_KEY = "perpage_to_pagecalFrg";
    private PpfrgAdapter adapter;

    @Override
    public View initView(LayoutInflater inflater) {
        view = inflater.inflate(R.layout.frg_ppagecal, null);
        return view;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        personalMain = bundle.getParcelable(PAGEFRG_KEY);
        scheduleEntity = personalMain.getScheduleEntity();
        userInfoList= personalMain.getUserEntity();
        if (adapter==null){
            adapter=new PpfrgAdapter(mActivity, scheduleEntity, userInfoList);
            lv_ppcal.setAdapter(adapter);
        }else {
            adapter.updata(scheduleEntity, userInfoList);
        }
//        lv_ppcal.setAdapter(new PpfrgAdapter(mActivity, scheduleEntity,userInfoList));
//        int lvHeight = ListViewUtil.MeasureListView(lv_ppcal);
//        MyLogUtils.degug("lvheight=======" + lvHeight);
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        if (receiverTalk == null) {
//            receiverTalk = new MyBroadCastReceiverTalk();
//            mActivity.registerReceiver(receiverTalk, new IntentFilter(PPLABELSCAL));
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (receiverTalk != null) {
//            mActivity.unregisterReceiver(receiverTalk);
//            receiverTalk = null;
//        }
//    }
//
//    private MyBroadCastReceiverTalk receiverTalk;
//    public static final String PPLABELSCAL = "cal_perpage_update";
//
//    private class MyBroadCastReceiverTalk extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            scheduleEntity = personalMain.getScheduleEntity();
//            userInfoList= personalMain.getUserEntity();
//            if (adapter==null){
//                adapter=new PpfrgAdapter(mActivity, scheduleEntity, userInfoList);
//                lv_ppcal.setAdapter(adapter);
//            }else {
//                adapter.updata(scheduleEntity, userInfoList);
//            }
//        }
//    }
}
