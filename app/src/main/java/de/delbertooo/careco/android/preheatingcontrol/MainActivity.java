package de.delbertooo.careco.android.preheatingcontrol;

import android.media.AudioTrack;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import de.delbertooo.careco.android.preheatingcontrol.soundgenerator.RectFunction;
import de.delbertooo.careco.android.preheatingcontrol.soundgenerator.SinusFunction;
import de.delbertooo.careco.android.preheatingcontrol.soundgenerator.SoundGenerator;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void playSinus(View view) {
        try {
            AudioTrack track = new SoundGenerator(new SinusFunction()).withFrequency(1000).withSampleRate(22000).createAudioTrack(10);
            track.play();
        } catch (Exception e) {
            String msg = e.getMessage();
            String ex = Log.getStackTraceString(e);
            Log.e("MainActivity", "Error on plaing audio track", e);
            return;
        }

    }

    public void playRect1k(View view) {
        new SoundGenerator(new RectFunction()).withFrequency(1000).withSampleRate(1000).createAudioTrack(10).play();
    }
    public void playRect10k(View view) {
        new SoundGenerator(new RectFunction()).withFrequency(10000).withSampleRate(22000).createAudioTrack(10).play();
    }
}
