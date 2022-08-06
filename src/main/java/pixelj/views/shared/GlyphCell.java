package pixelj.views.shared;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
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

        setLayout(new MigLayout(
            "insets 0",
            String.format("[min!]%dlp[grow, left]", Dimensions.MEDIUM_PADDING),
            "[min!]"
        ));
        add(picture);
        add(scalarCell);
        setBorder(Borders.LIST_ITEM);
    }

    /**
     * @param model
     */
    public void set(final Glyph model) {
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

        scalarCell.set(model.getCodePoint(), Resources.get().getScalar(model.getCodePoint()).name());
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
