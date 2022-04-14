package io.github.pixelj.views.shared;

import io.github.pixelj.controls.GlyphView;
import io.github.pixelj.models.CharacterModel;
import io.github.pixelj.resources.Resources;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class CharacterCell extends JPanel {
    private final JLabel letter = new JLabel(" ");
    private final int maxPictureSize;
    private final GlyphView picture = new GlyphView(Resources.get().colors.disabledIcon());
    private final JLabel subtitle = new JLabel(" ");
    private final JLabel title = new JLabel(" ");
    private final JPanel titleBox = new JPanel();

    public CharacterCell(int maxPictureSize) {
        this.maxPictureSize = maxPictureSize;

        final var letterSize = new Dimension(30, 30);
        letter.setMinimumSize(letterSize);
        letter.setPreferredSize(letterSize);

        titleBox.setLayout(new BoxLayout(title, BoxLayout.Y_AXIS));
        titleBox.setOpaque(false);
        titleBox.setBackground(new Color(0, 0, 0, 0));
        titleBox.add(Box.createVerticalGlue());
        titleBox.add(title);
        titleBox.add(subtitle);
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

    public void set(@NotNull CharacterModel model) {
        picture.setModel(model, false);

        final var pictureSize = Math.max(model.getGlyph().getWidth(), model.getGlyph().getHeight());
        if (pictureSize > maxPictureSize) {
            picture.setZoom(0);
            final var scale = ((double) maxPictureSize) / pictureSize;
            final var w = (int) Math.round(model.getGlyph().getWidth() * scale);
            final var h = (int) Math.round(model.getGlyph().getHeight() * scale);
            picture.setSize(new Dimension(w, h));
        } else {
            picture.setZoom(1);
        }

        title.setText(Integer.toString(model.getCodePoint()));
        subtitle.setText("Description here");
        letter.setText(Character.toString((char) model.getCodePoint()));
    }

    @Override
    public void setBackground(Color color) {
        titleBox.setBackground(color);
        super.setBackground(color);
    }

    @Override
    public void setForeground(Color color) {
        title.setForeground(color);
        subtitle.setForeground(color);
        letter.setForeground(color);
        super.setForeground(color);
    }
}
