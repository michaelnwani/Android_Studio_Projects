package edu.neu.madcourse.michaelnwani;

import android.app.ActionBar;
import android.app.Activity;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Created by michaelnwani on 2/15/15.
 */
public class DictionaryActivity extends Activity {

    private EditText mEditText;
    private TextView mTextView;
    private static final int DICTIONARY_WORD_COUNT = 432334;
    private final HashMap<String, String> words2 = new HashMap<String, String>(DICTIONARY_WORD_COUNT);
    private Button mClearButton;
    private Button mBackButton;
    private Button mAcknowledgementButton;
    private MediaPlayer mp;
    private TextView mAcknowledgementTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dictionary);

        ActionBar actionBar = getActionBar();
        actionBar.setTitle("Test Dictionary");


        readFile();

        mEditText = (EditText)findViewById(R.id.dictionary_search);
        mTextView = (TextView)findViewById(R.id.dictionary_results);
        mClearButton = (Button)findViewById(R.id.clear_button);
        mBackButton = (Button)findViewById(R.id.back_button);
        mAcknowledgementButton = (Button)findViewById(R.id.acknowledgement_button);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mAcknowledgementTextView = (TextView)findViewById(R.id.acknowledgements_textView);
        mAcknowledgementTextView.setVisibility(View.INVISIBLE);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                //right after text is changed
                if (s.toString().length() > 2)
                {
                    if (words2.containsKey(s.toString()))
                    {
                        if (mp != null)
                        {
                            mp.release();
                        }
                        mTextView.append(words2.get(s.toString()) + "\n");
                        playChime();
                    }

                }

            }
        });

        mClearButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                mEditText.getText().clear();
                mTextView.setText("");
            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mAcknowledgementButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
            mAcknowledgementTextView.setVisibility(View.VISIBLE);
            }
        });





    }

    private boolean readFile() {

        try {
            AssetManager am = getAssets();

            InputStream inputStream = am.open("wordlist.txt");

            if (inputStream != null)
            {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";

                while ((receiveString = bufferedReader.readLine()) != null )
                {
                    if (receiveString.length() >= 3)
                    {

                        words2.put(receiveString, receiveString);
                    }

                }

                inputStream.close();

            }
        }
        catch (FileNotFoundException e)
        {
           //
        }
        catch (IOException e)
        {
            //
        }

        return true;
    }

    public void playChime()
    {
        //Create a new MediaPlayer to play this sound
        mp = MediaPlayer.create(this, R.raw.chime);
        mp.start();
    }
}
