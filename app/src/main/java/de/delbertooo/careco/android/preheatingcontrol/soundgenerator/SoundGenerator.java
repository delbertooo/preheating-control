package de.delbertooo.careco.android.preheatingcontrol.soundgenerator;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

/**
 * Created by robert on 08.03.15.
 */
abstract public class SoundGenerator {
    // originally from http://marblemice.blogspot.com/2010/04/generate-and-play-tone-in-android.html
    // and modified by Steve Pomeroy <steve@staticfree.info>
    private int duration; // seconds
    private int sampleRate;
    //private int numSamples = duration * sampleRate;
    //private double sample[] = new double[numSamples];
    private byte generatedSnd[];// = new byte[2 * numSamples];
    private double freqOfTone; // hz
    

    public SoundGenerator(int duration) {
        this(duration, 8000);
    }

    public SoundGenerator(int duration, double frequency) {
        this(duration, frequency, 8000);
    }

    public SoundGenerator(int duration, double frequency, int sampleRate) {
        this.duration = duration;
        this.sampleRate = sampleRate;
        this.freqOfTone = frequency;
        //int numSamples = duration * sampleRate;
        //this.sample = new double[numSamples];
        //this.generatedSnd = new byte[2 * numSamples];
    }
    
    protected abstract double applyFunction(double x);


    private byte[] genTone() {
        double samples[] = new double[duration * sampleRate];
        // fill out the array
        for (int i = 0; i < samples.length; ++i) {
            double x = i / (sampleRate / freqOfTone);
            samples[i] = applyFunction(x);
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

    public AudioTrack createAudioTrack() {
        byte[] generatedSnd = genTone();
        AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, generatedSnd.length,
                AudioTrack.MODE_STATIC);


        audioTrack.write(generatedSnd, 0, generatedSnd.length);

        return audioTrack;
    }
}
