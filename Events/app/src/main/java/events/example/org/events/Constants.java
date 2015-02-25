package events.example.org.events;


import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by michaelnwani on 2/23/15.
 */
public interface Constants extends BaseColumns{
    public static final String TABLE_NAME = "events";

    //Columns in the Events database
    public static final String TIME = "time";
    public static final String TITLE = "title";

    public static final String AUTHORITY = "events.example.org.events";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
}
