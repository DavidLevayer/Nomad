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
public class MapsFragment extends Fragment
        implements GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {

    protected View mView;
    protected Context mContext;

    private MapFragment fragment;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location location;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map_main, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentManager fm = getChildFragmentManager();
        fragment = (MapFragment) fm.findFragmentById(R.id.map_container);
        if (fragment == null) {
            fragment = MapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map_container, fragment).commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMap == null) {
            mMap = fragment.getMap();
            setUpMap();
        }
    }

    private void setUpMap() {

        // Ajout de la carte comme listener de click sur les markers
        mMap.setOnMarkerClickListener(this);
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
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Getting Current Location
        location = locationManager.getLastKnownLocation(provider);

        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                if(location!=null) {
                    CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
                    mMap.moveCamera(center);
                }
            }
            public void onProviderDisabled(String provider) {
            }
            public void onProviderEnabled(String provider) {
            }
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
        };

        if(location!=null)
        {
            CameraUpdate center= CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 10);
            mMap.moveCamera(center);

            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(location.getLatitude(), location.getLongitude()))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title("TITLE")
                    .snippet("Text de description blablabla"));
        }

        //MARKER TEST
        /*mMap.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .title("CACA")
                .snippet("Population: 4,137,400"));*/

        locationManager.requestLocationUpdates(provider,20000,0,locationListener);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(getActivity(),"Info",Toast.LENGTH_SHORT).show();
    }
}
