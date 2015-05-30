package myapp.com.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Image;
import myapp.com.spotifystreamer.models.ArtistResult;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private String LOG_TAG = MainActivityFragment.class.getSimpleName();

    ArrayAdapter<ArtistResult> mAdapter;
    ArtistResult art_reslt;
    List<ArtistResult> arrayOfSearch;

    public MainActivityFragment() {
    }

    @InjectView(R.id.listview_search_result)
    protected ListView _listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.inject(this, rootView);


        _listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                Log.d(LOG_TAG, "Clicked");
                ArtistResult artist_data = mAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DisplayTop10TrackActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, artist_data.spotifyId);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        serachArtist();
//      mAdapter.notifyDataSetChanged();
    }

    private Handler handler = new Handler();

    private void serachArtist() {

        SpotifyApi api = new SpotifyApi();
        // Most (but not all) of the Spotify Web API endpoints require authorisation.
        // If you know you'll only use the ones that don't require authorisation you can skip this step
        //api.setAccessToken("2817a9dfb4774310a5dfb0ba36d6aa48");

        SpotifyService spotify = api.getService();

        spotify.searchArtists("Beyonce", new Callback<ArtistsPager>() {
            @Override
            public void success(ArtistsPager artistsPager, Response response) {

                List<Artist> items = artistsPager.artists.items;
                Artist obj = null;
//                Log.d("Album success", items.toString());
                String name = null;
                String img_url = null;
                String spotify_id = null;

                arrayOfSearch = new ArrayList<ArtistResult>();

                for (int i = 0; i < items.size(); i++) {

                    obj = items.get(i);
                    name = obj.name;
                    spotify_id = obj.id;
                    Log.d(LOG_TAG, name + " - id :" + spotify_id);

                    for (Image imtemp : obj.images) {
                        if (imtemp.width > 75 ) {
//                            Log.d(LOG_TAG, imtemp.url.toString());
                            img_url = imtemp.url.toString();
                        }
                    }
                    art_reslt = new ArtistResult(name, spotify_id, img_url);
                    arrayOfSearch.add(art_reslt);

                }

                new Thread( new Runnable()
                {
                    @Override
                    public void run()
                    {
                        // Do something
                        mAdapter = new SearchArrayAdapter(getActivity(),
                                R.layout.list_item_search_result, arrayOfSearch);
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
                Toast.makeText(getActivity(),
                        "Error searching...",
                        Toast.LENGTH_SHORT).show();

            }
        });


    }
}
