package io.github.pixelj.views.shared;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class Components {
    public static void addInnerBorder(final @NotNull JComponent component, final @NotNull Border border) {
        component.setBorder(BorderFactory.createCompoundBorder(component.getBorder(), border));
    }

    public static void addOuterBorder(final @NotNull JComponent component, final @NotNull Border border) {
        component.setBorder(BorderFactory.createCompoundBorder(border, component.getBorder()));
    }

    public static void setFixedSize(final @NotNull JComponent component, final @NotNull Dimension size) {
        component.setMinimumSize(size);
        component.setMaximumSize(size);
        component.setSize(size);
        component.revalidate();
    }
}
