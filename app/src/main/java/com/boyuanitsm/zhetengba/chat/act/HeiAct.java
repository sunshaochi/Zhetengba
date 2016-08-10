package com.boyuanitsm.zhetengba.chat.act;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;

import com.boyuanitsm.zhetengba.R;
import com.boyuanitsm.zhetengba.base.BaseActivity;
import com.boyuanitsm.zhetengba.chat.adapter.HeiAdapter;
import com.boyuanitsm.zhetengba.db.ActivityMessDao;
import com.boyuanitsm.zhetengba.utils.ZhetebaUtils;
import com.boyuanitsm.zhetengba.view.refresh.PullToRefreshListView;
import com.boyuanitsm.zhetengba.view.swipemenulistview.SwipeMenu;
import com.boyuanitsm.zhetengba.view.swipemenulistview.SwipeMenuCreator;
import com.boyuanitsm.zhetengba.view.swipemenulistview.SwipeMenuItem;
import com.boyuanitsm.zhetengba.view.swipemenulistview.SwipeMenuListView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by xiaoke on 2016/8/9.
 */
public class HeiAct extends BaseActivity {
    @ViewInject(R.id.lv_hei)
    private SwipeMenuListView lv_hei;
    private ProgressDialog progressDialog;
    @Override
    public void setLayout() {
        setContentView(R.layout.act_heimingdan);
    }

    @Override
    public void init(Bundle savedInstanceState) {
        setTopTitle("黑名单");
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.setMessage("数据加载中...");
//        progressDialog.show();
        //调用接口
        HeiAdapter adapter=new HeiAdapter(this);
        lv_hei.setAdapter(adapter);
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
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
        lv_hei.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
    }
}
