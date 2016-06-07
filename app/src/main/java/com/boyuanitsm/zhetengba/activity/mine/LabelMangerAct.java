package com.boyuanitsm.zhetengba.activity.mine;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.boyuanitsm.zhetengba.R;
import com.boyuanitsm.zhetengba.adapter.LabelGVadapter;
import com.boyuanitsm.zhetengba.adapter.LabelGvMyadapter;
import com.boyuanitsm.zhetengba.base.BaseActivity;
import com.boyuanitsm.zhetengba.bean.LabelBannerInfo;
import com.boyuanitsm.zhetengba.bean.ResultBean;
import com.boyuanitsm.zhetengba.bean.UserInterestInfo;
import com.boyuanitsm.zhetengba.http.callback.ResultCallback;
import com.boyuanitsm.zhetengba.http.manager.RequestManager;
import com.boyuanitsm.zhetengba.utils.MyLogUtils;
import com.boyuanitsm.zhetengba.utils.MyToastUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 标签管理
 * Created by xiaoke on 2016/5/27.
 */
public class LabelMangerAct extends BaseActivity {
    @ViewInject(R.id.gv1)
    private GridView gv1;
    @ViewInject(R.id.gv2)
    private GridView gv2;
    private List<LabelBannerInfo> list=new ArrayList<LabelBannerInfo>();
    private List<UserInterestInfo> mylist=new ArrayList<>();
    private UserInterestInfo userInterestInfo;
    private LabelBannerInfo labelBannerInfo;
    private LabelGVadapter labelGVadapter;
    private LabelGvMyadapter myadapter;
    private String labelids;//接口传入参数
    @Override
    public void setLayout() {
        setContentView(R.layout.act_labelmana2);
    }

    @Override
    public void init(Bundle savedInstanceState) {
        setTopTitle("标签管理");
        setRight("完成", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mylist==null){
                    MyToastUtils.showShortToast(LabelMangerAct.this,"至少选择一个兴趣标签");
                }else if (mylist.size()==1){
                    labelids=mylist.get(0).getInterestId();
                    addInterestLabel(labelids);
                }else if (mylist.size()>1){
                    labelids=mylist.get(0).getInterestId();
                    for (int i=1;i<mylist.size();i++){
                        labelids=labelids+","+mylist.get(i).getInterestId();
                    }
                    addInterestLabel(labelids);
                }

            }
        });
//        添加标签到全部标签
        getIntrestLabel("0");
        getMyInterestLabel();
        updata();

    }

    private void updata() {


        gv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LabelBannerInfo str = list.get(position);
                userInterestInfo=new UserInterestInfo();
                    userInterestInfo.setInterestId(str.getId());
                    userInterestInfo.setDictName(str.getDictName());
                    mylist.add(userInterestInfo);
                    myadapter.update(mylist);
                list.remove(position);
                labelGVadapter.update(list);

            }
        });
        gv1.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserInterestInfo str = mylist.get(position);
                mylist.remove(position);
                myadapter.update(mylist);
                gv1.setAdapter(myadapter);
                labelBannerInfo=new LabelBannerInfo(LabelMangerAct.this);
                labelBannerInfo.setDictName(str.getDictName());
                labelBannerInfo.setId(str.getId());
                list.add(labelBannerInfo);
                labelGVadapter.update(list);
            }
        });
    }
//    //刷新全部标签对应标签背景
//    private int labelposition(LabelBannerInfo str){
//        int position=-1;
//        for (int i=0;i<list.size();i++){
//            if (str.equals(list.get(i))){
//                return position=i;
//            }
//        }
//        return position;
//    }

    /**
     * 个人兴趣标签/全部标签
     * @param dictType
     * @return
     */
    private void getIntrestLabel(String dictType){
        RequestManager.getScheduleManager().getIntrestLabelList(dictType, new ResultCallback<ResultBean<List<LabelBannerInfo>>>() {
            @Override
            public void onError(int status, String errorMsg) {

            }

            @Override
            public void onResponse(ResultBean<List<LabelBannerInfo>> response) {
                list = response.getData();
                labelGVadapter=new LabelGVadapter(LabelMangerAct.this,list);
                gv2.setAdapter(labelGVadapter);
                gv2.setSelector(new ColorDrawable(Color.TRANSPARENT));
            }
        });
    }

    /**
     * 添加个人兴趣标签
     * @param labelIds
     */
    private void addInterestLabel(String labelIds){
        RequestManager.getScheduleManager().addInterestLabel(labelIds, new ResultCallback<ResultBean<String>>() {
            @Override
            public void onError(int status, String errorMsg) {

            }

            @Override
            public void onResponse(ResultBean<String> response) {
                finish();
            }
        });
    }

    private void getMyInterestLabel(){
        RequestManager.getScheduleManager().findMyLabelListMoreByUserId(new ResultCallback<ResultBean<List<UserInterestInfo>>>() {
            @Override
            public void onError(int status, String errorMsg) {

            }

            @Override
            public void onResponse(ResultBean<List<UserInterestInfo>> response) {
                mylist=response.getData();
                    myadapter=new LabelGvMyadapter(LabelMangerAct.this,mylist);
                   gv1.setAdapter(myadapter);
            }
        });
    }

}