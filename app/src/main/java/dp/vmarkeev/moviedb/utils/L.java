package dp.vmarkeev.moviedb.utils;

import android.support.compat.BuildConfig;
import android.util.Log;

public class L {
    private static boolean isDebug = !BuildConfig.DEBUG;
    private static String tagL = "tag";

    public static void i(Object msg) {
        if (isDebug) {
            Log.i(tagL, "........................" + msg);
        }
    }

    public static void d(Object msg) {
        if (isDebug) {
            Log.d(tagL, "........................" + msg);
        }
    }

    public static void e(Object msg) {
        if (isDebug) {
            Log.e(tagL, "........................" + msg);
        }
    }

    public static void v(Object msg) {
        if (isDebug) {
            Log.v(tagL, "........................" + msg);
        }
    }

    public static void i(String tag, Object msg) {
        if (isDebug) {
            Log.i(tag, "........................" + msg);
        }
    }

    public static void d(String tag, Object msg) {
        if (isDebug) {
            Log.d(tag, "........................" + msg);
        }
    }

    public static void e(String tag, Object msg) {
        if (isDebug)
            Log.e(tag, "........................" + msg);
    }

    public static void v(String tag, Object msg) {
        if (isDebug)
            Log.v(tag, "........................" + msg);
    }
}
