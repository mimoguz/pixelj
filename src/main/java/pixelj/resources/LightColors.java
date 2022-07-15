package pixelj.resources;

import java.awt.Color;

public final class LightColors implements Colors {

    private final Color accent = new Color(41, 121, 255);
    private final Color active = new Color(229, 115, 26);
    private final Color disabledIcon = new Color(176, 176, 176);
    private final Color divider = new Color(219, 219, 220);
    private final Color faintIcon = new Color(114, 114, 114);
    private final Color icon = new Color(72, 72, 74);

    @Override
    public Color accent() {
        return accent;
    }

    @Override
    public Color active() {
        return active;
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
    public Color icon() {

        return icon;
    }

    @Override
    public Color inactive() {
        return faintIcon;
    }

}
