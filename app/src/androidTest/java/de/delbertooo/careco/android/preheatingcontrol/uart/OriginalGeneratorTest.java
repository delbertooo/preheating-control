package de.delbertooo.careco.android.preheatingcontrol.uart;

import junit.framework.TestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class OriginalGeneratorTest extends TestCase {

    static final short H = Short.MAX_VALUE;
    static final short N = 0;
    static final short L = -Short.MAX_VALUE;

    public void testGetBits() {
        short[] actual = new OriginalGenerator(1, 1, "y@yyy").toShorts();
        // 121,64

        //assertEquals(csv(actual), "121,111,108,111");
        //assertThat(csv(actual), is("0 0 0 1 1 1 1 0 0 1 1 1 1"));
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

    public void testWrite_prepostamble() {

        short[] actual = new OriginalGenerator(8, 1, "").toShorts();

        assertThat(actual, is(new short[]{
                0, 4095, 8191, 12287, 16383, 20479, 24575, 28671, //preamble
                32767, 28671, 24575, 20479, 16383, 12287, 8191, 4095//postamble
        }));
    }

}