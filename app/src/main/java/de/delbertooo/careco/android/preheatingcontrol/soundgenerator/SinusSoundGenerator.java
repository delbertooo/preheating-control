package de.delbertooo.careco.android.preheatingcontrol.soundgenerator;

/**
 * Created by robert on 08.03.15.
 */
public class SinusSoundGenerator extends SoundGenerator {

    public SinusSoundGenerator(int duration) {
        super(duration);
    }

    public SinusSoundGenerator(int duration, double frequency) {
        super(duration, frequency);
    }

    public SinusSoundGenerator(int duration, double frequency, int sampleRate) {
        super(duration, frequency, sampleRate);
    }

    @Override
    protected double applyFunction(double x) {
        return Math.sin(2 * Math.PI * x);
    }
}
