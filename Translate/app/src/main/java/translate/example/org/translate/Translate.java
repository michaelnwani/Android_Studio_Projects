package translate.example.org.translate;

import android.app.Activity;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

/**
 * AS OF DECEMBER 2011, THE GOOGLE TRANSLATE API VERSION 1 IS NO LONGER AVAILABLE
 * VERSION 2 IS PAY ONLY AND REQUIRES KEY SIGNING, THUS RENDERING THIS APP A LITTLE INCOMPLETE
 */
public class Translate extends Activity {
    private Spinner fromSpinner;
    private Spinner toSpinner;
    private EditText origText;
    private TextView transText;
    private TextView retransText;
    private TextWatcher textWatcher;
    private AdapterView.OnItemSelectedListener itemListener;
    private Handler guiThread;
    private ExecutorService transThread;
    private Runnable updateTask;
    private Future transPending;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initThreading();
        findViews();
        setAdapters();
        setListeners();
    }

    private void findViews()
    {
        fromSpinner = (Spinner)findViewById(R.id.from_language);
        toSpinner = (Spinner)findViewById(R.id.to_language);
        origText = (EditText)findViewById(R.id.original_text);
        transText = (TextView)findViewById(R.id.translated_text);
        retransText = (TextView)findViewById(R.id.retranslated_text);
    }

    private void setAdapters()
    {
        //Spinner list comes from a resource
        //Spinner user interface uses standard layouts
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.languages, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(adapter);
        toSpinner.setAdapter(adapter);

        //Automatically select two spinner items
        fromSpinner.setSelection(8); //English (en)
        toSpinner.setSelection(11); //French (fr)
    }

    private void setListeners()
    {
        //Define event listeners
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Do nothing

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //these delays are so we avoid making too many calls
                //to the external web service
                queueUpdate(1000);

            }

            @Override
            public void afterTextChanged(Editable s) {
                //Do nothing

            }
        };

        itemListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //these delays are so we avoid making too many calls
                //to the external web service
                queueUpdate(200); //milliseconds
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Do nothing

            }
        };

        //Set listeners on graphical user interface widgets
        origText.addTextChangedListener(textWatcher);
        fromSpinner.setOnItemSelectedListener(itemListener);
        toSpinner.setOnItemSelectedListener(itemListener);

    }

    private void initThreading()
    {
        guiThread = new Handler();
        transThread = Executors.newSingleThreadExecutor();

        //This task does a translation and updates the screen
        updateTask = new Runnable()
        {
            public void run()
            {
                //Get text to translate
                String original = origText.getText().toString().trim();

                //Cancel previous translation if there was one
                if (transPending != null)
                {
                    transPending.cancel(true);
                }

                //Take care of the easy case
                if (original.length() == 0)
                {
                    transText.setText(R.string.empty);
                    retransText.setText(R.string.empty);
                }
                else
                {
                    //Let the user know we're doing something
                    transText.setText(R.string.translating);
                    retransText.setText(R.string.translating);

                    //Begin translation now but don't wait for it
                    try
                    {
                        TranslateTask translateTask = new TranslateTask(
                                Translate.this, //reference to activity
                                original, //original text
                                getLang(fromSpinner), //from language
                                getLang(toSpinner) //to language
                        );
                        transPending = transThread.submit(translateTask);
                    }
                    catch (RejectedExecutionException e)
                    {
                        //Unable to start new task
                        transText.setText(R.string.translation_error);
                        retransText.setText(R.string.translation_error);
                    }
                }
            }
        };

    }

    //Extract the language code from the current spinner item
    private String getLang(Spinner spinner)
    {
        String result = spinner.getSelectedItem().toString();
        int lparen = result.indexOf('(');
        int rparen = result.indexOf(')');
        result = result.substring(lparen + 1, rparen);

        return result;
    }

    //Request an update to start after a short delay
    private void queueUpdate(long delayMillis)
    {
        //Cancel previous update if it hasn't started yet
        guiThread.removeCallbacks(updateTask);
        //Start an update if nothing happens after a few milliseconds
        guiThread.postDelayed(updateTask, delayMillis);
    }

    //Modify text on the screen (called from another thread)
    public void setTranslated(String text)
    {
        guiSetText(transText, text);
    }

    //Modify text on the screen (called from another thread)
    public void setRetranslated(String text)
    {
        guiSetText(retransText, text);
    }

    //All changes to the GUI must be done in the GUI thread
    private void guiSetText(final TextView view, final String text)
    {
        guiThread.post(new Runnable()
        {
            public void run()
            {
                view.setText(text);
            }
        });
    }
}
