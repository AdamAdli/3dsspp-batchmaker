package util;

import javax.swing.*;
import java.awt.*;

public class GridBagPanelAdder {
    static GridBagConstraints gbc = new GridBagConstraints();
    static final Insets INSETS_NONE = new Insets(0,0,0,0);
    public static void add(JPanel panel, JComponent component, int gridX, int gridY) {
        add(panel, component, gridX, gridY, INSETS_NONE);
    }

    public static void add(JPanel panel, JComponent component, int gridX, int gridY, Insets margins) {
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = gridX;
        gbc.gridy = gridY;
        gbc.insets = margins;
        panel.add(component, gbc);
    }
}
