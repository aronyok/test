package baajna.scroll.owner.mobioapp.localDatabase;

/**
 * Created by Jewel on 12/24/15.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import baajna.scroll.owner.mobioapp.datamodel.MoAlbum;
import baajna.scroll.owner.mobioapp.datamodel.MoArtist;
import baajna.scroll.owner.mobioapp.datamodel.MoGenres;
import baajna.scroll.owner.mobioapp.datamodel.MoPlayList;
import baajna.scroll.owner.mobioapp.datamodel.MoSong;
import baajna.scroll.owner.mobioapp.datamodel.MoSongCount;
import baajna.scroll.owner.mobioapp.utils.MyApp;

import java.util.ArrayList;


public class DbManager extends SQLiteOpenHelper {

    //all table name
    private static final String TABLE_SONG = "song";
    private static final String TABLE_ALBUM = "album";
    private static final String TABLE_ARTIST = "artist";
    private static final String TABLE_GENRE = "genre";
    private static final String TABLE_TOP_ALBUMS = "top_albums";
    private static final String TABLE_TOP_SONGS = "top_songs";
    private static final String TABLE_TOP_ARTIST = "top_artist";
    private static final String TABLE_TRENDING_ARTIST = "trending_artist";

    private static final String TABLE_NEW_RELEASE = "new_release";
    private static final String TABLE_FEATURE_LIST = "feature_list";
    private static final String TABLE_MY_FAVORITE = "my_favorite";
    private static final String TABLE_DOWNLOAD = "my_download";
    private static final String TABLE_PLAYLIST = "my_play_list";
    private static final String TABLE_PLAYLIST_NAME = "my_play_list_name";
    private static final String TABLE_SONG_COUNT = "song_count";

    //all field name
    private static final String KEY_ID = "id";
    private static final String KEY_SONG_ID = "song_id";
    private static final String KEY_NAME = "file_name";
    private static final String KEY_ALBUM_ID = "album_id";
    private static final String KEY_ARTIST_ID = "artist_id";
    private static final String KEY_ARTIST_NAME = "artist_name";
    private static final String KEY_GENRE_ID = "genre_id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_TAG = "tag";
    private static final String KEY_DETAILS="details";
    private static final String KEY_DURATION = "duration";
    private static final String KEY_COVER_IMG = "cover_img";
    private static final String KEY_DOWNLOAD = "download";
    private static final String KEY_LIKE = "islike";
    private static final String KEY_RELEASE_DATE = "release_date";
    private static final String KEY_IMG = "img";
    private static final String KEY_TYPE = "type";
    private static final String KEY_CONTENT_ID = "content_id";
    private static final String KEY_MOD = "last_mod_time";
    private static final String KEY_STATUS = "status";
    private static final String KEY_ACTION = "action";

    private static final String KEY_COUNT = "song_count";
    private static final String KEY_APP_ID = "app_id";

    private static final int TYPE_SONG = 1;
    private static final int TYPE_ARTIST = 2;
    private static final int TYPE_ALBUM = 3;
    private static final int TYPE_GENRE = 4;

    private static final String DB_NAME = "sparkle";
    private static final int DB_VERSION = 1;
    private SQLiteDatabase db;
    private Cursor cursor;


    // for getting table data using custom query
    public static final String SQL_SONGS_PLAYLIST_RUNNING = "SELECT a.*,c." + KEY_DOWNLOAD + " FROM " + TABLE_SONG
            + " AS a INNER JOIN " + TABLE_PLAYLIST + " AS b ON a." + KEY_ID + "=b." + KEY_SONG_ID
            + " LEFT JOIN " + TABLE_DOWNLOAD + " AS c ON a." + KEY_ID + "=c." + KEY_SONG_ID;
    //+" WHERE b."+KEY_ID+"='0'";*/

    public static final String SQL_SONGS_DOWNLOAD = "select " + TABLE_SONG + "." + KEY_ID + "," + TABLE_SONG + "." + KEY_TITLE + "," + TABLE_SONG + "." + KEY_IMG + ","
            + TABLE_SONG + "." + KEY_DURATION + "," + TABLE_SONG + "." + KEY_ALBUM_ID + "," + TABLE_SONG + "." + KEY_ARTIST_ID + "," + TABLE_SONG + "." + KEY_MOD
            + "," + TABLE_DOWNLOAD + "." + KEY_NAME + "," + TABLE_DOWNLOAD + "." + KEY_DOWNLOAD

            + " FROM " + TABLE_SONG
            + " INNER JOIN " + TABLE_DOWNLOAD + " ON " + TABLE_DOWNLOAD + "." + KEY_SONG_ID + "=" + TABLE_SONG + "." + KEY_ID;

    public static final String SQL_ARTISTS = "select * from " + TABLE_ARTIST;
    public static final String SQL_ALBUMS_TOP = "select * from " + TABLE_ALBUM;
    public static final String SQL_TOP_ALBUMS = "select * from " + TABLE_TOP_ALBUMS;
    public static final String SQL_TOP_SONGS = "select * from " + TABLE_TOP_SONGS;

    public static final String SQL_TOP_ARTISTS = "select * from " + TABLE_TOP_ARTIST;
    public static final String SQL_TREND_ARTISTS = "select * from " + TABLE_TRENDING_ARTIST;
    public static final String SQL_GENRES = "select * from " + TABLE_GENRE;

    public static final String SQL_SONG_COUNT = "select * from " + TABLE_SONG_COUNT;


    public DbManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);


    }
    public static DbManager getInstance(){
        return new DbManager(MyApp.getAppContext());
    }

    @Override
    public void onCreate(SQLiteDatabase database) {

        String queryDownlodSong = DBQuery.init()
                .newTable(TABLE_DOWNLOAD)
                .addField(KEY_ID, DBQuery.INTEGER_PRI_AUTO)
                .addField(KEY_SONG_ID, DBQuery.INTEGER)
                .addField(KEY_DOWNLOAD, DBQuery.TEXT)
                .addField(KEY_NAME, DBQuery.TEXT)
                .addField(KEY_MOD, DBQuery.TEXT)
                .getTable();

        String queryAlbum = DBQuery.init()
                .newTable(TABLE_ALBUM)
                .addField(KEY_ID, DBQuery.INTEGER_PRI)
                .addField(KEY_TITLE, DBQuery.TEXT)
                .addField(KEY_IMG, DBQuery.TEXT)
                .addField(KEY_GENRE_ID, DBQuery.TEXT)
                .addField(KEY_ARTIST_ID, DBQuery.INTEGER)
                .addField(KEY_RELEASE_DATE, DBQuery.TEXT)
                .addField(KEY_MOD, DBQuery.TEXT)
                .getTable();

        String queryTopAlbum = DBQuery.init()
                .newTable(TABLE_TOP_ALBUMS)
                .addField(KEY_ID, DBQuery.INTEGER_PRI)
                .addField(KEY_TITLE, DBQuery.TEXT)
                .addField(KEY_IMG, DBQuery.TEXT)
                .addField(KEY_GENRE_ID, DBQuery.TEXT)
                .addField(KEY_ARTIST_ID, DBQuery.INTEGER)
                .addField(KEY_RELEASE_DATE, DBQuery.TEXT)
                .addField(KEY_MOD, DBQuery.TEXT)
                .getTable();

        String querySong = DBQuery.init()
                .newTable(TABLE_SONG)
                .addField(KEY_ID, DBQuery.INTEGER_PRI)
                .addField(KEY_TITLE, DBQuery.TEXT)
                .addField(KEY_NAME, DBQuery.TEXT)
                .addField(KEY_IMG, DBQuery.TEXT)
                .addField(KEY_DETAILS,DBQuery.TEXT)
                .addField(KEY_DURATION, DBQuery.TEXT)
                .addField(KEY_ALBUM_ID, DBQuery.TEXT)
                .addField(KEY_ARTIST_ID, DBQuery.TEXT)
                .addField(KEY_ARTIST_NAME,DBQuery.TEXT)
                .addField(KEY_GENRE_ID, DBQuery.TEXT)
                .addField(KEY_MOD, DBQuery.TEXT)
                .getTable();

        String querySongCount = DBQuery.init()
                .newTable(TABLE_SONG_COUNT)
                .addField(KEY_ID, DBQuery.INTEGER_PRI)
                .addField(KEY_SONG_ID, DBQuery.INTEGER)
                .addField(KEY_ACTION, DBQuery.TEXT)
                .addField(KEY_STATUS, DBQuery.INTEGER)
                .addField(KEY_MOD, DBQuery.TEXT)
                .getTable();

        String queryTopSong = DBQuery.init()
                .newTable(TABLE_TOP_SONGS)
                .addField(KEY_ID, DBQuery.INTEGER_PRI)
                .addField(KEY_TITLE, DBQuery.TEXT)
                .addField(KEY_NAME, DBQuery.TEXT)
                .addField(KEY_IMG, DBQuery.TEXT)
                .addField(KEY_DETAILS,DBQuery.TEXT)
                .addField(KEY_DURATION, DBQuery.TEXT)
                .addField(KEY_ALBUM_ID, DBQuery.TEXT)
                .addField(KEY_ARTIST_ID, DBQuery.TEXT)
                .addField(KEY_ARTIST_NAME,DBQuery.TEXT)
                .addField(KEY_GENRE_ID, DBQuery.TEXT)
                .addField(KEY_MOD, DBQuery.TEXT)
                .getTable();


        String queryArtist = DBQuery.init()
                .newTable(TABLE_ARTIST)
                .addField(KEY_ID, DBQuery.INTEGER_PRI)
                .addField(KEY_NAME, DBQuery.TEXT)
                .addField(KEY_IMG, DBQuery.TEXT)
                .addField(KEY_TAG, DBQuery.TEXT)
                .addField(KEY_MOD, DBQuery.TEXT)
                .getTable();

        String queryTopArtist = DBQuery.init()
                .newTable(TABLE_TOP_ARTIST)
                .addField(KEY_ID, DBQuery.INTEGER_PRI)
                .addField(KEY_NAME, DBQuery.TEXT)
                .addField(KEY_IMG, DBQuery.TEXT)
                .addField(KEY_TAG, DBQuery.TEXT)
                .addField(KEY_MOD, DBQuery.TEXT)
                .getTable();

        String queryTrendArtist = DBQuery.init()
                .newTable(TABLE_TRENDING_ARTIST)
                .addField(KEY_ID, DBQuery.INTEGER_PRI)
                .addField(KEY_NAME, DBQuery.TEXT)
                .addField(KEY_IMG, DBQuery.TEXT)
                .addField(KEY_TAG, DBQuery.TEXT)
                .addField(KEY_MOD, DBQuery.TEXT)
                .getTable();

        String queryGenre = DBQuery.init()
                .newTable(TABLE_GENRE)
                .addField(KEY_ID, DBQuery.INTEGER_PRI)
                .addField(KEY_TITLE, DBQuery.TEXT)
                .addField(KEY_IMG, DBQuery.TEXT)
                .addField(KEY_GENRE_ID, DBQuery.TEXT)
                .addField(KEY_MOD, DBQuery.TEXT)
                .getTable();

        String myPlayList = DBQuery.init()
                .newTable(TABLE_PLAYLIST)
                .addField(KEY_ID, DBQuery.INTEGER)
                .addField(KEY_SONG_ID, DBQuery.INTEGER)
                .addField(KEY_NAME, DBQuery.TEXT)
                .addField(KEY_TAG, DBQuery.TEXT)
                .addField(KEY_MOD, DBQuery.TEXT)
                .getTable();

        String myPlayListName = DBQuery.init()
                .newTable(TABLE_PLAYLIST_NAME)
                .addField(KEY_ID, DBQuery.INTEGER_PRI_AUTO)
                .addField(KEY_NAME, DBQuery.TEXT)
                .addField(KEY_MOD, DBQuery.TEXT)
                .getTable();

        database.execSQL(querySong);
        database.execSQL(queryAlbum);
        database.execSQL(myPlayList);
        database.execSQL(myPlayListName);
        database.execSQL(queryDownlodSong);
        database.execSQL(queryArtist);
        database.execSQL(queryGenre);
        database.execSQL(queryTopAlbum);
        database.execSQL(queryTopSong);
        database.execSQL(queryTopArtist);
        database.execSQL(queryTrendArtist);
        database.execSQL(querySongCount);

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {

        database.execSQL("DROP TABLE IF EXISTS " + TABLE_ALBUM);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_SONG);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYLIST);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYLIST_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_DOWNLOAD);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_GENRE);
        onCreate(database);
    }


    //Common Section

    private int getIntValue(String key) {
        if (cursor.getColumnIndex(key) == -1) {
            return -1;
        }
        return cursor.getInt(cursor.getColumnIndex(key));

    }

    private String getStringValue(String key) {
        if (cursor.getColumnIndex(key) == -1) {
            return "NA";
        }
        return cursor.getString(cursor.getColumnIndex(key));

    }

    private boolean isExist(String table, int id) {
        try {
            db = this.getReadableDatabase();

            String sql = "SELECT * FROM " + table + " WHERE " + KEY_ID + " = " + "'" + id + "'";

            cursor = db.rawQuery(sql, null);

            if (cursor.getCount() > 0) {
                cursor.close();
                return true;

            }

        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            cursor.close();
        }

        return false;
    }

    private boolean isExist(String sql) {
        try {
            db = this.getReadableDatabase();

            cursor = db.rawQuery(sql, null);

            if (cursor.getCount() > 0) {
                cursor.close();
                return true;

            }

        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            cursor.close();
        }

        return false;
    }

    private boolean isExist(String table, String key, int value) {
        try {
            db = this.getReadableDatabase();

            String sql = "SELECT * FROM " + table + " WHERE " + key + " = " + "'" + value + "'";

            cursor = db.rawQuery(sql, null);

            if (cursor.getCount() > 0) {
                cursor.close();
                return true;

            }

        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            cursor.close();
        }

        return false;
    }

    //section : Song
    public long addSong(MoSong moSong) {
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_ID, moSong.getId());
            values.put(KEY_IMG, moSong.getImgUrl());
            values.put(KEY_TITLE, moSong.getTitle());
            values.put(KEY_NAME, moSong.getFileName());
            values.put(KEY_DETAILS,moSong.getSongDetails());
            values.put(KEY_DURATION, moSong.getDuration());
            values.put(KEY_ALBUM_ID, moSong.getAlbumId());
            values.put(KEY_GENRE_ID, moSong.getGenreId());
            values.put(KEY_ARTIST_ID, moSong.getArtistId());
            values.put(KEY_ARTIST_NAME,moSong.getArtist_name());
            values.put(KEY_MOD, moSong.getLastModTime());

            if (isExist(TABLE_SONG, moSong.getId())) {
                Log.d("Jewel", "updated song");
                return db.update(TABLE_SONG, values, KEY_ID + "=?", new String[]{moSong.getId() + ""});
            } else {
                Log.d("Jewel", "inserted song");
                return db.insert(TABLE_SONG, null, values);
            }
        } catch (Exception e) {

        }

        return 0;
    }

    public long addTopSong(MoSong moSong) {
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_ID, moSong.getId());
            values.put(KEY_IMG, moSong.getImgUrl());
            values.put(KEY_TITLE, moSong.getTitle());
            values.put(KEY_NAME, moSong.getFileName());
            values.put(KEY_DETAILS,moSong.getSongDetails());
            values.put(KEY_DURATION, moSong.getDuration());
            values.put(KEY_ALBUM_ID, moSong.getAlbumId());
            values.put(KEY_GENRE_ID, moSong.getGenreId());
            values.put(KEY_ARTIST_ID, moSong.getArtistId());
            values.put(KEY_ARTIST_NAME,moSong.getArtist_name());
            values.put(KEY_MOD, moSong.getLastModTime());

            if (isExist(TABLE_TOP_SONGS, moSong.getId())) {
                Log.d("Jewel", "updated song");
                return db.update(TABLE_TOP_SONGS, values, KEY_ID + "=?", new String[]{moSong.getId() + ""});
            } else {
                Log.d("Jewel", "inserted song");
                return db.insert(TABLE_TOP_SONGS, null, values);
            }
        } catch (Exception e) {

        }

        return 0;
    }

    public long addSongCount(MoSongCount moSong) {
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_SONG_ID, moSong.getSongId());
            values.put(KEY_ACTION, moSong.getAction());
            values.put(KEY_STATUS, moSong.getStatus());
            values.put(KEY_MOD, moSong.getLastMod());
            return db.insert(TABLE_SONG_COUNT, null, values);

        } catch (Exception e) {

        }

        return 0;
    }
    public long updateSongCount(MoSongCount moSong) {
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_SONG_ID, moSong.getSongId());
            values.put(KEY_STATUS, moSong.getStatus());


            return db.update(TABLE_SONG_COUNT, values, KEY_SONG_ID + "=?", new String[]{"" + moSong.getSongId()});


        } catch (Exception e) {

        }

        return 0;
    }

    public void updateSongsCountList(ArrayList<MoSongCount>list){
        for(MoSongCount songCount:list){
            updateSongCount(songCount);
        }
    }
    public  ArrayList<MoSongCount>getSongsCount(){

        ArrayList<MoSongCount> songsCount = new ArrayList<>();
        String sql = "select * from " + TABLE_SONG_COUNT +" where "+KEY_STATUS+"='0'";
        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()) {

                do {
                    MoSongCount song = new MoSongCount();
                    song.setSongId(getIntValue(KEY_SONG_ID));
                    song.setAction(getStringValue(KEY_ACTION));
                    song.setStatus(getIntValue(KEY_STATUS));
                    song.setLastMod(getStringValue(KEY_MOD));
                    songsCount.add(song);
                } while (cursor.moveToNext());

            }
        } catch (Exception e) {
            Log.d("Jewel", e.toString());
        }
        return songsCount;
    }

    public void addSongs(ArrayList<MoSong> songs) {
        for (MoSong song : songs) {
            addSong(song);
        }
    }

    public void addTopSongs(ArrayList<MoSong> songs) {
        for (MoSong song : songs) {
            addTopSong(song);
        }
    }



    public MoSong getSong(int id) {
        MoSong song = new MoSong();
        try {
            db = this.getReadableDatabase();
            String sql = "SELECT * FROM " + TABLE_SONG + " WHERE " + KEY_ID + "='" + id + "'";
            cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()) {
                song.setId(getIntValue(KEY_ID));
                song.setTitle(getStringValue(KEY_TITLE));
                song.setFileName(getStringValue(KEY_NAME));
                song.setSongDetails(getStringValue(KEY_DETAILS));
                song.setAlbumId(getStringValue(KEY_ALBUM_ID));
                song.setArtistId(getStringValue(KEY_ARTIST_ID));
                song.setArtist_name(getStringValue(KEY_ARTIST_NAME));
                song.setGenreId(getStringValue(KEY_GENRE_ID));
                song.setDuration(getStringValue(KEY_DURATION));
                song.setLastModTime(getStringValue(KEY_MOD));
                song.setImgUrl(getStringValue(KEY_IMG));
            }
        } catch (Exception e) {
            Log.d("Jewel", "db error : " + e);
        }
        return song;

    }



    public ArrayList<MoSong> getSongs(String sql) {
        ArrayList<MoSong> songs = new ArrayList<>();
        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()) {


                do {
                    MoSong song = new MoSong();
                    song.setId(getIntValue(KEY_ID));
                    song.setTitle(getStringValue(KEY_TITLE));
                    song.setFileName(getStringValue(KEY_NAME));
                    song.setSongDetails(getStringValue(KEY_DETAILS));
                    song.setAlbumId(getStringValue(KEY_ALBUM_ID));
                    song.setAlbum_name(getStringValue("album_name"));
                    song.setArtistId(getStringValue(KEY_ARTIST_ID));
                    song.setArtist_name(getStringValue(KEY_ARTIST_NAME));
                    song.setGenreId(getStringValue(KEY_GENRE_ID));
                    song.setDuration(getStringValue(KEY_DURATION));
                    song.setIsDownloaded(getIntValue(KEY_DOWNLOAD));
                    song.setLastModTime(getStringValue(KEY_MOD));
                    song.setImgUrl(getStringValue(KEY_IMG));
                    songs.add(song);

                } while (cursor.moveToNext());

            }
        } catch (Exception e) {
            Log.d("Jewel", "$$ " + e.toString());
        }
        return songs;
    }


    public ArrayList<MoSong> getSongs(int playlistId) {
        ArrayList<MoSong> songs = new ArrayList<>();
        String sql = "select a.* from " + TABLE_SONG + " as a inner join " + TABLE_PLAYLIST + " as b on a." + KEY_ID + "=b." + KEY_SONG_ID
                + " where b." + KEY_ID + "='" + playlistId + "'";
        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()) {

                do {
                    MoSong song = new MoSong();
                    song.setId(getIntValue(KEY_ID));
                    song.setTitle(getStringValue(KEY_TITLE));
                    song.setFileName(getStringValue(KEY_NAME));
                    song.setSongDetails(getStringValue(KEY_DETAILS));
                    song.setAlbumId(getStringValue(KEY_ALBUM_ID));
                    song.setAlbum_name(getStringValue("album_name"));
                    song.setArtistId(getStringValue(KEY_ARTIST_ID));
                    song.setArtist_name(getStringValue(KEY_ARTIST_NAME));
                    song.setGenreId(getStringValue(KEY_GENRE_ID));
                    song.setDuration(getStringValue(KEY_DURATION));
                    song.setIsDownloaded(getIntValue(KEY_DOWNLOAD));
                    song.setLastModTime(getStringValue(KEY_MOD));
                    song.setImgUrl(getStringValue(KEY_IMG));
                    songs.add(song);
                } while (cursor.moveToNext());

            }
        } catch (Exception e) {
            Log.d("Jewel", e.toString());
        }
        return songs;
    }

    public ArrayList<MoSong> getTopSongsByArtist(int id) {
        ArrayList<MoSong> songs = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_TOP_SONGS + " WHERE " + KEY_ARTIST_ID + "='" + id + "'";
        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    MoSong song = new MoSong();
                    song.setId(getIntValue(KEY_ID));
                    song.setTitle(getStringValue(KEY_TITLE));
                    song.setFileName(getStringValue(KEY_NAME));
                    song.setSongDetails(getStringValue(KEY_DETAILS));
                    song.setAlbumId(getStringValue(KEY_ALBUM_ID));
                    song.setAlbum_name(getStringValue("album_name"));
                    song.setArtistId(getStringValue(KEY_ARTIST_ID));
                    song.setArtist_name(getStringValue(KEY_ARTIST_NAME));
                    song.setGenreId(getStringValue(KEY_GENRE_ID));
                    song.setDuration(getStringValue(KEY_DURATION));
                    song.setIsDownloaded(getIntValue(KEY_DOWNLOAD));
                    song.setLastModTime(getStringValue(KEY_MOD));
                    song.setImgUrl(getStringValue(KEY_IMG));
                    songs.add(song);
                } while (cursor.moveToNext());

            }
        } catch (Exception e) {

        }
        return songs;
    }


    public ArrayList<MoSong> getSongsByAlbum(int id) {
        ArrayList<MoSong> songs = new ArrayList<>();
        String sql = "select * from " + TABLE_SONG + " where " + KEY_ALBUM_ID + "='" + id + "'";

        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()) {

                do {
                    MoSong song = new MoSong();
                    song.setId(getIntValue(KEY_ID));
                    song.setTitle(getStringValue(KEY_TITLE));
                    song.setFileName(getStringValue(KEY_NAME));
                    song.setSongDetails(getStringValue(KEY_DETAILS));
                    song.setAlbumId(getStringValue(KEY_ALBUM_ID));
                    song.setAlbum_name(getStringValue("album_name"));
                    song.setArtistId(getStringValue(KEY_ARTIST_ID));
                    song.setArtist_name(getStringValue(KEY_ARTIST_NAME));
                    song.setGenreId(getStringValue(KEY_GENRE_ID));
                    song.setDuration(getStringValue(KEY_DURATION));
                    song.setIsDownloaded(getIntValue(KEY_DOWNLOAD));
                    song.setLastModTime(getStringValue(KEY_MOD));
                    song.setImgUrl(getStringValue(KEY_IMG));
                    songs.add(song);

                } while (cursor.moveToNext());

            }
        } catch (Exception e) {
            Log.d("Jewel", e.toString());
        }
        return songs;
    }

    public ArrayList<MoSong> getSongsByArtist(int id) {
        ArrayList<MoSong> songs = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_SONG + " WHERE " + KEY_ARTIST_ID + "='" + id + "'";
        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    MoSong song = new MoSong();
                    song.setId(getIntValue(KEY_ID));
                    song.setTitle(getStringValue(KEY_TITLE));
                    song.setFileName(getStringValue(KEY_NAME));
                    song.setSongDetails(getStringValue(KEY_DETAILS));
                    song.setAlbumId(getStringValue(KEY_ALBUM_ID));
                    song.setAlbum_name(getStringValue("album_name"));
                    song.setArtistId(getStringValue(KEY_ARTIST_ID));
                    song.setArtist_name(getStringValue(KEY_ARTIST_NAME));
                    song.setGenreId(getStringValue(KEY_GENRE_ID));
                    song.setDuration(getStringValue(KEY_DURATION));
                    song.setIsDownloaded(getIntValue(KEY_DOWNLOAD));
                    song.setLastModTime(getStringValue(KEY_MOD));
                    song.setImgUrl(getStringValue(KEY_IMG));
                    songs.add(song);
                } while (cursor.moveToNext());

            }
        } catch (Exception e) {

        }
        return songs;
    }

    public ArrayList<MoSong> getSongsByGenre(int id) {
        ArrayList<MoSong> songs = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_SONG + " WHERE " + KEY_GENRE_ID + "='" + id + "'";
        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    MoSong song = new MoSong();
                    song.setId(getIntValue(KEY_ID));
                    song.setTitle(getStringValue(KEY_TITLE));
                    song.setFileName(getStringValue(KEY_NAME));
                    song.setSongDetails(getStringValue(KEY_DETAILS));
                    song.setAlbumId(getStringValue(KEY_ALBUM_ID));
                    song.setAlbum_name(getStringValue("album_name"));
                    song.setArtistId(getStringValue(KEY_ARTIST_ID));
                    song.setGenreId(getStringValue(KEY_GENRE_ID));
                    song.setDuration(getStringValue(KEY_DURATION));
                    song.setIsDownloaded(getIntValue(KEY_DOWNLOAD));
                    song.setLastModTime(getStringValue(KEY_MOD));
                    song.setImgUrl(getStringValue(KEY_IMG));
                    songs.add(song);
                } while (cursor.moveToNext());

            }
        } catch (Exception e) {

        }
        return songs;
    }

    public ArrayList<MoSong> getSongsByGenres(int id) {
        ArrayList<MoSong> songs = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_SONG + " WHERE " + KEY_GENRE_ID + "='" + id + "'";
        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    MoSong song = new MoSong();
                    song.setId(getIntValue(KEY_ID));
                    song.setTitle(getStringValue(KEY_TITLE));
                    song.setGenreId(getStringValue(KEY_GENRE_ID));
                    song.setImgUrl(getStringValue(KEY_IMG));
                    songs.add(song);
                } while (cursor.moveToNext());

            }
        } catch (Exception e) {

        }
        return songs;
    }


    // Album Section

    public long addAlbum(MoAlbum album) {
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_ID, album.getId());
            values.put(KEY_IMG, album.getImgUrl());
            values.put(KEY_TITLE, album.getTitle());
            values.put(KEY_ARTIST_ID, album.getArtistId());
            values.put(KEY_GENRE_ID, album.getGenreId());
            values.put(KEY_RELEASE_DATE, album.getReleaseDate());
            values.put(KEY_MOD, album.getLastModTime());

            if (isExist(TABLE_ALBUM, album.getId())) {
                return db.update(TABLE_ALBUM, values, KEY_ID + "=?", new String[]{album.getId() + ""});
            } else {
                return db.insert(TABLE_ALBUM, null, values);
            }
        } catch (Exception e) {

        }

        return 0;
    }

    public long addTopAlbum(MoAlbum album) {
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_ID, album.getId());
            values.put(KEY_IMG, album.getImgUrl());
            values.put(KEY_TITLE, album.getTitle());
            values.put(KEY_ARTIST_ID, album.getArtistId());
            values.put(KEY_GENRE_ID, album.getGenreId());
            values.put(KEY_RELEASE_DATE, album.getReleaseDate());
            values.put(KEY_MOD, album.getLastModTime());

            if (isExist(TABLE_TOP_ALBUMS, album.getId())) {
                return db.update(TABLE_TOP_ALBUMS, values, KEY_ID + "=?", new String[]{album.getId() + ""});
            } else {
                return db.insert(TABLE_TOP_ALBUMS, null, values);
            }
        } catch (Exception e) {

        }

        return 0;
    }

    public void addAlbums(ArrayList<MoAlbum> albums) {
        for (MoAlbum album : albums) {
            addAlbum(album);
        }
    }

    public void addTopAlbums(ArrayList<MoAlbum> albums) {
        for (MoAlbum album : albums) {
            addTopAlbum(album);
        }
    }

    public MoAlbum getAlbum(int id) {
        MoAlbum album = new MoAlbum();
        try {
            db = this.getReadableDatabase();
            String sql = "SELECT * FROM " + TABLE_ALBUM + " WHERE " + KEY_ID + "='" + id + "'";
            cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()) {
                album.setId(getIntValue(KEY_ID));
                album.setTitle(getStringValue(KEY_TITLE));
                album.setImgUrl(getStringValue(KEY_IMG));
                album.setArtistId(getIntValue(KEY_ARTIST_ID));
                album.setGenreId(getIntValue(KEY_GENRE_ID));
                album.setReleaseDate(getStringValue(KEY_RELEASE_DATE));
                album.setLastModTime(getStringValue(KEY_MOD));

            }
        } catch (Exception e) {
            Log.d("Jewel", "db error : " + e);
        }
        return album;

    }

    public ArrayList<MoAlbum> getAlbums(String sql) {
        ArrayList<MoAlbum> albums = new ArrayList<>();
        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    MoAlbum album = new MoAlbum();
                    album.setId(getIntValue(KEY_ID));
                    album.setTitle(getStringValue(KEY_TITLE));
                    album.setImgUrl(getStringValue(KEY_IMG));
                    album.setArtistId(getIntValue(KEY_ARTIST_ID));
                    album.setGenreId(getIntValue(KEY_GENRE_ID));
                    album.setReleaseDate(getStringValue(KEY_RELEASE_DATE));
                    album.setLastModTime(getStringValue(KEY_MOD));
                    albums.add(album);
                } while (cursor.moveToNext());

            }
        } catch (Exception e) {

        }
        return albums;
    }


    public ArrayList<MoAlbum> getTopAlbums(int id) {
        ArrayList<MoAlbum> albums = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_TOP_ALBUMS + " WHERE " + KEY_ARTIST_ID + "='" + id + "'";
        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    MoAlbum album = new MoAlbum();
                    album.setId(getIntValue(KEY_ID));
                    album.setTitle(getStringValue(KEY_TITLE));
                    album.setImgUrl(getStringValue(KEY_IMG));
                    album.setArtistId(getIntValue(KEY_ARTIST_ID));
                    album.setGenreId(getIntValue(KEY_GENRE_ID));
                    album.setReleaseDate(getStringValue(KEY_RELEASE_DATE));
                    album.setLastModTime(getStringValue(KEY_MOD));
                    albums.add(album);
                } while (cursor.moveToNext());

            }
        } catch (Exception e) {

        }
        return albums;
    }

    public ArrayList<MoAlbum> getAlbumsByArtist(int id) {
        ArrayList<MoAlbum> albums = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_ALBUM + " WHERE " + KEY_ARTIST_ID + "='" + id + "'";
        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    MoAlbum album = new MoAlbum();
                    album.setId(getIntValue(KEY_ID));
                    album.setTitle(getStringValue(KEY_TITLE));
                    album.setImgUrl(getStringValue(KEY_IMG));
                    album.setArtistId(getIntValue(KEY_ARTIST_ID));
                    album.setGenreId(getIntValue(KEY_GENRE_ID));
                    album.setReleaseDate(getStringValue(KEY_RELEASE_DATE));
                    album.setLastModTime(getStringValue(KEY_MOD));
                    albums.add(album);
                } while (cursor.moveToNext());

            }
        } catch (Exception e) {

        }
        return albums;
    }

    public ArrayList<MoAlbum> getAlbumsByGenre(int id) {
        ArrayList<MoAlbum> albums = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_ALBUM + " WHERE " + KEY_GENRE_ID + "='" + id + "'";
        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    MoAlbum album = new MoAlbum();
                    album.setId(getIntValue(KEY_ID));
                    album.setTitle(getStringValue(KEY_TITLE));
                    album.setImgUrl(getStringValue(KEY_IMG));
                    album.setArtistId(getIntValue(KEY_ARTIST_ID));
                    album.setGenreId(getIntValue(KEY_GENRE_ID));
                    album.setReleaseDate(getStringValue(KEY_RELEASE_DATE));
                    album.setLastModTime(getStringValue(KEY_MOD));
                    albums.add(album);
                } while (cursor.moveToNext());

            }
        } catch (Exception e) {

        }
        return albums;
    }

    public ArrayList<MoAlbum> getNewAlbums(int day) {
        ArrayList<MoAlbum> albums = new ArrayList<>();
        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery("select * from " + TABLE_ALBUM + " where " + KEY_RELEASE_DATE + "> date('now','-" + day + " day')", null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    MoAlbum album = new MoAlbum();
                    album.setId(getIntValue(KEY_ID));
                    album.setTitle(getStringValue(KEY_TITLE));
                    album.setImgUrl(getStringValue(KEY_IMG));
                    album.setReleaseDate(getStringValue(KEY_RELEASE_DATE));
                    album.setLastModTime(getStringValue(KEY_MOD));
                    albums.add(album);
                } while (cursor.moveToNext());

            }
        } catch (Exception e) {

        }
        return albums;
    }

    // genre section
    public long addGenre(MoGenres genre) {
        try {
            db = this.getWritableDatabase();
            ContentValues gen_values = new ContentValues();
            gen_values.put(KEY_ID, genre.getId());
            gen_values.put(KEY_IMG, genre.getImgUrl());
            gen_values.put(KEY_TITLE, genre.getTitle());
            gen_values.put(KEY_MOD, genre.getLastModTime());

            if (isExist(TABLE_GENRE, genre.getId())) {
                return db.update(TABLE_GENRE, gen_values, KEY_ID + "=?", new String[]{genre.getId() + ""});
            } else {
                return db.insert(TABLE_GENRE, null, gen_values);
            }
        } catch (Exception e) {

        }

        return 0;
    }

    public void addGenres(ArrayList<MoGenres> genres) {
        for (MoGenres genre : genres) {
            addGenre(genre);
        }
    }

    public MoGenres getGenre(int id) {
        MoGenres genre = new MoGenres();
        try {
            db = this.getReadableDatabase();
            String sql = "SELECT * FROM " + TABLE_GENRE + " WHERE " + KEY_ID + "='" + id + "'";
            cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()) {
                genre.setId(getIntValue(KEY_ID));
                genre.setTitle(getStringValue(KEY_TITLE));
                genre.setImgUrl(getStringValue(KEY_IMG));
                genre.setLastModTime(getStringValue(KEY_MOD));
            }
        } catch (Exception e) {
            Log.d("Jewel", "db error : " + e);
        }
        return genre;

    }

    public ArrayList<MoGenres> getGenres(String sql) {
        ArrayList<MoGenres> genres = new ArrayList<>();
        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    MoGenres genre = new MoGenres();
                    genre.setId(getIntValue(KEY_ID));
                    genre.setTitle(getStringValue(KEY_TITLE));
                    genre.setImgUrl(getStringValue(KEY_IMG));
                    genre.setLastModTime(getStringValue(KEY_MOD));
                    genres.add(genre);
                } while (cursor.moveToNext());

            }
        } catch (Exception e) {

        }
        return genres;
    }


    // artist section
    public long addArtist(MoArtist artist) {
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_ID, artist.getId());
            values.put(KEY_IMG, artist.getImge());
            values.put(KEY_NAME, artist.getName());
            values.put(KEY_TAG, artist.getTag());
            values.put(KEY_MOD, artist.getMod());

            if (isExist(TABLE_ARTIST, artist.getId())) {
                return db.update(TABLE_ARTIST, values, KEY_ID + "=?", new String[]{artist.getId() + ""});
            } else {
                return db.insert(TABLE_ARTIST, null, values);
            }
        } catch (Exception e) {

        }

        return 0;
    }

    public long addTopArtist(MoArtist artist) {
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_ID, artist.getId());
            values.put(KEY_IMG, artist.getImge());
            values.put(KEY_NAME, artist.getName());
            values.put(KEY_TAG, artist.getTag());
            values.put(KEY_MOD, artist.getMod());

            if (isExist(TABLE_TOP_ARTIST, artist.getId())) {
                return db.update(TABLE_TOP_ARTIST, values, KEY_ID + "=?", new String[]{artist.getId() + ""});
            } else {
                return db.insert(TABLE_TOP_ARTIST, null, values);
            }
        } catch (Exception e) {

        }

        return 0;
    }

    public long addTrendArtist(MoArtist artist) {
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_ID, artist.getId());
            values.put(KEY_IMG, artist.getImge());
            values.put(KEY_NAME, artist.getName());
            values.put(KEY_TAG, artist.getTag());
            values.put(KEY_MOD, artist.getMod());

            if (isExist(TABLE_TRENDING_ARTIST, artist.getId())) {
                return db.update(TABLE_TRENDING_ARTIST, values, KEY_ID + "=?", new String[]{artist.getId() + ""});
            } else {
                return db.insert(TABLE_TRENDING_ARTIST, null, values);
            }
        } catch (Exception e) {

        }

        return 0;
    }

    public void addArtists(ArrayList<MoArtist> artists) {
        for (MoArtist artist : artists) {
            addArtist(artist);
        }
    }

    public void addTopArtists(ArrayList<MoArtist> artists) {
        for (MoArtist artist : artists) {
            addTopArtist(artist);
        }
    }

    public void addTrendArtists(ArrayList<MoArtist> artists) {
        for (MoArtist artist : artists) {
            addTrendArtist(artist);
        }
    }

    public MoArtist getArtist(int id) {
        MoArtist artist = new MoArtist();
        try {
            db = this.getReadableDatabase();
            String sql = "SELECT * FROM " + TABLE_ARTIST + " WHERE " + KEY_ID + "='" + id + "'";
            cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()) {
                artist.setId(getIntValue(KEY_ID));
                artist.setName(getStringValue(KEY_NAME));
                artist.setImage(getStringValue(KEY_IMG));
                artist.setTag(getStringValue(KEY_TAG));
                artist.setMod(getStringValue(KEY_MOD));
                Log.d("DB", "foundArtist " + artist.getId() + ":" + artist.getName());
            }
        } catch (Exception e) {
            Log.d("Jewel", "db error : " + e);
        }
        return artist;

    }

    public ArrayList<MoArtist> getArtsts(String sql) {
        ArrayList<MoArtist> artists = new ArrayList<>();
        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    MoArtist artist = new MoArtist();
                    artist.setId(getIntValue(KEY_ID));
                    artist.setName(getStringValue(KEY_NAME));
                    artist.setImage(getStringValue(KEY_IMG));
                    artist.setTag(getStringValue(KEY_TAG));
                    artist.setMod(getStringValue(KEY_MOD));
                    artists.add(artist);
                } while (cursor.moveToNext());

            }
        } catch (Exception e) {

        }
        return artists;
    }


    public ArrayList<MoArtist> getTopArtists(String sql) {
        ArrayList<MoArtist> artists = new ArrayList<>();
        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    MoArtist artist = new MoArtist();
                    artist.setId(getIntValue(KEY_ID));
                    artist.setName(getStringValue(KEY_NAME));
                    artist.setImage(getStringValue(KEY_IMG));
                    artist.setTag(getStringValue(KEY_TAG));
                    artist.setMod(getStringValue(KEY_MOD));
                    artists.add(artist);
                } while (cursor.moveToNext());

            }
        } catch (Exception e) {

        }
        return artists;
    }


    public ArrayList<MoArtist> getTrendArtists(String sql) {
        ArrayList<MoArtist> artists = new ArrayList<>();
        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    MoArtist artist = new MoArtist();
                    artist.setId(getIntValue(KEY_ID));
                    artist.setName(getStringValue(KEY_NAME));
                    artist.setImage(getStringValue(KEY_IMG));
                    artist.setTag(getStringValue(KEY_TAG));
                    artist.setMod(getStringValue(KEY_MOD));
                    artists.add(artist);
                } while (cursor.moveToNext());

            }
        } catch (Exception e) {

        }
        return artists;
    }


// my playlist section

    public long createPlaylist(String name) {
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_NAME, name);
            return db.insert(TABLE_PLAYLIST_NAME, null, values);


        } catch (Exception e) {

        }

        return 0;
    }

    public long addToPlaylist(MoPlayList playlist) {
        try {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_ID, playlist.getId());
            values.put(KEY_SONG_ID, playlist.getSongId());


            if (isExist("SELECT * FROM " + TABLE_PLAYLIST + " WHERE " + KEY_ID + "='" + playlist.getId() + "' AND " + KEY_SONG_ID + "='" + playlist.getSongId() + "'")) {

                return db.update(TABLE_PLAYLIST, values, KEY_SONG_ID + "=? AND " + KEY_ID + "=?", new String[]{playlist.getSongId() + "," + playlist.getId()});
            } else {

                return db.insert(TABLE_PLAYLIST, null, values);
            }
        } catch (Exception e) {

        }

        return 0;
    }

    public void addPlayLists(ArrayList<MoPlayList> lists) {
        for (MoPlayList list : lists) {
            addToPlaylist(list);
        }
    }

    public ArrayList<MoSong> getPlayListSongs(int playlistId) {
        ArrayList<MoSong> list = new ArrayList<>();
        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery("select a.* from " + TABLE_SONG + " as a INNER JOIN " + TABLE_PLAYLIST + " as b ON a." + KEY_ID + "=b." + KEY_SONG_ID, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    MoSong song = new MoSong();
                    song.setId(getIntValue(KEY_ID));
                    song.setTitle(getStringValue(KEY_TITLE));
                    song.setFileName(getStringValue(KEY_NAME));
                    song.setAlbumId(getStringValue(KEY_ALBUM_ID));
                    //song.setAlbum_name(getStringValue("album_name"));
                    song.setArtistId(getStringValue(KEY_ARTIST_ID));
                    song.setDuration(getStringValue(KEY_DURATION));
                    song.setIsDownloaded(getIntValue(KEY_DOWNLOAD));
                    song.setLastModTime(getStringValue(KEY_MOD));
                    song.setImgUrl(getStringValue(KEY_IMG));
                    list.add(song);
                } while (cursor.moveToNext());

            }
        } catch (Exception e) {

        }
        return list;
    }

    public ArrayList<MoPlayList> getPlayListName() {
        ArrayList<MoPlayList> list = new ArrayList<>();
        try {
            db = this.getReadableDatabase();
            cursor = db.rawQuery("select * from " + TABLE_PLAYLIST_NAME, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    MoPlayList playList = new MoPlayList();
                    playList.setId(getIntValue(KEY_ID));
                    playList.setName(getStringValue(KEY_NAME));
                    playList.setModTime(getStringValue(KEY_MOD));
                    list.add(playList);
                } while (cursor.moveToNext());

            }
        } catch (Exception e) {

        }
        return list;
    }

    public long removeFromPlaylist(int songId, int type) {
        try {
            db = this.getWritableDatabase();
            if (isExist("SELECT * FROM " + TABLE_PLAYLIST + " WHERE " + KEY_ID + "='" + type + "' AND " + KEY_SONG_ID + "='" + songId + "'")) {
                return db.delete(TABLE_PLAYLIST, KEY_ID + "=? AND " + KEY_SONG_ID + "=?", new String[]{type + "", songId + ""});
            }
        } catch (Exception e) {

        }
        return 0;
    }

    public long removePlayList(int playListId) {
        try {
            db = this.getWritableDatabase();
            if (isExist(TABLE_PLAYLIST, playListId)) {
                return db.delete(TABLE_PLAYLIST, KEY_ID + "=?", new String[]{playListId + ""});
            }
        } catch (Exception e) {

        }
        return 0;
    }

//download section

    public long addDownloadSong(int songId, String location) {
        try {
            if (!isExist(TABLE_DOWNLOAD, KEY_SONG_ID, songId)) {
                ContentValues values = new ContentValues();
                values.put(KEY_SONG_ID, songId);
                values.put(KEY_NAME, location);
                values.put(KEY_DOWNLOAD, "1");
                return db.insert(TABLE_DOWNLOAD, null, values);
            }
        } catch (Exception e) {

        }

        return 0;
    }

    public long removeDownload(int songId) {
        try {
            db = this.getWritableDatabase();
            return db.delete(TABLE_DOWNLOAD, KEY_SONG_ID + "=?", new String[]{songId + ""});
        } catch (Exception e) {
            Log.d("Jewel", e.toString());
        }
        return -1;
    }

}
