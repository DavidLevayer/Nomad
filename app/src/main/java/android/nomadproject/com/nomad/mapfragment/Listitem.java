package android.nomadproject.com.nomad.mapfragment;

import android.graphics.Bitmap;

/**
 * Created by David Levayer on 27/04/15.
 */
public class Listitem {

    private String title;
    private Bitmap image;

    public Listitem(String title, Bitmap image) {
        this.title = title;
        this.image = image;
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
}
