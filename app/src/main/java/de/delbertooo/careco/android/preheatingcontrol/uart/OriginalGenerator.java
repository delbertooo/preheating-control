package de.delbertooo.careco.android.preheatingcontrol.uart;

public class OriginalGenerator {
    private String data;
    private int sampleRate;
    private int header=sampleRate; // 1 sec to charge/discharge the cap;
    private int baud;
    private int samplesPerByte;
    private int bufferSize;

    public OriginalGenerator(int sampleRate, int baud, String data) {
        this.data = data;
        this.sampleRate = sampleRate;
        this.baud = baud;
        samplesPerByte = (this.sampleRate *11/ this.baud);
        bufferSize = samplesPerByte*data.length()/*samples*/ + header*2;
    }

    public short[] toShorts() {
        //var buffer = context.createBuffer(1, bufferSize, sampleRate);
        //var b = buffer.getChannelData(0);
        int[] b = new int[bufferSize];

        for (int i1 = 0; i1 <header; i1++) b[i1]= i1 / header;
        int offset = header;


        for (int c = 0; c < data.length(); ++c) {
            int aByte = data.codePointAt(c);
            if (aByte >= 0 && aByte <= 255) {
                for (int i1 = 0; i1 < samplesPerByte; i1++) {
                    int bit = Math.round(i1 * baud / sampleRate);
                    int value = 1;
                    if (bit == 0) value = 0; // start bit
                    else if (bit == 9 || bit == 10) value = 1; // stop bits
                    else value = (aByte & (1 << (bit - 1))) != 0 ? 1 : 0; // data
                    b[offset++] = value * 2 - 1;
                }
            } else {
                // just insert a pause
                for (int i1 = 0; i1 < samplesPerByte; i1++)
                    b[offset++] = 1;
            }
        }


        for (int i1 = 0; i1 <header; i1++) b[offset+ i1]=1-(i1 / header);
/*


  data.split("").forEach(function(c) {
        var byte = c.charCodeAt(0);
    });


  if (audio_serial_invert)
            for (var i=0;i<bufferSize;i++) b[i] = 1-b[i];
            */

        short[] shorts = new short[b.length];
        for (int i = 0; i < b.length; ++i) {
            shorts[i] = (short) (b[i] * Short.MAX_VALUE);
        }
        return shorts;
    }

}
