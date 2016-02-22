package com.example.octopuscabbage.vrmouse.rendering;

import android.graphics.SurfaceTexture;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.Surface;

import com.example.octopuscabbage.vrmouse.MainActivity;
import com.example.octopuscabbage.vrmouse.Toaster;

import java.io.IOException;

/**
 * Created by octopuscabbage on 1/24/16.
 */
public class PlayStream {
    private static final String TAG = "PlayStream";
    private Uri uri;
    private MediaPlayer mediaPlayer;
    private SurfaceTexture surfaceTexture;
    private Surface surface;
    private Toaster toaster;
    private boolean hasPrepared;
    int texture;
    SurfaceTexture.OnFrameAvailableListener listener;

    public PlayStream(String uri, int texture, SurfaceTexture.OnFrameAvailableListener listener){
        this.uri = Uri.parse(uri);
        this.texture = texture;
        this.listener = listener;
        this.surfaceTexture = new SurfaceTexture(texture);
    }

    public SurfaceTexture getSurfaceTexture(){
        return surfaceTexture;
    }

    public void setToaster(Toaster toaster){
        this.toaster = toaster;
    }

    public void pause(){
        mediaPlayer.pause();
    }

    public void start(){
        if(hasPrepared){
            return;
        }
        this.surfaceTexture.setOnFrameAvailableListener(listener);
        surface = new Surface(surfaceTexture);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setSurface(surface);
        try
        {
            mediaPlayer.setDataSource(MainActivity.getContext(), uri);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    //toaster.makeToast("Buffering: " + percent);
                }
            });
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    mp.setLooping(true);
                }
            });
            mediaPlayer.setVolume(0,1);
        }
        catch (IOException ioe)
        {
            toaster.makeToast("Cam launch failed");
            Log.w(TAG,"CAM LAUNCH FAILED");
        }
        hasPrepared = true;
    }

}

