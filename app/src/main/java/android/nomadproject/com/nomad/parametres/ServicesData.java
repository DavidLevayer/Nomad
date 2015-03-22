package android.nomadproject.com.nomad.parametres;

import android.app.Service;
import android.content.Intent;
import android.nomadproject.com.nomad.database.CustomMarker;
import android.nomadproject.com.nomad.database.MarkerDataSource;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.List;

import 	android.os.Handler;
import 	android.os.Message;
import android.util.Log;
import 	android.widget.Toast;

/**
 * Created by corentin on 22/03/2015.
 */
public class ServicesData extends Service{

    private Thread t = null;
    private boolean running;
    private long lastTimeUpdate;
    public  long durationTimeLapse;

    /* DATABASE */
    private MarkerDataSource mDataSource;

    /* TESTS */

    @Override
    public void onCreate() {
        super.onCreate();

        mDataSource = null;

        lastTimeUpdate = 0;

        //durationTimeLapse = 3600000; //1H
        durationTimeLapse = 30000; //1H


        running = false;

        Log.d("SERVICE","Creation service");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("SERVICE","Debut thread");
        if(!running){
            running=true;
            t = new Thread(new Runnable() {
                public void run() {
                    lookForData();
                    Log.d("SERVICE","fin run thread");
                }
            });
            t.start();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void lookForData() {

        while (running) {

            long currentTime= System.currentTimeMillis();
            // Nous avons atteind la durée de X minutes pour rafraichir la base
            if(currentTime-durationTimeLapse >= lastTimeUpdate) {
                Log.d("SERVICE","TELECHARGEMENT DATA");
                //Creation de la base SQLite
                /*mDataSource = new MarkerDataSource(this);
                mDataSource.open();*/
                List<CustomMarker> listMarker = new ArrayList<CustomMarker>();
                // Récupération données Facebook
                lookForFacebookData(listMarker);
                // Récupération données Maps
                lookForMapData(listMarker);
                // Remplissage de la basse de données
                fillDatabaseWithMarkes(listMarker);
                //Destruction de la base locale
               // mDataSource.close();
                lastTimeUpdate = System.currentTimeMillis();
            }

        }



    }

    private boolean lookForFacebookData(List<CustomMarker> v){
        return true;
    }

    private boolean lookForMapData(List<CustomMarker> v){
        return true;
    }

    private boolean fillDatabaseWithMarkes(List<CustomMarker> v){

        return true;
    }
    /*------------------------------------------------------------------------*/
    @Override
    public void onDestroy() {
        Log.d("SERVICE","Destroy");
        running = false;
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
