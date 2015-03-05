package glass.stats;

import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.LiveCard.PublishMode;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * A {@link Service} that publishes a {@link LiveCard} in the timeline.
 */
public class StatsService extends Service {


    private static final String TAG = StatsService.class.getName();

    private LiveCard mLiveCard;
    private RemoteViews rv;
    private StatsReceiver receiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mLiveCard == null) {
            Log.d(TAG, "Creating LiveCard");
            mLiveCard = new LiveCard(this, TAG);
            mLiveCard.setViews(remoteViews());
            mLiveCard.setAction(buildAction());
            mLiveCard.publish(PublishMode.REVEAL);

            buildReceiver(rv);

        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mLiveCard != null && mLiveCard.isPublished()) {
            mLiveCard.unpublish();
            mLiveCard = null;
        }
        super.onDestroy();
    }

    private RemoteViews remoteViews(){
        if (this.rv == null){
            rv = new RemoteViews(getPackageName(), R.layout.stats);
            rv.setTextViewText(R.id.time, StatsUtil.getCurrentTime(this));
            rv.setTextViewText(R.id.connected, StatsUtil.getConnectedString(this));
            Configuration config = getResources().getConfiguration();
            rv.setTextViewText(R.id.language, config.locale.getDisplayLanguage());
            rv.setTextViewText(R.id.country, config.locale.getDisplayCountry());
        }

        return rv;
    }

    private PendingIntent buildAction(){
        Intent menuIntent = new Intent(this, LiveCardMenuActivity.class);
        menuIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return PendingIntent.getActivity(this, 0, menuIntent, 0);
    }

    LiveCard getmLiveCard(){
        return mLiveCard;
    }

    private void buildReceiver(RemoteViews rv){
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        filter.addAction(Intent.ACTION_CONFIGURATION_CHANGED);
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        filter.addAction(Intent.ACTION_TIME_TICK); //update every minute
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        receiver = new StatsReceiver(mLiveCard, rv);
        registerReceiver(receiver, filter); //love this
    }
}
