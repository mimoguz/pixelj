package pixelj.views.shared;

import java.awt.Dimension;

public final class Dimensions {

    /** Top-bar button dimensions for the home screen. */
    public static final Dimension HOME_BUTTON_SIZE = new Dimension(160, 32);
    /** Large padding. */
    public static final int LARGE_PADDING = 12;
    /** A dimension with width and height is equal to the LARGE_PADDING. */
    public static final Dimension LARGE_SQUARE = new Dimension(LARGE_PADDING, LARGE_PADDING);
    /** For the letter preview on the list items. */
    public static final Dimension LETTER_BOX_SIZE = new Dimension(24, 24);
    /** A dimension with width and height is equal to the Integer.MAX_VALUE. */
    public static final Dimension MAXIMUM = new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    /** The maximum size for filter boxes. */
    public static final Dimension MAXIMUM_COMBO_BOX_SIZE = new Dimension(Integer.MAX_VALUE, 48);
    /** The maximum size for the image previews. */
    public static final int MAXIMUM_PREVIEW_SIZE = 48;
    /** The maximum zoom value for the editors. */
    public static final int MAXIMUM_ZOOM = 48;
    /** Medium padding. */
    public static final int MEDIUM_PADDING = 8;
    /** A dimension with width and height is equal to the MEDIUM_PADDING. */
    public static final Dimension MEDIUM_SQUARE = new Dimension(8, 8);
    /** The the size for filter boxes. */
    public static final Dimension MINIMUM_COMBO_BOX_SIZE = new Dimension(64, 48);
    /** Small padding. */
    public static final int SMALL_PADDING = 4;
    /** A dimension with width and height is equal to the SMALL_PADDING. */
    public static final Dimension SMALL_SQUARE = new Dimension(4, 4);
    /** The fixed size of the all spinners. */
    public static final Dimension SPINNER_SIZE = new Dimension(90, 30);
    /** The fixed size of the tab bar leading/trailing buttons. */
    public static final Dimension TAB_BAR_BUTTON_SIZE = new Dimension(48, 48);
    /** The fixed size of the text buttons on editors and custom dialogs. */
    public static final Dimension TEXT_BUTTON_SIZE = new Dimension(100, 25);

    private Dimensions() {
    }
}
