package de.delbertooo.careco.android.preheatingcontrol.uart;

import com.annimon.stream.Collectors;
import com.annimon.stream.IntStream;
import com.annimon.stream.Stream;
import com.annimon.stream.function.IntToDoubleFunction;

import java.util.List;

public class PcmShorts {

    private static final short HIGH = Short.MAX_VALUE;
    private static final short LOW = -Short.MAX_VALUE;
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
        return generateLinear(i -> (double) i / (double) sampleRate);
    }

    public List<Short> postabmle() {
        return generateLinear(i -> 1d - ((double) i / (double) sampleRate));
    }

    private List<Short> generateLinear(IntToDoubleFunction function) {
        return IntStream.range(0, sampleRate)
                .mapToDouble(function)
                .mapToObj(m -> (short) (HIGH * m))
                .collect(Collectors.toList());
    }
}
