package io.github.mimoguz.pixelj.controls;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JPanel;

import io.github.mimoguz.pixelj.models.CharacterModel;
import io.github.mimoguz.pixelj.views.shared.Dimensions;

public class StringView extends JPanel {
    private static final long serialVersionUID = 5414891564267425303L;

    private final Color backgroundColor;
    private final ArrayList<CharacterModel> characters = new ArrayList<>();
    private int padding;
    private final ArrayList<Integer> spaces = new ArrayList<>();
    private int zoom;
    private static BufferedImage empty = new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
    private transient BufferedImage renderTarget;

    public StringView(final Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        renderTarget = empty;
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
        var dimensions = Dimensions.LARGE_SQUARE;

        if (!characters.isEmpty()) {
            var w = characters.stream().mapToInt(CharacterModel::getWidth).sum()
                    + spaces.stream().mapToInt(i -> i).limit(characters.size()).reduce(0, Integer::sum);

            var h = characters.stream()
                    .mapToInt(chr -> chr.getGlyph() == null ? 0 : chr.getGlyph().getHeight())
                    .max()
                    .orElseGet(() -> 0);

            if (w != 0 && h != 0) {
                renderTarget = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                dimensions = new Dimension(w * zoom + 2 * padding, h * zoom + 2 * padding);
            } else {
                renderTarget = empty;
            }
        }

        setMinimumSize(dimensions);
        setMaximumSize(dimensions);
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
        if (characters.isEmpty()) {
            g2d.setColor(backgroundColor);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        } else {
            // Composite based drawing fails with NotImplementedException on Linux.
            // This method is more naive, but at least works.
            g2d.setColor(Color.WHITE);
            renderTarget.getGraphics().fillRect(0, 0, renderTarget.getWidth(), renderTarget.getHeight());
            var x = 0;
            for (var index = 0; index < characters.size(); index++) {
                final var character = characters.get(index);
                if (character.getGlyph() == null) {
                    // Non-printable character. Spaces should handle that.
                    x += spaces.size() > index ? spaces.get(index) : 0;
                    continue;
                }

                drawCharacter(renderTarget, character, x, 0);
                x += characters.get(index).getWidth() + (spaces.size() > index ? spaces.get(index) : 0);
            }
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.drawImage(
                    renderTarget,
                    padding,
                    padding,
                    getWidth() - 2 * padding,
                    getHeight() - 2 * padding,
                    backgroundColor,
                    null
            );
        }
        g2d.dispose();
    }

    private void drawCharacter(
            final BufferedImage target,
            final CharacterModel character,
            final int x,
            final int y
    ) {
        final var w = character.getWidth();
        final var sourceBuffer = new byte[w];
        final var targetBuffer = new int[w];
        final var image = character.getGlyph();
        for (var line = 0; line < image.getHeight(); line++) {
            image.getRaster().getDataElements(0, line, w, 1, sourceBuffer);
            target.getRaster().getDataElements(x, y + line, w, 1, targetBuffer);
            for (var i = 0; i < w; i++) {
                targetBuffer[i] = targetBuffer[i] & (sourceBuffer[i] == 1 ? 0xff_ff_ff_ff : 0);
            }
            target.getRaster().setDataElements(x, y + line, w, 1, targetBuffer);
        }
    }
}
