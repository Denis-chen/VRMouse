package com.example.octopuscabbage.vrmouse;

import android.graphics.SurfaceTexture;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import com.google.vrtoolkit.cardboard.CardboardView;
import com.google.vrtoolkit.cardboard.Eye;
import com.google.vrtoolkit.cardboard.HeadTransform;
import com.google.vrtoolkit.cardboard.Viewport;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by octopuscabbage on 1/24/16.
 */
public class Renderer implements CardboardView.StereoRenderer, SurfaceTexture.OnFrameAvailableListener{
    private static final String TAG = "Render";
    private static final int GL_TEXTURE_EXTERNAL_OES = 0x8D65; //I have no idea what this does
    private float[] quaternion;
    private SurfaceTexture left;
    private SurfaceTexture right;
    private PlayStream leftStream;
    private PlayStream rightStream;
    private RotationListener rotationListener;
    private Toaster toaster;
    private int[] textures;

    private float[] mView;
    private float[] mCamera;

    private final String vertexShaderCode =
            "attribute vec4 position;" +
                    "attribute vec2 inputTextureCoordinate;" +
                    "varying vec2 textureCoordinate;" +
                    "void main()" +
                    "{"+
                    "gl_Position = position;"+
                    "textureCoordinate = inputTextureCoordinate;" +
                    "}";

    private final String fragmentShaderCode =
            "#extension GL_OES_EGL_image_external : require\n"+
                    "precision mediump float;" +
                    "varying vec2 textureCoordinate;                            \n" +
                    "uniform samplerExternalOES s_texture;               \n" +
                    "void main(void) {" +
                    "  gl_FragColor = texture2D( s_texture, textureCoordinate );\n" +
                    "}";
    static final int COORDS_PER_VERTEX = 2;
    static float squareVertices[] = { // in counterclockwise order:
            -1.0f, -1.0f,   // 0.left - mid
            1.0f, -1.0f,   // 1. right - mid
            -1.0f, 1.0f,   // 2. left - top
            1.0f, 1.0f,   // 3. right - top
    };

    private ShortBuffer drawListBuffer, buf2;
    private int mProgram;
    private int mPositionHandle, mPositionHandle2;
    private int mColorHandle;
    private int mTextureCoordHandle;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    private short drawOrder[] =  {0, 2, 1, 1, 2, 3 }; // order to draw vertices
    static float textureVertices[] = {
            0.0f, 1.0f,  // A. left-bottom
            1.0f, 1.0f,  // B. right-bottom
            0.0f, 0.0f,  // C. left-top
            1.0f, 0.0f   // D. right-top
    };

    private FloatBuffer vertexBuffer, textureVerticesBuffer, vertexBuffer2;

    public Renderer(){
        mCamera = new float[16];
        mView = new float[16];
        quaternion = new float[4];
    }

    public interface RotationListener{ //Wish android supported regular function callbacks
        /*
        gives you the deltas from where you were looking last frame to where you are looking now;
         */
        void onMovement(float[] quaternionDeltas);
    }
    public void setOnTurnListener(RotationListener rotationListener){
        this.rotationListener = rotationListener;
    }

    public void setToast(Toaster toast){
        this.toaster = toast;
    }

    private float[] computeQuaternionDifference(float[] oldQuaternion, float[] newQuaternion){
       float[] outQuat = new float[4];
        for (int i = 0; i < 4; i++) {
            outQuat[i] = newQuaternion[i] - oldQuaternion[i];
        }
        return outQuat;
    }
    @Override
    public void onNewFrame(HeadTransform headTransform) {
        float[] oldQuaternion = quaternion.clone();
        headTransform.getQuaternion(quaternion, 0);
        if (rotationListener != null) {
           rotationListener.onMovement(computeQuaternionDifference(oldQuaternion,quaternion));
        }
        //I think Y is the major angle we care about?
        Log.i("renderer","X: " + quaternion[0] + " Y: " + quaternion[1] + " Z: " + quaternion[2] + " W: " + quaternion[3]);
        float[] mtx = new float[16];
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        left.updateTexImage();
        left.getTransformMatrix(mtx);
        right.updateTexImage();
        right.getTransformMatrix(mtx);
    }

    @Override
    public void onDrawEye(Eye eye) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        GLES20.glUseProgram(mProgram);

        int type = eye.getType();
        if(type == Eye.Type.LEFT){
            GLES20.glActiveTexture(GL_TEXTURE_EXTERNAL_OES);
            GLES20.glBindTexture(GL_TEXTURE_EXTERNAL_OES, textures[0]);
        }
        else if(type == Eye.Type.RIGHT){
            GLES20.glActiveTexture(GL_TEXTURE_EXTERNAL_OES);
            GLES20.glBindTexture(GL_TEXTURE_EXTERNAL_OES, textures[1]);
        } else {
                Log.e(TAG,"Somethings gone wrong" + type);
        }



        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "position");
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT,
                false,vertexStride, vertexBuffer);


        mTextureCoordHandle = GLES20.glGetAttribLocation(mProgram, "inputTextureCoordinate");
        GLES20.glEnableVertexAttribArray(mTextureCoordHandle);
        GLES20.glVertexAttribPointer(mTextureCoordHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT,
                false,vertexStride, textureVerticesBuffer);

        mColorHandle = GLES20.glGetAttribLocation(mProgram, "s_texture");



        GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);


        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTextureCoordHandle);

        Matrix.multiplyMM(mView, 0, eye.getEyeView(), 0, mCamera, 0);



    }

    @Override
    public void onFinishFrame(Viewport viewport) {

    }

    @Override
    public void onSurfaceChanged(int i, int i1) {

    }


    /**
     * Create textures array
     * @return textures array, 0 is left, 1 is right
     */
    private static int[] createTextures()
    {
        int[] texture = new int[2];

        GLES20.glGenTextures(2, texture,0);
        GLES20.glBindTexture(GL_TEXTURE_EXTERNAL_OES, texture[0]);
        GLES20.glBindTexture(GL_TEXTURE_EXTERNAL_OES, texture[1]);
        GLES20.glTexParameterf(GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameterf(GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameteri(GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
        return texture;
    }

    private int loadGLShader(int type, String code) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, code);
        GLES20.glCompileShader(shader);

        // Get the compilation status.
        final int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

        // If the compilation failed, delete the shader.
        if (compileStatus[0] == 0) {
            Log.e(TAG, "Error compiling shader: " + GLES20.glGetShaderInfoLog(shader));
            GLES20.glDeleteShader(shader);
            shader = 0;
        }

        if (shader == 0) {
            throw new RuntimeException("Error creating shader.");
        }

        return shader;
    }

    @Override
    public void onSurfaceCreated(EGLConfig eglConfig) {
        GLES20.glClearColor(0.1f, 0.1f, 0.1f, 0.5f); // Dark background so text shows up well

        ByteBuffer bb = ByteBuffer.allocateDirect(squareVertices.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareVertices);
        vertexBuffer.position(0);


        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);


        ByteBuffer bb2 = ByteBuffer.allocateDirect(textureVertices.length * 4);
        bb2.order(ByteOrder.nativeOrder());
        textureVerticesBuffer = bb2.asFloatBuffer();
        textureVerticesBuffer.put(textureVertices);
        textureVerticesBuffer.position(0);

        int vertexShader = loadGLShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadGLShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL ES Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);

        textures = createTextures();
        leftStream = new PlayStream("rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov", textures[0], this);
        leftStream.setToaster(toaster);
        leftStream.start();
        left = leftStream.getSurfaceTexture();
        rightStream = new PlayStream("rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov", textures[1], this);
        rightStream.setToaster(toaster);
        rightStream.start();
        right = rightStream.getSurfaceTexture();
    }

    @Override
    public void onRendererShutdown() {

    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {

    }

}
