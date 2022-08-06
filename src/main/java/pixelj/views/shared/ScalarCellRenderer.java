package pixelj.views.shared;

import java.awt.Component;

import javax.swing.JList;
import javax.swing.ListCellRenderer;

import pixelj.models.Scalar;
import pixelj.resources.Resources;

public final class ScalarCellRenderer implements ListCellRenderer<Scalar> {
    private final ScalarCell component = new ScalarCell();

    public ScalarCellRenderer() {
        component.setBorder(Borders.LIST_ITEM);
    }

    @Override
    public Component getListCellRendererComponent(
        final JList<? extends Scalar> list,
        final Scalar value,
        final int index,
        final boolean isSelected,
        final boolean cellHasFocus
    ) {
        component.set(value.codePoint(), Resources.get().getScalar(value.codePoint()).name());
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
