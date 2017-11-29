package com.m2team.library.utils;

import com.orhanobut.logger.Logger;


public class LogUtils {
    private static boolean isDebug = true;

    private static String mTag = "m2team";

    //for error Logger
    public static void error(String msg) {
        if (isDebug) {
            Logger.e(mTag, msg);
        }
    }

    //for warming Logger
    public static void warn(String msg) {
        if (isDebug) {
            Logger.w(mTag, msg);
        }
    }

    //for info Logger
    public static void info(String msg) {
        if (isDebug) {
            Logger.i(mTag, msg);
        }
    }

    //for debug Logger
    public static void debug(String msg) {
        if (isDebug) {
            Logger.d(mTag, msg);
        }
    }

    //for verbose Logger
    public static void verbose(String msg) {
        if (isDebug) {
            Logger.v(mTag, msg);
        }
    }

    //for error Logger
    public static void e(String msg) {
        if (isDebug) {
            Logger.e(mTag, msg);
        }
    }

    //for warming Logger
    public static void w(String msg) {
        if (isDebug) {
            Logger.w(mTag, msg);
        }
    }

    //for info Logger
    public static void i(String msg) {
        if (isDebug) {
            Logger.i(mTag, msg);
        }
    }

    //for debug Logger
    public static void d(String msg) {
        if (isDebug) {
            Logger.d(mTag, msg);
        }
    }

    //for verbose Logger
    public static void v(String msg) {
        if (isDebug) {
            Logger.v(mTag, msg);
        }
    }


    //for warming Logger
    public static void w(String tag, String msg) {
        if (isDebug) {
            if (tag == null || "".equalsIgnoreCase(tag.trim())) {
                tag = mTag;
            }
            Logger.w(tag, msg);
        }
    }

    //for info Logger
    public static void i(String tag, String msg) {
        if (isDebug) {
            if (tag == null || "".equalsIgnoreCase(tag.trim())) {
                tag = mTag;
            }
            Logger.i(tag, msg);
        }
    }

    //for debug Logger
    public static void d(String tag, String msg) {
        if (isDebug) {
            if (tag == null || "".equalsIgnoreCase(tag.trim())) {
                tag = mTag;
            }
            Logger.d(tag, msg);
        }
    }

    //for verbose Logger
    public static void v(String tag, String msg) {
        if (isDebug) {
            if (tag == null || "".equalsIgnoreCase(tag.trim())) {
                tag = mTag;
            }
            Logger.v(tag, msg);
        }
    }

    //for verbose Logger
    public static void e(String tag, String msg) {
        if (isDebug) {
            if (tag == null || "".equalsIgnoreCase(tag.trim())) {
                tag = mTag;
            }
            Logger.e(tag, msg);
        }
    }

    public static void setDebug(boolean isDebug) {
        LogUtils.isDebug = isDebug;
    }

    public static boolean isDebug() {
        return isDebug;
    }

    public static void showLogger(String tag, String msg) {
        if (isDebug) {
            if (tag == null || "".equalsIgnoreCase(tag.trim())) {
                tag = mTag;
            }
            StackTraceElement[] stackTraceElement = Thread.currentThread()
                    .getStackTrace();
            int currentIndex = -1;
            for (int i = 0; i < stackTraceElement.length; i++) {
                if (stackTraceElement[i].getMethodName().compareTo("showLogger") == 0) {
                    currentIndex = i + 1;
                    break;
                }
            }
            if (currentIndex >= 0) {
                String fullClassName = stackTraceElement[currentIndex].getClassName();
                String className = fullClassName.substring(fullClassName
                        .lastIndexOf(".") + 1);
                String methodName = stackTraceElement[currentIndex].getMethodName();
                String lineNumber = String
                        .valueOf(stackTraceElement[currentIndex].getLineNumber());

                Logger.i(tag, msg + "\n  ---->at " + className + "." + methodName + "("
                        + className + ".java:" + lineNumber + ")");
            } else {
                Logger.i(tag, msg);
            }

        }
    }
}
