package dp.vmarkeev.moviedb.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by vmarkeev on 28.02.2017.
 */

public class DialogUtils {

    /**
     * Method to show Toast-message with text from resources, duration short.
     *
     * @param context context of the app.
     * @param message text from resources need to be shown in Toast-message.
     */
    public static void show(Context context, int message) {
        Toast mToast = null;
        if (message == 0) {
            return;
        }
        if (context != null) {
            mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        }
        if (mToast != null) {
            mToast.setText(context.getString(message));
            mToast.show();
        }
    }

    /**
     * Method to show Toast-message with given text, duration short.
     *
     * @param context context of the app.
     * @param message text need to be shown in Toast-message.
     */
    public static void show(Context context, String message) {
        Toast mToast = null;
        if (message == null) {
            return;
        }
        if (context != null) {
            mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        }
        if (mToast != null) {
            mToast.setText(message);
            mToast.show();
        }
    }

    /**
     * Method to show Toast-message with text from resources, duration long.
     *
     * @param context context of the app.
     * @param message text from resources need to be shown in Toast-message.
     */
    public static void showLong(Context context, int message) {
        Toast mToast = null;
        if (message == 0) {
            return;
        }
        if (context != null) {
            mToast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        }
        if (mToast != null) {
            mToast.setText(context.getString(message));
            mToast.show();
        }
    }

    /**
     * Method to show Toast-message with text from resources, duration long.
     *
     * @param context context of the app.
     * @param message text from resources need to be shown in Toast-message.
     */
    public static void showLong(Context context, String message) {
        Toast mToast = null;
        if (message == null) {
            return;
        }
        if (context != null) {
            mToast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        }
        if (mToast != null) {
            mToast.setText(message);
            mToast.show();
        }
    }
}
