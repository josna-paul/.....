package riss.com.blindapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;

import java.util.Locale;

public class Home extends AppCompatActivity {

    Button bt_edit;
    SharedPreferences sh;
    TextToSpeech t1;
    String instructions = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        instructions = "Basic instructions for user."
                    + "Listen carefully the commands which is going to use."
                    + "Use command instruction followed by ok to get the instruction for using this app"
                    + "                                  "
                    + "for the voice process actions you need to long press the volume down button";

        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        bt_edit = (Button) findViewById(R.id.bt_edit_profile);

        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
                if (sh.getString("instruction_flag", "0").equals("0")) {
                    SharedPreferences.Editor ed = sh.edit();
                    ed.putString("instruction_flag", "1");
                    ed.commit();
                    speakResult(instructions);
                }
            }
        });

        bt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), EditCareTaker.class));
            }
        });
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_UP) {
                    if (event.getEventTime() - event.getDownTime() > ViewConfiguration.getLongPressTimeout()) {
                        try {
                            Intent rec = new Intent(getApplicationContext(), Recognizer.class);
                            startActivity(rec);
                            return true;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }

    void speakResult(String voice) {
        t1.speak(voice, TextToSpeech.QUEUE_FLUSH, null);
    }
}