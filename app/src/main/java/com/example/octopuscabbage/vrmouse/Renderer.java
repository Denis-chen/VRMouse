package com.example.octopuscabbage.vrmouse;

import android.graphics.Canvas;
import android.graphics.SurfaceTexture;
import android.util.Log;
import android.view.TextureView;

import com.google.vrtoolkit.cardboard.CardboardView;
import com.google.vrtoolkit.cardboard.Eye;
import com.google.vrtoolkit.cardboard.HeadTransform;
import com.google.vrtoolkit.cardboard.Viewport;

import javax.microedition.khronos.egl.EGLConfig;

/**
 * Created by octopuscabbage on 1/24/16.
 */
public class Renderer implements CardboardView.StereoRenderer, TextureView.SurfaceTextureListener{
    private float[] quaternion;
    private RobotController controller;
    public Renderer(RobotController controller){
        this.controller = controller;
        quaternion = new float[4];
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        //set texture surface here
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override
    public void onNewFrame(HeadTransform headTransform) {
        headTransform.getQuaternion(quaternion, 0);
        //I think Y is the major angle we care about?
        Log.i("renderer","X: " + quaternion[0] + " Y: " + quaternion[1] + " Z: " + quaternion[2] + " W: " + quaternion[3]);
    }

    @Override
    public void onDrawEye(Eye eye) {
        TextureView view = new TextureView(this);
        Canvas canvas = new Canvas();

    }

    @Override
    public void onFinishFrame(Viewport viewport) {

    }

    @Override
    public void onSurfaceChanged(int i, int i1) {

    }

    @Override
    public void onSurfaceCreated(EGLConfig eglConfig) {

    }

    @Override
    public void onRendererShutdown() {

    }
}
