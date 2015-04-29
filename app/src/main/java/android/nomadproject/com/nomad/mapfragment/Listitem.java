package android.nomadproject.com.nomad.mapfragment;

import android.graphics.Bitmap;

/**
 * Created by David Levayer on 27/04/15.
 */
public class Listitem {

    private String title;
    private Bitmap image;
    private double lat, lon;

    public Listitem(String title, Bitmap image, double lat, double lon) {
        this.title = title;
        this.image = image;
        this.lat = lat;
        this.lon = lon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
}
