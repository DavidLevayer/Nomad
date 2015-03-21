package android.nomadproject.com.nomad.mapfragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.nomadproject.com.nomad.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by David Levayer on 20/03/15.
 */
public class MapsFragment extends Fragment implements GoogleMap.OnInfoWindowClickListener {

    protected View mView;
    protected Context mContext;

    private static int MAPS_LOCATION_UPDATE = 20000;

    private MapFragment mMapFragment;
    private GoogleMap mMap;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private Location location;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map_main, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Chargement d'un sous-fragment contenant la map
        FragmentManager fm = getChildFragmentManager();
        mMapFragment = (MapFragment) fm.findFragmentById(R.id.map_container);
        if (mMapFragment == null) {
            // On créé une instance de MapFragment (il ne faut pas passer par le constructeur !)
            mMapFragment = MapFragment.newInstance();
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

                ImageView image = (ImageView)v.findViewById(R.id.info_image);
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
        location = mLocationManager.getLastKnownLocation(provider);

        mLocationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                if(location!=null) {
                    CameraUpdate center = CameraUpdateFactory.newLatLng(
                            new LatLng(location.getLatitude(),
                                    location.getLongitude()));
                    mMap.moveCamera(center);
                }
            }
            public void onProviderDisabled(String provider) { }
            public void onProviderEnabled(String provider) { }
            public void onStatusChanged(String provider, int status, Bundle extras) { }
        };

        if(location!=null)
        {
            CameraUpdate center= CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()),
                    10);

            mMap.moveCamera(center);

            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(location.getLatitude(), location.getLongitude()))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    .title("TITLE")
                    .snippet("Text de description blablabla"));
        }

        mLocationManager.requestLocationUpdates(
                provider,
                MAPS_LOCATION_UPDATE,
                0,
                mLocationListener);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(getActivity(),"onInfoWindowClick",Toast.LENGTH_SHORT).show();
    }
}
