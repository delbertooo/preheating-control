package de.delbertooo.careco.android.preheatingcontrol.uart;

import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class UartPcmWriterTest extends TestCase {

    static final short H = Short.MAX_VALUE;
    static final short N = 0;
    static final short L = -Short.MAX_VALUE;

    public void testWrite() throws IOException {

        InMemoryUartPcmWriter writer = new InMemoryUartPcmWriter(new UartBitGenerator(1, 1), new PcmShorts(1));
        writer.write(new ByteArrayInputStream("y@yyy".getBytes()));
        short[] actual = writer.toShortBuffer().array();

        assertThat(actual, is(new short[]{
                N, // preamble
                L, // start bit
                H, L, L, H, H, H, H, L, // y
                H, H, // stop  bits
                L, // start bit
                L, L, L, L, L, L, H, L, // @
                H, H, // stop  bits
                L, // start bit
                H, L, L, H, H, H, H, L, // y
                H, H, // stop  bits
                L, // start bit
                H, L, L, H, H, H, H, L, // y
                H, H, // stop  bits
                L, // start bit
                H, L, L, H, H, H, H, L, // y
                H, H, // stop  bits
                H // postamble
        }));
    }
    public void testWrite_prepostamble() throws IOException {

        InMemoryUartPcmWriter writer = new InMemoryUartPcmWriter(new UartBitGenerator(1, 8), new PcmShorts(8));
        writer.write(new ByteArrayInputStream("".getBytes()));
        short[] actual = writer.toShortBuffer().array();

        assertThat(actual, is(new short[]{
                0, 4095, 8191, 12287, 16383, 20479, 24575, 28671, //preamble
                32767, 28671, 24575, 20479, 16383, 12287, 8191, 4095//postamble
        }));
    }
}