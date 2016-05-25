package baajna.scroll.owner.mobioapp.interfaces;

import android.media.MediaPlayer;

import baajna.scroll.owner.mobioapp.datamodel.MoSong;

/**
 * Created by Jewel on 1/28/2016.
 */
public interface IMusic {
    void onUpdate(MediaPlayer mediaPlayer,MoSong song);
}
