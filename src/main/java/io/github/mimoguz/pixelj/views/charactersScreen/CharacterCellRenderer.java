package io.github.mimoguz.pixelj.views.charactersScreen;

import io.github.mimoguz.pixelj.models.CharacterModel;
import io.github.mimoguz.pixelj.resources.Resources;
import io.github.mimoguz.pixelj.views.shared.CharacterCell;

import javax.swing.*;
import java.awt.*;

public class CharacterCellRenderer implements ListCellRenderer<CharacterModel> {
    private final CharacterCell component;

    public CharacterCellRenderer(final int maxPictureSize) {
        component = new CharacterCell(maxPictureSize);
    }

    @Override
    public Component getListCellRendererComponent(
            final JList<? extends CharacterModel> list,
            final CharacterModel value,
            final int index,
            final boolean isSelected,
            final boolean cellHasFocus
    ) {
        component.set(value);
        if (cellHasFocus && isSelected) {
            component.setBackground(Resources.get().colors.focusBackground());
            component.setForeground(Resources.get().colors.focusForeground());
        } else if (isSelected) {
            component.setBackground(list.getSelectionBackground());
            component.setForeground(list.getSelectionForeground());
        } else {
            component.setBackground(list.getBackground());
            component.setForeground(list.getForeground());
        }
        return component;
    }
}
