package com.agiantbird.synth;

import com.agiantbird.synth.utils.RefWrapper;
import com.agiantbird.synth.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Oscillator extends SynthControlContainer {

    private static final int TONE_OFFSET_LIMIT = 2000;
    private WaveTable wavetable = WaveTable.Sine;
    private RefWrapper<Integer> toneOffset = new RefWrapper<>(0);
    private double keyFrequency;
    private int waveTableStepSize;
    private int waveTableIndex;

    public Oscillator(Synthesizer synth) {
        super(synth);
        JComboBox<WaveTable> comboBox = new JComboBox<>(WaveTable.values());
        comboBox.setSelectedItem(WaveTable.Sine);
        //bumped width from 75 to 105
        comboBox.setBounds(10, 10, 105, 25);
        comboBox.addItemListener(l -> {
           if (l.getStateChange() == ItemEvent.SELECTED) {
               wavetable = (WaveTable) l.getItem();
           }
        });
        add(comboBox);
        JLabel toneParameter = new JLabel("x0.00");
        toneParameter.setBounds(165, 65, 50, 25);
        toneParameter.setBorder(Utils.WindowDesign.LINE_BORDER);
        Utils.ParameterHandling.addParameterMouseListeners(toneParameter, this, -TONE_OFFSET_LIMIT, TONE_OFFSET_LIMIT, 1,toneOffset, () -> {
            applyToneOffset();
            toneParameter.setText(" x" + String.format("%.3f", getToneOffset()));
        });
        add(toneParameter);
        JLabel toneText = new JLabel("Tone");
        toneText.setBounds(172, 40, 75, 25);
        add(toneText);
        setSize(279, 100);
        setBorder(Utils.WindowDesign.LINE_BORDER);
        setLayout(null);
    }

    public void setKeyFrequency(double frequency) {
        keyFrequency = frequency;
        applyToneOffset();
    }

    private double getToneOffset() {
        return toneOffset.val / 1000d;
    }

    public double getNextSample() {
        double sample = wavetable.getSamples()[waveTableIndex];
        waveTableIndex = (waveTableIndex + waveTableStepSize) % WaveTable.SIZE;
        return sample;
    }

    private void applyToneOffset() {
        waveTableStepSize = (int)(WaveTable.SIZE * (keyFrequency * Math.pow(2, getToneOffset()))/ Synthesizer.AudioInfo.SAMPLE_RATE);
    }
}
