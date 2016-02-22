package com.example.octopuscabbage.vrmouse.rendering;

/**
 * Created by octopuscabbage on 1/28/16.
 */
public class RenderConstants {
    public static final String vertextShaderCode = "attribute vec4 position;" +
            "attribute vec2 inputTextureCoordinate;" +
            "varying vec2 textureCoordinate;" +
            "void main()" +
            "{"+
            "gl_Position = position;"+
            "textureCoordinate = inputTextureCoordinate;" +
            "}";
    public static final String fragmentShaderCode =
        "#extension GL_OES_EGL_image_external : require\n"+
        "precision mediump float;" +
        "varying vec2 textureCoordinate;                            \n" +
        "uniform samplerExternalOES s_texture;               \n" +
        "void main(void) {" +
        "  gl_FragColor = texture2D( s_texture, textureCoordinate );\n" +
        "}";

    public static final int COORDS_PER_VERTEX = 2;
    public static float squareVertices[] = { // in counterclockwise order:
            -1.0f, -1.0f,   // 0.left - mid
            1.0f, -1.0f,   // 1. right - mid
            -1.0f, 1.0f,   // 2. left - top
            1.0f, 1.0f,   // 3. right - top
    };

    public static final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
    public static short drawOrder[] =  {0, 2, 1, 1, 2, 3 }; // order to draw vertices
    public static float textureVertices[] = {
            0.0f, 1.0f,  // A. left-bottom
            1.0f, 1.0f,  // B. right-bottom
            0.0f, 0.0f,  // C. left-top
            1.0f, 0.0f   // D. right-top
    };

}
