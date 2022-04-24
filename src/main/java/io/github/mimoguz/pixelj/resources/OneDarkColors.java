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

    @Override
    @NotNull
    public Color accent() {
        return accent;
    }

    @Override
    @NotNull
    public Color active() {
        return active;
    }

    @Override
    @NotNull
    public Color disabledIcon() {
        return disabledIcon;
    }

    @Override
    @NotNull
    public Color divider() {
        return divider;
    }

    @Override
    @NotNull
    public Color faintIcon() {
        return faintIcon;
    }

    @Override
    @NotNull
    public Color focusBackground() {
        return focusBackground;
    }

    @Override
    @NotNull
    public Color focusForeground() {
        return focusForeground;
    }

    @Override
    @NotNull
    public Color icon() {
        return icon;
    }

    @Override
    @NotNull
    public Color text() {
        return text;
    }
}
