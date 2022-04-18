package io.github.mimoguz.pixelj.resources;

import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class OneDarkColors implements Colors {
    final Color accent = new Color(41, 121, 255);
    final Color active = new Color(229, 115, 26);
    final Color disabledIcon = new Color(45, 49, 58);
    final Color divider = new Color(53, 58, 68);
    final Color faintIcon = new Color(106, 112, 122);
    final Color focusBackground = accent;
    final Color focusForeground = new Color(244, 244, 244);
    final Color icon = new Color(151, 159, 173);
    final Color text = Color.WHITE;

    public @NotNull Color accent() {
        return accent;
    }

    public @NotNull Color active() {
        return active;
    }

    public @NotNull Color disabledIcon() {
        return disabledIcon;
    }

    public @NotNull Color divider() {
        return divider;
    }

    public @NotNull Color faintIcon() {
        return faintIcon;
    }

    public @NotNull Color focusBackground() {
        return focusBackground;
    }

    public @NotNull Color focusForeground() {
        return focusForeground;
    }

    public @NotNull Color icon() {
        return icon;
    }

    public @NotNull Color text() {
        return text;
    }
}
