package io.github.mimoguz.pixelj.views.characters_screen;

import java.awt.Component;

import javax.swing.JList;
import javax.swing.ListCellRenderer;

import io.github.mimoguz.pixelj.models.CharacterModel;
import io.github.mimoguz.pixelj.views.shared.CharacterCell;

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
        if (isSelected) {
            component.setBackgroundColor(list.getSelectionBackground());
            component.setForegroundColor(list.getSelectionForeground());
        } else {
            component.setBackgroundColor(list.getBackground());
            component.setForegroundColor(list.getForeground());
        }
        return component;
    }
}
