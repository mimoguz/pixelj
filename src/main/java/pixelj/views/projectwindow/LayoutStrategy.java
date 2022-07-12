package pixelj.views.projectwindow;

import pixelj.resources.Resources;

public enum LayoutStrategy {
    GRID_LAYOUT("gridLayoutStrategy");

    private String key;

    LayoutStrategy(final String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return Resources.get().getString(key);
    }
}
