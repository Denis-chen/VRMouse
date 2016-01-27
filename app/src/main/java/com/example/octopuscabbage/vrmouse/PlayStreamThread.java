package com.example.octopuscabbage.vrmouse;

import android.media.MediaPlayer;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by octopuscabbage on 1/24/16.
 */
public class PlayStreamThread extends Thread{
    private String uri;
    private SurfaceView surface;
    private MediaPlayer mediaPlayer;
    public PlayStreamThread(String uri, SurfaceView surface){
        mediaPlayer = new MediaPlayer();
        this.surface = surface;
    }

    @Override
    public void run() {
        try {
            mediaPlayer.setDataSource(uri);
            mediaPlayer.setDisplay(surface.getHolder());
            mediaPlayer.start();
        } catch (IOException e) {
            Log.e("playstream", "Exception occured", e);
        }
    }
}
