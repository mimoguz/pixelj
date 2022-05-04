package io.github.mimoguz.pixelj.views.kerning_pairs_screen;

import io.github.mimoguz.pixelj.models.KerningPairModel;
import io.github.mimoguz.pixelj.views.shared.CharacterCell;

import javax.swing.*;
import java.awt.*;

public class KerningPairCellRenderer implements ListCellRenderer<KerningPairModel> {
    private final JPanel component = new JPanel(new GridLayout(1, 2));
    private final CharacterCell left;
    private final CharacterCell right;

    public KerningPairCellRenderer(final int maximumPictureSize) {
        left = new CharacterCell(maximumPictureSize);
        right = new CharacterCell(maximumPictureSize);

        component.add(left);
        component.add(right);
    }

    @Override
    public Component getListCellRendererComponent(
            final JList<? extends KerningPairModel> list,
            final KerningPairModel value,
            final int index,
            final boolean isSelected,
            final boolean cellHasFocus
    ) {
        left.set(value.getLeft());
        right.set(value.getRight());

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
