package pixelj.views.shared;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

public class Borders {
    public static final Border EMPTY = BorderFactory.createEmptyBorder();
    public static final Border LARGE_EMPTY = BorderFactory.createEmptyBorder(
            Dimensions.LARGE_PADDING,
            Dimensions.LARGE_PADDING,
            Dimensions.LARGE_PADDING,
            Dimensions.LARGE_PADDING
    );
    public static final Border LIST_ITEM = BorderFactory.createEmptyBorder(
            Dimensions.SMALL_PADDING,
            Dimensions.MEDIUM_PADDING,
            Dimensions.SMALL_PADDING,
            Dimensions.MEDIUM_PADDING
    );
    public static final Border MEDIUM_EMPTY = BorderFactory.createEmptyBorder(
            Dimensions.MEDIUM_PADDING,
            Dimensions.MEDIUM_PADDING,
            Dimensions.MEDIUM_PADDING,
            Dimensions.MEDIUM_PADDING
    );
    public static final Border SMALL_EMPTY = BorderFactory.createEmptyBorder(
            Dimensions.SMALL_PADDING,
            Dimensions.SMALL_PADDING,
            Dimensions.SMALL_PADDING,
            Dimensions.SMALL_PADDING
    );
    public static final Border SMALL_EMPTY_BOTTOM_CENTER_PANEL = BorderFactory
            .createEmptyBorder(20, Dimensions.SMALL_PADDING, 24, 0);
    public static final Border SMALL_EMPTY_CENTER_PANEL = BorderFactory.createEmptyBorder(
            Dimensions.SMALL_PADDING,
            Dimensions.SMALL_PADDING,
            Dimensions.SMALL_PADDING,
            0
    );
    public static final Border SMALL_EMPTY_CUP = BorderFactory.createEmptyBorder(
            0,
            Dimensions.SMALL_PADDING,
            Dimensions.SMALL_PADDING,
            Dimensions.SMALL_PADDING
    );
    public static final Border SMALL_EMPTY_CUP_CENTER = BorderFactory
            .createEmptyBorder(0, Dimensions.SMALL_PADDING, Dimensions.SMALL_PADDING, 0);
    public static final Border TITLE = BorderFactory.createEmptyBorder(
            Dimensions.LARGE_PADDING,
            Dimensions.SMALL_PADDING,
            Dimensions.LARGE_PADDING,
            Dimensions.SMALL_PADDING
    );
    public static final Border TITLE_CENTER = BorderFactory.createEmptyBorder(
            Dimensions.LARGE_PADDING,
            Dimensions.SMALL_PADDING,
            Dimensions.LARGE_PADDING,
            0
    );
}
