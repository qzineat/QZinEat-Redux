package com.qe.qzin.util;

import android.app.Activity;
import android.content.Intent;

import com.qe.qzin.QZinApplication;
import com.qe.qzin.R;

/**
 * Created by srokde on 3/18/17.
 */

public class QZinUtil {
    public static void changeTheme(Activity activity) {
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        // Set Application variables
        QZinApplication.isHostView = !QZinApplication.isHostView;
    }

    public static void onActivityCreateSetTheme(Activity activity) {
        if(QZinApplication.isHostView){
            //activity.setTheme(R.style.HostTheme);
        }
    }
}
