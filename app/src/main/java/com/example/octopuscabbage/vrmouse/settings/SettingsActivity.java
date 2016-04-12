package com.example.octopuscabbage.vrmouse.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.octopuscabbage.vrmouse.CardboardActivity;
import com.example.octopuscabbage.vrmouse.R;

/**
 * Created by octopuscabbage on 3/20/16.
 */
public class SettingsActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.settingslayout);

        final SettingsStorage storage = new SettingsStorage(getApplicationContext());
        setLocationTextToPreviousLocations();

        View submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText leftText = (EditText) findViewById(R.id.left_stream_text);
                final EditText rightText = (EditText) findViewById(R.id.right_stream_text);
                final EditText commandText = (EditText) findViewById(R.id.command_url);
                String right = rightText.getText().toString();
                String left = leftText.getText().toString();
                if(!areValidandToastIfInvalid(left,right)){
                    return;
                }
                storage.setStreamLocations(leftText.getText().toString(),rightText.getText().toString());
                storage.setCommandURL(commandText.getText().toString());
                Intent mainProgram = new Intent(getBaseContext(), CardboardActivity.class);
                startActivity(mainProgram);
            }
        });
   }

    public boolean areValidandToastIfInvalid(String left, String right) {
        if (right.equals("") || left.equals("")) {
            Toast.makeText(SettingsActivity.this, "You must set the left and right stream fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!right.substring(0, 4).equals("rtsp") || !left.substring(0, 4).equals("rtsp")) {
            Toast.makeText(SettingsActivity.this, "Your streams don't appear to be rtsp streams.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void setLocationTextToPreviousLocations(){
        SettingsStorage storage = new SettingsStorage(getApplicationContext());
         final EditText leftText = (EditText) findViewById(R.id.left_stream_text);
        leftText.setText(storage.readLeftStreamLocation());

        final EditText rightText = (EditText) findViewById(R.id.right_stream_text);
        rightText.setText(storage.readRightStreamLocation());

    }
}
