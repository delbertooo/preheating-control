package de.delbertooo.careco.android.preheatingcontrol.uart;

import android.media.AudioTrack;

public class AudioTrackUartPcmWriter extends UartPcmWriter {


    private final AudioTrack audioTrack;
    private int offset = 0;

    public AudioTrackUartPcmWriter(UartBitGenerator uartBitGenerator, PcmShorts pcmShorts, AudioTrack audioTrack) {
        super(uartBitGenerator, pcmShorts);
        this.audioTrack = audioTrack;
    }


    protected void write(short[] shorts) {
        audioTrack.write(shorts, 0, shorts.length);
        //byte[] bytes = Stream.of(shorts).map(s -> (byte) ((s / Short.MAX_VALUE) * Byte.MAX_VALUE)).toArray();
        /*for (short s : shorts) {
            byte[] bytes = new byte[2];
            bytes[0] = (byte) (s & 0x00ff);
            bytes[1] = (byte) ((s & 0xff00) >>> 8);

            audioTrack.write(bytes, offset, bytes.length);
            offset += bytes.length;
        }*/
    }

}
