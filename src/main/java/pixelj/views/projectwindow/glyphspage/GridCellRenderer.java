package pixelj.views.projectwindow.glyphspage;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import pixelj.models.Glyph;
import pixelj.resources.Resources;
import pixelj.views.controls.GlyphView;
import pixelj.views.controls.Grid;
import pixelj.views.shared.Borders;
import pixelj.views.shared.Components;
import pixelj.views.shared.Dimensions;

import javax.swing.*;
import java.awt.*;

public class GridCellRenderer implements Grid.GridCellRenderer<Glyph> {

    private final JPanel component = new JPanel();
    private final JPanel emptyComponent = new JPanel();
    private final JLabel title = new JLabel();
    private final JLabel emptyCellLabel = new JLabel();
    private final GlyphView picture = new GlyphView(Resources.get().colors.disabledIcon());

    public GridCellRenderer() {
        component.setLayout(new MigLayout(
            "insets 2lp 4lp 8lp 2lp",
            "[center, grow]",
            "[top]6lp[center, grow]"
        ));

        title.setHorizontalTextPosition(SwingConstants.CENTER);
        title.putClientProperty(FlatClientProperties.STYLE_CLASS, "small");
        component.add(title, "wrap");

        final var pictureFrame = new JPanel();
        pictureFrame.setBorder(Borders.SMALL_EMPTY);
        pictureFrame.setBackground(Color.WHITE);
        Components.addOuterBorder(pictureFrame, BorderFactory.createMatteBorder(1, 1, 1, 1, Resources.get().colors.separator()));
        pictureFrame.add(picture);
        component.add(pictureFrame);

        emptyComponent.setLayout(new MigLayout(
            "",
            "[center, grow, fill]",
            "[center, grow, fill]"
        ));
        emptyCellLabel.putClientProperty(FlatClientProperties.STYLE_CLASS, "h3");
        emptyComponent.add(emptyCellLabel);
    }

    @Override
    public JComponent getComponent(final Glyph value, final int index) {
        if (value == null) {
            return emptyComponent;
        }
        title.setText((char) value.getCodePoint() + " (0x" + Integer.toHexString(value.getCodePoint()) + ')');
        picture.setModel(value, false);
        fixPictureSize(value);
        return component;
    }

    @Override
    public JComponent getEmpty(final String message) {
        emptyCellLabel.setText(message);
        return emptyComponent;
    }

    @Override
    public Dimension getFixedSize(final Glyph sample) {
        final var pictureSize = fixPictureSize(sample);
        final var w = 2 + Math.max(60, pictureSize.width) + 2 * Dimensions.SMALL_PADDING + 2 + 2;
        final var h = 4 + 20 + 6 + pictureSize.height + 2 * Dimensions.SMALL_PADDING + 2 + 8;
        return new Dimension(w, h);
    }

    private Dimension fixPictureSize(final Glyph model) {
        final int imageWidth = model.getImage().getImageWidth();
        final int imageHeight = model.getImage().getImageHeight();
        final var imageSize = Math.max(imageWidth, imageHeight);
        picture.setModel(model, false);
        if (imageSize > Dimensions.MAXIMUM_PREVIEW_SIZE) {
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
        return picture.getPreferredSize();
    }
}
