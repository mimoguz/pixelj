package io.github.mimoguz.pixelj.resources;

import java.awt.Color;

public class LightColors implements Colors {
    final Color accent = new Color(41, 121, 255);
    final Color active = new Color(229, 115, 26);
    final Color box = new Color(234, 234, 235);
    final Color disabledIcon = new Color(176, 176, 176);
    final Color divider = new Color(219, 219, 220);
    final Color faintIcon = new Color(114, 114, 114);
    final Color icon = new Color(72, 72, 74);

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
