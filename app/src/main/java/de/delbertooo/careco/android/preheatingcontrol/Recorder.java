package de.delbertooo.careco.android.preheatingcontrol;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robert on 05.03.17.
 */

public class Recorder {
    private static final int RECORDER_SAMPLE_RATE = 8000;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    private static final int BUFFER_SIZE = AudioRecord.getMinBufferSize(RECORDER_SAMPLE_RATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING);
    private AudioRecord recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, RECORDER_SAMPLE_RATE, RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING, BUFFER_SIZE);
    private boolean recording = false;
    private Thread readThread;
    private List<Short> result = new ArrayList<>();

    public void start() {
        if (recording) {
            throw new RuntimeException("Already recording.");
        }
        recording = true;
        recorder.startRecording();
        readThread = new Thread(new Runnable() {
            @Override
            public void run() {
                short buffer[] = new short[BUFFER_SIZE];
                while (recording) {
                    int bytesRead = recorder.read(buffer, 0, BUFFER_SIZE);
                    for (int i = 0; i < bytesRead; i++) {
                        result.add(buffer[i]);
                    }
                }
            }
        });
        readThread.start();
    }

    public void stop() {
        recording = false;
        try {
            readThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException("Failed to stop recording.", e);
        }
        recorder.stop();
        recorder.release();
        readThread = null;
    }

    public List<Short> getResult() {
        return result;
    }
}
