package pixelj.views.projectwindow.kerningpairspage;

import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import pixelj.models.KerningPair;
import pixelj.views.shared.GlyphCell;
import pixelj.views.shared.Dimensions;

final class KerningPairCellRenderer implements ListCellRenderer<KerningPair> {
    private final JPanel component = new JPanel(new GridLayout(1, 2));
    private final GlyphCell left;
    private final GlyphCell right;

    KerningPairCellRenderer(final int maximumPictureSize) {
        left = new GlyphCell(maximumPictureSize);
        right = new GlyphCell(maximumPictureSize);

        component.add(left);
        component.add(right);
    }

    @Override
    public Component getListCellRendererComponent(
            final JList<? extends KerningPair> list,
            final KerningPair value,
            final int index,
            final boolean isSelected,
            final boolean cellHasFocus
    ) {
        final var partWidth = list.getWidth() / 2 - Dimensions.MEDIUM_PADDING;
        left.set(value.getLeft(), partWidth);
        right.set(value.getRight(), partWidth);

        if (isSelected) {
            left.setBackgroundColor(list.getSelectionBackground());
            left.setForegroundColor(list.getSelectionForeground());
            right.setBackgroundColor(list.getSelectionBackground());
            right.setForegroundColor(list.getSelectionForeground());
            component.setBackground(list.getSelectionBackground());
        } else {
            left.setBackgroundColor(list.getBackground());
            left.setForegroundColor(list.getForeground());
            right.setBackgroundColor(list.getBackground());
            right.setForegroundColor(list.getForeground());
            component.setBackground(list.getBackground());
        }

        return component;
    }
}
