package com.boyuanitsm.zhetengba.activity.circle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.boyuanitsm.zhetengba.R;
import com.boyuanitsm.zhetengba.activity.mess.ContractsAct;
import com.boyuanitsm.zhetengba.activity.mine.AssignScanAct;
import com.boyuanitsm.zhetengba.base.BaseActivity;
import com.boyuanitsm.zhetengba.bean.CircleEntity;
import com.boyuanitsm.zhetengba.bean.ResultBean;
import com.boyuanitsm.zhetengba.http.callback.ResultCallback;
import com.boyuanitsm.zhetengba.http.manager.RequestManager;
import com.boyuanitsm.zhetengba.utils.MyToastUtils;
import com.boyuanitsm.zhetengba.view.CanotEmojEditText;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 建立圈子界面
 * Created by bitch-1 on 2016/5/9.
 */
public class CreatCirAct extends BaseActivity implements View.OnClickListener{
    @ViewInject(R.id.tv_ask)//邀请好友
    private TextView tv_ask;
    @ViewInject(R.id.tv_creat)//创建圈子
    private TextView tv_creat;
    @ViewInject(R.id.et_cir_name)//建立圈子名称
    private CanotEmojEditText et_cir_name;
    @ViewInject(R.id.etNotes)
    private EditText etNotes;//圈子公告
    private String personIds;//邀请成员id
    private CircleEntity circleEntity;//圈子实体
    private ProgressDialog progressDialog;
    @Override
    public void setLayout() {
        setContentView(R.layout.act_creatcir);

    }

    @Override
    public void init(Bundle savedInstanceState) {
        setTopTitle("建立圈子");
        circleEntity=new CircleEntity();
        progressDialog=new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
    }
    @OnClick({R.id.tv_ask,R.id.tv_creat})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_ask:
                Intent  intent = new Intent();
                Bundle bundle=new Bundle();
                String str3="circle";
                bundle.putString("can", str3);
                intent.putExtras(bundle);
                intent.setClass(this, AssignScanAct.class);
                startActivityForResult(intent, 5);
                break;
            case R.id.tv_creat://创建圈子，圈子管理增加一条，跳转至圈子管理
                if (TextUtils.isEmpty(et_cir_name.getText().toString().trim())){
                    MyToastUtils.showShortToast(this,"圈子名称不能为空");
                    return;
                }else {
                    progressDialog.show();
                    tv_creat.setEnabled(false);
                    circleEntity.setCircleName(et_cir_name.getText().toString().trim());
                    circleEntity.setNotice(etNotes.getText().toString().trim());
                    addCircle(circleEntity, personIds);
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null&&resultCode==5){
            Bundle bundle=data.getBundleExtra("bundle3");
           personIds= bundle.getString("bundleIds");
        }
    }

    /**
     * 建立圈子
     * @param entity
     * @param personIds
     */
    private void addCircle(CircleEntity entity,String personIds){
        RequestManager.getTalkManager().addCircle(entity, personIds, new ResultCallback<ResultBean<String>>() {
            @Override
            public void onError(int status, String errorMsg) {
                tv_creat.setEnabled(true);
                progressDialog.dismiss();
                MyToastUtils.showShortToast(CreatCirAct.this,"建立圈子失败，请检查网络！");
            }

            @Override
            public void onResponse(ResultBean<String> response) {
                tv_creat.setEnabled(true);
                progressDialog.dismiss();
                finish();
                sendBroadcast(new Intent(CircleglAct.INTENTFLAG));
            }
        });
    }
}
