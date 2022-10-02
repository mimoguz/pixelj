package pixelj.resources;

import java.awt.Color;

public final class DarkColors implements Colors {

    private final Color accent = new Color(41, 121, 255);
    private final Color active = new Color(229, 115, 26);
    private final Color disabledIcon = new Color(68, 75, 89);
    private final Color divider = new Color(33, 37, 43);
    private final Color inactive = new Color(106, 112, 122);
    private final Color icon = new Color(151, 159, 173);
    private final Color separator = new Color(53, 64, 72);

    @Override
    public Color accent() {
        return accent;
    }

    @Override
    public Color activeTab() {
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
        return inactive;
    }
    
    @Override
    public Color separator() {
        return separator;
    }
}
