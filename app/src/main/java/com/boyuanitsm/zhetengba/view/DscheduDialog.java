package com.boyuanitsm.zhetengba.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.boyuanitsm.zhetengba.R;
import com.boyuanitsm.zhetengba.adapter.CalDialogAdapter;
import com.boyuanitsm.zhetengba.bean.ResultBean;
import com.boyuanitsm.zhetengba.bean.SimpleInfo;
import com.boyuanitsm.zhetengba.http.callback.ResultCallback;
import com.boyuanitsm.zhetengba.http.manager.RequestManager;
import com.boyuanitsm.zhetengba.utils.MyToastUtils;

import java.util.List;

/**
 * 首页邀请dialog
 * Created by bitch-1 on 2016/11/16.
 */
public class DscheduDialog extends Dialog {
    private Context context;
    private List<SimpleInfo> simpleInfos;
    private String creatId;

    public DscheduDialog(Context context, int themeResId,List<SimpleInfo> list,String creatId) {
        super(context, themeResId);
        this.context=context;
        this.simpleInfos=list;
        this.creatId=creatId;
    }

    public DscheduDialog(Context context,List<SimpleInfo> list,String creatId) {
        this(context, R.style.Cal_Dialog,list,creatId);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.act_schule_layout,null);
        setContentView(view);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        ListView lv_theme = (ListView) view.findViewById(R.id.lv_theme);
        TextView tv_tishi= (TextView) view.findViewById(R.id.tv_tishi);
        LinearLayout rl_plan = (LinearLayout) view.findViewById(R.id.rl_plan);
        rl_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        if (simpleInfos.size()>0){
            tv_tishi.setVisibility(View.GONE);
            lv_theme.setVisibility(View.VISIBLE);
            CalDialogAdapter adapter=new CalDialogAdapter(context,simpleInfos);
            lv_theme.setAdapter(adapter);
            lv_theme.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    simpleInfos.get(position).getId();
//                    aboutTa(strid,simpleInfos.get(position).getId());
                    addActivityAbout(simpleInfos.get(position).getId(),creatId);
                    dismiss();
                }
            });
        }else {
            lv_theme.setVisibility(View.GONE);
            tv_tishi.setVisibility(View.VISIBLE);
            tv_tishi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }


    }

    private void addActivityAbout(String activityId,String personId ) {
        RequestManager.getScheduleManager().addActAbout(activityId, personId, new ResultCallback<ResultBean<String>>() {
            @Override
            public void onError(int status, String errorMsg) {
                MyToastUtils.showShortToast(context,errorMsg);
            }

            @Override
            public void onResponse(ResultBean<String> response) {
                MyToastUtils.showShortToast(context,response.getMessage());
            }
        });
    }


}

