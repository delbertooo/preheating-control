package de.delbertooo.careco.android.preheatingcontrol.soundgenerator;

/**
 * Created by robert on 08.03.15.
 */
public class RectFunction implements SoundGeneratorFunction {

    @Override
    public double apply(double x) {
        return Math.sin(2 * Math.PI * x) >= 0 ? 1 : 0;
    }
}
