package com.agiantbird.synth;

import com.agiantbird.synth.utils.Utils;

enum WaveTable {
    Sine, Square, Saw, Triangle;

    static final int SIZE = 8192;

    private final float[] samples = new float[SIZE];

    static {
        final double FUNDAMENTAL_FREQUENCY = 1d / (SIZE / (double)Synthesizer.AudioInfo.SAMPLE_RATE);
        for (int i = 0; i < SIZE; i++) {
            double t = i / (double)Synthesizer.AudioInfo.SAMPLE_RATE;
            double tDivP = t / (1d / FUNDAMENTAL_FREQUENCY);
            Sine.samples[i] = (float)Math.sin(Utils.Math.frequencyToAngularFrequency(FUNDAMENTAL_FREQUENCY) * t);
            Square.samples[i] = Math.signum(Sine.samples[i]);
            Saw.samples[i] = (float)(2d * (tDivP - Math.floor(0.5 + tDivP)));
            Triangle.samples[i] = (float)(2d * Math.abs(Saw.samples[i]) - 1d);
        }
    }

    float[] getSamples() {
        return samples;
    }
}
