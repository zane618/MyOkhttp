package com.example.myokhttp.wm;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.myokhttp.R;
import com.example.myokhttp.util.LogPrintUtil;

import pl.droidsonroids.gif.GifDrawable;


/**
 * created by zhangshi on 2020-03-10.
 */
public class DiamondEffectView extends FrameLayout {
    private Context mContext;
    private int mScreenWidth;
    private int mScreenHeight;
    private FrameLayout mActivityRootContainer;
    private float[] mLocaiton; //出现的位置
    private float viewHeight; //view高
    private float viewWidth; //view宽

    private View layout;
    ImageView ivGif;

    public DiamondEffectView(@NonNull Context context, float[] locaiton) {
        super(context);
        this.mLocaiton = locaiton;
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        mActivityRootContainer = getActivityRoot((Activity) mContext);

        createView();
        addFloatViewToActivity();
        show();
    }

    /**
     * 悬浮view添加到activity根视图
     */
    public void addFloatViewToActivity() {
        if (mActivityRootContainer == null) {
            return;
        }
        mActivityRootContainer.addView(this);
    }

    private void createView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        layout = inflater.inflate(R.layout.hyb_layout_diamond_effect, null);
        ivGif = layout.findViewById(R.id.iv_gif);
        addView(layout);
    }

    /**
     * 执行动画
     */
    public void show() {
        if (layout != null) {

            int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            this.measure(w, h);
            viewWidth = this.getMeasuredWidth();
            viewHeight = this.getMeasuredHeight();
            LogPrintUtil.zhangshi("view宽：" + viewWidth + "----view高：" + viewHeight);
            layout.setX(mLocaiton[0] - viewWidth / 2);
            layout.setY(mLocaiton[1] - viewHeight / 2);
            loadGif();

//            ObjectAnimator alpha = ObjectAnimator.ofFloat(this, "alpha", 0, 0.2f, 0.5f, 1);
//            ObjectAnimator zoom = ObjectAnimator.ofFloat(this, "alpha", 0, 0.2f, 0.5f, 1);
//            ObjectAnimator moveY = ObjectAnimator.ofFloat(this, "translationY", -300);
//            ObjectAnimator moveX = ObjectAnimator.ofFloat(this, "translationX", 200);
//            AnimatorSet animatorSet = new AnimatorSet();
//            animatorSet.setDuration(1000);
//            animatorSet.addListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    onDestroy();
//                }
//            });
//            animatorSet.play(alpha).with(zoom).with(moveX).with(moveY);
//            animatorSet.start();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onDestroy();
                }
            }, 1000);
        }
    }

    /**
     * 加在gif
     */
    private void loadGif() {
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
