package pixelj.views.shared;

import java.awt.Component;

import javax.swing.JList;
import javax.swing.ListCellRenderer;

import pixelj.models.ScalarRecord;
import pixelj.resources.Resources;

public class ScalarCellRenderer implements ListCellRenderer<ScalarRecord> {
    private final ScalarCell component = new ScalarCell();

    public ScalarCellRenderer() {
        component.setBorder(Borders.LIST_ITEM);
    }

    @Override
    public Component getListCellRendererComponent(
            final JList<? extends ScalarRecord> list,
            final ScalarRecord value,
            final int index,
            final boolean isSelected,
            final boolean cellHasFocus
    ) {
        component.set(
                value.codePoint(),
                Resources.get().getScalar(value.codePoint()).name(),
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