package events.example.org.events;

import android.app.Activity;
import android.app.ListActivity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.SimpleCursorAdapter;
import static events.example.org.events.Constants.TITLE;
import static events.example.org.events.Constants.TIME;
import static events.example.org.events.Constants.TABLE_NAME;
import static android.provider.BaseColumns._ID;
import static events.example.org.events.Constants.CONTENT_URI;

public class Events extends ListActivity {
    private EventsData events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        addEvent("Hello, Android!");
        Cursor cursor = getEvents();
        showEvents(cursor);
    }

    private void addEvent(String string){
        //Insert a new record into the Events data source.
        //You would do something similar for delete and update.
//        SQLiteDatabase db = events.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TIME, System.currentTimeMillis());
        values.put(TITLE, string);
        getContentResolver().insert(CONTENT_URI, values);
//        db.insertOrThrow(TABLE_NAME, null, values);
    }

    private static String[] FROM = {_ID, TIME, TITLE};
    private static String ORDER_BY = TIME + " DESC";
    private Cursor getEvents(){
        //Perform a managed query. The activity will handle closing
        //and re-querying the cursor when needed.

//        SQLiteDatabase db = events.getReadableDatabase();
//        Cursor cursor = db.query(TABLE_NAME, FROM, null, null, null, null, ORDER_BY);
        //deprecated because it does operations on the UI thread which can freeze up the UI
        //and deliver a poor user experience. Should use CursorLoader with a LoaderManager instead.
        //read here: http://stackoverflow.com/questions/19651680/cursorloader-with-startmanagingcursor
//        startManagingCursor(cursor);
//        return cursor;

        return managedQuery(CONTENT_URI, FROM, null, null, ORDER_BY);
    }

    private static int[] TO = {R.id.rowid, R.id.time, R.id.title, };

    private void showEvents(Cursor cursor){
        //Stuff them all into a big string
//        StringBuilder builder = new StringBuilder("Saved events:\n");
//        while (cursor.moveToNext()){
//            //Could use getColumnIndexOrThrow() to get indexes
//            long id = cursor.getLong(0);
//            long time = cursor.getLong(1);
//            String title = cursor.getString(2);
//            builder.append(id).append(": ");
//            builder.append(time).append(": ");
//            builder.append(title).append("\n");
//        }
//
//        //Display on the screen
//        TextView text = (TextView) findViewById(R.id.text);
//        text.setText(builder);

        //Set up data binding
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.item, cursor, FROM, TO, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        setListAdapter(adapter);
    }
}
