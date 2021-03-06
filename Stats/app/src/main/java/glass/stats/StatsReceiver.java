package glass.stats;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.widget.RemoteViews;

import com.google.android.glass.timeline.LiveCard;

/**
 * Created by michaelnwani on 3/4/15.
 */
public class StatsReceiver extends BroadcastReceiver {
    private LiveCard mLiveCard;
    private RemoteViews rv;

    public StatsReceiver(LiveCard liveCard, RemoteViews rv){
        this.mLiveCard = liveCard;
        this.rv = rv;
    }

    //I can replace these values with any information about my glass
    @Override
    public void onReceive(Context context, Intent intent) {
        //set the current time no matter what
        rv.setTextViewText(R.id.time, StatsUtil.getCurrentTime(context));

        //if connection status changes, set it
        if (Intent.ACTION_POWER_CONNECTED.equals(intent.getAction())){
            rv.setTextViewText(R.id.connected, context.getString(R.string.connected));
        }
        if (Intent.ACTION_POWER_DISCONNECTED.equals(intent.getAction())){
            rv.setTextViewText(R.id.connected, context.getString(R.string.disconnected));
        }

        //if a battery value changes, update them all
        if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())){
            rv.setTextViewText(R.id.battery_degrees, StatsUtil.getDegrees(intent) + "C");
            rv.setTextViewText(R.id.battery_voltage, StatsUtil.getVoltage(intent) + "V");
            rv.setProgressBar(R.id.battery_level, 100, StatsUtil.getBatteryLevel(intent), false);
        }

        if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())){
            rv.setImageViewResource(R.id.wifi_strength, StatsUtil.getWifiIconResource(context));
        }

        //inform the livecard of the changes to the views
        mLiveCard.setViews(rv);

    }
}
