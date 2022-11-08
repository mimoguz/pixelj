package pixelj.views.shared;

import pixelj.util.ChangeableInt;
import pixelj.util.ReadOnlyInt;

import javax.swing.*;
import java.awt.*;

public final class VerticalFlowLayout implements LayoutManager {

    @Override
    public void addLayoutComponent(final String name, final Component comp) {
    }

    @Override
    public void removeLayoutComponent(final Component comp) {
    }

    @Override
    public Dimension preferredLayoutSize(final Container parent) {
        return getMinimumSize(parent);
    }

    @Override
    public Dimension minimumLayoutSize(final Container parent) {
        return getMinimumSize(parent);
    }

    @Override
    public void layoutContainer(final Container parent) {
        final var componentCount = parent.getComponentCount();
        final var insets = parent.getInsets();
        final var maxY = parent.getSize().height - insets.bottom;

        var columnWidth = 0;
        var x = insets.left;
        var y = insets.top;

        for (var i = 0; i < componentCount; i++) {
            final var component = parent.getComponent(i);
            if (!component.isVisible()) {
                continue;
            }
            final var prefSize = component.getPreferredSize();
            if (y + prefSize.height > maxY) {
                x += columnWidth;
                y = insets.top;
                columnWidth = 0;
            }
            if (component instanceof JSeparator separator && separator.getOrientation() == SwingConstants.HORIZONTAL) {
                component.setBounds(x, y, columnWidth, prefSize.height);
            } else if (component instanceof JToolBar.Separator) {
                component.setBounds(x, y, columnWidth, 3);
            } else {
                component.setBounds(x, y, prefSize.width, prefSize.height);
            }
            y += prefSize.height;
            columnWidth = Math.max(columnWidth, prefSize.width);
        }
    }

    private static Dimension getMinimumSize(final Container parent) {
        final var componentCount = parent.getComponentCount();
        final var insets = parent.getInsets();
        final var maxHeight = parent.getSize().height - insets.top - insets.bottom;

        var columnWidth = 0;
        var columnHeight = 0;
        var minWidth = 0;
        var minHeight = 0;

        for (var i = 0; i < componentCount; i++) {
            final var component = parent.getComponent(i);
            if (!component.isVisible()) {
                continue;
            }
            final var prefSize = component.getPreferredSize();
            if (columnHeight + prefSize.height > maxHeight || i == componentCount - 1) {
                minHeight = Math.max(minHeight, columnHeight);
                minWidth += columnWidth;
                columnHeight = 0;
                columnWidth = 0;
            }
            if (i == componentCount - 1 && columnWidth < component.getWidth()) {
                minWidth += component.getWidth();
                minHeight = Math.max(columnHeight + component.getHeight(), minHeight);
            }
            columnWidth = Math.max(columnWidth, prefSize.width);
            columnHeight += prefSize.height;
        }

        return new Dimension(minWidth + insets.left + insets.right, minHeight + insets.top + insets.bottom);
    }
}
