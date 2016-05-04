package com.boyuanitsm.zhetengba.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.boyuanitsm.zhetengba.R;
import com.boyuanitsm.zhetengba.adapter.ActAdapter;
import com.boyuanitsm.zhetengba.adapter.ImageAdapter;
import com.boyuanitsm.zhetengba.view.CustomDialog;

import java.lang.ref.WeakReference;

/**
 * 简约界面
 * Created by xiaoke on 2016/4/24.
 */
public class SimpleFrg extends Fragment{
    private ListView lv_act;
    private View view;
    private ListView lv_calen;
    private View viewHeader_act;
    private ActAdapter adapter;
    private ViewPager viewPager;
    private LinearLayout ll_guanzhu;//关注数量
    private ImageView iv_simple_guanzhu;//关注图标
    private TextView tv_guanzhu_num;//关注数量设置
    private int gznum=0;//默认关注人数0
    private boolean tag=true;
    private ImageHandler handler = new ImageHandler(new WeakReference<SimpleFrg>(this));

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.act_frag, container, false);
        viewHeader_act = getLayoutInflater(savedInstanceState).inflate(R.layout.item_viewpager_act, null);
        lv_act = (ListView) view.findViewById(R.id.lv_act);
        //设置简约listview的headerview：item_viewpager_act.xml
        lv_act.addHeaderView(viewHeader_act);
        adapter = new ActAdapter(getActivity());
        lv_act.setAdapter(adapter);
        //设置listview条目点击事件
        lv_act.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ll_guanzhu = (LinearLayout) view.findViewById(R.id.ll_guanzhu);
                iv_simple_guanzhu = (ImageView) view.findViewById(R.id.iv_simple_guanzhu);
                tv_guanzhu_num = (TextView) view.findViewById(R.id.tv_guanzhu_num);
                ll_guanzhu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tag == true) {
                            iv_simple_guanzhu.setBackgroundDrawable(v.getResources().getDrawable(R.drawable.collect_b));
                            ++gznum;
                            tv_guanzhu_num.setText("" +gznum);
                            tag = false;

                        } else {
                            iv_simple_guanzhu.setBackgroundDrawable(v.getResources().getDrawable(R.drawable.collect));
                            tv_guanzhu_num.setText("" + (--gznum));
                            tag = true;
                        }
                    }
                });
            }
        });
        //设置viewpager
        viewPager = (ViewPager) view.findViewById(R.id.vp_loop_act);
        viewPager.setAdapter(new ImageAdapter(getContext()));
        viewPager.setOnPageChangeListener(new PagerChangeListener());
        viewPager.setCurrentItem(Integer.MAX_VALUE / 2);//默认在中间，使用户看不到边界
        //开始轮播效果
        handler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, ImageHandler.MSG_DELAY);
        return view;
    }
    /***
     * viewpager监听事件
     */
    private class PagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            handler.sendMessage(Message.obtain(handler, ImageHandler.MSG_PAGE_CHANGED, position, 0));
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            switch (state) {
                case ViewPager.SCROLL_STATE_DRAGGING:
                    handler.sendEmptyMessage(ImageHandler.MSG_KEEP_SILENT);
                    break;
                case ViewPager.SCROLL_STATE_IDLE:
                    handler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, ImageHandler.MSG_DELAY);
                    break;
                default:
                    break;
            }
        }
    }

    /***
     * 设置条目点击显示活动详情dialog
     *
     * @param
     */
    private void showDialog() {
        CustomDialog.Builder builder = new CustomDialog.Builder(getContext());
        builder.setPositiveButton("你们两个是同事", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("共参加过2次活动", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();


    }

    private static class ImageHandler extends android.os.Handler {
        /**
         * 请求更新显示的View。
         */
        protected static final int MSG_UPDATE_IMAGE = 1;
        /**
         * 请求暂停轮播。
         */
        protected static final int MSG_KEEP_SILENT = 2;
        /**
         * 请求恢复轮播。
         */
        protected static final int MSG_BREAK_SILENT = 3;
        /**
         * 记录最新的页号，当用户手动滑动时需要记录新页号，否则会使轮播的页面出错。
         * 例如当前如果在第一页，本来准备播放的是第二页，而这时候用户滑动到了末页，
         * 则应该播放的是第一页，如果继续按照原来的第二页播放，则逻辑上有问题。
         */
        protected static final int MSG_PAGE_CHANGED = 4;

        //轮播间隔时间
        protected static final long MSG_DELAY = 3000;
        private int currentItem = 0;
        private WeakReference<SimpleFrg> weakReference;

        protected ImageHandler(WeakReference<SimpleFrg> wk) {
            weakReference = wk;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            SimpleFrg simpleFrg = weakReference.get();
            if (simpleFrg == null) {
                return;
            }
            if (simpleFrg.handler.hasMessages(MSG_UPDATE_IMAGE)) {
                simpleFrg.handler.removeMessages(MSG_UPDATE_IMAGE);
            }
            switch (msg.what) {
                case MSG_UPDATE_IMAGE:
                    currentItem++;
                    simpleFrg.viewPager.setCurrentItem(currentItem);
                    break;
                case MSG_KEEP_SILENT:
                    //只要不发送消息就暂停了
                    break;
                case MSG_BREAK_SILENT:
                    simpleFrg.handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                    break;
                case MSG_PAGE_CHANGED:
                    //记录当前的页号，避免播放的时候页面显示不正确。
                    currentItem = msg.arg1;
                    break;
                default:
                    break;

            }
        }
    }
}
