package pixelj.services;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultListModel;

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
    /** Recent projects. */
    public final DefaultListModel<RecentItem> recentItems = new DefaultListModel<>();

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

    public List<RecentItem> getRecentItems() {
        return Collections.list(recentItems.elements());
    }

    public void setRecentItems(final Collection<RecentItem> items) {
        recentItems.clear();
        for (var item : items) {
            recentItems.addElement(item);
        }
    }
}
