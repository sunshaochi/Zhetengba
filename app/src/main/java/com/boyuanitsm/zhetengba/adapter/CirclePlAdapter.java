package com.boyuanitsm.zhetengba.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.boyuanitsm.zhetengba.R;
import com.boyuanitsm.zhetengba.bean.CircleEntity;
import com.boyuanitsm.zhetengba.utils.EmojUtils;
import com.boyuanitsm.zhetengba.utils.MyLogUtils;
import com.boyuanitsm.zhetengba.view.BadgeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoke on 2016/9/26.
 */
public class CirclePlAdapter extends BaseAdapter {
    private Context context;
    private List<CircleEntity> list=new ArrayList<>();
    public CirclePlAdapter(Context context,List<CircleEntity> list){
        this.context=context;
        this.list=list;
    }
    @Override
    public int getCount() {
        return list.size()>5?5:list.size();

    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PlHolder holder;
        if (convertView!=null&&convertView.getTag()!=null){
            holder = (PlHolder) convertView.getTag();
        }else {
            holder=new PlHolder();
            convertView= View.inflate(context, R.layout.item_pl_circle, null);
            holder.tv_ct = (TextView) convertView.findViewById(R.id.tv_ct);
        }
        if (!TextUtils.isEmpty(list.get(position).getFatherCommentId())){
            if (!TextUtils.isEmpty(list.get(position).getPetName())&&!TextUtils.isEmpty(list.get(position).getCommentedUsername())){
                String str=list.get(position).getPetName()+"回复"+list.get(position).getCommentedUsername()+":"+list.get(position).getCommentContent();
                SpannableStringBuilder builder = new SpannableStringBuilder(str);
                //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
                ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.parseColor("#52c791"));
                ForegroundColorSpan redSpan2 = new ForegroundColorSpan(Color.parseColor("#52c791"));
                builder.setSpan(redSpan, 0, list.get(position).getPetName().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(redSpan2,list.get(position).getPetName().length()+2,list.get(position).getPetName().length()+list.get(position).getCommentedUsername().length()+2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                holder.tv_ct.setText(builder);
            }
        }else {
            if (!TextUtils.isEmpty(list.get(position).getPetName())){
                SpannableStringBuilder style=new SpannableStringBuilder(list.get(position).getPetName()+ "："+ EmojUtils.decoder(list.get(position).getCommentContent()));
                style.setSpan(new ForegroundColorSpan(Color.parseColor("#52c791")), 0, list.get(position).getPetName().length()+1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                holder.tv_ct.setText(style);
            }
        }

        return convertView;
    }

//    public void update(List<CircleEntity> clist) {
//        this.list=clist;
//        notifyDataSetChanged();
//    }

    class  PlHolder{
        private TextView tv_ct;
    }
}
