package edu.neu.madcourse.michaelnwani;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


/**
 * Created by michaelnwani on 2/25/15.
 */
public class WordFadeActivity extends Activity {
    private final Button letters[] = new Button[21];
    private final ArrayList<String> letterPoints = new ArrayList<String>(26);
    private int points;

//    private int puzzle[] = new int[9 * 9];
    private String puzzle[] = new String[9 * 9];

    private BoardView boardView;
    private static Random rand = new Random();

    public static final String KEY_DIFFICULTY = "org.example.sudoku.difficulty";
    public static final int DIFFICULTY_EASY = 0;
    public static final int DIFFICULTY_MEDIUM = 1;
    public static final int DIFFICULTY_HARD = 2;

    private final String easyPuzzle =
            "360000000004230800000004200" +
                    "070460003820000014500013020" +
                    "001900000007048300000000045";
    private final String mediumPuzzle =
            "650000070000506000014000005" +
                    "007009000002314700000700800" +
                    "500000630000201000030000097";
    private final String hardPuzzle =
            "009000000080605020501078000" +
                    "000000700706040102004000000" +
                    "000720903090301080000000600";

    private final HashMap<String, Boolean> fileRead = new HashMap<String, Boolean>(26);
    private static final String TAG = "TESTING fileRead";

    private static final int FILE_WORD_COUNT = 50000;
    private final HashMap<String, String> words = new HashMap<String, String>(FILE_WORD_COUNT);
    private final ArrayList<String> placedWords = new ArrayList<String>();
    private ActionBar actionBar;
    private static HashMap<Integer, String> letterHashMap = new HashMap<Integer, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          actionBar = getActionBar();

//        actionBar.setTitle("Points: " + points);


        initializeAlphabet();
        fillLetterHashMap();
//        findLetters();
//        assignLettersToRack();
//        calculatePoints();

//        int diff = getIntent().getIntExtra(KEY_DIFFICULTY, DIFFICULTY_EASY);
//        puzzle = getPuzzle(1);

//        calculateUsedTiles();

        fileRead.put("a",false);
        fileRead.put("b",false);
        fileRead.put("c",false);
        fileRead.put("d",false);
        fileRead.put("e",false);
        fileRead.put("f",false);
        fileRead.put("g",false);
        fileRead.put("h",false);
        fileRead.put("i",false);
        fileRead.put("j",false);
        fileRead.put("k",false);
        fileRead.put("l",false);
        fileRead.put("m",false);
        fileRead.put("n",false);
        fileRead.put("o",false);
        fileRead.put("p",false);
        fileRead.put("q",false);
        fileRead.put("r",false);
        fileRead.put("s",false);
        fileRead.put("t",false);
        fileRead.put("u",false);
        fileRead.put("v",false);
        fileRead.put("w",false);
        fileRead.put("x",false);
        fileRead.put("y",false);
        fileRead.put("z",false);

        boardView = new BoardView(this);
        setContentView(boardView);
        boardView.requestFocus();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_wordfade, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Dialog dump = new DumpPad(this, boardView);
        switch (item.getItemId()){
            case R.id.split:

//                split.show();
                return true;
            case R.id.dump:
//                Dialog v = new LetterPad(this, boardView);
//                v.show();
                dump.show();
                //handle soon
                return true;

        }
        return false;
    }

    private void initializeAlphabet()
    {
        letterPoints.add("a");
        letterPoints.add("b");
        letterPoints.add("c");
        letterPoints.add("d");
        letterPoints.add("e");
        letterPoints.add("f");
        letterPoints.add("g");
        letterPoints.add("h");
        letterPoints.add("i");
        letterPoints.add("j");
        letterPoints.add("k");
        letterPoints.add("l");
        letterPoints.add("m");
        letterPoints.add("n");
        letterPoints.add("o");
        letterPoints.add("p");
        letterPoints.add("q");
        letterPoints.add("r");
        letterPoints.add("s");
        letterPoints.add("t");
        letterPoints.add("u");
        letterPoints.add("v");
        letterPoints.add("w");
        letterPoints.add("x");
        letterPoints.add("y");
        letterPoints.add("z");

    }

    public String getLetterFromLetterPoints(int index){
        return letterPoints.get(index);
    }

    private void findLetters()
    {

        letters[0] = (Button) findViewById(R.id.letter_1);
        letters[1] = (Button) findViewById(R.id.letter_2);
        letters[2] = (Button) findViewById(R.id.letter_3);
        letters[3] = (Button) findViewById(R.id.letter_4);
        letters[4] = (Button) findViewById(R.id.letter_5);
        letters[5] = (Button) findViewById(R.id.letter_6);
        letters[6] = (Button) findViewById(R.id.letter_7);
        letters[7] = (Button) findViewById(R.id.letter_8);
        letters[8] = (Button) findViewById(R.id.letter_9);
        letters[9] = (Button) findViewById(R.id.letter_10);
        letters[10] = (Button) findViewById(R.id.letter_11);
        letters[11] = (Button) findViewById(R.id.letter_12);
        letters[12] = (Button) findViewById(R.id.letter_13);
        letters[13] = (Button) findViewById(R.id.letter_14);
        letters[14] = (Button) findViewById(R.id.letter_15);
        letters[15] = (Button) findViewById(R.id.letter_16);
        letters[16] = (Button) findViewById(R.id.letter_17);
        letters[17] = (Button) findViewById(R.id.letter_18);
        letters[18] = (Button) findViewById(R.id.letter_19);
        letters[19] = (Button) findViewById(R.id.letter_20);
        letters[20] = (Button) findViewById(R.id.letter_21);
    }

    private void assignLettersToRack()
    {
        for (int i = 0; i < 5; i++)
        {
            //get a random integer value from 0 to 25
            //letters[i].setText(string from random indexing of dataset)
            int randomNumber = randInt(0, 25);
            letters[i].getText();


        }

    }

    //Awesome helper method.
    //Source: http://stackoverflow.com/questions/363681/generating-random-integers-in-a-range-with-java
    public int randInt(int min, int max)
    {


        //to make it inclusive of the max
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public void calculatePoints(String word)
    {
        if (!placedWords.contains(word)){
            for (int i = 0; i < word.length(); i++){
                switch (word.charAt(i))
                {
                    case 'a':
                        points += 1;
                        break;
                    case 'b':
                        points += 3;
                        break;
                    case 'c':
                        points += 3;
                        break;
                    case 'd':
                        points += 2;
                        break;
                    case 'e':
                        points += 1;
                        break;
                    case 'f':
                        points += 4;
                        break;
                    case 'g':
                        points += 2;
                        break;
                    case 'h':
                        points += 4;
                        break;
                    case 'i':
                        points += 1;
                        break;
                    case 'j':
                        points += 8;
                        break;
                    case 'k':
                        points += 5;
                        break;
                    case 'l':
                        points += 1;
                        break;
                    case 'm':
                        points += 3;
                        break;
                    case 'n':
                        points += 1;
                        break;
                    case 'o':
                        points += 1;
                        break;
                    case 'p':
                        points += 3;
                        break;
                    case 'q':
                        points += 10;
                        break;
                    case 'r':
                        points += 1;
                        break;
                    case 's':
                        points += 1;
                        break;
                    case 't':
                        points += 1;
                        break;
                    case 'u':
                        points += 1;
                        break;
                    case 'v':
                        points += 4;
                        break;
                    case 'w':
                        points += 4;
                        break;
                    case 'x':
                        points += 8;
                        break;
                    case 'y':
                        points += 4;
                        break;
                    case 'z':
                        points += 10;
                        break;
                    default:
                        break;
                }
            }
            actionBar.setTitle("Points: " + points);
            placedWords.add(word);
        }

    }


    protected void showKeypadOrError(int x, int y)
    {
//        int tiles[] = getUsedTiles(x, y);
//        if (tiles.length == 9)
//        {
//            Toast toast = Toast.makeText(this, R.string.no_moves_label, Toast.LENGTH_SHORT);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();
//        }
//        else
//        {

//            Dialog v = new LetterPad(this, tiles, boardView);
            Dialog letterPad = new LetterPad(this, boardView);
            letterPad.show();
//        }
    }

    protected boolean setTileIfValid(int x, int y, String value)
    {
        int tiles[] = getUsedTiles(x, y);
//        if (value != 0)
//        {
//            for (int tile : tiles)
//            {
//                if (tile == value)
//                    return false;
//            }
//        }
        setTile(x, y, value);
//        calculateUsedTiles();
        return true;
    }

    private final int used[][][] = new int[9][9][];

    protected int[] getUsedTiles(int x, int y)
    {
        return used[x][y];
    }

//    private void calculateUsedTiles()
//    {
//        for (int x = 0; x < 9; x++)
//        {
//            for (int y = 0; y < 9; y++)
//            {
//                used[x][y] = calculateUsedTiles(x, y);
//                //Log.d(TAG, "used[" + x + "][" + y + "] = "
//                //+ toPuzzleString(used[x][y]));
//            }
//        }
//    }

//    private int[] calculateUsedTiles(int x, int y)
//    {
//        int c[] = new int[9];
//        //horizontal
//        for (int i = 0; i < 9; i++)
//        {
//            if (i == y)
//                continue;
//            int t = getTile(x, i);
//            if (t != 0)
//            {
//                c[t-1] = t;
//            }
//        }
//
//        //vertical
//        for (int i = 0; i < 9; i++)
//        {
//            if (i == x)
//                continue;
//            int t = getTile(i, y);
//            if (t != 0)
//            {
//                c[t-1] = t;
//            }
//        }
//
//        //same cell block
//        int startx = (x / 3) * 3;
//        int starty = (y / 3) * 3;
//        for (int i = startx; i < startx + 3; i++)
//        {
//            for (int j = starty; j < starty + 3; j++)
//            {
//                if (i == x && j == y)
//                {
//                    continue;
//                }
//                int t = getTile(i, j);
//                if (t != 0)
//                {
//                    c[t - 1] = t;
//                }
//            }
//        }
//
//        //compress
//        int nused = 0;
//        for (int t : c)
//        {
//            if (t != 0)
//            {
//                nused++;
//            }
//        }
//        int c1[] = new int[nused];
//        nused = 0;
//        for (int t : c)
//        {
//            if (t != 0)
//            {
//                c1[nused++] = t;
//            }
//        }
//        return c1;
//    }



    private String getTile(int x, int y)
    {
        return puzzle[y * 9 + x]; //y * 9 tells you what row to go to; + x to get to the column
    }

    private void setTile(int x, int y, String value)
    {
        puzzle[y * 9 + x] = value;
    }

    protected String getTileString(int x, int y)
    {
//        int v = getTile(x, y);
//        if (v == 0) //that means the tile is blank
//            return "";
//        else
//            return String.valueOf(v);

        return puzzle[y * 9 + x];

    }

    private int[] getPuzzle(int diff)
    {
        String puz;
        switch (diff)
        {
//            case DIFFICULTY_CONTINUE:
//                puz = getPreferences(MODE_PRIVATE).getString(PREF_PUZZLE, easyPuzzle);
//                break;
            case DIFFICULTY_HARD:
                puz = hardPuzzle;
                break;
            case DIFFICULTY_MEDIUM:
                puz = mediumPuzzle;
                break;
            case DIFFICULTY_EASY:
            default:
                puz = easyPuzzle;
                break;
        }

        return fromPuzzleString(puz);
    }

    static private String toPuzzleString(int[] puz)
    {
        StringBuilder buf = new StringBuilder();
        for (int element : puz)
        {
            buf.append(element);
        }
        return buf.toString();
    }

    static protected int[] fromPuzzleString(String string)
    {
        int[] puz = new int[string.length()];
        for (int i = 0; i < puz.length; i++)
        {
            //turn a char to its unicode form; String.valueOf() to get opposite
            puz[i] = string.charAt(i) - '0';
        }

        return puz;
    }

    public boolean fileRead(String startingLetter){
        if (fileRead.get(startingLetter) == false){
            Log.d(TAG, "Working correctly: fileRead key value should not be null");
            readFile(startingLetter);
            fileRead.put(startingLetter, true);

            return true;
        }
        else{
            Log.d(TAG, "fileRead key value is somehow null...");
        }

        return false;
    }

    public void fileReadPut(String startingLetter, boolean b){
        fileRead.put(startingLetter, b);
    }

    public boolean readFile(String letter) {

        Log.d(TAG, "We're inside readFile");

        String file = letter + ".txt";

        try {
            AssetManager am = getAssets();

            InputStream inputStream = am.open(file);

            if (inputStream != null)
            {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";

                while ((receiveString = bufferedReader.readLine()) != null )
                {
                    if (receiveString.length() >= 3)
                    {

                        words.put(receiveString, receiveString);
                    }

                }

                inputStream.close();

            }

            Log.d(TAG, "the words hash map is working: first element in the hashmap is : " + words.get("bin"));
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

    public boolean containsKey(String s){

        if (words.containsKey(s)){
            return true;
        }

        return false;
    }

    public void dumpLetter(String s){
        Dialog letterPad = new LetterPad(this, boardView, s);
        letterPad.show();
    }

    public void fillLetterHashMap(){
        Log.d(TAG, "inside fillLetterHashMap(); so this must be working");
        letterHashMap.put(0, "a");
        letterHashMap.put(1, "b");
        letterHashMap.put(2, "c");
        letterHashMap.put(3, "d");
        letterHashMap.put(4, "e");
        letterHashMap.put(5, "f");
        letterHashMap.put(6, "g");
        letterHashMap.put(7, "h");
        letterHashMap.put(8, "i");
        letterHashMap.put(9, "j");
        letterHashMap.put(10, "k");
        letterHashMap.put(11, "l");
        letterHashMap.put(12, "m");
        letterHashMap.put(13, "n");
        letterHashMap.put(14, "o");
        letterHashMap.put(15, "p");
        letterHashMap.put(16, "q");
        letterHashMap.put(17, "r");
        letterHashMap.put(18, "s");
        letterHashMap.put(19, "t");
        letterHashMap.put(20, "u");
        letterHashMap.put(21, "v");
        letterHashMap.put(22, "w");
        letterHashMap.put(23, "x");
        letterHashMap.put(24, "y");
        letterHashMap.put(25, "z");


    }

    public void putLetterHashMap(int index, String value){
        letterHashMap.put(index, value);
    }

    public String getLetterHashMap(int index){
        return letterHashMap.get(index);
    }

//    letters[0] = findViewById(R.id.letter_1);
//    letters[1] = findViewById(R.id.letter_2);
//    letters[2] = findViewById(R.id.letter_3);
//    letters[3] = findViewById(R.id.letter_4);
//    letters[4] = findViewById(R.id.letter_5);
//    letters[5] = findViewById(R.id.letter_6);
//    letters[6] = findViewById(R.id.letter_7);
//    letters[7] = findViewById(R.id.letter_8);
//    letters[8] = findViewById(R.id.letter_9);
//    letters[9] = findViewById(R.id.letter_10);
//    letters[10] = findViewById(R.id.letter_11);
//    letters[11] = findViewById(R.id.letter_12);
//    letters[12] = findViewById(R.id.letter_13);
//    letters[13] = findViewById(R.id.letter_14);
//    letters[14] = findViewById(R.id.letter_15);
//    letters[15] = findViewById(R.id.letter_16);
//    letters[16] = findViewById(R.id.letter_17);
//    letters[17] = findViewById(R.id.letter_18);
//    letters[18] = findViewById(R.id.letter_19);
//    letters[19] = findViewById(R.id.letter_20);
//    letters[20] = findViewById(R.id.letter_21);




}
