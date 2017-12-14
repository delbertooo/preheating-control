package de.delbertooo.careco.android.preheatingcontrol.next;


import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Recorder {

    private static final String TAG = Recorder.class.getSimpleName();

    public static final int SAMPLING_RATE = 44100;
    private static final int AUDIO_SOURCE = MediaRecorder.AudioSource.MIC;
    private static final int CHANNEL_IN_CONFIG = AudioFormat.CHANNEL_IN_MONO;
    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    private static final int BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLING_RATE, CHANNEL_IN_CONFIG, AUDIO_FORMAT);

    private byte mAudioData[] = new byte[BUFFER_SIZE];
    private AudioRecord mAudioRecord;
    private boolean recording = false;
    private OutputStream output;

    public Recorder(OutputStream output) {
        this.output = output;
    }

    public void startRecording() throws IOException {
        if (recording || mAudioRecord != null) {
            return;
        }
        recording = true;
        try (BufferedOutputStream os = new BufferedOutputStream(output)) {
            mAudioRecord = new AudioRecord(AUDIO_SOURCE, SAMPLING_RATE, CHANNEL_IN_CONFIG, AUDIO_FORMAT, BUFFER_SIZE);
            mAudioRecord.startRecording();
            recordingLoop(os);
            mAudioRecord.stop();
            mAudioRecord.release();
            mAudioRecord = null;
        }
    }

    public void stopRecording() {
        recording = false;
    }

    synchronized private void recordingLoop(BufferedOutputStream os) throws IOException {
        while (recording) {
            int status = mAudioRecord.read(mAudioData, 0, mAudioData.length);
            if ((status < 0) || status == AudioRecord.ERROR_INVALID_OPERATION || status == AudioRecord.ERROR_BAD_VALUE) {
                String msg = "Could not read audio data.";
                Log.e(TAG, msg);
                throw new RuntimeException(msg);
            }
            os.write(mAudioData, 0, status);
        }
    }

}
