package de.delbertooo.careco.android.preheatingcontrol.uart;


import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

public class InMemoryUartPcmWriter extends UartPcmWriter {

    private List<Short> buffer = new ArrayList<>();

    public InMemoryUartPcmWriter(UartBitGenerator uartBitGenerator, PcmShorts pcmShorts) {
        super(uartBitGenerator, pcmShorts);
    }

    @Override
    protected void write(short[] shorts) {
        for (short s : shorts) {
            buffer.add(s);
        }
    }

    public ShortBuffer toShortBuffer() {
        final ShortBuffer shortBuffer = ShortBuffer.allocate(buffer.size());
        for (short s : buffer) {
            shortBuffer.put(s);
        }
        return shortBuffer;
    }
}
