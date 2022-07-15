package pixelj.services;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.ListModel;

import pixelj.util.ChangeableBoolean;
import pixelj.util.ChangeableInt;
import pixelj.util.ChangeableValue;

// TODO: MAJOR: Better statem management!
public final class AppState {

    /** The default image size for the export dialog. */
    public static final int DEFAULT_EXPORT_IMAGE_SIZE = 256;

    /** Text for the preview tab. */
    public final ChangeableValue<String> previewTextProperty = new ChangeableValue<>(null);
    /** Use dark theme.  */
    public final ChangeableBoolean darkThemeProperty = new ChangeableBoolean();
    /** Width of the exported images. */
    public final ChangeableInt exportImageWidthProperty = new ChangeableInt(DEFAULT_EXPORT_IMAGE_SIZE);
    /** Height of the exported images. */
    public final ChangeableInt exportImageHeightProperty = new ChangeableInt(DEFAULT_EXPORT_IMAGE_SIZE);

    /** Recent projects. */
    private final DefaultListModel<RecentItem> recentItems = new DefaultListModel<>();

    public ListModel<RecentItem> getRecentItemsListModel() {
        return recentItems;
    }

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

    public RecentItem getRecentItem(final int index) {
        return recentItems.get(index);
    }

    /**
     * Replace the title of a recent project.
     *
     * @param path
     * @param title
     */
    public void replaceRecentItemTitle(final Path path, final String title) {
        final var index = findItemIdex(path);
        if (index < 0) {
            return;
        }
        final var item = recentItems.get(index);
        final var newItem = new RecentItem(title, item.path());
        recentItems.set(index, newItem);
    }

    /**
     * Add a recent item. If an item with same save path is already exists, the old item will be replaced.
     *
     * @param item
     */
    public void addRecentItem(final RecentItem item) {
        final var index = findItemIdex(item.path());
        if (index >= 0) {
            recentItems.set(index, item);
        } else {
            recentItems.addElement(item);
        }
    }

    /**
     * Remove a recent item. The lookup will performed using its path.
     *
     * @param item
     */
    public void removeRecentItem(final RecentItem item) {
        final var index = findItemIdex(item.path());
        if (index >= 0) {
            recentItems.removeElementAt(index);
        }
    }

    private int findItemIdex(final Path path) {
        var index = -1;
        for (var i = 0; i < recentItems.size(); i++) {
            if (recentItems.get(i).path().equals(path)) {
                index = i;
                break;
            }
        }
        return index;
    }
}
