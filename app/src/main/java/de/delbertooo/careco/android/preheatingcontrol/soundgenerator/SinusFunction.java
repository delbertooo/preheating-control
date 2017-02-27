package de.delbertooo.careco.android.preheatingcontrol.soundgenerator;

/**
 * Created by robert on 08.03.15.
 */
public class SinusFunction implements SoundGeneratorFunction {

    @Override
    public double apply(double x) {
        return (1 + Math.sin(2 * Math.PI * x)) / 2;
    }
}
