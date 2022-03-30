package com.agiantbird.synth;

import javax.swing.*;

public class Synthesizer {
    private JFrame frame = new JFrame("Syntheziser");

    Synthesizer() {
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(613, 357);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static class AudioInfo {
        public static final int SAMPLE_RATE = 44100;
    }
}
