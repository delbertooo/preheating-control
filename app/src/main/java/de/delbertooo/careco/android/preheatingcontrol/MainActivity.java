package de.delbertooo.careco.android.preheatingcontrol;

import android.media.AudioFormat;
import android.media.AudioManager;
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
            AudioTrack track = new SoundGenerator(new SinusFunction()).withFrequency(1000).withSampleRate(22000).createAudioTrack(3);
            track.play();
        } catch (Exception e) {
            String msg = e.getMessage();
            String ex = Log.getStackTraceString(e);
            Log.e("MainActivity", "Error on plaing audio track", e);
            return;
        }

    }

    public void playRect1k(View view) {
        new SoundGenerator(new RectFunction()).withFrequency(1000).withSampleRate(22000).createAudioTrack(10).play();
    }

    public void playRect10k(View view) {
        new SoundGenerator(new RectFunction()).withFrequency(10000).withSampleRate(22000).createAudioTrack(10).play();
    }

    private AudioTrack generateBitSequence(int... bits) {
        final short high = Short.MAX_VALUE;
        final short low = 0;
        final double length0 = 0.8;
        final double length1 = 0.2;
        final double gap = 0.2;
        final double startDelayInMs = 200;
        final double sampleRate = 22000;
        double totalLength = startDelayInMs;
        for (int bit : bits) {
            totalLength += bit == 0 ? length0 : length1;
            totalLength += gap;
        }
        final int duration = (int) Math.ceil(totalLength / 1000.0);
        final short audioData[] = new short[duration * (int)sampleRate];
        // fill out the array
        int audioDataIndex = 0;
        for (int i = 0; i < (sampleRate * (startDelayInMs / 1000)); ++i) {
            audioData[audioDataIndex++] = low;
        }
        for (int bit : bits) {
            final double bitLength = sampleRate * ((bit == 0 ? length0 : length1) / 1000);
            //Log.e("MainActivity", "bitLength = " + bitLength);
            for (int i = 0; i < bitLength; ++i) {
                //audioDataIndex++;
                audioData[audioDataIndex++] = high;
            }
            for (int i = 0; i < (sampleRate * (gap / 1000)); i++) {
                audioData[audioDataIndex++] = low;
            }
        }
        Log.e("MainActivity", "Duration = " + duration + ", totalLength = " + totalLength + ", audioDataIndex = " + audioDataIndex);

        AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                (int)sampleRate, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, audioData.length,
                AudioTrack.MODE_STATIC);


        audioTrack.write(audioData, 0, audioData.length);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return audioTrack;
    }

    public void playSingleRect(View view) {
        int duration = 2;
        int sampleRate = 22000;
        int startDelay = sampleRate / 200;
        double freqOfTone = 1000;
        double durationOfOne = (sampleRate / freqOfTone);
        double samples[] = new double[duration * sampleRate];
        // fill out the array
        for (int i = 0; i < samples.length; ++i) {
            samples[i] = i > startDelay && i < startDelay + durationOfOne ? 1 : 0;
        }

        // convert to 16 bit pcm sound array
        // assumes the sample buffer is normalised.
        byte[] generatedSnd = new byte[2 * samples.length];
        int idx = 0;
        for (final double sample : samples) {
            // scale to maximum amplitude

            final short scaledSample = (short) ((sample * Short.MAX_VALUE));
            // in 16 bit wav PCM, first byte is the low order byte
            generatedSnd[idx++] = (byte) (scaledSample & 0x00ff);
            generatedSnd[idx++] = (byte) ((scaledSample & 0xff00) >>> 8);

        }
        AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, generatedSnd.length,
                AudioTrack.MODE_STATIC);


        audioTrack.write(generatedSnd, 0, generatedSnd.length);
        audioTrack.play();
        audioTrack.release();
    }

    public void playSequence(View view) {
        generateBitSequence(1, 0, 1, 0, 1, 1, 1, 1).play();
    }

    public void readAudio(View view) {
        Recorder recorder = new Recorder();
        recorder.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        recorder.stop();
        Log.d("debug", recorder.getResult().toString());
    }
}
