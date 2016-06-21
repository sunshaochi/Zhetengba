package com.boyuanitsm.zhetengba.fragment.circleFrg;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.boyuanitsm.zhetengba.ConstantValue;
import com.boyuanitsm.zhetengba.R;
import com.boyuanitsm.zhetengba.activity.MainAct;
import com.boyuanitsm.zhetengba.adapter.ChanAdapter;
import com.boyuanitsm.zhetengba.base.BaseFragment;
import com.boyuanitsm.zhetengba.bean.ChannelTalkEntity;
import com.boyuanitsm.zhetengba.bean.DataBean;
import com.boyuanitsm.zhetengba.bean.ImageInfo;
import com.boyuanitsm.zhetengba.bean.ResultBean;
import com.boyuanitsm.zhetengba.http.callback.ResultCallback;
import com.boyuanitsm.zhetengba.http.manager.RequestManager;
import com.boyuanitsm.zhetengba.utils.LayoutHelperUtil;
import com.boyuanitsm.zhetengba.utils.MyLogUtils;
import com.boyuanitsm.zhetengba.utils.Uitls;
import com.boyuanitsm.zhetengba.utils.ZtinfoUtils;
import com.boyuanitsm.zhetengba.view.refresh.PullToRefreshBase;
import com.boyuanitsm.zhetengba.view.refresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 频道里
 * viewpager适配的fragment
 * Created by xiaoke on 2016/5/3.
 */
public class ChaChildFrg extends BaseFragment {
    private List<String> list = new ArrayList<String>();
    private int flag;
    private List<List<ImageInfo>> datalist;
    private PullToRefreshListView lv_ch01;
    private int page=1;
    private int rows=10;
    private ChanAdapter adapter;
    private String lableId;
    private List<ChannelTalkEntity> channelTalkEntityList;

    @Override
    public View initView(LayoutInflater inflater) {
         view= inflater.inflate(R.layout.frag_chanel_child01,null);

        return view;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initView(view);
    }

    private void initView(View view) {
        lv_ch01 = (PullToRefreshListView) view.findViewById(R.id.lv_ch01);
        //传入参数，标签对应集合
        LayoutHelperUtil.freshInit(lv_ch01);
//        initDate();
//        datalist=new ArrayList<>();
//        getChannelTalks(lableId,page,rows);
//        adapter=new ChanAdapter(mActivity,datalist);
//        lv_ch01.getRefreshableView().setAdapter(adapter);
        lv_ch01.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                lv_ch01.setLastUpdatedLabel(ZtinfoUtils.getCurrentTime());
                page=1;
                getChannelTalks(lableId,page,rows);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                getChannelTalks(lableId,page,rows);
            }
        });

    }
//    public void setData(List<ChanelInfo> info){
//
//        adapter.notifyDateCshange();
//    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if(bundle != null){
            list = bundle.getStringArrayList("content");
            flag = bundle.getInt("flag");
        }
    }
    public static ChaChildFrg newInstance(List<String> contentList,int flag){
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("content", (ArrayList<String>) contentList);
        bundle.putInt("flag", flag);
        ChaChildFrg testFm = new ChaChildFrg();
        testFm.setArguments(bundle);
        return testFm;

    }

    private List<ChannelTalkEntity> datas=new ArrayList<>();
    /**
     * 获取频道说说列表
     * @param lableId
     * @param page
     * @param rows
     */
    private void getChannelTalks(String lableId, final int page,int rows){
        channelTalkEntityList=new ArrayList<>();
        datalist=new ArrayList<>();
        RequestManager.getTalkManager().getChannelTalks(lableId, page, rows, new ResultCallback<ResultBean<DataBean<ChannelTalkEntity>>>() {
            @Override
            public void onError(int status, String errorMsg) {
                lv_ch01.onPullUpRefreshComplete();
                lv_ch01.onPullDownRefreshComplete();
            }

            @Override
            public void onResponse(ResultBean<DataBean<ChannelTalkEntity>> response) {
                lv_ch01.onPullUpRefreshComplete();
                lv_ch01.onPullDownRefreshComplete();
                channelTalkEntityList=response.getData().getRows();
                if (channelTalkEntityList.size() == 0) {
                    if (page == 1) {

                    } else {
                        lv_ch01.setHasMoreData(false);
                    }
                }
                if(page==1){
                    datas.clear();
                }
                datas.addAll(channelTalkEntityList);
                for (int j=0;j<datas.size();j++) {
                    List<ImageInfo> itemList=new ArrayList<>();
                    //将图片地址转化成数组
                    if(!TextUtils.isEmpty(datas.get(j).getChannelImage())) {
                        String[] urlList = ZtinfoUtils.convertStrToArray(datas.get(j).getChannelImage());
                        for (int i = 0; i < urlList.length; i++) {
                            itemList.add(new ImageInfo(urlList[i], 1624, 914));
                        }
                    }
                    datalist.add(itemList);
                }
                if(adapter==null) {
                    adapter=new ChanAdapter(mActivity,datalist,datas);
                    lv_ch01.getRefreshableView().setAdapter(adapter);
                }else {
                    adapter.notifyChange(datalist,datas);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (receiverTalk==null){
            receiverTalk=new MyBroadCastReceiverTalk();
            mActivity.registerReceiver(receiverTalk, new IntentFilter(CHANNELTALKS));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(receiverTalk!=null){
            mActivity.unregisterReceiver(receiverTalk);
            receiverTalk=null;
        }
    }
    private MyBroadCastReceiverTalk receiverTalk;
    public static final String CHANNELTALKS="chanleTalk_update";
    private class MyBroadCastReceiverTalk extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int rFlag = intent.getIntExtra("flag",0);
            if(rFlag==flag){
                page=1;
                lableId=((MainAct)getActivity()).getLabelId();
                MyLogUtils.degug("=======label"+lableId);
                getChannelTalks(lableId,page, rows);
            }

        }
    }

}
