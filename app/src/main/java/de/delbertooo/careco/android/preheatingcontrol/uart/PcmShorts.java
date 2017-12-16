package de.delbertooo.careco.android.preheatingcontrol.uart;

import com.annimon.stream.Collectors;
import com.annimon.stream.IntStream;
import com.annimon.stream.Stream;

import java.util.Collections;
import java.util.List;

public class PcmShorts {

    private static final short HIGH = Short.MAX_VALUE;
    private static final short LOW = Short.MIN_VALUE;
    private final int sampleRate;

    public PcmShorts(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    public List<Short> convertBits(List<Byte> dataBits) {
        return Stream.of(dataBits)
                .map(this::convertBit)
                .collect(Collectors.toList());
    }

    public short convertBit(Byte bit) {
        return bit == 1 ? HIGH : LOW;
    }

    public List<Short> preamble() {
        return IntStream.range(0, sampleRate)
                .mapToDouble(i -> (double) i / (double) sampleRate)
                .mapToObj(m -> (short) (HIGH * m))
                .collect(Collectors.toList());
    }

    public List<Short> postabmle() {
        List<Short> result = preamble();
        Collections.reverse(result);
        return result;
    }
}
