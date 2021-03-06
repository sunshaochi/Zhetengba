package com.boyuanitsm.zhetengba.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boyuanitsm.zhetengba.R;
import com.boyuanitsm.zhetengba.activity.MainAct;
import com.boyuanitsm.zhetengba.activity.PersonalAct;
import com.boyuanitsm.zhetengba.activity.ShareDialogAct;
import com.boyuanitsm.zhetengba.activity.mine.MyColleitionAct;
import com.boyuanitsm.zhetengba.bean.ResultBean;
import com.boyuanitsm.zhetengba.bean.SimpleInfo;
import com.boyuanitsm.zhetengba.chat.db.InviteMessgeDao;
import com.boyuanitsm.zhetengba.db.UserInfoDao;
import com.boyuanitsm.zhetengba.http.callback.ResultCallback;
import com.boyuanitsm.zhetengba.http.manager.RequestManager;
import com.boyuanitsm.zhetengba.utils.MyToastUtils;
import com.boyuanitsm.zhetengba.utils.Uitls;
import com.boyuanitsm.zhetengba.utils.ZhetebaUtils;
import com.boyuanitsm.zhetengba.view.CircleImageView;
import com.boyuanitsm.zhetengba.view.CustomDialog;
import com.boyuanitsm.zhetengba.view.DscheduDialog;
import com.boyuanitsm.zhetengba.view.MyAlertDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页会友：listview适配器.
 * 设置条目点击
 * Created by xiaoke on 2016/4/25.
 */
public class ActAdapter extends BaseAdapter {
    private Context context;
    private List<SimpleInfo> infos = new ArrayList<>();
    private String strStart, strEnd;
    private LocalBroadcastManager broadcastManager;
    private InviteMessgeDao inviteMessgeDao;
    private List<SimpleInfo> simpleInfos;
    // 图片缓存 默认 等
    private DisplayImageOptions optionsImag = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.mipmap.userhead)
            .showImageOnFail(R.mipmap.userhead).cacheInMemory(true).cacheOnDisk(true)
            .considerExifParams(true).imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565).build();

    private DisplayImageOptions optionsImagd = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.mipmap.userhead)
            .showImageOnFail(R.mipmap.userhead).cacheInMemory(true).cacheOnDisk(true)
            .considerExifParams(true).imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565).build();

    public ActAdapter(Context context, List<SimpleInfo> infos) {
        this.infos = infos;
        this.context = context;
    }

    public void update(List<SimpleInfo> list) {
        this.infos = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return infos == null ? 0 : infos.size();
    }

    @Override
    public Object getItem(int position) {

        return infos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder viewHolder;
        final CustomDialog.Builder builder=new CustomDialog.Builder(context);
        if (convertView != null && convertView.getTag() != null) {
            viewHolder = (Holder) convertView.getTag();

        } else {
            convertView = View.inflate(context, R.layout.item_act, null);
            viewHolder = new Holder();
            viewHolder.iv_headphoto = (CircleImageView) convertView.findViewById(R.id.iv_headphoto);
            viewHolder.tv_niName = (TextView) convertView.findViewById(R.id.tv_niName);
            viewHolder.ll_person = (LinearLayout) convertView.findViewById(R.id.ll_person);
            viewHolder.tv_hdtheme = (TextView) convertView.findViewById(R.id.tv_hdtheme);
            viewHolder.tv_loaction = (TextView) convertView.findViewById(R.id.tv_loaction);
            viewHolder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            viewHolder.ll_guanzhu = (LinearLayout) convertView.findViewById(R.id.ll_guanzhu);
            viewHolder.iv_simple_guanzhu = (ImageView) convertView.findViewById(R.id.iv_simple_guanzhu);
            viewHolder.ll_join = (LinearLayout) convertView.findViewById(R.id.ll_join);
            viewHolder.iv_join = (ImageView) convertView.findViewById(R.id.iv_join);
            viewHolder.tv_join_num = (TextView) convertView.findViewById(R.id.tv_join_num);
            viewHolder.tv_join_tal_num = (TextView) convertView.findViewById(R.id.tv_join_tal_num);
            viewHolder.iv_gender = (ImageView) convertView.findViewById(R.id.iv_gender);
            viewHolder.iv_actdetial = (CircleImageView) convertView.findViewById(R.id.iv_actdetial);
            viewHolder.tv_text_jion = (TextView) convertView.findViewById(R.id.tv_text_jion);
            viewHolder.ll_show = (LinearLayout) convertView.findViewById(R.id.ll_show);
            viewHolder.ll_show2 = (LinearLayout) convertView.findViewById(R.id.ll_show2);
            viewHolder.ll_show3 = (RelativeLayout) convertView.findViewById(R.id.ll_show3);
            viewHolder.ll_del = (LinearLayout) convertView.findViewById(R.id.ll_del);
            viewHolder.ll_simple_share = (LinearLayout) convertView.findViewById(R.id.ll_simple_share);
            viewHolder.ll_theme_location = (LinearLayout) convertView.findViewById(R.id.ll_theme_location);
            viewHolder.tv_cj= (TextView) convertView.findViewById(R.id.tv_cj);//自己发布活动参加人数
            viewHolder.tv_tt= (TextView) convertView.findViewById(R.id.tv_tt);//能参与的总人数
            viewHolder.ll_yaoqin= (LinearLayout) convertView.findViewById(R.id.ll_yaoqin);//邀请
            convertView.setTag(viewHolder);

        }
        if (!TextUtils.isEmpty(infos.get(position).getUserNm())) {
            viewHolder.tv_niName.setText(infos.get(position).getUserNm());//字段缺少用户名
        }
        if (!TextUtils.isEmpty(infos.get(position).getActivitySite())) {
            viewHolder.ll_theme_location.setVisibility(View.VISIBLE);
            viewHolder.tv_loaction.setText(infos.get(position).getActivitySite());//活动位置
        } else {
            viewHolder.ll_theme_location.setVisibility(View.GONE);
        }

        viewHolder.tv_hdtheme.setText(infos.get(position).getActivityTheme());//活动主题
        if (!TextUtils.isEmpty(infos.get(position).getCreatePersonId())){
            if (!TextUtils.isEmpty(UserInfoDao.getUser().getId())){
                if (!infos.get(position).getCreatePersonId().equals(UserInfoDao.getUser().getId())) {
                    viewHolder.ll_guanzhu.setVisibility(View.VISIBLE);
                    viewHolder.ll_join.setVisibility(View.VISIBLE);
                    viewHolder.ll_del.setVisibility(View.GONE);
                    viewHolder.ll_simple_share.setVisibility(View.GONE);
                    viewHolder.ll_yaoqin.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.ll_guanzhu.setVisibility(View.GONE);
                    viewHolder.ll_join.setVisibility(View.GONE);
                    viewHolder.ll_del.setVisibility(View.VISIBLE);
                    viewHolder.ll_simple_share.setVisibility(View.VISIBLE);
                    viewHolder.tv_tt.setText("/" + infos.get(position).getInviteNumber());//自己发布总人数
                    viewHolder.tv_cj.setText(infos.get(position).getMemberNum()+"");//已经3响应人数
                    viewHolder.ll_yaoqin.setVisibility(View.GONE);
                }
            }
        }

//        if (infos.get(position).getFollowNum() == 0) {
//            viewHolder.tv_guanzhu_num.setVisibility(View.GONE);
//        } else {
//            viewHolder.tv_guanzhu_num.setVisibility(View.VISIBLE);
//            viewHolder.tv_guanzhu_num.setText(infos.get(position).getFollowNum() + "");//关注人数
//        }
        viewHolder.tv_join_num.setText(infos.get(position).getMemberNum() + "");//目前成员数量；
        viewHolder.tv_join_tal_num.setText(infos.get(position).getInviteNumber() + "");//邀约人数
        strStart = ZhetebaUtils.timeToDate(Long.parseLong(infos.get(position).getStartTime()));
        strEnd = ZhetebaUtils.timeToDate(Long.parseLong(infos.get(position).getEndTime()));
        if (strStart.substring(1, 6).equals(strEnd.substring(1, 6))) {
            String strTime = strStart + "—" + strEnd.substring(6);
            viewHolder.tv_date.setText(strTime);//活动时间；
        } else {
            viewHolder.tv_date.setText(strStart + "—" + strEnd);//活动时间；
        }
//        if (UserInfoDao.getUser().getId().equals(infos.get(position).getUserId())) {
//            ImageLoader.getInstance().displayImage(Uitls.imageFullUrl(UserInfoDao.getUser().getIcon()), viewHolder.iv_headphoto, optionsImag);
//        } else {
            ImageLoader.getInstance().displayImage(Uitls.imageFullUrl(infos.get(position).getUserIcon()), viewHolder.iv_headphoto, optionsImag);//用户头像
//        }
        if (!TextUtils.isEmpty(infos.get(position).getUserSex())) {
            if (infos.get(position).getUserSex().equals(1 + "")) {
                viewHolder.iv_gender.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.male));//用户性别
            } else if (infos.get(position).getUserSex().equals(0 + "")) {
                viewHolder.iv_gender.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.female));//用户性别
            }
        }

        if (infos.get(position).getIcon() != null) {
            ImageLoader.getInstance().displayImage(Uitls.imageFullUrl(infos.get(position).getIcon()), viewHolder.iv_actdetial, optionsImagd);//详情icon
        }
//        返回状态判断是否参加，
        if (infos.get(position).isJoining()) {
            viewHolder.iv_join.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.cancel));//参加icon
            viewHolder.tv_text_jion.setText("取消响应");
            viewHolder.tv_join_num.setTextColor(Color.parseColor("#fd3838"));

        } else {
            viewHolder.iv_join.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.add));//参加icon
            viewHolder.tv_text_jion.setText("响应");
            viewHolder.tv_join_num.setTextColor(Color.parseColor("#999999"));
        }
        if (!TextUtils.isEmpty(infos.get(position).getActivityParticulars())) {
            builder.setMessage(infos.get(position).getActivityParticulars());
        } else {
            builder.setMessage("没有详情");
        }
        if (!TextUtils.isEmpty(infos.get(position).getCreatePersonId())){
            if (!TextUtils.isEmpty(UserInfoDao.getUser().getId())){
                if (!infos.get(position).getCreatePersonId().equals(UserInfoDao.getUser().getId())) {
                    if (!infos.get(position).isFriend()){
                        builder.setNegativeButton("加为好友", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putString("userId", infos.get(position).getUserId());
                                bundle.putBoolean("friend", infos.get(position).isFriend());
                                intent.putExtras(bundle);
                                intent.setClass(context, PersonalAct.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                                dialog.dismiss();
                            }
                        });
                    }else {
                        builder.setNegativeButton("我们是好友", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                    }
                }
            }
        }
        if (infos.get(position).getJoinCount() > 0) {
            builder.setPositiveButton("一起参加了" + infos.get(position).getJoinCount() + "次档期", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }

        if (infos.get(position).getMemberNum() == infos.get(position).getInviteNumber()) {
            if (!infos.get(position).isJoining()) {
                viewHolder.ll_join.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MyToastUtils.showShortToast(context, "参加人数已满,请参加其他档期！");
                    }
                });
            } else {
                viewHolder.ll_join.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewHolder.ll_join.setEnabled(false);
                        if (infos.get(position).isJoining()) {
                            final MyAlertDialog dialog = new MyAlertDialog(context);
                            dialog.builder().setTitle("提示").setMsg("确认取消参加档期？").setPositiveButton("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    stateCancelChange(position, viewHolder);
                                }
                            }).setNegativeButton("取消", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    viewHolder.ll_join.setEnabled(true);
                                }
                            }).show();
                        } else {
                            stateJionChange(position, viewHolder);
                        }
                    }
                });
            }
        } else {
            viewHolder.ll_join.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.ll_join.setEnabled(false);
                    if (infos.get(position).isJoining()) {
                        final MyAlertDialog dialog = new MyAlertDialog(context);
                        dialog.builder().setTitle("提示").setMsg("确认取消参加档期？").setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                stateCancelChange(position, viewHolder);
                            }
                        }).setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                viewHolder.ll_join.setEnabled(true);
                            }
                        }).show();
                    } else {
                        stateJionChange(position, viewHolder);
                    }


                }
            });
        }


        viewHolder.ll_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MyAlertDialog(context).builder().setTitle("提示").setMsg("确认删除此条档期？").setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //调用删除此活动接口,刷新数据；
                        removeActivity(infos.get(position).getId(), position);
                    }
                }).setNegativeButton("取消", null).show();

            }
        });
        viewHolder.ll_simple_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //开启分享界面
                Intent intent = new Intent();
                intent.putExtra("type", 1);
                intent.putExtra("id", infos.get(position).getId());
                intent.putExtra("activitytheme", infos.get(position).getActivityTheme());
                intent.setClass(context, ShareDialogAct.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


//        返回状态判断是否关注;
        if (infos.get(position).isFollow()) {
            viewHolder.iv_simple_guanzhu.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.collect_c));//已关注
//            viewHolder.tv_text_guanzhu.setText("已关注");
            viewHolder.ll_guanzhu.setEnabled(false);
        } else {
            viewHolder.iv_simple_guanzhu.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.collect));//默认图标
//            viewHolder.tv_text_guanzhu.setText("关注");
            viewHolder.ll_guanzhu.setEnabled(true);
        }
        viewHolder.ll_guanzhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //接口调用
                RequestManager.getScheduleManager().getActivityCollection(infos.get(position).getId(), new ResultCallback<ResultBean<String>>() {
                    @Override
                    public void onError(int status, String errorMsg) {
                        MyToastUtils.showShortToast(context, errorMsg);
                    }

                    @Override
                    public void onResponse(ResultBean<String> response) {
                        infos.get(position).setFollow(true);
                        int noticNum = infos.get(position).getFollowNum();
                        noticNum = noticNum + 1;
                        infos.get(position).setFollowNum(noticNum);
//                        viewHolder.tv_guanzhu_num.setVisibility(View.VISIBLE);
                        viewHolder.ll_guanzhu.setClickable(false);
                        context.sendBroadcast(new Intent(MyColleitionAct.COLLECTION));
                        notifyDataSetChanged();
                        MyToastUtils.showShortToast(context, response.getMessage());
                    }
                });
            }
        });
        View.OnClickListener listenerDetial=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.create().show();
            }
        };
        viewHolder.iv_actdetial.setOnClickListener(listenerDetial);
        viewHolder.ll_show.setOnClickListener(listenerDetial);
        viewHolder.ll_show2.setOnClickListener(listenerDetial);
        viewHolder.ll_show3.setOnClickListener(listenerDetial);

        //展示个人资料
        View.OnClickListener listener1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("userId", infos.get(position).getUserId());
                bundle.putBoolean("friend", infos.get(position).isFriend());
                intent.putExtras(bundle);
                intent.setClass(context, PersonalAct.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        };
        viewHolder.iv_headphoto.setOnClickListener(listener1);
        viewHolder.tv_niName.setOnClickListener(listener1);
        //邀请相匹配
        viewHolder.ll_yaoqin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestManager.getScheduleManager().findWithAct(infos.get(position).getId(), new ResultCallback<ResultBean<List<SimpleInfo>>>() {
                    @Override
                    public void onError(int status, String errorMsg) {

                    }

                    @Override
                    public void onResponse(ResultBean<List<SimpleInfo>> response) {
                        simpleInfos=response.getData();
                        if (simpleInfos!=null&&simpleInfos.size()>0){
                            DscheduDialog dialog=new DscheduDialog(context,simpleInfos,infos.get(position).getCreatePersonId());
                            dialog.show();
                        }else {
                            MyToastUtils.showShortToast(context,"没有相匹配的档期");
                            return;
                        }


                    }
                });
            }
        });
          /*init(view);*/
         /* initData();*/
        return convertView;
    }

    /**
     * 参加或响应活动接口
     *
     * @param
     */

    private void stateJionChange(final int position, final Holder viewHolder) {
        RequestManager.getScheduleManager().getRespondActivity(infos.get(position).getId(), new ResultCallback<ResultBean<String>>() {
            @Override
            public void onError(int status, String errorMsg) {
                viewHolder.ll_join.setEnabled(true);
                MyToastUtils.showShortToast(context, errorMsg);
            }

            @Override
            public void onResponse(ResultBean<String> response) {
                if (TextUtils.equals(response.getData(), 1 + "")) {
                    viewHolder.ll_join.setEnabled(true);
                    int i = infos.get(position).getMemberNum();
                    i = i + 1;
                    infos.get(position).setMemberNum(i);
                    infos.get(position).setJoining(true);
                    context.sendBroadcast(new Intent(MyColleitionAct.COLLECTION));
                    notifyDataSetChanged();
                    addGroup(infos.get(position).getId());
                } else if (TextUtils.equals(response.getData(), 0 + "")) {
                    infos.remove(position);
                    viewHolder.ll_join.setEnabled(true);
                    notifyDataSetChanged();
                    MyToastUtils.showShortToast(context, response.getMessage());
                } else if (TextUtils.equals(response.getData(), -1 + "")) {
                    viewHolder.ll_join.setEnabled(true);
                    MyToastUtils.showShortToast(context, response.getMessage());
                } else if (TextUtils.equals(response.getData(), -2 + "")) {
                    viewHolder.ll_join.setEnabled(true);
                    MyToastUtils.showShortToast(context, response.getMessage());
                }

            }
        });
    }

    /**
     * 取消参加或响应活动接口
     *
     * @param
     */

    private void stateCancelChange(final int position, final Holder viewHolder) {
        RequestManager.getScheduleManager().cancelActivity(infos.get(position).getId(), new ResultCallback<ResultBean<String>>() {
            @Override
            public void onError(int status, String errorMsg) {
                viewHolder.ll_join.setEnabled(true);
                MyToastUtils.showShortToast(context,errorMsg);
            }

            @Override
            public void onResponse(ResultBean<String> response) {
                if (TextUtils.equals(response.getData(), 0 + "")) {
                    viewHolder.ll_join.setEnabled(true);
                    infos.remove(position);
                    notifyDataSetChanged();
                    MyToastUtils.showShortToast(context, "此条档期已被删除！");
                    return;
                } else if (TextUtils.equals(response.getData(), 1 + "")) {
                    viewHolder.ll_join.setEnabled(true);
                    int i = infos.get(position).getMemberNum();
                    i = i - 1;
                    infos.get(position).setJoining(false);
                    infos.get(position).setMemberNum(i);
                    context.sendBroadcast(new Intent(MyColleitionAct.COLLECTION));
                    notifyDataSetChanged();
                    delGroup(infos.get(position).getId());
                } else if (TextUtils.equals(response.getData(), -2 + "")) {
                    viewHolder.ll_join.setEnabled(true);
                    MyToastUtils.showShortToast(context, response.getMessage());
                } else {
                    viewHolder.ll_join.setEnabled(true);
                }

            }
        });
    }

    /***
     * 删除活动
     *
     * @param id
     * @param position
     */
    private void removeActivity(String id, final int position) {
        RequestManager.getScheduleManager().removeActivity(id, new ResultCallback<ResultBean<String>>() {
            @Override
            public void onError(int status, String errorMsg) {
                MyToastUtils.showShortToast(context,errorMsg);
            }

            @Override
            public void onResponse(ResultBean<String> response) {
                MyToastUtils.showShortToast(context, "删除档期成功！");
                infos.remove(position);
                notifyDataSetChanged();
//                Intent intent=new Intent(context,MainAct.class);
//                context.sendBroadcast(intent);
            }
        });
    }

    /**
     * 参加成功后，调用添加群组
     *
     * @param actId
     */
    private void addGroup(String actId) {
        RequestManager.getScheduleManager().addHXGroup(actId, new ResultCallback<ResultBean<String>>() {
            @Override
            public void onError(int status, String errorMsg) {

            }

            @Override
            public void onResponse(ResultBean<String> response) {

            }
        });
    }

    /**
     * 移除群组
     *
     * @param activityId
     */
    private void delGroup(String activityId) {
        RequestManager.getScheduleManager().deleGroup(activityId, new ResultCallback<ResultBean<String>>() {
            @Override
            public void onError(int status, String errorMsg) {

            }

            @Override
            public void onResponse(ResultBean<String> response) {
                broadcastManager=  LocalBroadcastManager.getInstance(context);
                Intent intent=new Intent(context,MainAct.class);
                broadcastManager.sendBroadcast(intent);
            }
        });
    }



    public static class Holder {
        public CircleImageView iv_headphoto;//头像
        public TextView tv_niName;//昵称
        public LinearLayout ll_person;//个人信息id
        public TextView tv_hdtheme;//活动主题
        public TextView tv_loaction;//活动位置
        public TextView tv_date;//活动日期
        public LinearLayout ll_guanzhu;//关注数量
        public ImageView iv_simple_guanzhu;//关注图标
        public LinearLayout ll_join;//参加人数
        public ImageView iv_join;//参加头像
        public TextView tv_join_num;//参加数量
        public TextView tv_join_tal_num;//活动总人数设置
        public CircleImageView iv_actdetial;//活动标签
        public ImageView iv_gender;//性别
        public TextView tv_text_jion;//参加/取消参加
        public LinearLayout ll_theme_location;//活动位置Linear
        public LinearLayout ll_show, ll_show2;
        public LinearLayout ll_del, ll_simple_share;
        private RelativeLayout ll_show3;
        public TextView tv_cj,tv_tt;//自己发布后参加人数和总的人数
        public LinearLayout ll_yaoqin;//邀请
    }
}
