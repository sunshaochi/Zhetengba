package com.boyuanitsm.zhetengba.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;


/**
 * Created by bitch-1 on 2016/5/12.
 */
public class MyRecyleview extends RecyclerView {
    public MyRecyleview(Context context) {
        super(context);}

    public MyRecyleview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public MyRecyleview(Context context, AttributeSet attrs,
                        int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    /**
     * 重写该方法、达到使ListView适应ScrollView的效果
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
