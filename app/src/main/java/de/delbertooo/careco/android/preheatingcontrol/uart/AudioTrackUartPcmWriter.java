package de.delbertooo.careco.android.preheatingcontrol.uart;

import android.media.AudioTrack;

public class AudioTrackUartPcmWriter extends UartPcmWriter {


    private final AudioTrack audioTrack;

    public AudioTrackUartPcmWriter(UartBitGenerator uartBitGenerator, PcmShorts pcmShorts, AudioTrack audioTrack) {
        super(uartBitGenerator, pcmShorts);
        this.audioTrack = audioTrack;
    }


    protected void write(short[] shorts) {
        audioTrack.write(shorts, 0, shorts.length);
    }

}
