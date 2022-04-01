package com.agiantbird.synth.utils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class Utils {
    public static void handleProcedure(Procedure procedure, boolean printStackTrace) {
        try {
            procedure.invoke();
        }
        catch (Exception e) {
            if (printStackTrace) {
                e.printStackTrace();
            }
        }
    }

    public static class WindowDesign {
        public static final Border LINE_BORDER = BorderFactory.createLineBorder(Color.BLACK);
    }
}
