package com.reactnativecomponent.splashscreen;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.lang.ref.WeakReference;


public class RCTSplashScreen {

    public static final int UIAnimationNone = 0;
    public static final int UIAnimationFade = 1;
    public static final int UIAnimationScale = 2;
    private static final String TAG = "RCTSplashScreen";

    private static Dialog dialog;
    private static ImageView imageView;
    private static String ORIETNATION = "";

    private static WeakReference<Activity> wr_activity;

    protected static Activity getActivity() {
        return wr_activity.get();
    }

    public static void openSplashScreen(Activity activity, String orientation) {
        ORIETNATION = orientation;
        openSplashScreen(activity, false);
    }

    public static void openSplashScreen(Activity activity, boolean isFullScreen) {
        openSplashScreen(activity, isFullScreen, ImageView.ScaleType.CENTER_CROP);
    }

    public static void openSplashScreen(final Activity activity, final boolean isFullScreen, final ImageView.ScaleType scaleType) {
        if (activity == null) return;
        wr_activity = new WeakReference<>(activity);
        final int drawableId = getImageId();
        if ((dialog != null && dialog.isShowing()) || (drawableId == 0)) {
            return;
        }
        activity.runOnUiThread(new Runnable() {
            public void run() {

                if (!getActivity().isFinishing()) {
                    Context context = getActivity();
                    imageView = new ImageView(context);

                    imageView.setImageResource(drawableId);

                    LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                    imageView.setLayoutParams(layoutParams);

                    imageView.setImageResource(drawableId);
                    imageView.setScaleType(scaleType);

                    dialog = new Dialog(context, isFullScreen ? android.R.style.Theme_Translucent_NoTitleBar_Fullscreen : android.R.style.Theme_Translucent_NoTitleBar);

//                    if ((getActivity().getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN)
//                            == WindowManager.LayoutParams.FLAG_FULLSCREEN) {
//                        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//                    }
                    dialog.setContentView(imageView);
                    dialog.setCancelable(false);
                    dialog.show();
                }

            }
        });
    }

    public static void removeSplashScreen(Activity activity, final int animationType, final int duration) {
        if (activity == null) {
            activity = getActivity();
            if (activity == null) return;
        }
        activity.runOnUiThread(new Runnable() {
            public void run() {
                if (dialog != null && dialog.isShowing()) {
                    AnimationSet animationSet = new AnimationSet(true);

                    if (animationType == UIAnimationScale) {
                        AlphaAnimation fadeOut = new AlphaAnimation(1, 0);
                        fadeOut.setDuration(duration);
                        animationSet.addAnimation(fadeOut);

                        ScaleAnimation scale = new ScaleAnimation(1, 1.5f, 1, 1.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.65f);
                        scale.setDuration(duration);
                        animationSet.addAnimation(scale);
                    } else if (animationType == UIAnimationFade) {
                        AlphaAnimation fadeOut = new AlphaAnimation(1, 0);
                        fadeOut.setDuration(duration);
                        animationSet.addAnimation(fadeOut);
                    } else {
                        AlphaAnimation fadeOut = new AlphaAnimation(1, 0);
                        fadeOut.setDuration(0);
                        animationSet.addAnimation(fadeOut);
                    }

                    final View view = ((ViewGroup) dialog.getWindow().getDecorView()).getChildAt(0);
                    view.startAnimation(animationSet);

                    animationSet.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            view.post(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.dismiss();
                                    dialog = null;
                                    imageView = null;
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    private static String getOrientation() {
        Log.e("RCTSplashScreen","getOrientation : " +  ORIETNATION);
        return ORIETNATION;
//        String[] landscapes = RCTSplashScreenPackage.LandescapeMode.split(",");
//        Log.e("RCTSplashScreen","Setting LandescapeMode to : " +  RCTSplashScreenPackage.LandescapeMode);
//        String model = Build.MANUFACTURER + " - " + Build.MODEL;
//        Log.e("RCTSplashScreen","Current model is  : " + model);
//        return java.util.Arrays.asList(landscapes).indexOf(model) != -1?"LANDSCAPE" :"PORTRAIT";
    }

    private static int getImageId() {
        int drawableId = getActivity().getResources().getIdentifier("splash", "drawable", getActivity().getClass().getPackage().getName());
        if (getOrientation().compareTo("LANDSCAPE") == 0) {
            drawableId = getActivity().getResources().getIdentifier("splash_horizontal", "drawable", getActivity().getPackageName());
            Log.e(TAG, "Drawable id for landscape is 2 : " + drawableId);
        } else {
            if (drawableId == 0) {
                drawableId = getActivity().getResources().getIdentifier("splash", "drawable", getActivity().getPackageName());
            }
        }
        return drawableId;
    }


}
