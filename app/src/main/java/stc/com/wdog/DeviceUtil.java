package stc.com.wdog;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.CopyOnWriteArrayList;

import static android.content.Context.ACTIVITY_SERVICE;

public class DeviceUtil {

    public Context mContext;

    public DeviceUtil(Context context){
        this.mContext=context;
    }

    public static Double DeviceTotalRamInstalled(Context Context) {
        Double availableMegs;
        ActivityManager actManager = (ActivityManager) Context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        if (actManager != null)
        {
            actManager.getMemoryInfo(memInfo);
            availableMegs = (memInfo.totalMem)/(1024.0*1024.0*1204.0);

            Double availableGB = BigDecimal.valueOf(availableMegs)
                    .setScale(4, RoundingMode.HALF_UP)
                    .doubleValue();
            return availableGB;
        }
        return null;


    }

    public static Double DeviceRamAvailable(Context mContext) {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager)mContext.getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        Double availableMegs = mi.availMem/(1024.0*1024.0*1204.0);

        Double availableGB = BigDecimal.valueOf(availableMegs)
                .setScale(4, RoundingMode.HALF_UP)
                .doubleValue();
        return availableGB;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static Double DeviceRamUsage(Context mContext) {

      /*  Double ramInstalled = new DeviceUtil(mContext).DeviceTotalRamInstalled();
        Double ramAvailable = DeviceRamAvailable(mContext);*/
        Double ramInstalled = DeviceTotalRamInstalled(mContext);
        Double ramAvailable = DeviceRamAvailable(mContext);

        //Double usagepercent = (ramInstalled - ramAvailable);
        Double usagepercent = DeviceTotalRamInstalled(mContext)-DeviceRamAvailable(mContext);

        Double availableGB = BigDecimal.valueOf(usagepercent)
                .setScale(4, RoundingMode.HALF_UP)
                .doubleValue();
        return availableGB;
    }
}
