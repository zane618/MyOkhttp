package com.example.myokhttp.wm;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myokhttp.R;
import com.example.myokhttp.util.LogPrintUtil;


/**
 * created by zhangshi on 2020-03-10.
 */
public class MyEffectView extends FrameLayout {
    private Context mContext;
    private int mScreenWidth;
    private int mScreenHeight;
    private FrameLayout mActivityRootContainer;

    private View layout;

    public MyEffectView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public MyEffectView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        mActivityRootContainer = getActivityRoot((Activity) mContext);

        createView();
        doAnim();
        addFloatViewToActivity();
//        doAnim();
    }

    /**
     * 悬浮view添加到activity根视图
     */
    public void addFloatViewToActivity() {
        if (mActivityRootContainer == null) {
            return;
        }
        FrameLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
        mActivityRootContainer.addView(this, params);
    }

    private void createView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        layout = inflater.inflate(R.layout.mwm, null);
//        layout.setX(300);
//        layout.setY(1000);
        addView(layout);
    }

    /**
     * 执行动画
     */
    public void doAnim() {
        //view.post 获取控件的宽高
        this.post(new Runnable() {
            @Override
            public void run() {

            }
        });
        if (layout != null) {
            int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            this.measure(w, h);
            int height = this.getMeasuredHeight();
            int width = this.getMeasuredWidth();

//            int width = getWidth();
//            int height = getHeight();
            LogPrintUtil.zhangshi("width:" + width + "------height:" + height);
            this.setPivotX(getWidth() / 2);
            this.setPivotY(getHeight());
            float pivotX = MyEffectView.this.getPivotX();
            float pivotY = MyEffectView.this.getPivotY();
            LogPrintUtil.zhangshi("pivotX:" + pivotX + "---pivotY:" + pivotY);

            ObjectAnimator alpha = ObjectAnimator.ofFloat(this, "alpha", 0, 0.2f, 0.5f, 1f, 2f);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(this, "scaleY", 0, 0.2f, 0.5f, 1f);
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(this, "scaleX", 0, 0.2f, 0.5f, 1f);
            ObjectAnimator moveY = ObjectAnimator.ofFloat(this, "translationY", -100);
            ObjectAnimator moveX = ObjectAnimator.ofFloat(this, "translationX", 200);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.setDuration(1000);
            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    onDestroy();
                }
            });
//            animatorSet.play(alpha)/*.with(scaleX).with(scaleY).with(moveX).with(moveY)*/;
            animatorSet.play(scaleX).with(scaleY);
//            animatorSet.play(scaleX).with(scaleY).with(moveY);

            animatorSet.start();
        }

    }

    private FrameLayout getActivityRoot(Activity activity) {
        if (activity == null) {
            return null;
        }
        try {
            return (FrameLayout) activity.getWindow().getDecorView().findViewById(android.R.id.content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void onDestroy() {
        try {
            if (mActivityRootContainer != null) {
                mActivityRootContainer.removeView(this);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
