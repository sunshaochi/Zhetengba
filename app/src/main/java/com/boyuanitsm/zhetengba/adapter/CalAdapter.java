package com.boyuanitsm.zhetengba.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.boyuanitsm.zhetengba.R;
import com.boyuanitsm.zhetengba.activity.PersonalAct;
import com.boyuanitsm.zhetengba.activity.ShareDialogAct;
import com.boyuanitsm.zhetengba.bean.ResultBean;
import com.boyuanitsm.zhetengba.bean.ScheduleInfo;
import com.boyuanitsm.zhetengba.bean.SimpleInfo;
import com.boyuanitsm.zhetengba.db.UserInfoDao;
import com.boyuanitsm.zhetengba.http.callback.ResultCallback;
import com.boyuanitsm.zhetengba.http.manager.RequestManager;
import com.boyuanitsm.zhetengba.utils.MyToastUtils;
import com.boyuanitsm.zhetengba.utils.Uitls;
import com.boyuanitsm.zhetengba.utils.ZhetebaUtils;
import com.boyuanitsm.zhetengba.view.CircleImageView;
import com.boyuanitsm.zhetengba.view.MyAlertDialog;
import com.boyuanitsm.zhetengba.view.ScheduDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页有空 listview适配器
 * Created by xiaoke on 2016/4/27.
 */
public class CalAdapter extends BaseAdapter {
    private Context context;
    private List<ScheduleInfo> list;
    private String strStart,strEnd;
    private List<SimpleInfo> simpleInfos;
    private int index;
    private PopupWindow popupWindow;

    // 图片缓存 默认 等
    private DisplayImageOptions optionsImag = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.mipmap.userhead)
            .showImageOnFail(R.mipmap.userhead).cacheInMemory(true).cacheOnDisk(true)
            .considerExifParams(true).imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565).build();

    public CalAdapter(Context context, List<ScheduleInfo> list) {
        this.context = context;
        this.list = list;

    }

    public void update(List<ScheduleInfo> datas) {
        this.list = datas;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
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
    public View getView(final int position,   View convertView, ViewGroup parent) {
        index=position;
        final CalHolder calHolder;
        if (convertView != null && convertView.getTag() != null) {
            calHolder = (CalHolder) convertView.getTag();
        } else {
            convertView = View.inflate(context, R.layout.item_calen, null);
            calHolder = new CalHolder();
            calHolder.iv_icon = (CircleImageView) convertView.findViewById(R.id.iv_icon);
            calHolder.tv_Name = (TextView) convertView.findViewById(R.id.tv_Name);
            calHolder.iv_gen = (ImageView) convertView.findViewById(R.id.iv_gen);
            calHolder.tv_time_cal = (TextView) convertView.findViewById(R.id.tv_time_cal);
            calHolder.tv_state = (TextView) convertView.findViewById(R.id.tv_state);
            calHolder.ll_yh = (LinearLayout) convertView.findViewById(R.id.ll_yh);
            calHolder.iv_bag= (ImageView) convertView.findViewById(R.id.iv_bag);
            calHolder.iv_bag2= (ImageView) convertView.findViewById(R.id.iv_bag2);
            calHolder.rl_main= (LinearLayout) convertView.findViewById(R.id.rl_main);
            convertView.setTag(calHolder);
        }
        if (!UserInfoDao.getUser().getId().equals(list.get(position).getCreatePersonId())){//用户id不得与创建id
            calHolder.iv_bag.setVisibility(View.VISIBLE);
            calHolder.iv_bag2.setVisibility(View.GONE);
            if (list.get(position).isAgreeAbout()){
                calHolder.iv_bag.setImageResource(R.mipmap.yue);
                calHolder.iv_bag.setEnabled(false);
            }else{
                calHolder.iv_bag.setEnabled(true);
                if (list.get(position).getDictName().equals("闲来无事")){
                    calHolder.iv_bag.setImageResource(R.mipmap.lv);
                }else if (list.get(position).getDictName().equals("百无聊懒")){
                    calHolder.iv_bag.setImageResource(R.mipmap.huang);
                }else if (list.get(position).getDictName().equals("闲的要死")){
                    calHolder.iv_bag.setImageResource(R.mipmap.hong);
                }else if (list.get(position).getDictName().equals("无聊至极")){
                    calHolder.iv_bag.setImageResource(R.mipmap.lan);
                }
            }

        }else {
            calHolder.iv_bag2.setVisibility(View.VISIBLE);
            calHolder.iv_bag.setVisibility(View.GONE);
            calHolder.iv_bag2.setImageResource(R.mipmap.bmore);
        }
        calHolder.iv_bag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        calHolder.iv_bag.setEnabled(false);
                simpleInfos=new ArrayList<SimpleInfo>();
                RequestManager.getScheduleManager().findMatchingActivities(list.get(position).getScheduleId(), new ResultCallback<ResultBean<List<SimpleInfo>>>() {
                    @Override
                    public void onError(int status, String errorMsg) {
                        calHolder.iv_bag.setEnabled(true);
                    }

                    @Override
                    public void onResponse(ResultBean<List<SimpleInfo>> response) {
                        simpleInfos = response.getData();
                        ScheduDialog dialog = new ScheduDialog(context, simpleInfos, list.get(position).getScheduleId(),position);
                        dialog.show();
                        calHolder.iv_bag.setEnabled(true);
                    }
                });
            }
        });
        calHolder.iv_bag2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow(calHolder.rl_main,position);
            }
        });
        if (list != null) {
            ImageLoader.getInstance().displayImage(Uitls.imageFullUrl(list.get(position).getUserIcon()), calHolder.iv_icon, optionsImag);//用户头像；
            if (!TextUtils.isEmpty(list.get(position).getUserNm())) {
                calHolder.tv_Name.setText(list.get(position).getUserNm());//用户昵称
            } else {
                calHolder.tv_Name.setText("无用户名");
            }
            if (!TextUtils.isEmpty(list.get(position).getUserSex())){
                if (list.get(position).getUserSex().equals(1+"")) {
                    calHolder.iv_gen.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.male));//用户性别
                } else if (list.get(position).getUserSex().equals(0+"")){
                    calHolder.iv_gen.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.female));
                }
            }
            strStart=ZhetebaUtils.timeToDate(Long.parseLong(list.get(position).getStartTime()));
            strEnd=ZhetebaUtils.timeToDate(Long.parseLong(list.get(position).getEndTime()));
            if (strStart.substring(1,6).equals(strEnd.substring(1,6))){
                String strTime=strStart+"—"+strEnd.substring(6);
                calHolder.tv_time_cal.setText(strTime);//活动时间；
            }else {
                calHolder.tv_time_cal.setText(strStart + "—" + strEnd);//活动时间；
            }
            if (!TextUtils.isEmpty(list.get(position).getDictName())) {
                calHolder.tv_state.setText(list.get(position).getDictName());//标签名称
                if (list.get(position).getDictName().equals("闲来无事")){
                    calHolder.tv_state.setBackgroundResource(R.drawable.rdbt_xl_check);
                }else if (list.get(position).getDictName().equals("百无聊懒")){
                    calHolder.tv_state.setBackgroundResource(R.drawable.rdbt_bw_check);
                }else if (list.get(position).getDictName().equals("闲的要死")){
                    calHolder.tv_state.setBackgroundResource(R.drawable.rdbt_wl_check);
                }else if (list.get(position).getDictName().equals("无聊至极")){
                    calHolder.tv_state.setBackgroundResource(R.drawable.rdbt_ys_check);
                }
            }

            //点击头像昵称进入个人主页
            calHolder.iv_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("userId", list.get(position).getUserId());
                    bundle.putBoolean("friend",list.get(position).isFriend());
                    intent.putExtras(bundle);
                    intent.setClass(context, PersonalAct.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
        return convertView;
    }

    /**
     *
     */
    private void showPopupWindow(View parent, final int position) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(context).inflate(
                R.layout.popuwindowsxx_dialog, null);

        // 实例化popupWindow
         popupWindow = new PopupWindow(layout, AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT);
        //控制键盘是否可以获得焦点
        popupWindow.setFocusable(true);
        //设置popupWindow弹出窗体的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable(null, ""));
        WindowManager manager = (WindowManager)context. getSystemService(context.WINDOW_SERVICE);
        @SuppressWarnings("deprecation")
        //获取xoff
                int xpos = manager.getDefaultDisplay().getWidth() / 2 - popupWindow.getWidth() / 2;
        //xoff,yoff基于anchor的左下角进行偏移。
        popupWindow.showAsDropDown(parent, xpos, -25);

        LinearLayout iv_shanc= (LinearLayout) layout.findViewById(R.id.iv_shanc);
        LinearLayout iv_fenxiang= (LinearLayout) layout.findViewById(R.id.iv_fenxiang);

        iv_shanc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MyAlertDialog(context).builder().setTitle("提示").setMsg("确认删除此条档期？").setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //调用删除此活动接口,刷新数据；
                        removeSchuldel(list.get(position).getScheduleId(), position);
                    }
                }).setNegativeButton("取消", null).show();
            }
        });

        iv_fenxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("type",2);
                intent.putExtra("id",list.get(position).getScheduleId());
                intent.setClass(context,ShareDialogAct.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }


    public static class CalHolder {
        public CircleImageView iv_icon;//头像
        public TextView tv_Name;//昵称
        public ImageView iv_gen;//性别
        public TextView tv_time_cal;//活动日期
        public TextView tv_state;//状态
        public LinearLayout ll_yh;//约会
        private ImageView iv_bag,iv_bag2;//档期列表右边的
        private LinearLayout rl_main;//item大布局
    }
    /***
     * 删除档期
     * @param id
     * @param position
     */
    private void removeSchuldel(String id, final int position){
        RequestManager.getScheduleManager().removeSchuldel(id, new ResultCallback<ResultBean<String>>() {
            @Override
            public void onError(int status, String errorMsg) {

            }

            @Override
            public void onResponse(ResultBean<String> response) {
                popupWindow.dismiss();
                MyToastUtils.showShortToast(context, "删除档期成功！");
                list.remove(position);
                notifyDataSetChanged();
            }
        });
    }



}
