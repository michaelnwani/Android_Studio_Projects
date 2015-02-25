package events.example.org.events;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import static android.provider.BaseColumns._ID;
import static events.example.org.events.Constants.TITLE;
import static events.example.org.events.Constants.TIME;
import static events.example.org.events.Constants.TABLE_NAME;
/**
 * Created by michaelnwani on 2/24/15.
 */
public class EventsProvider extends ContentProvider {
    private static final int EVENTS = 1;
    private static final int EVENTS_ID = 2;

    //The MIME type of a directory of events
    private static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.example.event";

    //The MIME type of a single event
    private static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.example.event";

    private EventsData events;
    private UriMatcher uriMatcher;

    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
