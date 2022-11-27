package pixelj.resources;

import java.awt.Color;

public final class LightColors implements Colors {

    private final Color accent = new Color(41, 121, 255);
    private final Color activeTab = new Color(229, 115, 26);
    private final Color disabledIcon = new Color(176, 176, 176);
    private final Color inactive = new Color(114, 114, 114);
    private final Color icon = new Color(96, 96, 100);
    private final Color panel = new Color(244, 244, 244);
    private final Color separator = new Color(219, 219, 220);

    @Override
    public Color accent() {
        return accent;
    }

    @Override
    public Color activeTab() {
        return activeTab;
    }

    @Override
    public Color disabledIcon() {
        return disabledIcon;
    }

    @Override
    public Color icon() {

        return icon;
    }

    @Override
    public Color inactive() {
        return inactive;
    }

    @Override
    public Color panel() {
        return panel;
    }

    @Override
    public Color separator() {
        return separator;
    }
}
