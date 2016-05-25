package baajna.scroll.owner.mobioapp.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ProgressBar;

import baajna.scroll.owner.mobioapp.adapter.SongAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


/**
 * Created by Luna on 2015-05-02.
 */

public class RetrieveSong extends AsyncTask<Void, Void, Void> {

    //This URL should be changed based on the button events(New Releases/Top Charts/Featured Playlist/Albums/Artists)
    //The current sourceUrl is having a test song list for our demo
    final String sourceUrl = "http://s3.amazonaws.com/sparklesongs/songs/songlist.txt";
    private Context mContext;

    private AbsListView songListView;
    private SongAdapter songAdt;
    private ProgressBar spinner;

    public RetrieveSong(Context context) {
        mContext = context;
    }

    public RetrieveSong() {

    }

    @Override
    protected Void doInBackground(Void... params) {

        ArrayList<String> songUrlList = new ArrayList<String>();

        try {
            // Create a URL for the desired page
            URL url = new URL(sourceUrl);

            // Read all the text returned by the server
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String str;
            while ((str = in.readLine()) != null) {
                songUrlList.add(str);
            }
            in.close();
        } catch (MalformedURLException e) {
            Log.d("MalformedURLException: ", e.getMessage());
        } catch (IOException e) {
            Log.d("IOException: ", e.getMessage());
        }
//
//        if ((songUrlList.size() > 0)) {
//            FFmpegMediaMetadataRetriever fmmr = new FFmpegMediaMetadataRetriever();
//
//            ArrayList<Song> songList = new ArrayList<Song>();
//
//            for (int i = 0; i < songUrlList.size() ; i++) {
//
//                try {
//
//                    fmmr.setDataSource(songUrlList.get(i));
//
//                    String thisTitle = fmmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_TITLE);
//                    String thisArtist = fmmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_ARTIST);
//                    String thisAlbum = fmmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_ALBUM);
//                    String thisUri = songUrlList.get(i);
//
//                    byte[] data = fmmr.getEmbeddedPicture();
//                    if (data != null) {
//                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
//                        songList.add(new Song(true, i, 0, 0, thisTitle, thisArtist, thisAlbum, thisUri, null, bitmap));
//                    } else {
//                        songList.add(new Song(true, i, 0, 0, thisTitle, thisArtist, thisAlbum, thisUri, null, null));
//                    }
//                } catch (Exception e) {
//                    Log.d("Exception: ", e.getMessage());
//                }
//
//            }
//            Globals.playlist = songList;
//
//        }
        return null;

    }

//    @Override
//    protected void onPostExecute(Void result) {
//        songAdt = new SongAdapter(mContext, Globals.playlist);
//        songListView.setAdapter(songAdt);
//        spinner.setVisibility(View.GONE);
//        super.onPostExecute(result);
//    }
}
