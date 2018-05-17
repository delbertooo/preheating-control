package de.delbertooo.careco.android.preheatingcontrol.uart;

public class OriginalGenerator {
    private String data;
    private int sampleRate;
    private int header;
    private int baud;
    private int samplesPerByte;
    private int bufferSize;

    public OriginalGenerator(int sampleRate, int baud, String data) {
        this.data = data;
        this.sampleRate = sampleRate;
        this.baud = baud;
        samplesPerByte = (int) (this.sampleRate *11/ (float)this.baud);
        header = this.sampleRate; // 1 sec to charge/discharge the cap;
        bufferSize = samplesPerByte*data.length()/*samples*/ + header*2;
    }

    public short[] toShorts() {
        //var buffer = context.createBuffer(1, bufferSize, sampleRate);
        //var b = buffer.getChannelData(0);
        float[] b = new float[bufferSize];

        for (int i = 0; i <header; i++) b[i]= (float)i / (float)header;
        int offset = header;


        for (int c = 0; c < data.length(); ++c) {
            int aByte = data.codePointAt(c);
            if (aByte >= 0 && aByte <= 255) {
                for (int i1 = 0; i1 < samplesPerByte; i1++) {
                    int bit = Math.round(i1 * baud / sampleRate);
                    float value = 1;
                    if (bit == 0) value = 0; // start bit
                    else if (bit == 9 || bit == 10) value = 1; // stop bits
                    else value = (aByte & (1 << (bit - 1))) != 0 ? 1 : 0; // data
                    b[offset++] = value * 2f - 1f;
                }
            } else {
                // just insert a pause
                for (int i = 0; i < samplesPerByte; i++)
                    b[offset++] = 1;
            }
        }


        for (int i = 0; i <header; i++) b[offset+ i]=1f-((float)i / (float)header);
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
