package com.boyuanitsm.zhetengba.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boyuanitsm.zhetengba.R;
import com.boyuanitsm.zhetengba.activity.circle.CircleglAct;
import com.boyuanitsm.zhetengba.view.CircleImageView;

import java.util.List;

/**
 * 个人主页界面她的圈子下面的 水平listview适配器
 * Created by bitch-1 on 2016/5/16.
 */
public class HlvppAdapter extends BaseAdapter {
    private Context context;
    List<Integer>list;

    public HlvppAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=View.inflate(context, R.layout.item_hlvpp,null);
        CircleImageView cir_pagehand= (CircleImageView) view.findViewById(R.id.cir_pagehand);
        TextView tv_more= (TextView) view.findViewById(R.id.tv_more);
        LinearLayout ll_quanzi= (LinearLayout) view.findViewById(R.id.ll_quanzi);
        if(position==4){
            cir_pagehand.setImageResource(R.mipmap.cirxq_more);
            tv_more.setText("更多");
            ll_quanzi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, CircleglAct.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }else {
//            ll_quanzi.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent=new Intent(context, CirFriendAct.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(intent);
//                }
//            });

        }
        return view;
    }
}
