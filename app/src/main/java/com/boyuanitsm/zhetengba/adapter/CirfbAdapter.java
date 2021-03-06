package com.boyuanitsm.zhetengba.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.boyuanitsm.zhetengba.R;

/**
 * 圈子发布适配
 */
public class CirfbAdapter extends BaseAdapter {
    private TextView tv_qz;
    private int selectedPosition=-1;

    private final String TITLES[] = {"摄影圈", "聚餐圈", "吃饭圈", "桑拿圈", "足球圈",
            "八卦圈","保龄圈","娱乐圈","星座圈","地方圈","明清圈","东方圈"
    };

    private Context context;
    private int clickTemp = -1;//标识选择的Item
    public void setSeclection(int position) {
        clickTemp = position;
    }

    public CirfbAdapter(Context context) {
        this.context = context;
    }

    //这句是把listview的点击position,传递过来
    public void clearSelection(int position) {
        selectedPosition = position;
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }

    @Override
    public Object getItem(int position) {
        return TITLES[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=View.inflate(context, R.layout.cir_item,null);
        tv_qz= (TextView) view.findViewById(R.id.ck_xx);
        tv_qz.setText(TITLES[position]);

        if(selectedPosition==position){tv_qz.setBackgroundResource(R.drawable.cirfb_ckecked);
            tv_qz.setTextColor(Color.parseColor("#FFFFFF"));}
       else {tv_qz.setBackgroundResource(R.drawable.cirfb_uncheck);
            tv_qz.setTextColor(Color.parseColor("#666666"));}
        return view;
    }
}