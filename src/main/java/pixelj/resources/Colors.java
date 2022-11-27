package pixelj.resources;

import java.awt.Color;

@SuppressWarnings("unused")
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
     * @return Icon foreground color.
     */
    Color icon();

    /**
     * @return Inactive window title text color.
     */
    Color inactive();

    /**
     * @return Panel background color.
     */
    Color panel();
    
    /**
     * @return Background color for separators.
     */
    Color separator();
}
