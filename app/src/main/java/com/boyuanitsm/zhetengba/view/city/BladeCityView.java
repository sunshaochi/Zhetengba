package com.boyuanitsm.zhetengba.view.city;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * Created by wangbin on 16/2/18.
 */
public class BladeCityView extends View{

    private OnItemClickListener mOnItemClickListener;
    String[] b = { "当前","热门","A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
            "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
            "Y", "Z" };
    int choose = -1;
    Paint paint = new Paint();
    boolean showBkg = false;
    private PopupWindow mPopupWindow;
    private TextView mPopupText;
    private Handler handler = new Handler();

    public BladeCityView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public BladeCityView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BladeCityView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (showBkg) {
            canvas.drawColor(Color.parseColor("#00000000"));
        }

        int height = getHeight();
        int width = getWidth();
        int singleHeight = height / b.length;
        for (int i = 0; i < b.length; i++) {
            paint.setColor(Color.parseColor("#17a4e6"));
//            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setFakeBoldText(false);
            paint.setAntiAlias(true);
            paint.setTextSize(20);
            if (i == choose) {
                paint.setColor(Color.parseColor("#17a4e6"));
            }
            float xPos = width / 2 - paint.measureText(b[i]) / 2;
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(b[i], xPos, yPos, paint);
            paint.reset();
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = choose;
        final int c = (int) (y / getHeight() * b.length);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                showBkg = true;
                if (oldChoose != c) {
                    if (c>=0 && c < b.length) {
                        performItemClicked(c);
                        choose = c;
                        invalidate();
                    }
                }

                break;
            case MotionEvent.ACTION_MOVE:
                if (oldChoose != c) {
                    if (c > 0 && c < b.length) {
                        performItemClicked(c);
                        choose = c;
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                showBkg = false;
                choose = -1;
                dismissPopup();
                invalidate();
                break;
        }
        return true;
    }

    private void showPopup(int item) {
        if (mPopupWindow == null) {
            handler.removeCallbacks(dismissRunnable);
            mPopupText = new TextView(getContext());
            mPopupText.setBackgroundColor(Color.GRAY);
            mPopupText.setTextColor(Color.CYAN);
            mPopupText.setTextSize(40);
            mPopupText.setGravity(Gravity.CENTER_HORIZONTAL
                    | Gravity.CENTER_VERTICAL);
            mPopupWindow = new PopupWindow(mPopupText, 200, 200);
        }

        String text = "";
		if (item == 0) {
			text = "当前";
		} else if(item==1){
			text="热门";
		}
//        else if(item==2){
//            text="热门";
//        }
        else{
        text = Character.toString((char) ('A' + (item-2) ));
		}
        mPopupText.setText(text);
        if (mPopupWindow.isShowing()) {
            mPopupWindow.update();
        } else {
            mPopupWindow.showAtLocation(getRootView(),
                    Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        }
    }

    private void dismissPopup() {
        handler.postDelayed(dismissRunnable, 800);
    }

    Runnable dismissRunnable = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            if (mPopupWindow != null) {
                mPopupWindow.dismiss();
            }
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    private void performItemClicked(int item) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(b[item]);
            showPopup(item);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String s);
    }

}
