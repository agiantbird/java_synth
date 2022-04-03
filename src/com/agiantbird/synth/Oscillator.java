package com.agiantbird.synth;

import com.agiantbird.synth.utils.Utils;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.util.Random;

public class Oscillator extends SynthControlContainer {

    private final Random random = new Random();

    private Waveform waveform = Waveform.Sine;
    private double keyFrequency;
    private double frequency;
    private int wavePos;

    public Oscillator(Synthesizer synth) {
        super(synth);
        JComboBox<Waveform> comboBox = new JComboBox<>(new Waveform[] {Waveform.Sine, Waveform.Square, Waveform.Saw, Waveform.Triangle, Waveform.Noise});
        comboBox.setSelectedItem(Waveform.Sine);
        //bumped width from 75 to 105
        comboBox.setBounds(10, 10, 105, 25);
        comboBox.addItemListener(l -> {
           if (l.getStateChange() == ItemEvent.SELECTED) {
               waveform = (Waveform) l.getItem();
           }
        });
        add(comboBox);
        JLabel toneParameter = new JLabel("x0.00");
        toneParameter.setBounds(165, 65, 50, 25);
        toneParameter.setBorder(Utils.WindowDesign.LINE_BORDER);
        add(toneParameter);
        JLabel toneText = new JLabel("Tone");
        toneText.setBounds(172, 40, 75, 25);
        add(toneText);
        setSize(279, 100);
        setBorder(Utils.WindowDesign.LINE_BORDER);
        setLayout(null);
    }

    private enum Waveform {
        Sine, Square, Saw, Triangle, Noise
    }

    public double getKeyFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        keyFrequency = this.frequency = frequency;
        // apply tone offset
    }

    public double nextSample() {
        double tDivP = (wavePos++ / (double) Synthesizer.AudioInfo.SAMPLE_RATE ) / (1d / frequency);
        switch (waveform) {
            case Sine:
                return Math.sin(Utils.Math.frequencyToAngularFrequency(frequency) * (wavePos - 1) / Synthesizer.AudioInfo.SAMPLE_RATE);
            case Square:
                return Math.signum(Math.sin(Utils.Math.frequencyToAngularFrequency(frequency) * (wavePos - 1) / Synthesizer.AudioInfo.SAMPLE_RATE));
            case Saw:
                return 2d * (tDivP - Math.floor(0.5 + tDivP));
            case Triangle:
                return 2d * Math.abs(2d * (tDivP - Math.floor(0.5 + tDivP))) - 1;
            case Noise: {
                return random.nextDouble();
            }
            default:
                throw new IllegalStateException("Unexpected value: " + waveform);
        }
    }
}
