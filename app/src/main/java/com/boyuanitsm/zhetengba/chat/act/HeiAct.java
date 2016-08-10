package com.boyuanitsm.zhetengba.chat.act;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import com.boyuanitsm.zhetengba.R;
import com.boyuanitsm.zhetengba.base.BaseActivity;
import com.boyuanitsm.zhetengba.bean.DataBean;
import com.boyuanitsm.zhetengba.bean.FriendsBean;
import com.boyuanitsm.zhetengba.bean.ResultBean;
import com.boyuanitsm.zhetengba.chat.adapter.HeiAdapter;
import com.boyuanitsm.zhetengba.http.callback.ResultCallback;
import com.boyuanitsm.zhetengba.http.manager.RequestManager;
import com.boyuanitsm.zhetengba.utils.MyToastUtils;
import com.boyuanitsm.zhetengba.utils.ZhetebaUtils;
import com.boyuanitsm.zhetengba.view.swipemenulistview.SwipeMenu;
import com.boyuanitsm.zhetengba.view.swipemenulistview.SwipeMenuCreator;
import com.boyuanitsm.zhetengba.view.swipemenulistview.SwipeMenuItem;
import com.boyuanitsm.zhetengba.view.swipemenulistview.SwipeMenuListView;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoke on 2016/8/9.
 */
public class HeiAct extends BaseActivity {
    @ViewInject(R.id.lv_hei)
    private SwipeMenuListView lv_hei;
    private ProgressDialog progressDialog;
    private List<FriendsBean>list=new ArrayList<>();//返回来的黑名单列表
    private HeiAdapter adapter;
    @Override
    public void setLayout() {
        setContentView(R.layout.act_heimingdan);
    }

    @Override
    public void init(Bundle savedInstanceState) {
        setTopTitle("黑名单");
        findBlackList();//获取黑名单列表
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.setMessage("数据加载中...");
//        progressDialog.show();
        //调用接口

//        lv_hei.setAdapter(adapter);
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                switch (menu.getViewType()) {
                    case 0:
                        SwipeMenuItem deleteItem = new SwipeMenuItem(
                                getApplicationContext());
                        deleteItem.setBackground(R.color.delete_red);
                        deleteItem.setWidth(ZhetebaUtils.dip2px(HeiAct.this, 80));
                        deleteItem.setTitle("删除");
                        deleteItem.setTitleSize(14);
                        deleteItem.setTitleColor(Color.WHITE);
                        menu.addMenuItem(deleteItem);
                        break;
                }

            }
        };
        lv_hei.setMenuCreator(creator);
        lv_hei.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        //向左滑动，删除操作
                        deleteFriendPer(list.get(position).getId());
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
        lv_hei.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
    }




    /**
     * 获取黑名单列表
     */
    private void findBlackList() {
        RequestManager.getScheduleManager().findBlackList(new ResultCallback<ResultBean<DataBean<FriendsBean>>>() {
            @Override
            public void onError(int status, String errorMsg) {

            }

            @Override
            public void onResponse(ResultBean<DataBean<FriendsBean>> response) {
                list=response.getData().getRows();
                if(list.size()!=0){
                    adapter=new HeiAdapter(HeiAct.this,list);
                    lv_hei.setAdapter(adapter);
                }

            }
        });

    }


    /**
     * 删除好友接口
     *
     * @param friendId
     */
    private void deleteFriendPer(String friendId) {
        RequestManager.getMessManager().deleteFriend(friendId, new ResultCallback<ResultBean<String>>() {
            @Override
            public void onError(int status, String errorMsg) {

            }

            @Override
            public void onResponse(ResultBean<String> response) {
                MyToastUtils.showShortToast(HeiAct.this,"删除好友成功");
//                Intent intent = new Intent();
//                intent.setAction(SimpleFrg.DATA_CHANGE_KEY);
//                intent.setAction(CalFrg.CAL_DATA_CHANGE_KEY);
//                sendBroadcast(intent);
//                finish();
            }
        });
    }




}