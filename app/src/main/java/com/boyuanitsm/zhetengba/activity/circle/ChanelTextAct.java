package com.boyuanitsm.zhetengba.activity.circle;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.boyuanitsm.zhetengba.R;
import com.boyuanitsm.zhetengba.adapter.ChaTextAdapter;
import com.boyuanitsm.zhetengba.adapter.PicGdAdapter;
import com.boyuanitsm.zhetengba.base.BaseActivity;
import com.boyuanitsm.zhetengba.bean.ChannelTalkEntity;
import com.boyuanitsm.zhetengba.bean.DataBean;
import com.boyuanitsm.zhetengba.bean.ImageInfo;
import com.boyuanitsm.zhetengba.bean.ResultBean;
import com.boyuanitsm.zhetengba.http.callback.ResultCallback;
import com.boyuanitsm.zhetengba.http.manager.RequestManager;
import com.boyuanitsm.zhetengba.utils.LayoutHelperUtil;
import com.boyuanitsm.zhetengba.utils.MyToastUtils;
import com.boyuanitsm.zhetengba.utils.Uitls;
import com.boyuanitsm.zhetengba.utils.ZhetebaUtils;
import com.boyuanitsm.zhetengba.utils.ZtinfoUtils;
import com.boyuanitsm.zhetengba.view.CircleImageView;
import com.boyuanitsm.zhetengba.view.CustomImageView;
import com.boyuanitsm.zhetengba.view.LoadingView;
import com.boyuanitsm.zhetengba.view.MyGridView;
import com.boyuanitsm.zhetengba.view.PicShowDialog;
import com.boyuanitsm.zhetengba.view.refresh.PullToRefreshBase;
import com.boyuanitsm.zhetengba.view.refresh.PullToRefreshListView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

/**
 * 频道正文
 * Created by xiaoke on 2016/5/11.
 */
public class ChanelTextAct extends BaseActivity implements View.OnClickListener{
    @ViewInject(R.id.et_comment)
    private EditText etComment;//评论内容
    @ViewInject(R.id.my_lv)
    private PullToRefreshListView my_lv;
    @ViewInject(R.id.iv_chanel_comment)
    private Button btnSend;
    private LinearLayout ll_two;
    private LinearLayout llphoto;
    private CustomImageView ng_one_image, iv_two_one, iv_two_two, iv_two_three, iv_two_four;
    private MyGridView iv_ch_image;
    private List<List<ImageInfo>> dataList;
    private String comNum;
    // 图片缓存 默认 等
    private DisplayImageOptions optionsImag = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.mipmap.tum)
            .showImageOnLoading(R.mipmap.tum)
            .showImageOnFail(R.mipmap.tum).cacheInMemory(true).cacheOnDisk(true)
            .considerExifParams(true).imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565).build();
    private DisplayImageOptions optionshead = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.mipmap.userhead)
            .showImageOnFail(R.mipmap.userhead).cacheInMemory(true).cacheOnDisk(true)
            .considerExifParams(true).imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565).build();
    private String channelId;//频道说说id
    private ChannelTalkEntity channelTalkEntity;//频道说说实体
    private CircleImageView head;//头像
    private TextView name;//姓名
    private ImageView sex;//性别
    private TextView time;//时间
    private TextView content;//说说内容
    private TextView commentNum;//评论数
    private View headView;
    private int page=1;
    private int rows=10;
    private List<ChannelTalkEntity> list;
    private int position;
    ChaTextAdapter adapter;
    @ViewInject(R.id.load_view)
    private LoadingView load_view;
    @Override
    public void setLayout() {
        setContentView(R.layout.act_chanel_text);
    }

    @Override
    public void init(Bundle savedInstanceState) {
        setTopTitle("");
        headView=getLayoutInflater().inflate(R.layout.hannel_headerview,null);
        assignView(headView);
        channelTalkEntity=getIntent().getParcelableExtra("channelEntity");
        channelId=getIntent().getStringExtra("channelId");
        position= getIntent().getIntExtra("CommentPosition", 0);
        LayoutHelperUtil.freshInit(my_lv);
        my_lv.getRefreshableView().addHeaderView(headView);
        setChannel(channelTalkEntity);
        getCircleCommentsList(channelId, page, rows);
        load_view.setOnRetryListener(new LoadingView.OnRetryListener() {
            @Override
            public void OnRetry() {
                getCircleCommentsList(channelId, page, rows);
            }
        });

        my_lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                my_lv.setLastUpdatedLabel(ZtinfoUtils.getCurrentTime());
                page=1;
                getCircleCommentsList(channelId,page,rows);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                getCircleCommentsList(channelId,page,rows);
            }
        });

        etComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s.toString().trim())){
                    btnSend.setBackgroundResource(R.drawable.main_btn_nor);
                    btnSend.setTextColor(Color.parseColor("#FFFFFF"));
                }else {
                    btnSend.setBackgroundColor(Color.parseColor("#f4f4f4"));
                    btnSend.setTextColor(Color.parseColor("#999999"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setChannel(ChannelTalkEntity entity){
        if(entity!=null){
            if (!TextUtils.isEmpty(entity.getUserIcon())){
                ImageLoader.getInstance().displayImage(Uitls.imageFullUrl(entity.getUserIcon()), head, optionshead);
            }
            if(!TextUtils.isEmpty(entity.getUserName())){
                name.setText(entity.getUserName());
            }else {
                String str=entity.getCreatePersonId();
                name.setText(str.substring(0,3)+"***"+str.substring(str.length()-3,str.length()));
            }
            if(!TextUtils.isEmpty(entity.getUserSex())){
                if (entity.getUserSex().equals(1+"")){
                    sex.setImageResource(R.drawable.male);
                }else if (entity.getUserSex().equals(0+"")){
                    sex.setImageResource(R.drawable.female);
                }
            }
            if(!TextUtils.isEmpty(entity.getCreateTiem())){
                time.setText(ZtinfoUtils.timeChange(Long.parseLong(entity.getCreateTiem())));
            }
            if(!TextUtils.isEmpty(entity.getChannelContent())){
                content.setVisibility(View.VISIBLE);
                content.setText(entity.getChannelContent());
            }else {
                content.setVisibility(View.GONE);
            }
            if(!TextUtils.isEmpty(entity.getCommentCounts()+"")){
                commentNum.setText("评论"+entity.getCommentCounts());
            }
            if(!TextUtils.isEmpty(entity.getChannelImage())){
                initDate(entity);
            }
        }
    }

    private void assignView(View view) {
        head= (CircleImageView) view.findViewById(R.id.iv_ch_head);//头像
        name= (TextView) view.findViewById(R.id.tv_ch_niName);//姓名
        sex= (ImageView) view.findViewById(R.id.iv_ch_gendar);//性别
        time= (TextView) view.findViewById(R.id.tv_time);//时间
        content= (TextView) view.findViewById(R.id.content);//说说内容
        commentNum= (TextView) view.findViewById(R.id.commentNum);//评论数
        iv_ch_image = (MyGridView) view.findViewById(R.id.iv_ch_image);
        ng_one_image = (CustomImageView) view.findViewById(R.id.ng_one_image);
        ll_two = (LinearLayout) view.findViewById(R.id.ll_two);
        iv_two_one = (CustomImageView) view.findViewById(R.id.iv_two_one);
        iv_two_two = (CustomImageView) view.findViewById(R.id.iv_two_two);
        iv_two_three = (CustomImageView) view.findViewById(R.id.iv_two_three);
        iv_two_four = (CustomImageView) view.findViewById(R.id.iv_two_four);
        llphoto= (LinearLayout) view.findViewById(R.id.llphoto);
    }

    private void initDate(ChannelTalkEntity channelTalkEntity) {
        dataList = new ArrayList<>();
//        final List<ImageInfo> singleList=new ArrayList<>();
            //将图片地址转化成数组
        if(!TextUtils.isEmpty(channelTalkEntity.getChannelImage())) {
            llphoto.setVisibility(View.VISIBLE);
            final String[] urlList = ZtinfoUtils.convertStrToArray(channelTalkEntity.getChannelImage());
            if (urlList.length== 1) {
                ll_two.setVisibility(View.GONE);
                iv_ch_image.setVisibility(View.GONE);
                ng_one_image.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(Uitls.imageFullUrl(urlList[0]), ng_one_image, optionsImag);
                ng_one_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PicShowDialog dialog = new PicShowDialog(ChanelTextAct.this, urlList, 0);
                        dialog.show();
                    }
                });
            } else if (urlList.length == 4) {
                iv_ch_image.setVisibility(View.GONE);
                ng_one_image.setVisibility(View.GONE);
                ll_two.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(Uitls.imageFullUrl(urlList[0]), iv_two_one, optionsImag);
                ImageLoader.getInstance().displayImage(Uitls.imageFullUrl(urlList[1]), iv_two_two, optionsImag);
                ImageLoader.getInstance().displayImage(Uitls.imageFullUrl(urlList[2]), iv_two_three, optionsImag);
                ImageLoader.getInstance().displayImage(Uitls.imageFullUrl(urlList[3]), iv_two_four, optionsImag);
                iv_two_one.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PicShowDialog dialog = new PicShowDialog(ChanelTextAct.this, urlList, 0);
                        dialog.show();
                    }
                });

                iv_two_two.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PicShowDialog dialog = new PicShowDialog(ChanelTextAct.this, urlList, 1);
                        dialog.show();
                    }
                });

                iv_two_three.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PicShowDialog dialog = new PicShowDialog(ChanelTextAct.this, urlList, 2);
                        dialog.show();
                    }
                });

                iv_two_four.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PicShowDialog dialog = new PicShowDialog(ChanelTextAct.this, urlList, 3);
                        dialog.show();
                    }
                });

            } else {
                ng_one_image.setVisibility(View.GONE);
                ll_two.setVisibility(View.GONE);
                iv_ch_image.setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ZhetebaUtils.dip2px(ChanelTextAct.this, 255), ActionBar.LayoutParams.WRAP_CONTENT);
                iv_ch_image.setLayoutParams(params);
                iv_ch_image.setNumColumns(3);
                PicGdAdapter adapter = new PicGdAdapter(ChanelTextAct.this, urlList);
                iv_ch_image.setAdapter(adapter);
            }
        }else {
            llphoto.setVisibility(View.GONE);
            ll_two.setVisibility(View.GONE);
            ng_one_image.setVisibility(View.GONE);
            iv_ch_image.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.ll_comment,R.id.iv_chanel_comment})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_comment:
                break;
            case R.id.iv_chanel_comment:
                if (!TextUtils.isEmpty(etComment.getText().toString().trim())) {
                    btnSend.setEnabled(false);
                    btnSend.setClickable(false);
                    commentChannelTalk(channelId, null, etComment.getText().toString().trim());
                }else {
                    MyToastUtils.showShortToast(ChanelTextAct.this,"请输入评论内容！");
                }
                break;
        }
    }

    /**
     * 频道说说评论
     * @param channelTalkId
     * @param fatherCommentId
     * @param commentContent
     */
    private void commentChannelTalk(final String channelTalkId  ,String fatherCommentId ,String commentContent){
        RequestManager.getTalkManager().commentChannelTalk(channelTalkId, fatherCommentId, commentContent, new ResultCallback<ResultBean<String>>() {
            @Override
            public void onError(int status, String errorMsg) {
                btnSend.setEnabled(true);
                btnSend.setClickable(true);
            }

            @Override
            public void onResponse(ResultBean<String> response) {
                //重新获取评论列表，刷新评论数目，关闭键盘
                ZtinfoUtils.hideSoftKeyboard(ChanelTextAct.this, etComment);
                etComment.setText("");
                commentNum.setText("评论" + response.getData());
                comNum=response.getData();
                page=1;
                getCircleCommentsList(channelTalkId, page, rows);
                btnSend.setEnabled(true);
                btnSend.setClickable(true);

            }
        });
    }

    private List<ChannelTalkEntity> datas=new ArrayList<>();
    //获取评论列表
    private void getCircleCommentsList(String channelTalkId, final int page, int rows){
        list = new ArrayList<>();
        RequestManager.getTalkManager().getChannelCommentsList(channelTalkId, page, rows, new ResultCallback<ResultBean<DataBean<ChannelTalkEntity>>>() {
            @Override
            public void onError(int status, String errorMsg) {
                my_lv.onPullUpRefreshComplete();
                my_lv.onPullDownRefreshComplete();
                load_view.loadError();
            }

            @Override
            public void onResponse(ResultBean<DataBean<ChannelTalkEntity>> response) {
                my_lv.onPullUpRefreshComplete();
                my_lv.onPullDownRefreshComplete();
                load_view.loadComplete();
                list=response.getData().getRows();
                if (list.size() == 0) {
                    if (page == 1) {
                    } else {
                        my_lv.setHasMoreData(false);
                    }
                }

                if(page==1){
                    datas.clear();
                }
                datas.addAll(list);
//                commentNum.setText("评论" + datas.size());
                if (adapter==null) {
                    adapter = new ChaTextAdapter(ChanelTextAct.this, datas);
                    my_lv.getRefreshableView().setAdapter(adapter);
                }else {
                    adapter.notifyChange(datas);
                }
                Intent intent=new Intent(SquareAct.TALK_LIST);
                Bundle bundle=new Bundle();
                bundle.putInt("ComtPosition", position);
                bundle.putString("tag", "comTag");
                if (!TextUtils.isEmpty(comNum)){
                    bundle.putInt("ComtNum", Integer.parseInt(comNum));
                }
                if (datas!=null&&datas.size()>0){
                    bundle.putParcelableArrayList("ComtList", (ArrayList<ChannelTalkEntity>) datas);
                }
                intent.putExtras(bundle);
                sendBroadcast(intent);
            }
        });
    }


}
