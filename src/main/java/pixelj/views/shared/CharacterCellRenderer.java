package pixelj.views.shared;

import java.awt.Component;

import javax.swing.JList;
import javax.swing.ListCellRenderer;

import pixelj.models.CharacterItem;

public class CharacterCellRenderer implements ListCellRenderer<CharacterItem> {
    private final CharacterModelCell component;

    public CharacterCellRenderer(final int maxPictureSize) {
        component = new CharacterModelCell(maxPictureSize);
    }

    @Override
    public Component getListCellRendererComponent(
            final JList<? extends CharacterItem> list,
            final CharacterItem value,
            final int index,
            final boolean isSelected,
            final boolean cellHasFocus
    ) {
        component.set(value, list.getWidth());
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
