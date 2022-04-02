package com.agiantbird.synth;

import javax.swing.*;

public class SynthControlContainer extends JPanel {
    protected boolean on;
    public boolean isOn() {
        return on;
    }

    public boolean setOn(boolean on) {
        this.on = on;
    }
}
