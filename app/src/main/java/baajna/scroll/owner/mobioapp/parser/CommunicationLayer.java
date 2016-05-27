package baajna.scroll.owner.mobioapp.parser;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import baajna.scroll.owner.mobioapp.asynctask.DownloadAsyncTask;
import baajna.scroll.owner.mobioapp.datamodel.MoAlbum;
import baajna.scroll.owner.mobioapp.datamodel.MoSongReq;
import baajna.scroll.owner.mobioapp.utils.MyApp;
import baajna.scroll.owner.mobioapp.utils.Urls;
import baajna.scroll.owner.mobioapp.datamodel.MoArtist;
import baajna.scroll.owner.mobioapp.datamodel.MoGenres;
import baajna.scroll.owner.mobioapp.datamodel.MoSong;
import baajna.scroll.owner.mobioapp.datamodel.MoUser;
import baajna.scroll.owner.mobioapp.localDatabase.DbManager;
import baajna.scroll.owner.mobioapp.utils.Globals;
import baajna.scroll.owner.mobioapp.utils.SparkleApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class CommunicationLayer {

    static AsyncHttpClient client;
    static RequestParams params;
    private static MoSong info = new MoSong();
    private static Context context;
    private static String fileUrl = "invalid";

    private static int status;

    public static CommunicationLayer getInstance() {
        return new CommunicationLayer();
    }

    public static int getSignUpData(String email_id, String password) throws Exception {
        int signUpData = 0;
/*
        List<NameValuePair> postData = new ArrayList<NameValuePair>(2);
//		postData.add((NameValuePair) new BasicNameValuePair("user_request", func_id));
        postData.add((NameValuePair) new BasicNameValuePair("user_email", email_id));
        postData.add((NameValuePair) new BasicNameValuePair("password", password));*/


        // String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(SparkleApp.getInstance().getBase_url(), postData, true);


        //   signUpData = parseSignUpData(serverResponse);

        return signUpData;
    }

    private static int parseSignUpData(String jData) throws JSONException {

        int singUpdata = 0;
        JSONObject jDataObj = new JSONObject(jData);
        singUpdata = jDataObj.getInt("message");

        if (singUpdata == 1) {
            JSONObject dataObj = jDataObj.getJSONObject("user_data");
            MoUser info = new MoUser();
            info.setId(dataObj.getInt("id"));
            info.setEmail(dataObj.getString("user_email"));
            info.setPassword(dataObj.getString("password"));

            SparkleApp.getInstance().setMoUser(info);
        } else {
            SparkleApp.getInstance().setMoUser(null);
        }

        return singUpdata;
//
//		// DiscoverApp.getInstance().setUserProfilePic(dataObj.getString("pic"));
//		preferenceUtil.setUserPic(dataObj.getString("pic"));
//		// DiscoverApp.getInstance().setUserName(dataObj.getString("name"));
//		preferenceUtil.setUserName(dataObj.getString("name"));
//
//		return singUpdata;
    }

    //
    public static int getLoginData(String email_id, String password) throws Exception {

        int loginUpData = 0;
/*

        List<NameValuePair> postData = new ArrayList<NameValuePair>(2);
        postData.add((NameValuePair) new BasicNameValuePair("user_email", email_id));
        postData.add((NameValuePair) new BasicNameValuePair("password", password));

        String serverResponse = WebServerOperation.sendHTTPPostRequestToServer(SparkleApp.getInstance().getLogin_url(), postData, true);

*/

        //loginUpData = parseLoginData(serverResponse);
        //signUpData = parseSignUpData(serverResponse);
        return loginUpData;
    }

    //
    private static int parseLoginData(String jData)
            throws JSONException {
        int logindata = 0;
        JSONObject jDataObj = new JSONObject(jData);
        logindata = jDataObj.getInt("message");

        if (logindata == 1) {
            JSONObject dataObj = jDataObj.getJSONObject("user_data");
            MoUser info = new MoUser();
            info.setId(dataObj.getInt("id"));
            info.setEmail(dataObj.getString("user_email"));
            info.setPassword(dataObj.getString("password"));

            SparkleApp.getInstance().setMoUser(info);
        } else {
            SparkleApp.getInstance().setMoUser(null);
        }

//		// DiscoverApp.getInstance().setUserProfilePic(dataObj.getString("pic"));
//		preferenceUtil.setUserPic(dataObj.getString("pic"));
//		// DiscoverApp.getInstance().setUserName(dataObj.getString("name"));
//		preferenceUtil.setUserName(dataObj.getString("name"));

        return logindata;
    }


    //Song Request
    public static int getSongRequestData(String id) {


        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("user_id", "");
        params.add("song_id", "");
        client.post(Urls.BASE_URL + "index.php/api/request_song", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                status = parseSongRequestData(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
        return status;
    }

    //
    private static int parseSongRequestData(JSONObject jDataObj) {
        int songreqdata = 0;

        try {
            songreqdata = jDataObj.getInt("message");

            if (songreqdata == 1) {
                JSONObject dataObj = jDataObj.getJSONObject("user_data");
                MoSongReq info = new MoSongReq();
                info.setId(dataObj.getInt("id"));
                info.setSongUrl(dataObj.getString("file_name"));

                SparkleApp.getInstance().setSingleSongPlayUrl(dataObj.getString("file_name"));

                SparkleApp.getInstance().setSongReqInfo(info);
            } else {
                SparkleApp.getInstance().setSongReqInfo(null);
            }
        } catch (JSONException e) {

        }

        return songreqdata;
    }

    //
    // Album List
    public static int getAlbumListData() throws Exception {
        //  List<NameValuePair> postData = new ArrayList<NameValuePair>(1);


        //dev panel http://mobioapp.net/apps/sparkle/
        final String[] serverResponse = new String[1];
       /* String serverResponse = WebServerOperation.
                sendHTTPPostRequestToServer(Urls.BASE_URL + "index.php/api/album_list", postData, true);*/


        return 0;
    }

    public static int parseAlbumListPost(JSONObject jDataObj) {
        int albumListStatus = 0;
        try {

            albumListStatus = jDataObj.getInt("message");

            if (albumListStatus == 1) {
                JSONArray jDataObjArray = jDataObj.getJSONArray("user_data");
                ArrayList<MoAlbum> list = new ArrayList<MoAlbum>();
                ArrayList<MoSong> songList = new ArrayList<MoSong>();

                if (jDataObjArray.length() > 0) {
                    for (int i = 0; i < jDataObjArray.length(); i++) {
                        final MoAlbum album = new MoAlbum();
                        JSONObject values = jDataObjArray.getJSONObject(i);
                        album.setId(values.getInt("id"));
                        album.setTitle(values.getString("name"));
                        album.setReleaseDate(values.getString("release_date"));
                        album.setImgUrl(values.getString("cover_image"));
                        list.add(album);

                        JSONArray songsArray = values.getJSONArray("songs");

                        for (int j = 0; j < songsArray.length(); j++) {
                            MoSong song = new MoSong();
                            JSONObject songObject = songsArray.getJSONObject(j);
                            song.setId(songObject.getInt("id"));
                            song.setAlbumId(songObject.getString("album_id"));
                            song.setAlbum_name(songObject.getString("album_name"));
                            song.setTitle(songObject.getString("name"));
                            song.setArtistId(songObject.getString("artist_name"));
                            song.setImgUrl(songObject.getString("cover_image"));
                            song.setFileName(songObject.getString("file_name"));
                            song.setSongDetails(songObject.getString("details"));
                            songList.add(song);

                        }

                    }

                    SparkleApp.getInstance().setAlbumList(list);
                    DbManager db = new DbManager(MyApp.getAppContext());
                    db.addSongs(songList);
                    db.addAlbums(list);
                    db.close();

                }
                else {
                    //SparkleApp.getInstance().setAlbumList(null);
                }


            } else {

            }

        } catch (JSONException e) {

        }

        return albumListStatus;
    }

    public static int parseArtistAlbumsPost(JSONObject jDataObj) {
        int albumListStatus = 0;
        try {

            albumListStatus = jDataObj.getInt("message");

            if (albumListStatus == 1) {
                JSONArray jDataObjArray = jDataObj.getJSONArray("user_data");
                ArrayList<MoArtist> list = new ArrayList<MoArtist>();
                ArrayList<MoAlbum> albumList = new ArrayList<MoAlbum>();

                if (jDataObjArray.length() > 0) {
                    for (int i = 0; i < jDataObjArray.length(); i++) {
                        final MoArtist artist = new MoArtist();
                        JSONObject values = jDataObjArray.getJSONObject(i);
                        artist.setId(values.getInt("id"));
                        artist.setName(values.getString("name"));
                        artist.setArtistDetails(values.getString("details"));
                        artist.setImage(values.getString("cover_image"));
                        list.add(artist);

                        JSONArray songsArray = values.getJSONArray("songs");

                        for (int j = 0; j < songsArray.length(); j++) {
                            final MoAlbum album = new MoAlbum();
                            JSONObject songObject = songsArray.getJSONObject(j);
                            album.setId(songObject.getInt("album_id"));
                            album.setTitle(songObject.getString("album_name"));
                            album.setImgUrl(songObject.getString("cover_image"));

                            albumList.add(album);

                        }
                        Log.e("ARTIST", artist.getName());

                    }


                    SparkleApp.getInstance().setArtistList(list);
                    DbManager db = new DbManager(MyApp.getAppContext());
                    db.addAlbums(albumList);
                    db.addArtists(list);
                    db.close();

                } else {
                    //SparkleApp.getInstance().setAlbumList(null);
                }


            } else {

            }

        } catch (JSONException e) {

        }
        Log.e("ALBUM","Status : "+albumListStatus);
        return albumListStatus;
    }

    public static int parseArtistAlbumPost(JSONObject jDataObj) {
        int albumListStatus = 0;
        try {
            SparkleApp.getInstance().setAlbumList(null);
            albumListStatus = jDataObj.getInt("message");

            if (albumListStatus == 1) {
                JSONArray jDataObjArray = jDataObj.getJSONArray("album_list");
                ArrayList<MoAlbum> list = new ArrayList<MoAlbum>();

                if (jDataObjArray.length() > 0) {
                    MoAlbum info = new MoAlbum();
                    for (int i = 0; i < jDataObjArray.length(); i++) {
                        info = new MoAlbum();
                        JSONObject album_values = jDataObjArray.getJSONObject(i);
                        info.setId(album_values.getInt("album_id"));
                        info.setTitle(album_values.getString("album_name"));
                        info.setArtistId(album_values.getInt("artist_id"));
                        info.setImgUrl(album_values.getString("album_cover_image"));
                        list.add(info);
                    }
                    Log.e("ALBUM", info.getTitle());

                    SparkleApp.getInstance().setAlbumList(list);
                    Globals.albumslist=list;
                    DbManager myDb = new DbManager(MyApp.getAppContext());
                    myDb.addAlbums(list);
                    myDb.close();
                }
                else {
                    //SparkleApp.getInstance().setAlbumList(null);
                }

            } else {

            }

        } catch (JSONException e) {

        }
        Log.e("ALBUM","Status : "+albumListStatus);
        return albumListStatus;
    }


    public static int parseGenreWiseAlbumPost(JSONObject jDataObj) {
        int albumListStatus = 0;
        try {

            albumListStatus = jDataObj.getInt("message");
            if (albumListStatus == 1) {
                JSONArray jDataObjArray = jDataObj.getJSONArray("album_list");
                ArrayList<MoAlbum> list = new ArrayList<MoAlbum>();

                if (jDataObjArray.length() > 0) {
                    MoAlbum info = new MoAlbum();
                    for (int i = 0; i < jDataObjArray.length(); i++) {
                        info = new MoAlbum();
                        JSONObject album_values = jDataObjArray.getJSONObject(i);
                        info.setId(album_values.getInt("album_id"));
                        info.setTitle(album_values.getString("album_name"));
                        info.setArtistId(album_values.getInt("artist_id"));
                        info.setImgUrl(album_values.getString("album_cover_image"));
                        info.setReleaseDate(album_values.getString("release_date"));
                        list.add(info);
                    }
                    Log.e("ALBUM", info.getTitle());

                    SparkleApp.getInstance().setAlbumList(list);
                    DbManager myDb = new DbManager(MyApp.getAppContext());
                    myDb.addAlbums(list);
                    myDb.close();
                }
                else {
                    //SparkleApp.getInstance().setAlbumList(null);
                }

            } else {

            }

        } catch (JSONException e) {

        }
        Log.e("ALBUM","Status : "+albumListStatus);
        return albumListStatus;
    }

    public static int parseGenreWiseSongPost(JSONObject jDataObj) {
        int songListStatus = 0;

        try {
            SparkleApp.getInstance().setSongList(null);
            songListStatus = jDataObj.getInt("message");
            if (songListStatus == 1) {

                JSONArray jDataObjArray = jDataObj.getJSONArray("songs_list");
                ArrayList<MoSong> list = new ArrayList<MoSong>();

                if (jDataObjArray.length() > 0) {
                    MoSong info = new MoSong();
                    for (int i = 0; i < jDataObjArray.length(); i++) {
                        info = new MoSong();
                        JSONObject song_values = jDataObjArray.getJSONObject(i);
                        info.setId(song_values.getInt("song_id"));
                        info.setGenreId(song_values.getString("genre_id"));
                        info.setTitle(song_values.getString("song_name"));
                        info.setImgUrl(song_values.getString("cover_image"));
                        Log.e("SONG_GENRE_com", info.getTitle());
                        list.add(info);
                        Log.e("SONG_GENRE_com", list.toString());
                    }
                    SparkleApp.getInstance().setSongList(list);
                    Globals.playlistSong = list;
                    DbManager myDb = new DbManager(MyApp.getAppContext());
                    myDb.addSongs(list);
                    myDb.close();
                }
                else {
                    //SparkleApp.getInstance().setSongList(null);
                }

            } else {

            }

        } catch (JSONException e) {

        }
        Log.e("SONG","Status : "+songListStatus);
        return songListStatus;
    }


    public static int parseTopAlbumData(JSONObject jDataObj) {
        int albumListStatus = 0;
        try {

            albumListStatus = jDataObj.getInt("message");

            if (albumListStatus == 1) {
                JSONArray jDataObjArray = jDataObj.getJSONArray("user_data");
                ArrayList<MoAlbum> list = new ArrayList<MoAlbum>();

                if (jDataObjArray.length() > 0) {
                    for (int i = 0; i < jDataObjArray.length(); i++) {
                        final MoAlbum album = new MoAlbum();
                        JSONObject values = jDataObjArray.getJSONObject(i);
                        album.setId(values.getInt("id"));
                        album.setTitle(values.getString("name"));
                        album.setReleaseDate(values.getString("release_date"));
                        album.setImgUrl(values.getString("cover_image"));
                        list.add(album);
                    }

                    SparkleApp.getInstance().setTopAlbumList(list);
                    DbManager myDb = new DbManager(MyApp.getAppContext());
                    myDb.addTopAlbums(list);
                    myDb.addAlbums(list);
                    myDb.close();

                } else {
                    //SparkleApp.getInstance().setAlbumList(null);
                }

            } else {

            }

        } catch (JSONException e) {

        }

        return albumListStatus;
    }

    public static int parseTopAlbumArtistData(JSONObject jDataObj) {
        int albumListStatus = 0;
        try {

            albumListStatus = jDataObj.getInt("message");

            if (albumListStatus == 1) {
                JSONArray jDataObjArray = jDataObj.getJSONArray("album_list");
                ArrayList<MoAlbum> list = new ArrayList<MoAlbum>();

                if (jDataObjArray.length() > 0) {
                    for (int i = 0; i < jDataObjArray.length(); i++) {
                        final MoAlbum album = new MoAlbum();
                        JSONObject values = jDataObjArray.getJSONObject(i);
                        album.setId(values.getInt("album_id"));
                        album.setTitle(values.getString("album_name"));
                        album.setArtistId(values.getInt("artist_id"));
                        album.setImgUrl(values.getString("album_image"));
                        list.add(album);
                        Log.e("ALBUM", "Name " + album.getTitle());
                    }
                    Log.e("ALBUM Name top", list.toString());
                    SparkleApp.getInstance().setTopAlbumList(list);
                    DbManager myDb = DbManager.getInstance();
                    myDb.addTopAlbums(list);
                    myDb.addAlbums(list);
                    myDb.close();

                } else {
                    //SparkleApp.getInstance().setAlbumList(null);
                }

            } else {

            }

        } catch (JSONException e) {

        }
        Log.e("ALBUM Name top", "Status "+albumListStatus);
        return albumListStatus;
    }

    public static int parseTopSongArtistData(JSONObject jDataObj) {
        int songListStatus = 0;
        try {

            songListStatus = jDataObj.getInt("message");

            if (songListStatus == 1) {
                JSONArray jDataObjArray = jDataObj.getJSONArray("song_list");
                ArrayList<MoSong> list = new ArrayList<MoSong>();

                if (jDataObjArray.length() > 0) {
                    for (int i = 0; i < jDataObjArray.length(); i++) {
                        final MoSong song = new MoSong();
                        JSONObject values = jDataObjArray.getJSONObject(i);
                        song.setId(values.getInt("song_id"));
                        song.setTitle(values.getString("song_name"));
                        song.setArtistId(values.getString("artist_id"));
                        song.setArtist_name(values.getString("artist_name"));
                        song.setImgUrl(values.getString("song_image"));
                        song.setFileName(values.getString("file_name"));

                        list.add(song);
                        Log.e("SONG", "Name " + song.getTitle());
                    }
                    Log.e("SONG Name top", list.toString());
                    SparkleApp.getInstance().setTopSongList(list);
                    DbManager myDb = DbManager.getInstance();
                    myDb.addTopSongs(list);
                    myDb.addSongs(list);
                    myDb.close();

                } else {
                    //SparkleApp.getInstance().setAlbumList(null);
                }

            } else {

            }

        } catch (JSONException e) {

        }
        Log.e("SONG Name top", "Status "+songListStatus);
        return songListStatus;
    }


    // Artist List


    public static int parseArtistListData(JSONObject jDataObj) {
        int albumListStatus = 0;

        try {

            albumListStatus = jDataObj.getInt("message");

            if (albumListStatus == 1) {
                JSONArray jDataObjArray = jDataObj.getJSONArray("user_data");
                ArrayList<MoArtist> list = new ArrayList<MoArtist>();

                if (jDataObjArray.length() > 0) {
                    for (int i = 0; i < jDataObjArray.length(); i++) {
                        final MoArtist info = new MoArtist();
                        JSONObject values = jDataObjArray.getJSONObject(i);
                        info.setId(values.getInt("id"));
                        info.setName(values.getString("name"));
                        //info.setAlbumDetails(values.getString("details"));
                        info.setImage(values.getString("cover_image"));
                        list.add(info);
                    }

                    SparkleApp.getInstance().setArtistList(list);
                    DbManager myDb = new DbManager(MyApp.getAppContext());
                    myDb.addArtists(list);
                    myDb.close();
                }
                else {
                    //SparkleApp.getInstance().setAlbumList(null);
                }

            } else {

            }

        } catch (JSONException e) {

        }

        return albumListStatus;
    }

    public static int parseTopArtistListData(JSONObject jDataObj) {
        int albumListStatus = 0;

        try {

            albumListStatus = jDataObj.getInt("message");

            if (albumListStatus == 1) {
                JSONArray jDataObjArray = jDataObj.getJSONArray("top_artist");
                ArrayList<MoArtist> list = new ArrayList<MoArtist>();

                if (jDataObjArray.length() > 0) {
                    for (int i = 0; i < jDataObjArray.length(); i++) {
                        final MoArtist info = new MoArtist();
                        JSONObject values = jDataObjArray.getJSONObject(i);
                        info.setId(values.getInt("artist_id"));
                        info.setName(values.getString("artist_name"));
                        //info.setAlbumDetails(values.getString("details"));
                        info.setImage(values.getString("cover_image"));
                        list.add(info);
                    }

                    SparkleApp.getInstance().setTopArtistList(list);
                    DbManager myDb = new DbManager(MyApp.getAppContext());
                    myDb.addTopArtists(list);
                    myDb.addArtists(list);
                    myDb.close();
                }
                else {
                    //SparkleApp.getInstance().setAlbumList(null);
                }

            } else {

            }

        } catch (JSONException e) {

        }

        return albumListStatus;
    }


    public static int parseTrendingArtistListData(JSONObject jDataObj) {
        int albumListStatus = 0;

        try {

            albumListStatus = jDataObj.getInt("message");

            if (albumListStatus == 1) {
                JSONArray jDataObjArray = jDataObj.getJSONArray("trending_artist");
                ArrayList<MoArtist> list = new ArrayList<MoArtist>();

                if (jDataObjArray.length() > 0) {
                    for (int i = 0; i < jDataObjArray.length(); i++) {
                        final MoArtist info = new MoArtist();
                        JSONObject values = jDataObjArray.getJSONObject(i);
                        info.setId(values.getInt("artist_id"));
                        info.setName(values.getString("artist_name"));
                        //info.setAlbumDetails(values.getString("details"));
                        info.setImage(values.getString("cover_image"));
                        list.add(info);
                    }

                    SparkleApp.getInstance().setTrendArtistList(list);
                    DbManager myDb = new DbManager(MyApp.getAppContext());
                    myDb.addTrendArtists(list);
                    myDb.addArtists(list);
                    myDb.close();
                }
                else {
                    //SparkleApp.getInstance().setAlbumList(null);
                }

            } else {

            }

        } catch (JSONException e) {

        }

        return albumListStatus;
    }

    public static int parseGenreListData(JSONObject jDataObj) {
        int genreListStatus = 0;

        try {

            genreListStatus = jDataObj.getInt("message");

            if (genreListStatus == 1) {
                JSONArray jDataObjArray = jDataObj.getJSONArray("genre_list");
                ArrayList<MoGenres> list = new ArrayList<MoGenres>();

                if (jDataObjArray.length() > 0) {
                    MoGenres info = new MoGenres();
                    for (int i = 0; i < jDataObjArray.length(); i++) {
                        info = new MoGenres();
                        JSONObject gen_values = jDataObjArray.getJSONObject(i);
                        info.setId(gen_values.getInt("genre_id"));
                        info.setTitle(gen_values.getString("genre_name"));
                        //info.setAlbumDetails(values.getString("details"));
                        info.setImgUrl(gen_values.getString("genre_image"));
                        list.add(info);
                    }

                    Log.e("GENRE",info.getTitle());
                    SparkleApp.getInstance().setGenreList(list);
                    DbManager myDb = new DbManager(MyApp.getAppContext());
                    myDb.addGenres(list);
                    myDb.close();
                }
                else {
                    //SparkleApp.getInstance().setAlbumList(null);
                }

            } else {

            }

        } catch (JSONException e) {

        }
        Log.e("GENRE","Status : "+genreListStatus);
        return genreListStatus;
    }




    //new Release
    public static int parseNewReleaseDate(JSONObject jDataObj) {
        int dateLimit = 365 * 5;

        try {

            dateLimit = jDataObj.getInt("user_data");


        } catch (JSONException e) {

        }

        return dateLimit;
    }


    // Song List

    public static int getSongListData(boolean isArtist, int id) throws Exception {
        String serverResponse = "";
        /*List<NameValuePair> postData = new ArrayList<NameValuePair>(1);

//		SharedPreferences sharedPreferences = context.getSharedPreferences("SPARKLE_APP", Context.MODE_PRIVATE);
//		int album = sharedPreferences.getInt("album_id", 0);

//		Log.d("SPARKLE_APP",String.valueOf(album));

        if (isArtist) {
            postData.add((NameValuePair) new BasicNameValuePair("artist_id", String.valueOf(id)));
        } else {
            postData.add((NameValuePair) new BasicNameValuePair("album_id", String.valueOf(id)));
        }
*/

        /*if (isArtist) {
            serverResponse = WebServerOperation.sendHTTPPostRequestToServer(Urls.BASE_URL + "index.php/api/song_list", postData, true);
        } else {
            serverResponse = WebServerOperation.sendHTTPPostRequestToServer(Urls.BASE_URL + "index.php/api/song_list", postData, true);
        }
*/

        return parseSongListPost(serverResponse, isArtist);
    }

    public static int parseSongListPost(String jData, boolean isArtist) throws JSONException, java.text.ParseException {
        int songListStatus = 0;
        SparkleApp.getInstance().setSongList(null);
        JSONObject jDataObj = new JSONObject(jData);
        songListStatus = jDataObj.getInt("message");

        if (songListStatus == 1) {
            JSONArray jDataObjArray = jDataObj.getJSONArray("user_data");

            ArrayList<MoSong> list = new ArrayList<MoSong>();
            if (jDataObjArray.length() > 0) {
                for (int i = 0; i < jDataObjArray.length(); i++) {
                    MoSong song = new MoSong();
                    JSONObject values = jDataObjArray.getJSONObject(i);
                    song.setId(values.getInt("id"));
                    song.setTitle(values.getString("name"));
                    song.setArtistId(values.getString("artist_id"));
                    song.setAlbumId(values.getString("album_id"));
                    song.setAlbum_name(values.getString("album_name"));
                    song.setArtist_name(values.getString("artist_name"));
                    song.setSongDetails(values.getString("details"));
                    song.setImgUrl(values.getString("cover_image"));
                    song.setFileName(values.getString("file_name"));
                    song.setSongDetails(values.getString("details"));
                    if (!isArtist) {
                        song.setDuration(values.getString("duration"));
                    } else {
                        song.setDuration("");
                    }

                    list.add(song);

                }
                Log.e("SONG","s Size : "+list.size());
                DbManager db=new DbManager(MyApp.getAppContext());
                db.addSongs(list);
                db.close();
                SparkleApp.getInstance().setSongList(list);
                Globals.playlistSong = list;

            } else {
                SparkleApp.getInstance().setSongList(null);
            }

        } else {
            Log.i("Song_List_From_Album", "Not Found");
        }
        Log.e("SONG","Status : "+songListStatus);
        return songListStatus;
    }

    public static int parseSongListArtistPost(String jData, boolean isArtist) throws JSONException, java.text.ParseException {
        int songListStatus = 0;
        SparkleApp.getInstance().setSongList(null);
        JSONObject jDataObj = new JSONObject(jData);
        songListStatus = jDataObj.getInt("message");

        if (songListStatus == 1) {
            JSONArray jDataObjArray = jDataObj.getJSONArray("user_data");

            ArrayList<MoSong> list = new ArrayList<MoSong>();
            if (jDataObjArray.length() > 0) {
                for (int i = 0; i < jDataObjArray.length(); i++) {
                    MoSong song = new MoSong();
                    JSONObject values = jDataObjArray.getJSONObject(i);
                    song.setId(values.getInt("id"));
                    song.setTitle(values.getString("name"));
                    song.setArtistId(values.getString("artist_id"));
                    song.setArtist_name(values.getString("artist_name"));
                    song.setAlbumId(values.getString("album_id"));
                    song.setAlbum_name(values.getString("album_name"));
                    song.setSongDetails(values.getString("details"));
                    song.setImgUrl(values.getString("cover_image"));
                    song.setFileName(values.getString("file_name"));
                    song.setSongDetails(values.getString("details"));
                    if (!isArtist) {
                        song.setDuration(values.getString("duration"));
                    } else {
                        song.setDuration("");
                    }
                    Log.e("SONG_ARTIST_com",song.getTitle());
                    list.add(song);

                }
                Log.e("SONG_ARTIST_com",list.toString());
                SparkleApp.getInstance().setSongList(list);
                DbManager myDb = new DbManager(MyApp.getAppContext());
                myDb.addSongs(list);
                myDb.close();
                //Globals.playlistSong = list;

            } else {
                SparkleApp.getInstance().setSongList(null);
            }

        } else {
            Log.i("Song_List_From_Album", "Not Found");
        }
        Log.e("SONG","Status : "+songListStatus);
        return songListStatus;
    }


    public static int parseSongListGenrePost(String jData, boolean isGenre) throws JSONException, java.text.ParseException {
        int songListStatus = 0;
        SparkleApp.getInstance().setSongList(null);
        JSONObject jDataObj = new JSONObject(jData);
        songListStatus = jDataObj.getInt("message");

        if (songListStatus == 1) {
            JSONArray jDataObjArray = jDataObj.getJSONArray("user_data");

            ArrayList<MoSong> list = new ArrayList<MoSong>();
            if (jDataObjArray.length() > 0) {
                for (int i = 0; i < jDataObjArray.length(); i++) {
                    MoSong song = new MoSong();
                    JSONObject values = jDataObjArray.getJSONObject(i);
                    song.setId(values.getInt("id"));
                    song.setTitle(values.getString("name"));
                    song.setGenreId(values.getString("genre_id"));
                    song.setArtistId(values.getString("artist_id"));
                    song.setAlbumId(values.getString("album_id"));
                    song.setAlbum_name(values.getString("album_name"));
                    song.setArtist_name(values.getString("artist_name"));
                    song.setSongDetails(values.getString("details"));
                    song.setImgUrl(values.getString("cover_image"));
                    song.setFileName(values.getString("file_name"));
                    song.setSongDetails(values.getString("details"));
                    if (!isGenre) {
                        song.setDuration(values.getString("duration"));
                    } else {
                        song.setDuration("");
                    }

                    Log.e("SONG_GENRE_com",song.getTitle());
                    list.add(song);

                }
                Log.e("SONG_GENRE_com",list.toString());
                SparkleApp.getInstance().setSongList(list);
                DbManager myDb = new DbManager(MyApp.getAppContext());
                myDb.addSongs(list);
                myDb.close();
                //Globals.playlistSong = list;

            } else {
                SparkleApp.getInstance().setSongList(null);
            }

        } else {
            Log.i("Song_List_From_Album", "Not Found");
        }
        Log.e("SONG","Status : "+songListStatus);
        return songListStatus;
    }


    public String getDownloadFile(String userId, String songID) {

        AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();
        params.add("user_id", userId);
        params.add("song_id", songID);

        client.post(Urls.BASE_URL + "index.php/api/download", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                parseDownloadSong(response);
            }
        });
        return fileUrl;
    }



    private void parseDownloadSong(JSONObject obj) {

        int status = getIntValue(obj, "message");

        if (status > 0) {

            try {
                JSONObject rObj = obj.getJSONObject("user_data");
                String id = getStringValue(rObj, "id");
                String fileName = getStringValue(rObj, "file_name");
                String filePath = getStringValue(rObj, "mp3_file_path");
                // String filePath = getStringValue(rObj, "mp3_file_path");

                new DownloadAsyncTask(id, fileName).execute(filePath);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private String getStringValue(JSONObject obj, String key) {
        String value = "invalid";
        if (obj.isNull(key))
            value = "invalid";
        try {
            value = obj.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return value;
    }

    private int getIntValue(JSONObject obj, String key) {
        int value = 0;
        if (obj.isNull(key))
            value = 0;
        try {
            value = obj.getInt(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return value;
    }

}
