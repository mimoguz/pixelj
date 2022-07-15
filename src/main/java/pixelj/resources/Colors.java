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
    Color active();

    /**
     * @return Disabled icon foreground color.
     */
    Color disabledIcon();

    /**
     * @return Background color for dividers (separators, borders etc.)
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
}
