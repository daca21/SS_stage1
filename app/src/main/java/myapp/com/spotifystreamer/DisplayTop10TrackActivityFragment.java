package myapp.com.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A placeholder fragment containing a simple view.
 */
public class DisplayTop10TrackActivityFragment extends Fragment {

    private String LOG_TAG = DisplayTop10TrackActivity.class.getSimpleName();
    private String data;

    public DisplayTop10TrackActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_display_top10_track, container, false);
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
