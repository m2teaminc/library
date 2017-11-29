package com.m2team.library.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;

import java.lang.reflect.Method;

public class DisplayUtils {
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int sp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int getScreenWidthPixels(Activity context) {
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }

    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }

    public static int getStatusHeight(Context context) {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int id = (Integer) (clazz.getField("status_bar_height").get(object));
            statusHeight = context.getResources().getDimensionPixelSize(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    public static int getRealScreenHeight(Context context) {
        int dpi = 0;
        Display display = ((Activity) context).getWindowManager()
                .getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked") Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            dpi = dm.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dpi;
    }

    public static int getNavigationAreaHeight(Context context) {
        int realScreenHeight = getRealScreenHeight(context);
        int screenHeight = getScreenHeight(context);

        return realScreenHeight - screenHeight;
    }

    public static int getNavigationBarrH(Context c) {
        Resources resources = c.getResources();
        int identifier = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        return resources.getDimensionPixelOffset(identifier);
    }

    public static Bitmap snapShotWithStatusBar(Activity activity)
    {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;

    }

    public static Bitmap snapShotWithoutStatusBar(Activity activity)
    {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        return bp;
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    public static int getDisplayRotation(Activity activity) {
        switch (activity.getWindowManager().getDefaultDisplay().getRotation()) {
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
            default:
                return 0;
        }
    }

    public static boolean isLandscape(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static boolean isPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }


    public static void lockOrientationLandscape(final Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    public static void lockOrientationPortrait(final Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public static void lockOrientation(final Activity activity) {
        final int orientation = activity.getResources().getConfiguration().orientation;
        final int rotation = ((WindowManager) activity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();

        if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_90) {
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        } else if (rotation == Surface.ROTATION_180 || rotation == Surface.ROTATION_270) {
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
            } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
            }
        }
    }

    public static void unlockOrientation(final Activity activity) {
        activity.setRequestedOrientation(getManifestOrientation(activity));
    }

    public static int getManifestOrientation(final Activity activity) {
        try {
            ActivityInfo app = activity
                    .getPackageManager()
                    .getActivityInfo(activity.getComponentName(), PackageManager.GET_ACTIVITIES | PackageManager.GET_META_DATA);
            return app.screenOrientation;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
    }

    public static boolean isCurrentOrientationPortrait(final Context activityContext) {
        final int orientation = activityContext.getResources().getConfiguration().orientation;
        final int rotation = ((WindowManager) activityContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();

        if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_90) {
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                return true;
            } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                return false;
            }
        } else if (rotation == Surface.ROTATION_180 || rotation == Surface.ROTATION_270) {
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                return true;
            } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                return false;
            }
        }
        return false;
    }

    public static boolean isCurrentOrientationLandscape(final Context activityContext) {
        return !isCurrentOrientationPortrait(activityContext);
    }

    public static Point getDisplaySize(Context context) {
        int sAPILevel = Build.VERSION.SDK_INT;
        final Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        DisplayMetrics sDisplayMetrics = context.getResources().getDisplayMetrics();
        if (sAPILevel < 17) {
            display.getMetrics(sDisplayMetrics);
        } else /*if (hasAPI(17))*/ {
            display.getRealMetrics(sDisplayMetrics);
        }

        final Point sizePoint = new Point(0, 0);
        if (sAPILevel > 16) {
            boolean tryToUseReflection = false;
            try {
                display.getRealSize(sizePoint);
            } catch (Exception ignored) {
                tryToUseReflection = true;
            }
            if (tryToUseReflection) {
                // includes window decorations (statusbar bar/menu bar)
                try {
                    Display.class.getMethod("getRealSize", Point.class).invoke(display, sizePoint);
                } catch (Exception ignored) {
                }
            }
        }
        // also for API lower than 17
        if (sizePoint.x == 0 || sizePoint.y == 0) {
            if (sAPILevel < 14) {
                sizePoint.set(sDisplayMetrics.widthPixels, sDisplayMetrics.heightPixels);
            } else /*if (sAPILevel > 13 && sAPILevel < 17)*/ {
                // includes window decorations (statusbar bar/menu bar) 14,15,16 api levels
                // or if API 17 does not have getRealSize() method for some reason (impossible?)
                try {
                    sizePoint.set((int) Display.class.getMethod("getRawWidth").invoke(display),
                            (int) Display.class.getMethod("getRawHeight").invoke(display));
                } catch (Exception ignored) {
                }
            }
        }
        return sizePoint;
    }


    public static int getCurrentOrientationScreenHeight(Context context) {
        return getDisplaySize(context).y;
    }

    public static int getCurrentOrientationScreenWidth(Context context) {
        return getDisplaySize(context).x;
    }


}
