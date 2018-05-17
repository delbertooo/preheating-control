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
        audioTrack.write(shorts, offset, shorts.length);
        offset += shorts.length;
    }

}
