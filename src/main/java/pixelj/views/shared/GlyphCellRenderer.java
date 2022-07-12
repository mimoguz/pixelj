package pixelj.views.shared;

import java.awt.Component;

import javax.swing.JList;
import javax.swing.ListCellRenderer;

import pixelj.models.Glyph;

public final class GlyphCellRenderer implements ListCellRenderer<Glyph> {
    private final GlyphCell component;

    public GlyphCellRenderer(final int maxPictureSize) {
        component = new GlyphCell(maxPictureSize);
    }

    @Override
    public Component getListCellRendererComponent(
            final JList<? extends Glyph> list,
            final Glyph value,
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
