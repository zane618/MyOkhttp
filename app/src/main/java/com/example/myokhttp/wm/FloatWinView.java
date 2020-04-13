package com.example.myokhttp.wm;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.myokhttp.R;
import com.example.myokhttp.util.Api;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Package Name: com.wtyt.yzone.views
 *
 * @author ysr
 * @Email yfeng1023@gmail.com
 * @Description: 悬浮控件
 * @Create Date: 2019/7/1
 */
public class FloatWinView extends FrameLayout implements View.OnTouchListener {

    private final int ACTION_CHANGE_ALHPA = 100;//停留3s后变暗
    private final int ACTION_HIDE_FLOATWINDOW = 101;//变暗3s后靠边隐藏
    private final int ACTION_HIDE_NO_FLOATWINDOW = 110;//变暗3s后不靠边隐藏

    private final int ACTION_ANIM_LEFT = 102;
    private final int ACTION_ANIM_RIGHT = 103;
    private boolean mCurGriRight = false;
    //悬浮窗停留3s后变暗，再3s后靠边隐藏，中途触碰则中断ACTION_HIDE_FLOATWINDOW
    private boolean mTouchAgain = false;

    private WindowManager.LayoutParams mWmParams;
    private WindowManager mWindowManager;
    private boolean isHide = false;
    private long mLastClickTime = 0;


    private Context mContext;

    private ImageView mIvFloatLogo;
    private TextView mLlFloatMenu;

    private boolean mIsRight;//logo是否在右边
    private boolean mCanHide;//是否允许隐藏

    private int mAniType;
    private final int typeLeft = 10011;
    private final int typeRight = 10012;

    private float mTouchStartX;
    private float mTouchStartY;
    private int mScreenWidth;
    private int mScreenHeight;
    private boolean mDraging;

    private Timer mTimer;
    private TimerTask mTimerTask;

    private boolean isRunning = false;

    private int initX;
    private int initY;
    private static int tmpY;
    private static boolean tmpIsRight = true;
    private static int MARGIN = 60;
    private static int WIDGET_WIDTH;

    public FloatWinView(Context context) {
        super(context);
        init(context);
    }

    public FloatWinView(Context context, AttributeSet attrs) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        if (MARGIN == 0) {
//            MARGIN = Util.dip2px(mContext, 20);
        }
        if (WIDGET_WIDTH == 0) {
            WIDGET_WIDTH = getWidth();
        }
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        // 更新浮动窗口位置参数 靠边
        DisplayMetrics dm = new DisplayMetrics();
        // 获取屏幕信息
        mWindowManager.getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;

        this.mWmParams = new WindowManager.LayoutParams();
        // 设置window type
        mWmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION;
        // 设置图片格式，效果为背景透明
        mWmParams.format = PixelFormat.RGBA_8888;
        // 设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        mWmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mWmParams.gravity = Gravity.LEFT | Gravity.TOP;

        mScreenHeight = mWindowManager.getDefaultDisplay().getHeight();

        initX = mScreenWidth;
//        if (tmpIsRight) {
//            mIsRight = initX > mScreenWidth / 2;
//            // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
//            mWmParams.x = mIsRight ? mScreenWidth - MARGIN - WIDGET_WIDTH: MARGIN;
//        } else {
//            mIsRight = tmpIsRight;
//        }
        //调整上下距离，与保障计划小助手错开
        mWmParams.y = 300;
        mWmParams.x = 300;

        // 设置悬浮窗口长宽数据
        mWmParams.width = LayoutParams.WRAP_CONTENT;
        mWmParams.height = LayoutParams.WRAP_CONTENT;
        addView(createView(mContext));
        mWindowManager.addView(this, mWmParams);

        mTimer = new Timer();
//        timerForHide();
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // 更新浮动窗口位置参数 靠边
        DisplayMetrics dm = new DisplayMetrics();
        // 获取屏幕信息
        mWindowManager.getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;
        int oldX = mWmParams.x;
        int oldY = mWmParams.y;
        switch (newConfig.orientation) {
            case Configuration.ORIENTATION_LANDSCAPE://横屏
                if (mIsRight) {
                    mWmParams.x = mScreenWidth;
                    mWmParams.y = oldY;
                } else {
                    mWmParams.x = oldX;
                    mWmParams.y = oldY;
                }
                break;
            case Configuration.ORIENTATION_PORTRAIT://竖屏
                if (mIsRight) {
                    mWmParams.x = mScreenWidth;
                    mWmParams.y = oldY;
                } else {
                    mWmParams.x = oldX;
                    mWmParams.y = oldY;
                }
                break;
        }
        mWindowManager.updateViewLayout(this, mWmParams);
    }

    /**
     * 创建Float view
     *
     * @param context
     * @return
     */
    private View createView(final Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        // 从布局文件获取浮动窗口视图
        View rootFloatView = inflater.inflate(R.layout.widget_win_float_view, null);
        mIvFloatLogo = (ImageView) rootFloatView.findViewById(R.id.pj_float_view_icon_imageView);
        mLlFloatMenu = (TextView) rootFloatView.findViewById(R.id.ll_menu);
        rootFloatView.setOnTouchListener(this);
        rootFloatView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                long curTime = System.currentTimeMillis();
                if (curTime - mLastClickTime < 200) {
                    return;
                }
                mLastClickTime = curTime;
            }
        });
        rootFloatView.measure(MeasureSpec.makeMeasureSpec(0,
                MeasureSpec.UNSPECIFIED), MeasureSpec
                .makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

//        GlideUtil.loadGif(context, mIvFloatLogo, R.drawable.help_float_iocn, R.drawable.help_float_iocn);

        return rootFloatView;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (isRunning)
            return false;

        // 获取相对屏幕的坐标，即以屏幕左上角为原点
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                mTouchAgain = true;
                mTouchStartX = event.getX();
                mTouchStartY = event.getY();
                mIvFloatLogo.setPadding(0, 0, 0, 0);
                mWmParams.alpha = 1f;
                if (isHide) {
                    mWmParams.x += mWmParams.x > mScreenWidth / 2 ? -240 : 140;
                }
//                mWindowManager.updateViewLayout(this, mWmParams);
                isHide = false;
                mDraging = false;
                break;
            case MotionEvent.ACTION_MOVE:

                float mMoveStartX = event.getX();
                float mMoveStartY = event.getY();


                // 如果移动量大于3才移动
                float delX = Math.abs(mTouchStartX - mMoveStartX);
                float delY = Math.abs(mTouchStartY - mMoveStartY);
                if (delX > 3
                        && delY > 3) {
                    //太靠边的时候就不进行放大处理了，以免边界超出屏幕
                    if (mMoveStartX > 15 && mMoveStartX < mScreenWidth - 15) {
                    }

                    mDraging = true;
                    // 更新浮动窗口位置参数
//                    LogUtil.d("before showAni x = "+mFlFloatLogo.getX()
//                            +" y = "+mFlFloatLogo.getY());
                    if ((int) x - mTouchStartX < MARGIN) {
                        mWmParams.x = MARGIN;
                    } else {
                        mWmParams.x = (int) (x - mTouchStartX) > mScreenWidth - WIDGET_WIDTH - MARGIN
                                ? mScreenWidth - WIDGET_WIDTH - MARGIN : (int) (x - mTouchStartX);
                    }
                    mWmParams.y = (int) (y - mTouchStartY);
                    mWindowManager.updateViewLayout(this, mWmParams);

                    return false;
                }

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                int left = mCurGriRight ? mWmParams.x + mLlFloatMenu.getLayoutParams().width : mWmParams.x;

                if (left >= mScreenWidth / 2 - mIvFloatLogo.getWidth() / 2) {
                    mAniType = typeRight;
                    mIsRight = true;
                } else if (left < mScreenWidth / 2 - mIvFloatLogo.getWidth() / 2) {
                    mAniType = typeLeft;
                    mIsRight = false;
                }
                // 初始化
                mTouchStartX = mTouchStartY = 0;
                break;
        }
        return false;
    }

    private void removeFloatView() {
        try {
            if (mWindowManager != null) {
                mWindowManager.removeViewImmediate(this);
                mWindowManager.removeView(this);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 显示悬浮窗
     */
    public void show() {
        mWmParams.y = 200;
        mWmParams.x = 200;
        mWindowManager.updateViewLayout(this, mWmParams);
    }

    /**
     * 去除悬浮球
     */
    public void destroy() {
        initX = mWmParams.x;
        initY = mWmParams.y;
        removeFloatView();
    }
}