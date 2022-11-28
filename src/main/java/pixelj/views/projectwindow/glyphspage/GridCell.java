package pixelj.views.projectwindow.glyphspage;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import pixelj.models.Glyph;
import pixelj.resources.Resources;
import pixelj.views.controls.GlyphView;
import pixelj.views.shared.Borders;
import pixelj.views.shared.Components;
import pixelj.views.shared.Dimensions;

import javax.swing.*;
import java.awt.*;

public class GridCell extends JPanel {

    GridCell(final Glyph glyph) {
        setLayout(new MigLayout("insets 6lp 8lp 10lp 8lp", "[center, grow]", "[top]4lp[center, grow]"));

        final var cellTitle = new JLabel((char) glyph.getCodePoint() + " (0x" + Integer.toHexString(glyph.getCodePoint()) + ')');
        cellTitle.setHorizontalTextPosition(SwingConstants.CENTER);
        cellTitle.putClientProperty(FlatClientProperties.STYLE_CLASS, "small");
        add(cellTitle, "wrap");


        final var picture = new GlyphView(Resources.get().colors.disabledIcon());
        final int imageWidth = glyph.getImage().getImageWidth();
        final int imageHeight = glyph.getImage().getImageHeight();
        final var imageSize = Math.max(imageWidth, imageHeight);
        if (imageSize> Dimensions.MAXIMUM_PREVIEW_SIZE) {
            picture.setZoom(0);
            final var scale = ((double) Dimensions.MAXIMUM_PREVIEW_SIZE) / imageSize;
            Components.setFixedSize(
                picture,
                new Dimension(
                    (int) Math.round(imageWidth * scale),
                    (int) Math.round(imageHeight * scale)
                )
            );
        } else {
            picture.setZoom(1);
        }
        picture.setModel(glyph, false);

        final var pictureBox = new JPanel();
        pictureBox.setBorder(Borders.SMALL_EMPTY);
        pictureBox.setBackground(Color.WHITE);
        pictureBox.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Resources.get().colors.separator()));
        pictureBox.add(picture);
        add(pictureBox);
    }
}
