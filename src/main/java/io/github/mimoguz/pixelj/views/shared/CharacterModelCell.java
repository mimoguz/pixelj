package io.github.mimoguz.pixelj.views.shared;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import io.github.mimoguz.pixelj.views.controls.GlyphView;
import io.github.mimoguz.pixelj.models.CharacterItem;
import io.github.mimoguz.pixelj.resources.Resources;

public class CharacterModelCell extends JPanel {
    private final CharacterCell characterCell = new CharacterCell();
    private final int maxPictureSize;
    private final GlyphView picture = new GlyphView(Resources.get().colors.disabledIcon());

    public CharacterModelCell() {
        this(48);
    }

    public CharacterModelCell(final int maxPictureSize) {
        this.maxPictureSize = maxPictureSize;

        characterCell.setOpaque(false);

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(Borders.LIST_ITEM);
        add(picture);
        add(Box.createHorizontalStrut(8));
        add(characterCell);
    }

    public void set(final CharacterItem model, final int contentWidth) {
        picture.setModel(model, false);

        final int pictureWidth = model.getGlyph().getWidth();
        final int pictureHeight = model.getGlyph().getHeight();
        final var pictureSize = Math.max(pictureWidth, pictureHeight);
        if (pictureSize > maxPictureSize) {
            picture.setZoom(0);
            final var scale = ((double) maxPictureSize) / pictureSize;
            picture.setSize(
                    new Dimension(
                            (int) Math.round(pictureWidth * scale),
                            (int) Math.round(pictureHeight * scale)
                    )
            );
        } else {
            picture.setZoom(1);
        }

        characterCell.set(
                model.getCodePoint(),
                Resources.get().getCharacterData(model.getCodePoint()).name(),
                contentWidth - pictureWidth - Dimensions.MEDIUM_PADDING * 4
        );
    }

    public void setBackgroundColor(final Color color) {
        characterCell.setBackgroundColor(color);
        setBackground(color);
    }

    public void setForegroundColor(final Color color) {
        characterCell.setForegroundColor(color);
        setForeground(color);
    }
}
