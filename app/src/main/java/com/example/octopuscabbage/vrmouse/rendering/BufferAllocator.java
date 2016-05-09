package com.example.octopuscabbage.vrmouse.rendering;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by octopuscabbage on 5/8/16.
 */
public class BufferAllocator {

   public static FloatBuffer allocateFloatBuffer(FloatBuffer curbuffer, float[] content, int length ){
        ByteBuffer bb = ByteBuffer.allocateDirect(length);
        bb.order(ByteOrder.nativeOrder());
        curbuffer = bb.asFloatBuffer();
        curbuffer.put(content);
        curbuffer.position(0);
        return curbuffer;
   }

   public static ShortBuffer allocateShortBuffer(ShortBuffer curbuffer, short[] content, int length ){
        ByteBuffer bb = ByteBuffer.allocateDirect(length);
        bb.order(ByteOrder.nativeOrder());
        curbuffer = bb.asShortBuffer();
        curbuffer.put(content);
        curbuffer.position(0);
        return curbuffer;
   }

}
