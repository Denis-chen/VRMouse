package com.example.octopuscabbage.vrmouse.settings;

/**
 * Created by octopuscabbage on 5/8/16.
 */
public class RTSPStreamLocationValidator {
    public static boolean isValid(String stream){
        return !(stream.isEmpty() || stream.substring(0, 4).equals("rtsp"));
    }
}
