package stc.com.wdog.reciever;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import stc.com.wdog.ForeGroundServerService;
import stc.com.wdog.MainActivity;


/**
 * Created By Nadeem STC
 * */

public class BootReceiver extends BroadcastReceiver {
    ForeGroundServerService mForegroundservice;
    public static final int REQUEST_CODE = 1;
	@Override
    public void onReceive(Context context, Intent intent) {
       final Context mcontext = context;
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                            Intent i = new Intent(mcontext, MainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mcontext.startActivity(i);
                    }
                },
                0050); }
}