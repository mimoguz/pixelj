package io.github.pixelj.views;

import io.github.pixelj.controls.GlyphView;
import io.github.pixelj.models.CharacterModel;
import io.github.pixelj.resources.Resources;

import javax.swing.*;
import java.awt.*;

public class CharacterCell extends JPanel {
    private final JLabel letter = new JLabel(" ");
    private final GlyphView picture = new GlyphView(Resources.get().colors.disabledIcon());
    private final JLabel subtitle = new JLabel(" ");
    private final JLabel title = new JLabel(" ");
    private final JPanel titleBox = new JPanel();

    public CharacterCell() {

    }

    public void fill(CharacterModel model) {
        final var pictureSize = Math.max(model.getGlyph().getWidth(), model.getGlyph().getHeight());
        picture.setModel(model);
        
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
