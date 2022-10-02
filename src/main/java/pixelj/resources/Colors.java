package pixelj.resources;

import java.awt.Color;

public interface Colors {

    /**
     * @return Accent color.
     */
    Color accent();

    /**
     * @return Active tab icon color.
     */
    Color activeTab();

    /**
     * @return Disabled icon foreground color.
     */
    Color disabledIcon();

    /**
     * @return Background color for dividers.
     */
    Color divider();

    /**
     * @return Icon foreground color.
     */
    Color icon();

    /**
     * @return Inactive window title text color.
     */
    Color inactive();
    
    /**
     * @return Background color for separators.
     */
    Color separator();
}
