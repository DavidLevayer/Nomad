package android.nomadproject.com.nomad.mapfragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.nomadproject.com.nomad.R;
import android.nomadproject.com.nomad.database.CustomMarker;
import android.nomadproject.com.nomad.database.MarkerDataSource;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.devsmart.android.ui.HorizontalListView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.InputStream;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import se.walkercrou.places.GooglePlaces;
import se.walkercrou.places.Photo;
import se.walkercrou.places.Place;

/**
 * Created by David Levayer on 20/03/15.
 */
public class MapsFragment extends Fragment implements GoogleMap.OnInfoWindowClickListener {

    private static int MAPS_LOCATION_UPDATE = 20000;

    private SupportMapFragment mMapFragment;
    private GoogleMap mMap;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private Location mLocation;

    private HorizontalListView mHorizontalListView;
    private ListitemAdapter mListitemAdapter;

    private MarkerDataSource mDataSource;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_map_main, container, false);
        /*
        Button refresh = (Button) v.findViewById(R.id.map_button);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDataSource != null){
                    mDataSource.deleteAllMarkers();
                    loadMarkers();
                }
            }
        });
        */

        mHorizontalListView = (HorizontalListView)v.findViewById(R.id.hlistview);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Chargement d'un sous-fragment contenant la map
        FragmentManager fm = getChildFragmentManager();
        mMapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map_container);
        if (mMapFragment == null) {
            // On créé une instance de MapFragment (il ne faut pas passer par le constructeur !)
            mMapFragment = SupportMapFragment.newInstance();
            // Chargement effectif du sous-fragment
            fm.beginTransaction().replace(R.id.map_container, mMapFragment).commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMap == null) {
            // Au besoin (pas déjà fait), on recharge la map
            mMap = mMapFragment.getMap();
            setUpMap();
        }
        if(mDataSource != null)
            mDataSource.open();
    }

    @Override
    public void onPause() {
        if(mDataSource != null)
            mDataSource.close();
        super.onPause();
    }

    private void setUpMap() {

        // Trigger les clics sur les descriptions des markers
        mMap.setOnInfoWindowClickListener(this);

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                View v = getActivity().getLayoutInflater().inflate(
                        R.layout.fragment_map_info_marker,
                        null);

                TextView info_title = (TextView) v.findViewById(R.id.info_title);
                info_title.setText(marker.getTitle());

                TextView info = (TextView) v.findViewById(R.id.info_content);
                info.setText(marker.getSnippet());

                ImageView image = (ImageView) v.findViewById(R.id.info_image);
                image.setImageResource(R.drawable.demo_image);

                return v;
            }
        });

        mMap.setMyLocationEnabled(true);

        // Getting LocationManager object from System Service LOCATION_SERVICE
        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Getting the name of the best provider
        String provider = mLocationManager.getBestProvider(criteria, true);

        // Getting Current Location
        mLocation = mLocationManager.getLastKnownLocation(provider);

        mLocationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                if(location!=null) {
                    mLocation = location;
                }
            }
            public void onProviderDisabled(String provider) { }
            public void onProviderEnabled(String provider) { }
            public void onStatusChanged(String provider, int status, Bundle extras) { }
        };

        if(mLocation !=null)
        {
            CameraUpdate center= CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mLocation.getLatitude(), mLocation.getLongitude()),
                    15);

            mMap.moveCamera(center);
        }

        loadMarkers();

        mLocationManager.requestLocationUpdates(
                provider,
                MAPS_LOCATION_UPDATE,
                0,
                mLocationListener);


        // Chargement de Places
        try {
            Listitem[] list = new PlaceTask().execute().get();
            mListitemAdapter = new ListitemAdapter(getActivity(), R.layout.fragment_map_listitem, list);
            mHorizontalListView.setAdapter(mListitemAdapter);

            for(Listitem item : list)
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(item.getLat(), item.getLon()))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                        .title(item.getTitle()));

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

        Bundle b = new Bundle();
        b.putString(MapsDialogFragment.TITLE,marker.getTitle());
        b.putString(MapsDialogFragment.INFO,marker.getSnippet());

        FragmentManager fm = getActivity().getSupportFragmentManager();
        MapsDialogFragment mDialogFragment = new MapsDialogFragment();
        mDialogFragment.setArguments(b);
        mDialogFragment.show(fm, "maps_dialog_fragment");
    }

    private void loadMarkers(){

        if(mDataSource == null){
            mDataSource = new MarkerDataSource(getActivity());

            mDataSource.open();
            List<CustomMarker> values = mDataSource.getAllCustomMarkers();

            if(values.size()==0) {
                loadSampleValues(mDataSource);
                values = mDataSource.getAllCustomMarkers();
            }

            for(CustomMarker marker: values){
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(marker.getLat(), marker.getLon()))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                        .title(marker.getName())
                        .snippet(marker.getInformation()));
            }
            mDataSource.close();
        }

    }

    private void loadSampleValues(MarkerDataSource database){

        database.createCustomMarker(new CustomMarker(
                "Cafe Jeunesse",
                "Description du Cafe Jeunesse",
                48.426633f,
                -71.069603f));

        database.createCustomMarker(new CustomMarker(
                "Service de travail",
                "Description du Service de travail",
                48.424861f,
                -71.047181f));

        database.createCustomMarker(new CustomMarker(
                "Loge M'entraide",
                "Description de Loge M'entraide",
                48.427896f,
                -71.067512f));

        database.createCustomMarker(new CustomMarker(
                "Lastuse du Saguenay",
                "Description du Lastuse",
                48.426032f,
                -71.055818f));

        database.createCustomMarker(new CustomMarker(
                "Aide juridique",
                "Description de l'aide juridique",
                48.427702f,
                -71.060376f));
    }

    private class PlaceTask extends AsyncTask<Void,Void,Listitem[]> {

        @Override
        protected Listitem[] doInBackground(Void... params) {

            //String key = getResources().getString(R.string.google_map_key);
            GooglePlaces client = new GooglePlaces(getResources().getString(R.string.google_places_api));

            List<Place> places = client.getNearbyPlaces(
                    mLocation.getLatitude(),
                    mLocation.getLongitude(),
                    30000,
                    10);

            //List<Place> places = client.getPlacesByQuery("Empire State Building", 10);

            Listitem[] list = new Listitem[places.size()];
            int i = 0;
            for(Place p: places) {
                Log.d("PLACES", p.getName());
                String title = p.getName();
                List<Photo> photos = p.getPhotos();
                Bitmap image = null;
                if(photos.size()>0) {
                    Photo photo = photos.get(new Random().nextInt(photos.size()));
                    InputStream imageStream = photo.download(200, 230).getInputStream();
                    image = BitmapFactory.decodeStream(imageStream);
                }
                list[i] = new Listitem(title, image, p.getLatitude(), p.getLongitude());
                i++;
            }

            //Listitem[] list = new Listitem[5];
            //for(int i=0; i<5; i++)
            //    list[i] = new Listitem("test"+i,null);
            return list;
        }
    }
}
