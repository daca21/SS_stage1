package myapp.com.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import myapp.com.spotifystreamer.models.TrackResult;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A placeholder fragment containing a simple view.
 */
public class DisplayTop10TrackActivityFragment extends Fragment {

    private String LOG_TAG = DisplayTop10TrackActivity.class.getSimpleName();
    private String data;
    List<TrackResult> arrayOfTracks;
    TrackResult _trackResult;
    TopTrackAdapter mAdapter;


    @InjectView(android.R.id.list)
    protected ListView _listView;

    public DisplayTop10TrackActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_display_top10_track, container, false);
        ButterKnife.inject(this, rootView);
        // The detail Activity called via intent.  Inspect the intent for forecast data.
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            data = intent.getStringExtra(Intent.EXTRA_TEXT);
            ((TextView) rootView.findViewById(R.id.detail_text))
                    .setText(data);
        }
//        serachTop10Track();
        return rootView;
    }
    private Handler handler = new Handler();
    @Override
    public void onResume() {
        super.onResume();
        serachTop10Track();
    }

    private void serachTop10Track() {

        SpotifyApi api = new SpotifyApi();
        SpotifyService spotify = api.getService();
        Map<String, Object> mTrackoption = new HashMap<>();
        mTrackoption.put("country", "US");


        spotify.getArtistTopTrack(data, mTrackoption, new Callback<Tracks>() {
            @Override
            public void success(Tracks tracks, Response response) {
                Log.d(LOG_TAG, "success" + tracks.tracks.toString());

                List<Track> tracksList = tracks.tracks;

                Track obj = null;
                String track_name = null;
                String album_name = null;
                String thumdnail_Large = null;
                String thumdnail_Small = null;
                String previewURL = null;

                arrayOfTracks = new ArrayList<TrackResult>();

                for (int i = 0; i < tracksList.size(); i++) {

                    obj = tracksList.get(i);
                    track_name = obj.name;
                    album_name = obj.album.name;
                    previewURL = obj.preview_url;

                    for (Image imtemp : obj.album.images) {
                        if (imtemp.height > 400 ) {
                            thumdnail_Large = imtemp.url.toString();
                        }
                        else if (imtemp.height > 200)
                            thumdnail_Small = imtemp.url.toString();
                    }
                    _trackResult = new TrackResult(track_name, album_name, thumdnail_Large, thumdnail_Small, previewURL);
                    arrayOfTracks.add(_trackResult);

                }

                new Thread( new Runnable()
                {
                    @Override
                    public void run()
                    {
                        // Do something
                        mAdapter = new TopTrackAdapter(getActivity(),
                                android.R.id.list, arrayOfTracks);
                        handler.post( new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                _listView.setAdapter(mAdapter);
                            }
                        } );
                    }
                } ).start();




            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(LOG_TAG, error.getMessage().toString());
//                runOnUiThread(() -> {
//                    setLoading(false);
//                    Toast.makeText(getActivity(),
//                            getString(R.string.error_failed),
//                            Toast.LENGTH_LONG).show();
//                });

            }
        });

    }
}
