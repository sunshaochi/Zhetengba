package com.boyuanitsm.zhetengba.activity.circle;

import android.os.Bundle;

import com.boyuanitsm.zhetengba.R;
import com.boyuanitsm.zhetengba.base.BaseActivity;

/**
 * 评论界面
 * Created by xiaoke on 2016/5/17.
 */
public class CommentAct extends BaseActivity {
    @Override
    public void setLayout() {
        setContentView(R.layout.act_comment);
    }

    @Override
    public void init(Bundle savedInstanceState) {
        setTopTitle("评论");
    }
}