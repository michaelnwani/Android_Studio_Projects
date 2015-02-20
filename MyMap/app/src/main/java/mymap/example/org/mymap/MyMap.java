package mymap.example.org.mymap;

import android.app.Activity;
import android.os.Bundle;
import com.google.android.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;


public class MyMap extends Activity {
    private MapView map;
    private MapController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initMapView();
        initMyLocation();
    }

    @Override
    protected boolean isRouteDisplayed(){
        //Required by MapActivity
        return false;
    }

    private void initMapView()
    {
        map = (MapView)findViewById(R.id.map);
        controller = map.getController();
        map.setSatellite(true);
    }

    private void initMyLocation(){
        final MyLocationOverlay overlay = new MyLocationOverlay(this, map);
        overlay.enableMyLocation();
        //overlay.enableCompass(); //does not work in emulator
        overlay.runOnFirstFix(new Runnable(){
           public void run(){
               //Zoom in to current location
               controller.setZoom(8);
               controller.animateTo(overlay.getMyLocation());
           }
        });
        map.getOverlays().add(overlay);
    }

}
