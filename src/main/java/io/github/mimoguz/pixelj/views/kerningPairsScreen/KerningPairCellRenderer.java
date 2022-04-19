package io.github.mimoguz.pixelj.views.kerningPairsScreen;

import io.github.mimoguz.pixelj.models.KerningPairModel;
import io.github.mimoguz.pixelj.resources.Resources;
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

        var background = list.getBackground();
        var foreground = list.getForeground();

        if (cellHasFocus && isSelected) {
            background = Resources.get().colors.focusBackground();
            foreground = Resources.get().colors.focusForeground();
        } else if (isSelected) {
            background = list.getSelectionBackground();
            foreground = list.getSelectionForeground();
        }

        left.setBackground(background);
        left.setForeground(foreground);

        right.setBackground(background);
        right.setForeground(foreground);

        component.setBackground(background);

        return component;
    }
}
