package pixelj.services;

import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.ListModel;

import com.jthemedetecor.OsThemeDetector;

import pixelj.resources.Resources;
import pixelj.util.ChangeableInt;
import pixelj.util.ChangeableValue;
import pixelj.views.projectwindow.LayoutStrategy;

// TODO: MAJOR: Better state management!
public final class AppState {

    /** The default image size for the export dialog. */
    public static final int DEFAULT_EXPORT_IMAGE_SIZE = 256;

    /** Text for the preview tab. */
    public final ChangeableValue<String> previewTextProperty = new ChangeableValue<>(null);
    /** Current theme.  */
    public final ChangeableValue<Theme> themeProperty = new ChangeableValue<>(Theme.SYSTEM);
    /** Width of the exported images. */
    public final ChangeableInt exportImageWidthProperty = new ChangeableInt(DEFAULT_EXPORT_IMAGE_SIZE);
    /** Height of the exported images. */
    public final ChangeableInt exportImageHeightProperty = new ChangeableInt(DEFAULT_EXPORT_IMAGE_SIZE);
    /** Controls the packer-rectangle extractor combo to be used. */
    public final ChangeableValue<LayoutStrategy> layoutStrategyProperty = new ChangeableValue<>(
        LayoutStrategy.GRID_LAYOUT
    );

    private boolean darkTheme;
    /** Recent projects. */
    private final DefaultListModel<RecentItem> recentItems = new DefaultListModel<>();

    public AppState() {
        setTheme(AppState.Theme.SYSTEM);
    }

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
        return darkTheme;
    }

    public Theme getTheme() {
        return themeProperty.getValue();
    }

    public void setTheme(final Theme value) {
        themeProperty.setValue(value);
        if (value == Theme.SYSTEM) {
            final var detector = OsThemeDetector.getDetector();
            darkTheme = detector.isDark();
        } else {
            darkTheme = value == Theme.DARK;
        }
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

    public LayoutStrategy getLayoutStrategy() {
        return layoutStrategyProperty.getValue();
    }

    public void setLayoutStrategy(final LayoutStrategy value) {
        layoutStrategyProperty.setValue(value);
    }

    public List<RecentItem> getRecentItems() {
        return Collections.list(recentItems.elements());
    }

    public void setRecentItems(final Collection<RecentItem> items) {
        recentItems.clear();
        items.stream()
            .sorted(Comparator.<RecentItem, OffsetDateTime>comparing(it ->
                it.lastOpened() != null
                    ? it.lastOpened()
                    : OffsetDateTime.of(1, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC)
            ).reversed())
            .forEach(recentItems::addElement);
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
        final var newItem = new RecentItem(title, item.path(), item.lastOpened());
        recentItems.set(index, newItem);
    }

    /**
     * Replace the last opened info of a recent project.
     *
     * @param path
     * @param dateTime
     */
    public void replaceRecentItemDate(final Path path, final OffsetDateTime dateTime) {
        final var index = findItemIdex(path);
        if (index < 0) {
            return;
        }
        final var item = recentItems.get(index);
        final var newItem = new RecentItem(item.title(), item.path(), dateTime);
        recentItems.set(index, newItem);
        setRecentItems(getRecentItems()); // Reorder
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

    public enum Theme {
        SYSTEM, DARK, LIGHT;

        @Override
        public String toString() {
            return Resources.get().getString("theme_" + this.ordinal());
        }
    }
}
