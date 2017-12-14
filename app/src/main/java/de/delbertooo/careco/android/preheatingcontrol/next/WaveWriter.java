package de.delbertooo.careco.android.preheatingcontrol.next;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class WaveWriter {
    private final long samplingRate;
    private final byte recorderBpp;
    private final int channels;
    private final long byteRate;
    private final byte blockAlign;

    public WaveWriter(int samplingRate, int recorderBpp, int channels) {
        this.samplingRate = samplingRate;
        this.recorderBpp = (byte) recorderBpp;
        this.channels = channels;
        byteRate = this.samplingRate * this.channels * this.recorderBpp / 8;
        blockAlign = (byte) (this.channels * this.recorderBpp / 8);
    }


    public ReadWave read(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[4096];

        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();

        return new ReadWave(buffer.toByteArray());
    }

    public class ReadWave {
        private final byte[] audioData;
        private final long totalAudioLen;
        private final long totalDataLen;

        private ReadWave(byte[] audioData) {
            this.audioData = audioData;
            totalAudioLen = audioData.length;
            totalDataLen = totalAudioLen + 36;
        }

        public void copyTo(OutputStream outputStream) throws IOException {
            writeWaveFileHeader(outputStream);
            outputStream.write(audioData);
        }

        private void writeWaveFileHeader(OutputStream out) throws IOException {
            byte[] header = new byte[44];

            header[0] = 'R';  // RIFF/WAVE header
            header[1] = 'I';
            header[2] = 'F';
            header[3] = 'F';
            header[4] = (byte) (totalDataLen & 0xff);
            header[5] = (byte) ((totalDataLen >> 8) & 0xff);
            header[6] = (byte) ((totalDataLen >> 16) & 0xff);
            header[7] = (byte) ((totalDataLen >> 24) & 0xff);
            header[8] = 'W';
            header[9] = 'A';
            header[10] = 'V';
            header[11] = 'E';
            header[12] = 'f';  // 'fmt ' chunk
            header[13] = 'm';
            header[14] = 't';
            header[15] = ' ';
            header[16] = 16;  // 4 bytes: size of 'fmt ' chunk
            header[17] = 0;
            header[18] = 0;
            header[19] = 0;
            header[20] = 1;  // format = 1
            header[21] = 0;
            header[22] = (byte) channels;
            header[23] = 0;
            header[24] = (byte) (samplingRate & 0xff);
            header[25] = (byte) ((samplingRate >> 8) & 0xff);
            header[26] = (byte) ((samplingRate >> 16) & 0xff);
            header[27] = (byte) ((samplingRate >> 24) & 0xff);
            header[28] = (byte) (byteRate & 0xff);
            header[29] = (byte) ((byteRate >> 8) & 0xff);
            header[30] = (byte) ((byteRate >> 16) & 0xff);
            header[31] = (byte) ((byteRate >> 24) & 0xff);
            //header[32] = (byte) (2 * 16 / 8);  // block align
            header[32] = blockAlign;  // block align
            header[33] = 0;
            header[34] = recorderBpp;  // bits per sample
            header[35] = 0;
            header[36] = 'd';
            header[37] = 'a';
            header[38] = 't';
            header[39] = 'a';
            header[40] = (byte) (totalAudioLen & 0xff);
            header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
            header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
            header[43] = (byte) ((totalAudioLen >> 24) & 0xff);

            out.write(header, 0, 44);
        }
    }


}