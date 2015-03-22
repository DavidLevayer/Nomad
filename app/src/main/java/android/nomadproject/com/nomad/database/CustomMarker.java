package android.nomadproject.com.nomad.database;

/**
 * Created by David Levayer on 21/03/15.
 */
public class CustomMarker {

    private long id;
    private String name, information;
    private float lat, lon;

    public CustomMarker() { }

    public CustomMarker(String name, String information, float lat, float lon){
        this.name = name;
        this.information = information;
        this.lat = lat;
        this.lon = lon;
    }

    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }
}
