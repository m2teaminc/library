package com.m2team.library.utils;


import timber.log.Timber;

public class LogUtils {
    private static boolean isDebug = true;

    private static String mTag = "m2team";

    //for error Timber
    public static void error(String msg) {
        if (isDebug) {
            Timber.e(mTag, msg);
        }
    }

    //for warming Timber
    public static void warn(String msg) {
        if (isDebug) {
            Timber.w(mTag, msg);
        }
    }

    //for info Timber
    public static void info(String msg) {
        if (isDebug) {
            Timber.i(mTag, msg);
        }
    }

    //for debug Timber
    public static void debug(String msg) {
        if (isDebug) {
            Timber.d(mTag, msg);
        }
    }

    //for verbose Timber
    public static void verbose(String msg) {
        if (isDebug) {
            Timber.v(mTag, msg);
        }
    }

    //for error Timber
    public static void e(String msg) {
        if (isDebug) {
            Timber.e(mTag, msg);
        }
    }

    //for warming Timber
    public static void w(String msg) {
        if (isDebug) {
            Timber.w(mTag, msg);
        }
    }

    //for info Timber
    public static void i(String msg) {
        if (isDebug) {
            Timber.i(mTag, msg);
        }
    }

    //for debug Timber
    public static void d(String msg) {
        if (isDebug) {
            Timber.d(mTag, msg);
        }
    }

    //for verbose Timber
    public static void v(String msg) {
        if (isDebug) {
            Timber.v(mTag, msg);
        }
    }


    //for warming Timber
    public static void w(String tag, String msg) {
        if (isDebug) {
            if (tag == null || "".equalsIgnoreCase(tag.trim())) {
                tag = mTag;
            }
            Timber.w(tag, msg);
        }
    }

    //for info Timber
    public static void i(String tag, String msg) {
        if (isDebug) {
            if (tag == null || "".equalsIgnoreCase(tag.trim())) {
                tag = mTag;
            }
            Timber.i(tag, msg);
        }
    }

    //for debug Timber
    public static void d(String tag, String msg) {
        if (isDebug) {
            if (tag == null || "".equalsIgnoreCase(tag.trim())) {
                tag = mTag;
            }
            Timber.d(tag, msg);
        }
    }

    //for verbose Timber
    public static void v(String tag, String msg) {
        if (isDebug) {
            if (tag == null || "".equalsIgnoreCase(tag.trim())) {
                tag = mTag;
            }
            Timber.v(tag, msg);
        }
    }

    //for verbose Timber
    public static void e(String tag, String msg) {
        if (isDebug) {
            if (tag == null || "".equalsIgnoreCase(tag.trim())) {
                tag = mTag;
            }
            Timber.e(tag, msg);
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

                Timber.i(tag, msg + "\n  ---->at " + className + "." + methodName + "("
                        + className + ".java:" + lineNumber + ")");
            } else {
                Timber.i(tag, msg);
            }

        }
    }
}
