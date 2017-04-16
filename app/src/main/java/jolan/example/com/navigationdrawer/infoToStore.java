package jolan.example.com.navigationdrawer;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Jolan on 09-Mar-17.
 */

public class infoToStore {
    String cat;
    String url;
    String key;
    String lat;
    String lng;
    String desc;
    String name;
    String pushID;

    public infoToStore(String cat, String url, String key, String lat, String lng, String desc,
                       String name, String pushID) {
        this.cat = cat;
        this.url = url;
        this.key = key;
        this.lat = lat;
        this.lng = lng;
        this.desc = desc;
        this.name = name;
        this.pushID = pushID;
    }

    public String getCat() {
        return this.cat;
    }

    public String getUrl() {
        return this.url;
    }

    public String getKey() {
        return this.key;
    }

    public String getLat() {
        return this.lat;
    }

    public String getLng() {
        return this.lng;
    }

    public String getDesc() {
        return this.desc;
    }

    public String getName() {
        return this.name;
    }

    public String getPushID () {
        return this.pushID;
    }
}
