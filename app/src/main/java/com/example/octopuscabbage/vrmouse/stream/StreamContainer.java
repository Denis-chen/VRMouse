package com.example.octopuscabbage.vrmouse.stream;

import android.content.Context;
import android.graphics.SurfaceTexture;
import com.example.octopuscabbage.vrmouse.Toaster;
import com.example.octopuscabbage.vrmouse.settings.SettingsStorage;

/**
 * Created by octopuscabbage on 5/8/16.
 */
public class StreamContainer {
    private SurfaceTexture left;
    private SurfaceTexture right;
    private PlayStream leftStream;
    private PlayStream rightStream;

    public StreamContainer(Context context, Toaster toaster, int[] textures, SurfaceTexture.OnFrameAvailableListener listener){
        SettingsStorage settingsStorage = new SettingsStorage(context);
        leftStream = new PlayStream(settingsStorage.readLeftStreamLocation(), textures[0], listener);
        leftStream.setToaster(toaster);
        leftStream.start();
        left = leftStream.getSurfaceTexture();
        //Test Stream:
        //rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov
        rightStream = new PlayStream(settingsStorage.readRightStreamLocation(), textures[1], listener);
        rightStream.setToaster(toaster);
        rightStream.start();
        right = rightStream.getSurfaceTexture();
    }
    public void pauseStreams(){
        applyToBothStreams(new StreamApply() {
            @Override
            public void applyToStream(PlayStream stream) {
                stream.pause();
            }
        });
    }

    public void onUpdate(float[] mtx){
        left.updateTexImage();
        left.getTransformMatrix(mtx);
        right.updateTexImage();
        right.getTransformMatrix(mtx);
    }

    /*
    Used for applying an operation to a stream.
    I miss Java 8
     */
    public interface StreamApply {
        void applyToStream(PlayStream stream);
    }
    /*
    Apply an operation to the left and right stream;
     */
    public void applyToBothStreams(StreamApply apply){
        apply.applyToStream(leftStream);
        apply.applyToStream(rightStream);
    }

}
