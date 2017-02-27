package de.delbertooo.careco.android.preheatingcontrol.soundgenerator;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

/**
 * Created by robert on 08.03.15.
 */
public class SoundGenerator {
    // originally from http://marblemice.blogspot.com/2010/04/generate-and-play-tone-in-android.html
    // and modified by Steve Pomeroy <steve@staticfree.info>
    private int sampleRate = 8000;
    private double freqOfTone = 8000; // hz
    private SoundGeneratorFunction function;
    

    public SoundGenerator(SoundGeneratorFunction function) {
        this.function = function;
    }

    public SoundGenerator withSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
        return this;
    }

    public SoundGenerator withFrequency(double frequency) {
        this.freqOfTone = frequency;
        return this;
    }

    private byte[] genTone(int duration) {
        double samples[] = new double[duration * sampleRate];
        // fill out the array
        for (int i = 0; i < samples.length; ++i) {
            double x = i / (sampleRate / freqOfTone);
            samples[i] = function.apply(x);
            //sample[i] = Math.sin(2 * Math.PI * i / (sampleRate / freqOfTone));
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

        return generatedSnd;
    }

    public AudioTrack createAudioTrack(int duration) {
        byte[] generatedSnd = genTone(duration);
        AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, generatedSnd.length,
                AudioTrack.MODE_STATIC);


        audioTrack.write(generatedSnd, 0, generatedSnd.length);

        return audioTrack;
    }
}
