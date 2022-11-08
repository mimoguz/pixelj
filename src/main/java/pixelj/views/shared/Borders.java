package pixelj.views.shared;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

public final class Borders {

    /**
     * Empty border.
     */
    public static final Border EMPTY = BorderFactory.createEmptyBorder();

    /**
     * LARGE_PADDING sized empty border.
     */
    public static final Border
        LARGE_EMPTY =
        BorderFactory.createEmptyBorder(Dimensions.LARGE_PADDING,
            Dimensions.LARGE_PADDING,
            Dimensions.LARGE_PADDING,
            Dimensions.LARGE_PADDING
        );

    /**
     * An empty border for list items.
     */
    public static final Border
        LIST_ITEM =
        BorderFactory.createEmptyBorder(Dimensions.SMALL_PADDING,
            Dimensions.MEDIUM_PADDING,
            Dimensions.SMALL_PADDING,
            Dimensions.MEDIUM_PADDING
        );

    /**
     * MEDIUM_PADDING sized empty border.
     */
    public static final Border
        MEDIUM_EMPTY =
        BorderFactory.createEmptyBorder(Dimensions.MEDIUM_PADDING,
            Dimensions.MEDIUM_PADDING,
            Dimensions.MEDIUM_PADDING,
            Dimensions.MEDIUM_PADDING
        );

    /**
     * SMALL_PADDING sized empty border.
     */
    public static final Border
        SMALL_EMPTY =
        BorderFactory.createEmptyBorder(Dimensions.SMALL_PADDING,
            Dimensions.SMALL_PADDING,
            Dimensions.SMALL_PADDING,
            Dimensions.SMALL_PADDING
        );

    /**
     * Bottom-center panel border. Compensates for the splitter's width.
     */
    public static final Border
        SMALL_EMPTY_BOTTOM_CENTER_PANEL =
        BorderFactory.createEmptyBorder(20, Dimensions.SMALL_PADDING, 24, 0);

    /**
     * Center panel border. Compensates for the splitter's width.
     */
    public static final Border
        SMALL_EMPTY_CENTER_PANEL =
        BorderFactory.createEmptyBorder(Dimensions.SMALL_PADDING,
            Dimensions.SMALL_PADDING,
            Dimensions.SMALL_PADDING,
            0
        );

    /**
     * SMALL_PADDING sized empty border with 0 top width.
     */
    public static final Border
        SMALL_EMPTY_CUP =
        BorderFactory.createEmptyBorder(0,
            Dimensions.SMALL_PADDING,
            Dimensions.SMALL_PADDING,
            Dimensions.SMALL_PADDING
        );

    /**
     * SMALL_PADDING sized empty border with 0 top width. Compensates for the splitter's width.
     */
    public static final Border
        SMALL_EMPTY_CUP_CENTER =
        BorderFactory.createEmptyBorder(0, Dimensions.SMALL_PADDING, Dimensions.SMALL_PADDING, 0);

    /**
     * Title panel border.
     */
    public static final Border
        TITLE =
        BorderFactory.createEmptyBorder(Dimensions.MEDIUM_PADDING,
            Dimensions.SMALL_PADDING,
            Dimensions.MEDIUM_PADDING,
            Dimensions.SMALL_PADDING
        );

    private Borders() {
    }
}
