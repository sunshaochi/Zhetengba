package com.boyuanitsm.zhetengba.activity.mine;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.boyuanitsm.zhetengba.R;
import com.boyuanitsm.zhetengba.activity.ShareDialogAct;
import com.boyuanitsm.zhetengba.base.BaseActivity;
import com.boyuanitsm.zhetengba.bean.ErEntity;
import com.boyuanitsm.zhetengba.db.UserInfoDao;
import com.boyuanitsm.zhetengba.utils.GsonUtils;
import com.boyuanitsm.zhetengba.utils.ZhetebaUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 分享二维码界面
 * Created by bitch-1 on 2016/5/3.
 */
public class ShareqrcodeAct extends BaseActivity {
    @ViewInject(R.id.iverm)
    private ImageView iverm;
    @ViewInject(R.id.tv_shareer)
    private TextView tv_shareer;

    private Bitmap bitmap;
    String erUrl = "http://m.myjinzhu.com/#/tab/home?redirctUrl=/register/";
    @Override
    public void setLayout() {
        setContentView(R.layout.act_shareqrcode);

    }

    @Override
    public void init(Bundle savedInstanceState) {
        setTopTitle("我的二维码");
        ErEntity erEntity=new ErEntity();
        if (!TextUtils.isEmpty(UserInfoDao.getUser().getId())){
            erEntity.setId(UserInfoDao.getUser().getId());
            erEntity.setType(1);//个人主页
        }
//        if (!TextUtils.isEmpty(UserInfoDao.getUser().getId())){
            bitmap= ZhetebaUtils.createQRImage(GsonUtils.bean2Json(erEntity));
//        }
        iverm.setScaleType(ImageView.ScaleType.FIT_XY);
        iverm.setImageBitmap(bitmap);
    }


    @OnClick({R.id.tv_shareer})
    public void OnClick(View v){
        switch (v.getId()){
           case R.id.tv_shareer:
               Intent intent=new Intent(ShareqrcodeAct.this,ShareDialogAct.class);
               intent.putExtra("type",5);
//                openActivity(ShareDialogAct.class);
               startActivity(intent);
                break;
        }
    }
}
