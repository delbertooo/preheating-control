package de.delbertooo.careco.android.preheatingcontrol.uart;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public abstract class UartPcmWriter {


    private final UartBitGenerator uartBitGenerator;
    private final PcmShorts pcmShorts;

    public UartPcmWriter(UartBitGenerator uartBitGenerator, PcmShorts pcmShorts) {
        this.uartBitGenerator = uartBitGenerator;
        this.pcmShorts = pcmShorts;
    }

    public void write(InputStream inputStream) throws IOException {
        write(pcmShorts.preamble());
        int nextByte;
        while ((nextByte = inputStream.read()) != -1) {
            List<Byte> chunk = new ArrayList<>();
            chunk.addAll(uartBitGenerator.startBit());
            chunk.addAll(uartBitGenerator.byteToUartBits(nextByte));
            chunk.addAll(uartBitGenerator.stopBit());
            chunk.addAll(uartBitGenerator.stopBit());
            write(pcmShorts.convertBits(chunk));
        }
        write(pcmShorts.postabmle());
    }

    protected abstract void write(short[] shorts);

    private void write(List<Short> shorts) {
        write(toShortArray(shorts));
    }

    private short[] toShortArray(List<Short> shorts) {
        short[] shortsArray = new short[shorts.size()];
        for (int i = 0; i < shorts.size(); ++i) {
            shortsArray[i] = shorts.get(i);
        }
        return shortsArray;
    }

}
