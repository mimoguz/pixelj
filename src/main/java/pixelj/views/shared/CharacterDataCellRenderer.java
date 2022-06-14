package pixelj.views.shared;

import java.awt.Component;

import javax.swing.JList;
import javax.swing.ListCellRenderer;

import pixelj.models.CharacterData;
import pixelj.resources.Resources;

public class CharacterDataCellRenderer implements ListCellRenderer<CharacterData> {
    private final CharacterCell component = new CharacterCell();

    public CharacterDataCellRenderer() {
        component.setBorder(Borders.LIST_ITEM);
    }

    @Override
    public Component getListCellRendererComponent(
            final JList<? extends CharacterData> list,
            final CharacterData value,
            final int index,
            final boolean isSelected,
            final boolean cellHasFocus
    ) {
        component.set(
                value.codePoint(),
                Resources.get().getCharacterData(value.codePoint()).name(),
                list.getWidth() - Dimensions.MEDIUM_PADDING * 3
        );
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