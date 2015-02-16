package edu.neu.madcourse.michaelnwani;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.neu.madcourse.michaelnwani.org.example.sudoku.Sudoku;


public class MainActivity extends Activity {

    private Button mAboutButton;
    private Button mSudokuButton;
    private Button mQuitButton;
    private Button mErrorButton;
    private Button mDictionaryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAboutButton = (Button)findViewById(R.id.about_button);
        mSudokuButton = (Button)findViewById(R.id.sudoku_button);
        mQuitButton = (Button)findViewById(R.id.quit_button);

        mErrorButton = (Button)findViewById(R.id.error_button);
        mDictionaryButton = (Button)findViewById(R.id.dictionary_button);

        mAboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(i);
            }
        });

        mSudokuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Sudoku.class);
                startActivity(i);
            }
        });

        mQuitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mErrorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Not really sure what to do here... so here's an infinite loop


                try
                {
                    int i = 1, b=10000000;

                    while (i < 10)
                    {
                        b *= b;
                    }
                    throw new IllegalAccessException("An error I purposely created");
                }
                catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });

        mDictionaryButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, DictionaryActivity.class);
                startActivity(i);
            }
        });

    }
}
