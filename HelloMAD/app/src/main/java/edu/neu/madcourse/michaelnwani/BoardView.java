package edu.neu.madcourse.michaelnwani;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import edu.neu.madcourse.michaelnwani.org.example.sudoku.Prefs;

/**
 * Created by michaelnwani on 2/25/15.
 */
public class BoardView extends View {
    private static final String SELX = "selX";
    private static final String SELY = "selY";
    private static final String VIEW_STATE = "viewState";
    private static final int ID = 60;

    private static final String TAG = "WORD DETECTION";

    private final WordFadeActivity game;
    private char[] wordArray;
//    private static final int FILE_WORD_COUNT = 50000;
//    private final HashMap<String, String> words = new HashMap<String, String>(FILE_WORD_COUNT);
//    private final HashMap<String, Boolean> fileRead = new HashMap<String, Boolean>(26);
    private final ArrayList<String> wordList = new ArrayList<String>(40);


    private String startingLetter = "";
    private String s = "";

    public BoardView(Context context)
    {
        super(context);
        this.game = (WordFadeActivity) context;
        setFocusable(true);
        setFocusableInTouchMode(true);

        setId(ID);


//        fileRead.put("a",false);
//        fileRead.put("b",false);
//        fileRead.put("c",false);
//        fileRead.put("d",false);
//        fileRead.put("e",false);
//        fileRead.put("f",false);
//        fileRead.put("g",false);
//        fileRead.put("h",false);
//        fileRead.put("i",false);
//        fileRead.put("j",false);
//        fileRead.put("k",false);
//        fileRead.put("l",false);
//        fileRead.put("m",false);
//        fileRead.put("n",false);
//        fileRead.put("o",false);
//        fileRead.put("p",false);
//        fileRead.put("q",false);
//        fileRead.put("r",false);
//        fileRead.put("s",false);
//        fileRead.put("t",false);
//        fileRead.put("u",false);
//        fileRead.put("v",false);
//        fileRead.put("w",false);
//        fileRead.put("x",false);
//        fileRead.put("y",false);
//        fileRead.put("z",false);
    }

    private float width;    //width of one tile
    private float height;   //height of one tile
    private int selX;       //X index of selection
    private int selY;       //Y index of selection
    private final Rect selRect = new Rect();

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w / 9f;
        height = h / 9f;
        getRect(selX, selY, selRect);
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void getRect(int x, int y, Rect rect)
    {
        rect.set((int)(x * width), (int)(y * height),
                (int)(x * width + width), (int)(y * height + height));
    }

    @Override
    protected void onDraw(Canvas canvas) {

        //Draw the background...
        Paint background = new Paint();
        background.setColor(getResources().getColor(R.color.puzzle_background));

        canvas.drawRect(0, 0, getWidth(), getHeight(), background);

        //Draw the board...
        //Define colors for the grid lines
        Paint dark = new Paint();
        dark.setColor(getResources().getColor(R.color.puzzle_dark));

        Paint hilite = new Paint();
        hilite.setColor(getResources().getColor(R.color.puzzle_hilite));

        Paint light = new Paint();
        light.setColor(getResources().getColor(R.color.puzzle_light));

        //Draw the minor grid lines
        for (int i = 0; i < 9; i++)
        {
            //makes perfect sense if you visualize it
            canvas.drawLine(0, i * height, getWidth(), i * height, light);
            canvas.drawLine(0, i * height + 1, getWidth(), i * height + 1, hilite);
            canvas.drawLine(i * width, 0, i * width, getHeight(), light);
            canvas.drawLine(i * width + 1, 0, i * width + 1, getHeight(), hilite);
        }
        //Draw the major grid lines
        for (int i = 0; i < 9; i++)
        {
            if (i % 3 != 0)
            {
                continue;
            }

            canvas.drawLine(0, i * height, getWidth(), i * height, dark);
            canvas.drawLine(0, i * height + 1, getWidth(), i * height + 1, hilite);
            canvas.drawLine(i * width, 0, i * width, getHeight(), dark);
            canvas.drawLine(i * width + 1, 0, i * width + 1, getHeight(), hilite);
        }
        //Draw the numbers...
        //Define the color and style for numbers
        Paint foreground = new Paint(Paint.ANTI_ALIAS_FLAG);
        foreground.setColor(getResources().getColor(R.color.puzzle_foreground));
        foreground.setStyle(Paint.Style.FILL);
        foreground.setTextSize(height * 0.75f);
        foreground.setTextScaleX(width / height);
        foreground.setTextAlign(Paint.Align.CENTER);
        //Draw the number in the center of the tile
        Paint.FontMetrics fm = foreground.getFontMetrics();
        //Centering in X: use alignment (and X at midpoint)
        float x = width / 2;
        //Centering in Y: measure ascent/descent first
        float y = height / 2 - (fm.ascent + fm.descent) / 2;
        for (int i = 0; i < 9; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                if (this.game.getTileString(i, j) != null){
                    canvas.drawText(this.game.getTileString(i, j), i * width + x, j * height + y, foreground);
                }

            }
        }

        //Draw the hints...
        if (Prefs.getHints(getContext()))
        {
            //Pick a hint color based on #moves left
            Paint hint = new Paint();
            int c[] = {
                    getResources().getColor(R.color.puzzle_hint_0),
                    getResources().getColor(R.color.puzzle_hint_1),
                    getResources().getColor(R.color.puzzle_hint_2),
            };
//            Rect r = new Rect();
//            for (int i = 0; i < 9; i++)
//            {
//                for (int j = 0; j < 9; j++)
//                {
//                    if (this.game.getTileString(i, j) != null){
//                        int movesLeft = 9 - game.getUsedTiles(i, j).length;
//
//                        if (movesLeft < c.length)
//                        {
//                            getRect(i, j, r);
//                            hint.setColor(c[movesLeft]);
//                            canvas.drawRect(r, hint);
//                        }
//                    }
////                    int movesLeft = 9 - game.getUsedTiles(i, j).length;
////                    if (movesLeft < c.length)
////                    {
////                        getRect(i, j, r);
////                        hint.setColor(c[movesLeft]);
////                        canvas.drawRect(r, hint);
////                    }
//                }
//            }
        }

        //Draw the selection...

        Paint selected = new Paint();
        selected.setColor(getResources().getColor(R.color.puzzle_selected));
        canvas.drawRect(selRect, selected);

        //Keep track of if a full word from the dictionary has been drawn on the board.
        wordArray = new char[40];
        for (int i = 0; i < 9; i++){
            for (int j = 0; j < 9; j++){
                if (this.game.getTileString(i, j) != null){
                    wordList.clear();
                    wordList.add(this.game.getTileString(i, j));
                    int k = 1;
                    while (this.game.getTileString(i + k, j) != null && (j * 9 + (i +k)) < 81){
                        Log.d(TAG, "k is: " + k + ", [y * 9 + (i + k)] is " + (j * 9 + (i + k)));
                        Log.d(TAG, "game.getTileString gives back the string: " + this.game.getTileString(i + k, j));
                        wordList.add(this.game.getTileString(i + k, j));

                        s = "";
                        for (String character : wordList){
                            Log.d(TAG, "wordList characters: " + character + "\n");
                            s += character;
                        }
                        Log.d(TAG, "String s is: " + s);

                        if (s.length() > 0){
                                startingLetter = s.substring(0, 1);
                                Log.d(TAG, "STARTING LETTER IS " + startingLetter);
                            Log.d(TAG, "After setting startingLetter, s is still " + s);
                        }

                        game.fileRead(startingLetter);

                        //right after text is changed
                        if (s.length() > 2)
                        {
                            if (game.containsKey(s))
                            {
                                game.calculatePoints(s);
//                                    if (mp != null)
//                                    {
//                                        mp.release();
//                                    }
//                                    mTextView.append(words.get(s) + "\n");
//                                    playChime();
                                //Do something to the word on the board.
                                Log.d(TAG, "IT'S WORKING BY GOD IT'S WORKING");
                            }

                        }

                        k++;
                    }
                }

            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG, "onKeyDown: keycode=" + keyCode + ", event=" + event);
        return super.onKeyDown(keyCode, event);

//        switch(keyCode)
//        {
//            case KeyEvent.KEYCODE_DPAD_UP:
//                select(selX, selY - 1);
//                break;
//            case KeyEvent.KEYCODE_DPAD_DOWN:
//                select(selX, selY + 1);
//                break;
//            case KeyEvent.KEYCODE_DPAD_LEFT:
//                select(selX - 1, selY);
//                break;
//            case KeyEvent.KEYCODE_DPAD_RIGHT:
//                select(selX + 1, selY);
//                break;
//            case KeyEvent.KEYCODE_0:
//            case KeyEvent.KEYCODE_SPACE:
//                setSelectedTile(0);
//                break;
//            case KeyEvent.KEYCODE_1:
//                setSelectedTile(1);
//                break;
//            case KeyEvent.KEYCODE_2:
//                setSelectedTile(2);
//                break;
//            case KeyEvent.KEYCODE_3:
//                setSelectedTile(3);
//                break;
//            case KeyEvent.KEYCODE_4:
//                setSelectedTile(4);
//                break;
//            case KeyEvent.KEYCODE_5:
//                setSelectedTile(5);
//                break;
//            case KeyEvent.KEYCODE_6:
//                setSelectedTile(6);
//                break;
//            case KeyEvent.KEYCODE_7:
//                setSelectedTile(7);
//                break;
//            case KeyEvent.KEYCODE_8:
//                setSelectedTile(8);
//                break;
//            case KeyEvent.KEYCODE_9:
//                setSelectedTile(9);
//                break;
//            case KeyEvent.KEYCODE_ENTER:
//            case KeyEvent.KEYCODE_DPAD_CENTER:
//                game.showKeypadOrError(selX, selY);
//                break;
//            default:
//                return super.onKeyDown(keyCode, event);
//        }

//        return true;

    }

    private void select(int x, int y)
    {
        invalidate(selRect);
        selX = Math.min(Math.max(x, 0), 8);
        selY = Math.min(Math.max(y, 0), 8);
        getRect(selX, selY, selRect);
        invalidate(selRect);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_DOWN)
        {
            return super.onTouchEvent(event);
        }

        select((int)(event.getX() / width),
                (int)(event.getY() / height));
        game.showKeypadOrError(selX, selY);
        Log.d(TAG, "onTouchEvent: x " + selX + ", y " + selY);

        return true;
    }

    public void setSelectedTile(String tile)
    {
        if (game.setTileIfValid(selX, selY, tile))
        {
            invalidate(); //may change hints
        }
        else
        {
            //Number is not valid for this tile
            Log.d(TAG, "setSelectedTile: invalid: " + tile);
            startAnimation(AnimationUtils.loadAnimation(game, R.anim.shake));
        }
    }


    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable p = super.onSaveInstanceState();
        Log.d(TAG, "onSaveInstanceState");
        Bundle bundle = new Bundle();
        bundle.putInt(SELX, selX);
        bundle.putInt(SELY, selY);
        bundle.putParcelable(VIEW_STATE, p);

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Log.d(TAG, "onRestoreInstanceState");
        Bundle bundle = (Bundle) state;
        select(bundle.getInt(SELX), bundle.getInt(SELY));

        super.onRestoreInstanceState(bundle.getParcelable(VIEW_STATE));
        return;
    }


//    private boolean readFile(String letter) {
//
//        Log.d(TAG, "We're inside readFile");
//
//        String file = letter + ".txt";
//
//        try {
//            AssetManager am = game.getAssets();
//
//            InputStream inputStream = am.open(file);
//
//            if (inputStream != null)
//            {
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//                String receiveString = "";
//
//                while ((receiveString = bufferedReader.readLine()) != null )
//                {
//                    if (receiveString.length() >= 3)
//                    {
//
//                        words.put(receiveString, receiveString);
//                    }
//
//                }
//
//                inputStream.close();
//
//            }
//
//            Log.d(TAG, "the words hash map is working: first element in the hashmap is : " + words.get("bin"));
//        }
//        catch (FileNotFoundException e)
//        {
//            //
//        }
//        catch (IOException e)
//        {
//            //
//        }
//
//        return true;
//    }


}
