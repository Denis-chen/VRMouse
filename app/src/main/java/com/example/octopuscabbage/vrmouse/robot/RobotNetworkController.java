package com.example.octopuscabbage.vrmouse.robot;

import com.example.octopuscabbage.vrmouse.CardboardToaster;
import com.example.octopuscabbage.vrmouse.Toaster;
import com.example.octopuscabbage.vrmouse.rendering.Renderer;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by octopuscabbage on 1/24/16.
 */
public class RobotNetworkController implements Renderer.RotationListener, RobotNetworkClient{
    private String networkURL;
    private static final String turnUrl = "turn/";
    private static final int partOfQuaternionWeCareAbout = 1;

    public RobotNetworkController(String networkURL) {
        this.networkURL = networkURL;
    }

    @Override
    public void sendTurnDirection(int angle) {
        try {
            URL url = new URL(networkURL + turnUrl + angle);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.getResponseCode(); //Actually causes request execution
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void onMovement(float[] quaternionDeltas) {
        sendTurnDirection(Math.round(quaternionDeltas[partOfQuaternionWeCareAbout]));
    }
}
