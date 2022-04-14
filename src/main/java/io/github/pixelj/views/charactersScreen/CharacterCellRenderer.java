package io.github.pixelj.views.charactersScreen;

import io.github.pixelj.models.CharacterModel;
import io.github.pixelj.resources.Resources;
import io.github.pixelj.views.shared.CharacterCell;

import javax.swing.*;
import java.awt.*;

public class CharacterCellRenderer implements ListCellRenderer<CharacterModel> {
    private final CharacterCell cell;

    public CharacterCellRenderer(int maxPictureSize) {
        cell = new CharacterCell(maxPictureSize);
    }

    @Override
    public Component getListCellRendererComponent(
            JList<? extends CharacterModel> list,
            CharacterModel value,
            int index,
            boolean isSelected,
            boolean cellHasFocus
    ) {
        cell.set(value);
        cell.setForeground(isSelected
                ? (cellHasFocus ? Resources.get().colors.focusForeground() : list.getSelectionForeground())
                : list.getForeground()
        );
        cell.setBackground(isSelected
                ? (cellHasFocus ? Resources.get().colors.focusBackground() : list.getSelectionBackground())
                : list.getBackground()
        );
        return cell;
    }
}
