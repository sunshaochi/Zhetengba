package com.boyuanitsm.zhetengba.adapter;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boyuanitsm.zhetengba.R;
import com.boyuanitsm.zhetengba.activity.ShareDialogAct;
import com.boyuanitsm.zhetengba.activity.circle.CircleTextAct;
import com.boyuanitsm.zhetengba.activity.mess.PerpageAct;
import com.boyuanitsm.zhetengba.bean.CircleEntity;
import com.boyuanitsm.zhetengba.bean.ImageInfo;
import com.boyuanitsm.zhetengba.bean.ResultBean;
import com.boyuanitsm.zhetengba.http.callback.ResultCallback;
import com.boyuanitsm.zhetengba.http.manager.RequestManager;
import com.boyuanitsm.zhetengba.utils.LayoutHelperUtil;
import com.boyuanitsm.zhetengba.utils.MyToastUtils;
import com.boyuanitsm.zhetengba.utils.Uitls;
import com.boyuanitsm.zhetengba.utils.ZhetebaUtils;
import com.boyuanitsm.zhetengba.utils.ZtinfoUtils;
import com.boyuanitsm.zhetengba.view.CustomImageView;
import com.boyuanitsm.zhetengba.view.MyGridView;
import com.boyuanitsm.zhetengba.view.PicShowDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

/**
 * 圈子详情圈子动态适配器
 * Created by xiaoke on 2016/5/4.
 */
public class CirclexqListAdapter extends BaseAdapter {
    private Context context;
    private List<List<ImageInfo>> dateList = new ArrayList<>();
    private List<CircleEntity> list = new ArrayList<>();
    int clickPos;
    // 图片缓存 默认 等
    private DisplayImageOptions optionsImag = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.mipmap.tum)
            .showImageOnFail(R.mipmap.tum).cacheInMemory(true).cacheOnDisk(true)
            .considerExifParams(true).imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565).build();
    private DisplayImageOptions optionsImagh = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.mipmap.userhead)
            .showImageOnFail(R.mipmap.userhead).cacheInMemory(true).cacheOnDisk(true)
            .considerExifParams(true).imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565).build();

    public CirclexqListAdapter(Context context, List<List<ImageInfo>> dateList, List<CircleEntity> list) {
        this.context = context;
        this.dateList = dateList;
        this.list = list;
    }

    public void notifyChange(List<List<ImageInfo>> dateList, List<CircleEntity> list) {
        this.dateList = dateList;
        this.list = list;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return list.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        List<ImageInfo> itemList1 = new ArrayList<>();
        if (list.get(position) != null) {
            itemList1 = dateList.get(position);
        }


        if (convertView != null && convertView.getTag() != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_circle, null);
            viewHolder.ivChHead = (ImageView) convertView.findViewById(R.id.iv_ch_head);
            viewHolder.tvChNiName = (TextView) convertView.findViewById(R.id.tv_ch_niName);
            viewHolder.ivChGendar = (ImageView) convertView.findViewById(R.id.iv_ch_gendar);
            viewHolder.zimg = (ImageView) convertView.findViewById(R.id.zimg);
            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.ll_share = (LinearLayout) convertView.findViewById(R.id.ll_share);
            viewHolder.ll_comment = (LinearLayout) convertView.findViewById(R.id.ll_comment);
            viewHolder.iv_comment = (ImageView) convertView.findViewById(R.id.iv_comment);
            viewHolder.llphoto = (LinearLayout) convertView.findViewById(R.id.llphoto);
            viewHolder.iv_ch_image = (MyGridView) convertView.findViewById(R.id.iv_ch_image);
            viewHolder.iv_oneimage = (CustomImageView) convertView.findViewById(R.id.iv_oneimage);
            viewHolder.tv_cir_name = (TextView) convertView.findViewById(R.id.tv_cir_name);
            viewHolder.ll_two = (LinearLayout) convertView.findViewById(R.id.ll_two);
            viewHolder.like = (LinearLayout) convertView.findViewById(R.id.like);
            viewHolder.iv_two_one = (ImageView) convertView.findViewById(R.id.iv_two_one);
            viewHolder.iv_two_two = (ImageView) convertView.findViewById(R.id.iv_two_two);
            viewHolder.iv_two_three = (ImageView) convertView.findViewById(R.id.iv_two_three);
            viewHolder.iv_two_four = (ImageView) convertView.findViewById(R.id.iv_two_four);
            viewHolder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            viewHolder.znum = (TextView) convertView.findViewById(R.id.znum);
            viewHolder.cnum = (TextView) convertView.findViewById(R.id.cnum);
            viewHolder.iv_share= (ImageView) convertView.findViewById(R.id.iv_share);
            convertView.setTag(viewHolder);
        }
        viewHolder.llphoto.setVisibility(View.VISIBLE);
        final List<ImageInfo> itemList = itemList1;
        if (itemList.isEmpty() || itemList.isEmpty()) {
            viewHolder.llphoto.setVisibility(View.GONE);
            viewHolder.iv_ch_image.setVisibility(View.GONE);
            viewHolder.iv_oneimage.setVisibility(View.GONE);
            viewHolder.ll_two.setVisibility(View.GONE);
        } else if (itemList.size() == 1) {
            viewHolder.iv_ch_image.setVisibility(View.GONE);
            viewHolder.ll_two.setVisibility(View.GONE);
            viewHolder.iv_oneimage.setVisibility(View.VISIBLE);
            itemList.get(0).setWidth(200);
            itemList.get(0).setHeight(200);
            LayoutHelperUtil.handlerOneImage(context, itemList.get(0), viewHolder.iv_oneimage);
            viewHolder.iv_oneimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PicShowDialog dialog = new PicShowDialog(context, itemList, 0);
                    dialog.show();
                }
            });
        } else if (itemList.size() == 4) {
            viewHolder.iv_ch_image.setVisibility(View.GONE);
            viewHolder.iv_oneimage.setVisibility(View.GONE);
            viewHolder.ll_two.setVisibility(View.VISIBLE);

            ImageLoader.getInstance().displayImage(Uitls.imageFullUrl(itemList.get(0).getUrl()), viewHolder.iv_two_one, optionsImag);
            ImageLoader.getInstance().displayImage(Uitls.imageFullUrl(itemList.get(1).getUrl()), viewHolder.iv_two_two, optionsImag);
            ImageLoader.getInstance().displayImage(Uitls.imageFullUrl(itemList.get(2).getUrl()), viewHolder.iv_two_three, optionsImag);
            ImageLoader.getInstance().displayImage(Uitls.imageFullUrl(itemList.get(3).getUrl()), viewHolder.iv_two_four, optionsImag);
            viewHolder.iv_two_one.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PicShowDialog dialog = new PicShowDialog(context, itemList, 0);
                    dialog.show();
                }
            });

            viewHolder.iv_two_two.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PicShowDialog dialog = new PicShowDialog(context, itemList, 1);
                    dialog.show();
                }
            });

            viewHolder.iv_two_three.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PicShowDialog dialog = new PicShowDialog(context, itemList, 2);
                    dialog.show();
                }
            });

            viewHolder.iv_two_four.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PicShowDialog dialog = new PicShowDialog(context, itemList, 3);
                    dialog.show();
                }
            });

        } else {
            viewHolder.iv_oneimage.setVisibility(View.GONE);
            viewHolder.ll_two.setVisibility(View.GONE);
            viewHolder.iv_ch_image.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ZhetebaUtils.dip2px(context, 255), ActionBar.LayoutParams.WRAP_CONTENT);
            viewHolder.iv_ch_image.setLayoutParams(params);
            viewHolder.iv_ch_image.setNumColumns(3);
            PicGdAdapter adapter = new PicGdAdapter(context, itemList, position);
            viewHolder.iv_ch_image.setAdapter(adapter);
        }

        if (list != null && list.size() > 0) {
            ImageLoader.getInstance().displayImage(Uitls.imageFullUrl(list.get(position).getUserIcon()), viewHolder.ivChHead, optionsImagh);
            if (!TextUtils.isEmpty(list.get(position).getUserName())) {
                viewHolder.tvChNiName.setText(list.get(position).getUserName());
            } else {
                String str = list.get(position).getUserId();
                viewHolder.tvChNiName.setText(str.substring(0, 3) + "***" + str.substring(str.length() - 3, str.length()));
            }
            if (!TextUtils.isEmpty(list.get(position).getUserSex())) {
                if ("0".equals(list.get(position).getUserSex())) {
                    viewHolder.ivChGendar.setImageResource(R.mipmap.gfemale);//女0
                } else if ("1".equals(list.get(position).getUserSex())) {
                    viewHolder.ivChGendar.setImageResource(R.mipmap.male);//男1
                }
            }
            if (!TextUtils.isEmpty(list.get(position).getCreateTime() + "")) {
                viewHolder.tvTime.setText(ZtinfoUtils.timeChange(Long.parseLong(list.get(position).getCreateTime() + "")));
            }
            if (!TextUtils.isEmpty(list.get(position).getTalkContent())) {
                viewHolder.tv_content.setText(list.get(position).getTalkContent());
            }else {
                viewHolder.tv_content.setText("");
            }
            if (!TextUtils.isEmpty(list.get(position).getCircleName())) {
                viewHolder.tv_cir_name.setText(list.get(position).getCircleName());
            }
            if (!TextUtils.isEmpty(list.get(position).getLiked() + "")) {
                if (0 == list.get(position).getLiked()) {//未点赞
                    viewHolder.zimg.setImageResource(R.drawable.zan);
                } else if (1 == list.get(position).getLiked()) {
                    viewHolder.zimg.setImageResource(R.drawable.zan_b);
                }
            }
            if (!TextUtils.isEmpty(list.get(position).getLikedCounts() + "")) {
                if (list.get(position).getLikedCounts() == 0) {
                    viewHolder.znum.setVisibility(View.GONE);
                } else {
                    viewHolder.znum.setVisibility(View.VISIBLE);
                    viewHolder.znum.setText(list.get(position).getLikedCounts() + "");
                }
            }
            if (!TextUtils.isEmpty(list.get(position).getCommentCounts() + "")) {
                if (list.get(position).getCommentCounts() == 0) {
                    viewHolder.cnum.setVisibility(View.GONE);
                } else {
                    viewHolder.cnum.setVisibility(View.VISIBLE);
                    viewHolder.cnum.setText(list.get(position).getCommentCounts() + "");
                }
            }
        }
        //点击用户头像，进入用户圈子主页
        viewHolder.ivChHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("userId", list.get(position).getUserId());
                intent.putExtras(bundle);
                intent.setClass(context, PerpageAct.class);
                //需要开启新task,否则会报错
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.like.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        switch (v.getId()) {
                            case R.id.like:
                                finalViewHolder.zimg.setAlpha(0.5f);
                                break;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        switch (v.getId()) {
                            case R.id.like://点赞
                                finalViewHolder.zimg.setAlpha(1.0f);
                                //接口调用
                                clickPos = position;
                                if (0 == list.get(position).getLiked()) {
                                    addCircleLike(list.get(position).getId());
                                } else if (1 == list.get(position).getLiked()) {
                                    removeCircleLike(list.get(position).getId());
                                }
                                break;
                        }
                        break;
                }
                return true;
            }
        });
//        //点赞
//        viewHolder.like.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                clickPos = position;
//                if (0 == list.get(position).getLiked()) {
//                    addCircleLike(list.get(position).getId());
//                } else if (1 == list.get(position).getLiked()) {
//                    removeCircleLike(list.get(position).getId());
//                }
//            }
//        });

        viewHolder.ll_share.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        switch (v.getId()){
                            case R.id.ll_share:
                                finalViewHolder.iv_share.setAlpha(0.5f);
                                break;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        switch (v.getId()){
                            case R.id.ll_share:
                                finalViewHolder.iv_share.setAlpha(1.0f);
                                Intent intent = new Intent(context, ShareDialogAct.class);
                                intent.putExtra("type", 5);
                                intent.putExtra("id",list.get(position).getId());
                                context.startActivity(intent);
                                break;
                        }
                        break;
                }
                return true;
            }
        });
        //分享对话框
//        viewHolder.ll_share.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, ShareDialogAct.class);
//                intent.putExtra("type", 5);
//                intent.putExtra("id",list.get(position).getId());
//                context.startActivity(intent);
//            }
//        });
        //评论
        viewHolder.ll_comment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        switch (v.getId()) {
                            case R.id.ll_comment:
                                finalViewHolder.iv_comment.setAlpha(0.5f);
                                break;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        switch (v.getId()) {
                            case R.id.ll_comment://点赞
                                finalViewHolder.iv_comment.setAlpha(1.0f);
                                //接口调用
                                Intent intent = new Intent();
                                intent.setClass(context, CircleTextAct.class);
                                intent.putExtra("circleEntity", list.get(position));
                                intent.putExtra("circleId", list.get(position).getId());
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                                break;
                        }
                        break;
                }
                return true;
            }
        });
//        viewHolder.ll_comment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(context, CircleTextAct.class);
//                intent.putExtra("circleEntity", list.get(position));
//                intent.putExtra("circleId", list.get(position).getId());
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);
//            }
//        });
        viewHolder.tv_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context, CircleTextAct.class);
                intent.putExtra("circleEntity", list.get(position));
                intent.putExtra("circleId", list.get(position).getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        return convertView;
    }


    class ViewHolder {
        public ImageView ivChHead;
        public TextView tvChNiName;
        public ImageView ivChGendar;
        public ImageView zimg;
        public TextView tvTime;
        public MyGridView iv_ch_image;
        public CustomImageView iv_oneimage;
        public TextView tv_cir_name;
        private LinearLayout ll_two;
        private ImageView iv_two_one, iv_two_two, iv_two_three, iv_two_four;
        private LinearLayout like;
        private LinearLayout ll_share;
        private LinearLayout ll_comment;
        private LinearLayout llphoto;
        private TextView tv_content;
        private TextView znum;
        private TextView cnum;
        private TextView snum;
        private ImageView iv_comment,iv_share;

    }

    /**
     * 圈子说说点赞
     *
     * @param circleTalkId
     */
    private void addCircleLike(String circleTalkId) {
        RequestManager.getTalkManager().addCircleLike(circleTalkId, new ResultCallback<ResultBean<String>>() {
            @Override
            public void onError(int status, String errorMsg) {
                MyToastUtils.showShortToast(context, errorMsg);
            }

            @Override
            public void onResponse(ResultBean<String> response) {
                list.get(clickPos).setLiked(1);
                if (!TextUtils.isEmpty(response.getData())) {
                    list.get(clickPos).setLikedCounts(Integer.parseInt(response.getData()));
                }
                notifyDataSetChanged();
            }
        });
    }

    /**
     * 取消圈子说说点赞
     *
     * @param circleTalkId
     */
    private void removeCircleLike(String circleTalkId) {
        RequestManager.getTalkManager().removeCircleLike(circleTalkId, new ResultCallback<ResultBean<String>>() {
            @Override
            public void onError(int status, String errorMsg) {
                MyToastUtils.showShortToast(context, errorMsg);
            }

            @Override
            public void onResponse(ResultBean<String> response) {
                list.get(clickPos).setLiked(0);
                if (!TextUtils.isEmpty(response.getData())) {
                    list.get(clickPos).setLikedCounts(Integer.parseInt(response.getData()));
                }
                notifyDataSetChanged();
            }
        });
    }

}
