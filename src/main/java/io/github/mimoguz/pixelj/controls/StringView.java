package io.github.mimoguz.pixelj.controls;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JPanel;

import io.github.mimoguz.pixelj.graphics.AndComposite;
import io.github.mimoguz.pixelj.models.CharacterModel;

public class StringView extends JPanel {
    private final Color backgroundColor;
    private final ArrayList<CharacterModel> characters = new ArrayList<>();
    private final AndComposite composite = new AndComposite();
    private Dimension minimumSize = new Dimension(16, 16);
    private int padding = 8;
    private final ArrayList<Integer> spaces = new ArrayList<>();
    private int zoom;

    public StringView(final Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        setZoom(1);
    }

    public int getPadding() {
        return padding;
    }

    public int getZoom() {
        return zoom;
    }

    public void set(Collection<CharacterModel> characters, Collection<Integer> spaces) {
        this.characters.clear();
        this.characters.addAll(characters);

        this.spaces.clear();
        this.spaces.addAll(spaces);

        updateView();
    }

    public void setPadding(final int value) {
        padding = value;
        minimumSize = new Dimension(value * 2, value * 2);
        updateView();
    }

    public void setSpaces(Collection<Integer> spaces) {
        this.spaces.clear();
        this.spaces.addAll(spaces);
        updateView();
    }

    public void setZoom(final int value) {
        zoom = value;
        updateView();
    }

    private void resizeCanvas() {
        var dimensions = minimumSize;

        if (characters.size() > 0) {
            final var w = characters.stream().mapToInt(CharacterModel::getWidth).sum()
                    + spaces.stream().limit(characters.size()).reduce(0, Integer::sum);
            final var h = characters.stream()
                    .mapToInt(chr -> chr.getGlyph().getHeight())
                    .max()
                    .orElseGet(() -> 0);
            dimensions = new Dimension(w + 2 * padding, h + 2 * padding);
        }

        setMinimumSize(dimensions);
        setMinimumSize(dimensions);
        setPreferredSize(dimensions);
        setSize(dimensions);
        revalidate();
    }

    private void updateView() {
        resizeCanvas();
        repaint();
    }

    @Override
    protected void paintComponent(final Graphics graphics) {
        final var g2d = (Graphics2D) graphics.create();
        g2d.setColor(backgroundColor);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        g2d.setComposite(composite);

        var x = 0;
        for (var index = 0; index < characters.size(); index++) {
            final var chr = characters.get(index);
            final var image = chr.getGlyph();
            g2d.drawImage(
                    image.getSubImage(0, 0, chr.getWidth(), image.getHeight()),
                    x,
                    0,
                    chr.getWidth() * zoom,
                    image.getHeight() * zoom,
                    backgroundColor,
                    null
            );
            x += (chr.getWidth() + spaces.size() > index ? spaces.get(index) : 0) * zoom;
        }

        g2d.dispose();
    }
}
