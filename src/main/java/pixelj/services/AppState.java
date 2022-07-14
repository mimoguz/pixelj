package pixelj.services;

import pixelj.util.ChangeableBoolean;
import pixelj.util.ChangeableInt;
import pixelj.util.ChangeableValue;

public final class AppState {
    /** The default image size for the export dialog. */
    public static final int DEFAULT_EXPORT_IMAGE_SIZE = 256;

    /** Text for the preview tab. */
    public final ChangeableValue<String> previewTextProperty = new ChangeableValue<>(null);
    /** Use dark theme.  */
    public final ChangeableBoolean darkThemeProperty = new ChangeableBoolean();
    /** Width of the exported images. */
    public final ChangeableInt exportImageWidthProperty = new ChangeableInt(256);
    /** Height of the exported images. */
    public final ChangeableInt exportImageHeightProperty = new ChangeableInt(2);

    public String getPreviewText() {
        return previewTextProperty.getValue();
    }

    public void setPreviewText(final String value) {
        previewTextProperty.setValue(value);
    }

    public boolean isDarkTheme() {
        return darkThemeProperty.getValue();
    }

    public void setDarkTheme(final boolean value) {
        darkThemeProperty.setValue(value);
    }

    public int getExportImageWidth() {
        return exportImageWidthProperty.getValue();
    }

    public void setExportImageWidth(final int value) {
        exportImageWidthProperty.setValue(value);
    }

    public int getExportImageHeight() {
        return exportImageHeightProperty.getValue();
    }

    public void setExportImageHeight(final int value) {
        exportImageHeightProperty.setValue(value);
    }
}
