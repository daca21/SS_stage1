package myapp.com.spotifystreamer.models;

/**
 * Created  on 5/29/15.
 */
public class TrackResult {

    public String track_name;
    public String album_name;
    public String thumbnail_large; // 600px
    public String thumbnail_small; //200px
    public String preview_url; //use to stream audio

    //setter
    public TrackResult(String name, String album_name, String thumbnail_large,String thumbnail_small, String preview_url ) {
        this.track_name = name;
        this.album_name = album_name;
        this.thumbnail_large = thumbnail_large;
        this.thumbnail_small = thumbnail_small;
        this.preview_url = preview_url;
    }
}

