package io.github.mimoguz.pixelj.resources;

import java.awt.Color;

public class OneDarkColors implements Colors {
    final Color accent = new Color(41, 121, 255);
    final Color active = new Color(229, 115, 26);
    final Color background = new Color(40, 44, 52);
    final Color disabledIcon = new Color(68, 75, 89);
    final Color divider = new Color(53, 58, 68);
    final Color faintIcon = new Color(106, 112, 122);
    final Color focusBackground = accent;
    final Color focusForeground = new Color(244, 244, 244);
    final Color icon = new Color(151, 159, 173);
    final Color text = Color.WHITE;

    @Override
    public Color accent() {
        return accent;
    }

    @Override
    public Color active() {
        return active;
    }

    @Override
    public Color background() {
        return background;
    }

    @Override
    public Color disabledIcon() {
        return disabledIcon;
    }

    @Override
    public Color divider() {
        return divider;
    }

    @Override
    public Color faintIcon() {
        return faintIcon;
    }

    @Override
    public Color focusBackground() {
        return focusBackground;
    }

    @Override
    public Color focusForeground() {
        return focusForeground;
    }

    @Override
    public Color icon() {
        return icon;
    }

    @Override
    public Color text() {
        return text;
    }
}
