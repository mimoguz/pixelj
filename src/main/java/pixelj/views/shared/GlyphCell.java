package pixelj.views.shared;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import pixelj.models.Glyph;
import pixelj.resources.Resources;
import pixelj.views.controls.GlyphView;

public final class GlyphCell extends JPanel {
    private final ScalarCell scalarCell = new ScalarCell();
    private final int maxPictureSize;
    private final GlyphView picture = new GlyphView(Resources.get().colors.disabledIcon());

    public GlyphCell() {
        this(48);
    }

    public GlyphCell(final int maxPictureSize) {
        this.maxPictureSize = maxPictureSize;

        scalarCell.setOpaque(false);

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(Borders.LIST_ITEM);
        add(picture);
        add(Box.createHorizontalStrut(8));
        add(scalarCell);
    }

    /**
     * @param model
     * @param contentWidth The width of the cell
     */
    public void set(final Glyph model, final int contentWidth) {
        picture.setModel(model, false);

        final int pictureWidth = model.getImage().getImageWidth();
        final int pictureHeight = model.getImage().getImageHeight();
        final var pictureSize = Math.max(pictureWidth, pictureHeight);
        if (pictureSize > maxPictureSize) {
            picture.setZoom(0);
            final var scale = ((double) maxPictureSize) / pictureSize;
            Components.setFixedSize(
                    picture,
                    new Dimension(
                            (int) Math.round(pictureWidth * scale),
                            (int) Math.round(pictureHeight * scale)
                    )
            );
        } else {
            picture.setZoom(1);
        }

        scalarCell.set(
                model.getCodePoint(),
                Resources.get().getScalar(model.getCodePoint()).name(),
                contentWidth - pictureWidth - Dimensions.MEDIUM_PADDING * 4
        );
    }

    public void setBackgroundColor(final Color color) {
        scalarCell.setBackgroundColor(color);
        setBackground(color);
    }

    public void setForegroundColor(final Color color) {
        scalarCell.setForegroundColor(color);
        setForeground(color);
    }
}
