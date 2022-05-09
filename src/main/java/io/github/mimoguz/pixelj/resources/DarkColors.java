package io.github.mimoguz.pixelj.resources;

import java.awt.Color;

public class DarkColors implements Colors {
    final Color accent = new Color(41, 121, 255);
    final Color active = new Color(229, 115, 26);
    final Color box = new Color(33, 37, 43);
    final Color disabledIcon = new Color(68, 75, 89);
    final Color divider = new Color(53, 58, 68);
    final Color faintIcon = new Color(106, 112, 122);
    final Color icon = new Color(151, 159, 173);

    @Override
    public Color accent() {
        return accent;
    }

    @Override
    public Color active() {
        return active;
    }

    @Override
    public Color box() {
        return box;
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
    public Color inactive() {
        return faintIcon;
    }

    @Override
    public Color icon() {
        return icon;
    }

}
