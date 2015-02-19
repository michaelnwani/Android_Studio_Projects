package video.example.org.video;

import android.app.Activity;
import android.os.Bundle;

import android.widget.VideoView;


public class Video extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Fill view from resource
        setContentView(R.layout.main);
        VideoView video = (VideoView)findViewById(R.id.video);

        //Load and start the movie
        video.setVideoPath("/sdcard/samplevideo.3gp");
        video.start();
    }


}
