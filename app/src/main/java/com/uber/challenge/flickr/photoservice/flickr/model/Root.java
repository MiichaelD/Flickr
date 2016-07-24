
package com.uber.challenge.flickr.photoservice.flickr.model;

import java.util.HashMap;
import java.util.Map;

public class Root {
    //https://api.flickr.com/services/rest/?method=flickr.photos.search
    // &api_key=3e7cc266ae2b0e0d78e279ce8e361736&format=json&nojsoncallback=1&text=kittens

    private Photos photos;
    private String stat;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The photos
     */
    public Photos getPhotos() { return photos; }

    /**
     * 
     * @param photos
     *     The photos
     */
    public void setPhotos(Photos photos) {
        this.photos = photos;
    }

    /**
     * 
     * @return
     *     The stat
     */
    public String getStat() {
        return stat;
    }

    /**
     * 
     * @param stat
     *     The stat
     */
    public void setStat(String stat) {
        this.stat = stat;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
