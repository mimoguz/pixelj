package io.github.mimoguz.pixelj.views.shared;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

@ParametersAreNonnullByDefault
public class Components {
    public static void addInnerBorder(final JComponent component, final Border border) {
        component.setBorder(BorderFactory.createCompoundBorder(component.getBorder(), border));
    }

    public static void addOuterBorder(final JComponent component, final Border border) {
        component.setBorder(BorderFactory.createCompoundBorder(border, component.getBorder()));
    }

    public static void setFixedSize(final JComponent component, final Dimension size) {
        component.setMinimumSize(size);
        component.setMaximumSize(size);
        component.setSize(size);
        component.revalidate();
    }
}
