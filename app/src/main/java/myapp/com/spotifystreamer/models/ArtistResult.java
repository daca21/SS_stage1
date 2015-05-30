package myapp.com.spotifystreamer.models;

/**
 * Created  on 5/28/15.
 */
public class ArtistResult {


    public String name;
    public String spotifyId;
    public String thumbnail;

    public ArtistResult(String name, String spotifyId, String thumbnail) {
        this.name = name;
        this.spotifyId = spotifyId;
        this.thumbnail = thumbnail;
    }
}
