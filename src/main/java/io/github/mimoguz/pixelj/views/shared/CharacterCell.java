package io.github.mimoguz.pixelj.views.shared;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.formdev.flatlaf.FlatClientProperties;

import io.github.mimoguz.pixelj.controls.GlyphView;
import io.github.mimoguz.pixelj.models.CharacterModel;
import io.github.mimoguz.pixelj.resources.Resources;

public class CharacterCell extends JPanel {
    private static final long serialVersionUID = 5319221937480404986L;

    private final JLabel letter;
    private final int maxPictureSize;
    private final GlyphView picture;
    private final JLabel subtitle;
    private final JLabel title;
    private final JPanel titleBox;

    public CharacterCell() {
        this(48);
    }

    public CharacterCell(final int maxPictureSize) {
        this.maxPictureSize = maxPictureSize;

        letter = new JLabel(" ");
        picture = new GlyphView(Resources.get().colors.disabledIcon());
        subtitle = new JLabel(" ");
        title = new JLabel(" ");
        titleBox = new JPanel();

        final var letterSize = new Dimension(30, 30);
        letter.setMinimumSize(letterSize);
        letter.setPreferredSize(letterSize);

        subtitle.putClientProperty(FlatClientProperties.STYLE_CLASS, "small");

        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));
        titleBox.setOpaque(false);
        titleBox.setBackground(new Color(0, 0, 0, 0));
        titleBox.add(Box.createVerticalGlue());
        titleBox.add(subtitle);
        titleBox.add(title);
        titleBox.add(Box.createVerticalGlue());

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(Borders.mediumEmpty);
        add(picture);
        add(Box.createHorizontalStrut(8));
        add(letter);
        add(Box.createHorizontalStrut(8));
        add(titleBox);
        add(Box.createHorizontalGlue());
    }

    public void set(final CharacterModel model) {
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

        title.setText(Resources.get().getCharacterData(model.getCodePoint()).name());
        subtitle.setText("0x" + Integer.toHexString(model.getCodePoint()));
        letter.setText(Character.toString((char) model.getCodePoint()));
    }

    public void setBackgroundColor(final Color color) {
        titleBox.setBackground(color);
        setBackground(color);
    }

    public void setForegroundColor(final Color color) {
        title.setForeground(color);
        subtitle.setForeground(color);
        letter.setForeground(color);
        setForeground(color);
    }
}
