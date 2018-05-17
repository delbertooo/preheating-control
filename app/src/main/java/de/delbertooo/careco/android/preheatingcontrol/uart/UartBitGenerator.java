package de.delbertooo.careco.android.preheatingcontrol.uart;

import com.annimon.stream.Collectors;
import com.annimon.stream.IntStream;
import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.List;

public class UartBitGenerator {
    private final int baudRate;
    private final int sampleRate;
    private final int samplesPerByte;
    private final int samplesPerBit;
    private final int startBits = 1;
    private final int stopBits = 2;
    private final int dateBits = 8;

    public UartBitGenerator(int baudRate, int sampleRate) {
        this.baudRate = baudRate;
        this.sampleRate = sampleRate;
        samplesPerBit = (int)Math.floor((float)sampleRate / (float)baudRate);
        samplesPerByte = samplesPerBit * (dateBits + startBits + stopBits);
    }

    public List<Byte> byteToUartBits(int oneByte) {
        if (oneByte < 0 || oneByte > 255)
            throw new IllegalArgumentException("The given byte needs to be in the range 0..255.");
        List<Byte> r = new ArrayList<>();
        return IntStream
                //.of(7, 6, 5, 4, 3, 2, 1, 0)
                .range(0, 8)
                .mapToObj(bitIndex -> (oneByte >> bitIndex) & 1)
                .map(this::bitToUartBits)
                .flatMap(Stream::of)
                .collect(Collectors.toList());
        /*
        .forEach(r::addAll);
        return r;
        final int bitsPerByte = 8;
        byte[] r = new byte[bitsPerByte * samplesPerBit];
        for (int bitIndex = 0; bitIndex < bitsPerByte; ++bitIndex) {
            byte oneBit = (byte) ((oneByte >> bitIndex) & 1);
            byte[] bytes = bitToUartBits(oneBit);
        }
        return null;*/
    }

    public List<Byte> bitToUartBits(int oneBit) {
        if (oneBit < 0 || oneBit > 1)
            throw new IllegalArgumentException("The given byte needs to be in the range 0..1.");
        return IntStream.range(0, samplesPerBit)
                .mapToObj(x -> (byte) oneBit)
                .collect(Collectors.toList());
        /*byte[] r = new byte[samplesPerBit];
        for (int sample = 0; sample < samplesPerBit; ++sample) {
            r[sample] = oneBit;
        }
        return r;*/
    }

    public List<Byte> startBit() {
        return bitToUartBits(0);
    }

    public List<Byte> stopBit() {
        return bitToUartBits(1);
    }

}
