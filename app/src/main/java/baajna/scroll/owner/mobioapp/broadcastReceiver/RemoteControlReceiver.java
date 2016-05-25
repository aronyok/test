package baajna.scroll.owner.mobioapp.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by rohan on 9/16/15.
 * Updated by Jewel on 12/30/15
 */
public class RemoteControlReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {


        /*String action = intent.getAction();

        if (action.equals(Globals.ACTION_PLAYER_PLAY)) {
            // do your stuff to play action;

            if (MusicService.player.isPlaying()) {
                MusicService.player.pause();
                MusicService.notificationView.setImageViewResource(R.id.not_play, R.drawable.ico_play);

            } else if (!MusicService.player.isPlaying()) {

                MusicService.player.getCurrentPosition();
                MusicService.player.start();
                MusicService.notificationView.setImageViewResource(R.id.not_play, R.drawable.ico_pause);
            }
            MusicService.builder.setContent(MusicService.notificationView);
            MusicService.mNotificationManager.notify(1, MusicService.builder.build());


        } else if (action.equals(Globals.ACTION_PLAYER_EXIT)) {
            MusicService.remoteStop();
        }else if(action.equals(Globals.ACTION_PLAYER_NEXT)){
            MusicService.playNext();
        }else if(action.equals(Globals.ACTION_PLAYER_PREV)){
            MusicService.playPrev();
        }*/

    }
}
