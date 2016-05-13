package com.boyuanitsm.zhetengba.activity.circle;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.boyuanitsm.zhetengba.R;
import com.boyuanitsm.zhetengba.adapter.CircleglAdapter;
import com.boyuanitsm.zhetengba.base.BaseActivity;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 圈子管理界面
 * Created by bitch-1 on 2016/5/6.
 */
public class CircleglAct extends BaseActivity {
    @ViewInject(R.id.lv_circlegl)
    private ListView lv_circlegl;
    @Override
    public void setLayout() {
        setContentView(R.layout.act_circlegl);
    }

    @Override
    public void init(Bundle savedInstanceState) {
        setTopTitle("圈子管理");
        lv_circlegl.setAdapter(new CircleglAdapter(CircleglAct.this));

    }
    @OnClick({R.id.iv_jia,R.id.iv_serch})
    public void OnClick(View v){
        switch (v.getId()){
            case R.id.iv_serch:

                break;

            case R.id.iv_jia:
                openActivity(CreatCirAct.class);
                break;


        }
    }
}
