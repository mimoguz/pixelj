package io.github.pixelj.util;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class Components {
    public static void setFixedSize(@NotNull JComponent component, @NotNull Dimension size) {
        component.setMinimumSize(size);
        component.setMaximumSize(size);
        component.setSize(size);
        component.revalidate();
    }
}
