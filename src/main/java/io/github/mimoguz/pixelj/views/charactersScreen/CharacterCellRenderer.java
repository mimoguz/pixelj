package io.github.mimoguz.pixelj.views.charactersScreen;

import io.github.mimoguz.pixelj.models.CharacterModel;
import io.github.mimoguz.pixelj.resources.Resources;
import io.github.mimoguz.pixelj.views.shared.CharacterCell;

import javax.swing.*;
import java.awt.*;

public class CharacterCellRenderer implements ListCellRenderer<CharacterModel> {
    private final CharacterCell cell;

    public CharacterCellRenderer(final int maxPictureSize) {
        cell = new CharacterCell(maxPictureSize);
    }

    @Override
    public Component getListCellRendererComponent(
            final JList<? extends CharacterModel> list,
            final CharacterModel value,
            final int index,
            final boolean isSelected,
            final boolean cellHasFocus
    ) {
        cell.set(value);
        if (cellHasFocus && isSelected) {
            cell.setBackground(Resources.get().colors.focusBackground());
            cell.setForeground(Resources.get().colors.focusForeground());
        } else if (isSelected) {
            cell.setBackground(list.getSelectionBackground());
            cell.setForeground(list.getSelectionForeground());
        } else {
            cell.setBackground(list.getBackground());
            cell.setForeground(list.getForeground());
        }
        return cell;
    }
}
