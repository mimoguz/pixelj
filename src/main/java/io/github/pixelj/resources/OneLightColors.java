package io.github.pixelj.resources;

import java.awt.*;

public class OneLightColors implements Colors {
    final Color accent = new Color(41, 121, 255);
    final Color active = new Color(229, 115, 26);
    final Color disabledIcon = new Color(234, 234, 234);
    final Color divider = new Color(219, 219, 220);
    final Color faintIcon = new Color(158, 158, 158);
    final Color focusBackground = accent;
    final Color focusForeground = new Color(244, 244, 244);
    final Color icon = new Color(72, 72, 74);
    final Color text = new Color(35, 35, 36);

    public Color accent() {
        return accent;
    }

    public Color active() {
        return active;
    }

    public Color disabledIcon() {
        return disabledIcon;
    }

    public Color divider() {
        return divider;
    }

    public Color faintIcon() {
        return faintIcon;
    }

    public Color focusBackground() {
        return focusBackground;
    }

    public Color focusForeground() {
        return focusForeground;
    }

    public Color icon() {

        return icon;
    }

    public Color text() {
        return text;
    }
}
