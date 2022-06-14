package pixelj.views.kerning_pairs_screen;

import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import pixelj.models.KerningPair;
import pixelj.views.shared.CharacterModelCell;
import pixelj.views.shared.Dimensions;

public class KerningPairCellRenderer implements ListCellRenderer<KerningPair> {
    private final JPanel component = new JPanel(new GridLayout(1, 2));
    private final CharacterModelCell left;
    private final CharacterModelCell right;

    public KerningPairCellRenderer(final int maximumPictureSize) {
        left = new CharacterModelCell(maximumPictureSize);
        right = new CharacterModelCell(maximumPictureSize);

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
