package pixelj.views.shared;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

import pixelj.util.ChangeableInt;
import pixelj.util.ReadOnlyInt;

public final class ToolLayout implements LayoutManager {

    private final ChangeableInt cutoff = new ChangeableInt(-1);

    /** First index for hidden components. */
    public final ReadOnlyInt cutoffProperty = new ReadOnlyInt(cutoff);

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
        final var insets = parent.getInsets();
        final var pw = parent.getWidth();
        final var ph = parent.getHeight();
        final var maxY = ph - insets.bottom;
        final var componentCount = parent.getComponentCount();
        var y = insets.top;

        var componentIndex = 0;
        for (; componentIndex < componentCount; componentIndex++) {
            final var component = parent.getComponent(componentIndex);
            final var prefSize = component.getPreferredSize();

            if (y + prefSize.height <= maxY) {
                component.setVisible(true);
                component.setBounds(insets.left, y, Math.min(prefSize.width, pw), prefSize.height);
                y += prefSize.height;
            } else {
                break;
            }
        }

        if (cutoff.getValue() != componentIndex) {
            cutoff.setValue(componentIndex);
        }

        for (; componentIndex < componentCount; componentIndex++) {
            parent.getComponent(componentIndex).setVisible(false);
        }
    }

    private static Dimension getMinimumSize(final Container parent) {
        final var componentCount = parent.getComponentCount();
        var w = 0;
        var h = 0;

        for (var i = 0; i < componentCount; i++) {
            final var component = parent.getComponent(i);
            final var prefSize = component.getPreferredSize();
            w = Math.max(w, prefSize.width);
            h += prefSize.height;
        }

        final var insets = parent.getInsets();
        return new Dimension(w + insets.left + insets.right, h + insets.top + insets.bottom);
    }
}
