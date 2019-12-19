package stc.com.wdog;


import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.List;

import stc.com.wdog.reciever.BootReceiver;

/**
 * Created By Nadeem STC
 * */


public class ForeGroundServerService extends Service {

    private static final String TAG = "ForeGroundServerService";
    private Handler h;
    private Runnable r;
    List<ActivityManager.RunningAppProcessInfo> RAP ;
    ActivityManager activitymanager;
    Context context = this;
    public static int START_PERIOD = 3000;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        h = new Handler();

        if (intent.getAction().contains("start")) {
            h = new Handler();
            new Thread(new Runnable() {
                @Override
                public void run() {

                    checkRunningProcess();
                   // mcontext.getPackageManager().getLaunchIntentForPackage("sa.hod.sweetroomksa.demolgwork")
                    startForeground(101, updateNotification());
                    h.postDelayed(this, 20000);//Update  notification on every 20 seconds.
                }}).start();
        } else {
            h.removeCallbacks(r);
            stopForeground(true);
            Log.i(TAG,"#application ForegroundService Stopped");
            stopSelf();
        }

       // return Service.START_NOT_STICKY;
        return Service.START_STICKY;
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private Notification updateNotification() {
        Intent notificationIntent = new Intent(this,MainActivity.class);
        notificationIntent.setAction("start");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        String info ="Watch Dog "+"\n";
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(info)
                .setTicker(info)
                .setContentText(info)
                .setSmallIcon(R.mipmap.launcher_foreground)
                .setPriority(0)
                .setLargeIcon(
                        Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                                R.mipmap.launcher_foreground), 130, 130, false))
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Watch Dog: "+"\t"+"\n"+
                                        "Total Ram: "+"\t"+DeviceUtil.DeviceTotalRamInstalled(this).toString()+"\t"+"GB"+"\n"
                                        +"Avail Ram: "+"\t"+ DeviceUtil.DeviceRamAvailable(this).toString()+"\t"+"GB"+"\n"
                                        +"Used Ram: "+"\t"+ DeviceUtil.DeviceRamUsage(this).toString()+"\t"+"GB"+"\n"))
                .build();

        startForeground(1,notification);
        return notification;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        //Log.i(TAG,"#application Is under onTaskRemoved() by swiping");
        Intent restartServiceIntent = new Intent(getApplicationContext(), MainActivity.class);
        restartServiceIntent.setPackage(getPackageName());
        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartServicePendingIntent);
        Log.i(TAG,"#application Is stopped by swiping");
        super.onTaskRemoved(rootIntent);

    }
    @Override
    public void onDestroy() {
        Log.i(TAG,"#application Is Destroyed by swiping");
//        fGServiceCancel(this);
//        fGServiceStart(getApplicationContext(),System.currentTimeMillis() + START_PERIOD);
        super.onDestroy();
    }

    public  void checkRunningProcess() {
         final Context context = this;
         new Thread(new Runnable() {
             @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
             @Override
             public void run() {
                 activitymanager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
                 RAP = activitymanager.getRunningAppProcesses();
                 try{
                 for(ActivityManager.RunningAppProcessInfo processInfo: RAP ){
                     if(processInfo.processName.compareTo("sa.hod.sweeetroomksa.demolgwork")==0){
                         Log.i(TAG,"#application process existed and running");
                     }else{
                         Intent launchIntent = getPackageManager().getLaunchIntentForPackage("sa.hod.sweetroomksa.demolgwork");
                         startActivity(launchIntent);
                         Log.i(TAG,"#application restarts the process");
                     }
                 }
                 }catch (NullPointerException e){
                    Log.e(TAG,"#application didnot found any package yet");
                 }


             }
         }).start();
     }
    }
