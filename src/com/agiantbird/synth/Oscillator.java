package com.agiantbird.synth;

import com.agiantbird.synth.utils.RefWrapper;
import com.agiantbird.synth.utils.Utils;

import javax.swing.*;
import java.awt.event.ItemEvent;

public class Oscillator extends SynthControlContainer {

    private static final int TONE_OFFSET_LIMIT = 2000;
    private Wavetable wavetable = Wavetable.Sine;
    private RefWrapper<Integer> toneOffset = new RefWrapper<>(0);
    private RefWrapper<Integer> volume = new RefWrapper<>(100);
    private double keyFrequency;
    private int waveTableStepSize;
    private int waveTableIndex;

    public Oscillator(Synthesizer synth) {
        super(synth);
        JComboBox<Wavetable> comboBox = new JComboBox<>(Wavetable.values());
        comboBox.setSelectedItem(Wavetable.Sine);
        //bumped width from 75 to 105
        comboBox.setBounds(10, 10, 105, 25);
        comboBox.addItemListener(l -> {
           if (l.getStateChange() == ItemEvent.SELECTED) {
               wavetable = (Wavetable) l.getItem();
           }
        });
        add(comboBox);
        JLabel toneParameter = new JLabel("x0.00");
        toneParameter.setBounds(165, 65, 50, 25);
        toneParameter.setBorder(Utils.WindowDesign.LINE_BORDER);
        Utils.ParameterHandling.addParameterMouseListeners(toneParameter, this, -TONE_OFFSET_LIMIT, TONE_OFFSET_LIMIT, 1, toneOffset, () ->
        {
            applyToneOffset();
            toneParameter.setText(" x" + String.format("%.3f", getToneOffset()));
        });
        add(toneParameter);
        JLabel toneText = new JLabel("Tone");
        toneText.setBounds(172, 40, 75, 25);
        add(toneText);
        JLabel volumeParameter = new JLabel("100%");
        volumeParameter.setBounds( 222, 65, 50, 25);
        volumeParameter.setBorder(Utils.WindowDesign.LINE_BORDER);
        Utils.ParameterHandling.addParameterMouseListeners(volumeParameter, this, 0, 100, 1, volume, () -> volumeParameter.setText(" " + volume.val + "%"));
        add(volumeParameter);
        JLabel volumeText = new JLabel("Volume");
        setSize(279, 100);
        setBorder(Utils.WindowDesign.LINE_BORDER);
        volumeText.setBounds(225,40,75,25);
        add(volumeText);
        setLayout(null);
    }

    public void setKeyFrequency(double frequency) {
        keyFrequency = frequency;
        applyToneOffset();
    }

    private double getToneOffset() {
        return toneOffset.val / 1000.0;
    }

    private double getVolumeMultiplier() {
        return volume.val / 100.0;
    }

    public double getNextSample() {
        double sample = wavetable.getSamples()[waveTableIndex] * getVolumeMultiplier();
        waveTableIndex = (waveTableIndex + waveTableStepSize) % Wavetable.SIZE;
        return sample;
    }

    private void applyToneOffset() {
        waveTableStepSize = (int)(Wavetable.SIZE * (keyFrequency * Math.pow(2, getToneOffset()))/ Synthesizer.AudioInfo.SAMPLE_RATE);
    }
}
