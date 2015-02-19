package localbrowser.example.org.localbrowser;

import android.app.Activity;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class LocalBrowser extends Activity {
    private static final String TAG = "LocalBrowser";
    private final Handler handler = new Handler();
    private WebView webView;
    private TextView textView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //Find the Android controls on the screen
        webView = (WebView)findViewById(R.id.web_view);
        textView = (TextView)findViewById(R.id.text_view);
        button = (Button)findViewById(R.id.button);
        //Rest of onCreate follows...

        //Turn on JavaScript in the embedded browser
        webView.getSettings().setJavaScriptEnabled(true);

        //Expose a Java object to JavaScript in the browser
        webView.addJavascriptInterface(new AndroidBridge(), "android");

        //Set up a function to be called when JavaScript tries
        //to open an alert window
        webView.setWebChromeClient(new WebChromeClient()
        {
            @Override
            public boolean onJsAlert(final WebView view, final String url, final String message,
                                 JsResult result)
            {
                Log.d(TAG, "onJsAlert(" + view + ", " + url + ", " + message + ", " + result + ")");
                Toast.makeText(LocalBrowser.this, message, Toast.LENGTH_LONG).show();
                result.confirm();

                return true; //I handled it
            }


        });

        //Load the web page from a local asset
        webView.loadUrl("file:///android_asset/index.html");

        //This function will be called when the user presses the
        //button on the Android side
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick(" + v + ")");
                webView.loadUrl("javascript:callJS('Hello from Android')");
            }
        });
    }

    //Object exposed to JavaScript
    private class AndroidBridge{
        @JavascriptInterface
        public void callAndroid(final String arg) //must be final
        {
            handler.post(new Runnable()
            {
                @Override
                public void run() {
                    Log.d(TAG, "callAndroid(" + arg + ")");
                    textView.setText(arg);
                }
            });
        }
    }



}
