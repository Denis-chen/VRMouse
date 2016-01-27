package com.example.octopuscabbage.vrmouse;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.vrtoolkit.cardboard.CardboardActivity;
import com.google.vrtoolkit.cardboard.CardboardView;

public class MainActivity extends CardboardActivity{
    private Renderer renderer;
    private RobotController robotController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        robotController = new RobotController();
        renderer = new Renderer(robotController);
        CardboardView cardboardView = (CardboardView) findViewById(R.id.cardboard_view);
        cardboardView.setRestoreGLStateEnabled(false);
        cardboardView.setRenderer(renderer);
    }

    @Override
    public void onCardboardTrigger() {
        super.onCardboardTrigger();
        robotController.toggleMovingForward();
    }
}
