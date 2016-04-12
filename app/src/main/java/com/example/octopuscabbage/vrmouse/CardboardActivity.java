/*
 * Copyright 2014 Google Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.octopuscabbage.vrmouse;

import android.content.Context;
import android.os.Bundle;

import com.example.octopuscabbage.vrmouse.rendering.Renderer;
import com.example.octopuscabbage.vrmouse.robot.RobotController;
import com.google.vrtoolkit.cardboard.*;

public class CardboardActivity extends com.google.vrtoolkit.cardboard.CardboardActivity {

    private static final String TAG = "CardboardActivity";

    private CardboardView cardboardView;
    private CardboardToaster cardboardToaster;
    private static Context context;
    private static Renderer renderer;
    private static RobotController controller;

    public static Context getContext(){
        return context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CardboardActivity.context = getApplicationContext(); //Probably dangerous

        controller = new RobotController();
        renderer = new Renderer(getContext());
        cardboardToaster = (CardboardToaster) findViewById(R.id.overlay);
        renderer.setToast(cardboardToaster);
        renderer.setOnTurnListener(controller);

        setContentView(R.layout.activity_main);
        cardboardView = (CardboardView) findViewById(R.id.cardboard_view);
        cardboardView.setRenderer(renderer);
        setCardboardView(cardboardView);

    }

    @Override
    public void onCardboardTrigger(){
        controller.toggleMovingForward();
    }

    @Override
    protected void onPause() {
        super.onPause();
        renderer.pauseStreams();
    }
}