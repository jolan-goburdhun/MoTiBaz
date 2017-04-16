package jolan.example.com.navigationdrawer;

import java.util.List;

/**
 * Created by Jolan on 17-Feb-17.
 */

public class adService1 {
    public String serviceName;
    public String serviceDesc;
    public String downloadUrl;
    public String latitude;
    public String longitude;
    public String cat;
    public String key;

    public adService1(String serviceName, String serviceDesc, String cat, String downloadUrl,
                      String latitude, String longitude, String key) {
        this.serviceName = serviceName;
        this.serviceDesc = serviceDesc;
        this.cat = cat;
        this.downloadUrl = downloadUrl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.key = key;
    }
}
