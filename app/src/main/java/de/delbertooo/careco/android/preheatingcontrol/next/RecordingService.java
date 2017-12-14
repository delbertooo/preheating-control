package de.delbertooo.careco.android.preheatingcontrol.next;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RecordingService extends IntentService {

    private static final String TAG = RecordingService.class.getSimpleName();

    public RecordingService() {
        super("PreheatingControlRecordingService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        File f = generateFile();
        if (f == null) {
            Toast.makeText(this, "could not create file", Toast.LENGTH_SHORT).show();
            return;
        }
        try (FileOutputStream os = new FileOutputStream(f)) {
            Recorder recorder = new Recorder(os);
            new Thread(() -> {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    Log.e(TAG, "Interrupted: " + e.getMessage());
                    Thread.currentThread().interrupt();
                }
                recorder.stopRecording();
            }).start();

            Log.d(TAG, "start recording");
            recorder.startRecording();
            Log.d(TAG, "stopped recording");
            writeWave(f);

        } catch (java.io.IOException e) {
            e.printStackTrace();
            Log.e(TAG, "IO error: " + e.getMessage());
        }
    }

    private void writeWave(File f) throws IOException {
        try (FileOutputStream os = new FileOutputStream(new File(f.getAbsolutePath().replace(".pcm", ".wav")))) {
            new WaveWriter(Recorder.SAMPLING_RATE, 16, 1)
                    .read(new FileInputStream(f))
                    .copyTo(os);
        }
    }

    public static File generateFile() {
        return generateFile(".pcm");
    }

    public static File generateFile(String extension) {
        String filename = Environment.getExternalStorageDirectory().getAbsolutePath();
        filename += "/PreheatingControl";

        File dir = new File(filename);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                // failed to create dir
                throw new RuntimeException("failed to create dir");
                //Toast.makeText(getApplicationContext(), "failed to create dir", Toast.LENGTH_SHORT).show();
                //return null;
            }
        }

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        File file = new File(dir, "REC_" + timestamp + extension);
        int count = 1;
        while (file.exists()) {
            file = new File(dir, "REC_" + timestamp + "_" + String.valueOf(count++) + extension);
        }

        return file;
    }
}
