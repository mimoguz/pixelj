package pixelj.views.controls;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JPanel;

import pixelj.models.Glyph;
import pixelj.views.shared.Dimensions;

public class StringView extends JPanel {
    private final Color backgroundColor;
    private final ArrayList<Glyph> characters = new ArrayList<>();
    private int lineSpacing;
    private int maxY;
    private int padding;
    private BufferedImage renderTarget;
    private final ArrayList<Integer> spaces = new ArrayList<>();
    private int zoom;

    public StringView(final Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        setZoom(1);
    }

    public int getLineSpacing() {
        return lineSpacing;
    }

    public int getMaxY() {
        return maxY;
    }

    public int getPadding() {
        return padding;
    }

    public int getZoom() {
        return zoom;
    }

    public void set(Collection<Glyph> characters, Collection<Integer> spaces) {
        this.characters.clear();
        this.characters.addAll(characters);

        this.spaces.clear();
        this.spaces.addAll(spaces);

        updateView();
    }

    public void setLineSpacing(int lineSpacing) {
        this.lineSpacing = lineSpacing;
        updateView();
    }

    public void setMaxY(int maxY) {
        this.maxY = maxY;
        updateView();
    }

    public void setPadding(final int value) {
        padding = value;
        resizeCanvas();
        repaint();
    }

    public void setSpaces(Collection<Integer> spaces) {
        this.spaces.clear();
        this.spaces.addAll(spaces);
        updateView();
    }

    public void setZoom(final int value) {
        zoom = value;
        resizeCanvas();
        repaint();
    }

    public void updateView() {
        renderString();
        resizeCanvas();
        repaint();
    }

    @SuppressWarnings("SameParameterValue")
    private void drawCharacter(final BufferedImage target, final Glyph character, final int x, final int y) {
        final var w = character.getWidth();
        final var sourceBuffer = new byte[w];
        final var targetBuffer = new int[w];
        final var source = character.getImage();
        final var firstLine = maxY > 0 ? source.getHeight() - maxY : 0;
        for (var sourceLine = firstLine; sourceLine < source.getHeight(); sourceLine++) {
            final var targetLine = sourceLine - firstLine;
            if (targetLine >= target.getHeight() - lineSpacing) {
                break;
            }
            source.getRaster().getDataElements(0, sourceLine, w, 1, sourceBuffer);
            target.getRaster().getDataElements(x, y + targetLine, w, 1, targetBuffer);
            for (var i = 0; i < w; i++) {
                targetBuffer[i] = targetBuffer[i] & (sourceBuffer[i] == 1 ? 0xff_ff_ff_ff : 0);
            }
            target.getRaster().setDataElements(x, y + targetLine, w, 1, targetBuffer);
        }
    }

    private void renderString() {
        if (characters.isEmpty()) {
            renderTarget = null;
            return;
        }

        final var w = characters.stream().mapToInt(Glyph::getWidth).sum()
                + spaces.stream().mapToInt(i -> i).limit(characters.size()).reduce(0, Integer::sum);

        var h = characters.stream()
                .mapToInt(chr -> chr.getImage() == null ? 0 : chr.getImage().getHeight())
                .max()
                .orElse(0);

        if (maxY > 0) {
            h = Math.min(h, maxY);
        }

        if (w == 0 || h == 0) {
            renderTarget = null;
            return;
        }

        renderTarget = new BufferedImage(w, h + lineSpacing, BufferedImage.TYPE_INT_RGB);
        final var g = (Graphics2D) renderTarget.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, renderTarget.getWidth(), renderTarget.getHeight());
        var x = 0;
        for (var index = 0; index < characters.size(); index++) {
            final var character = characters.get(index);
            if (character.getImage() == null) {
                // Non-printable character. Spaces should handle that.
                x += spaces.size() > index ? spaces.get(index) : 0;
                continue;
            }
            drawCharacter(renderTarget, character, x, 0);
            x += characters.get(index).getWidth() + (spaces.size() > index ? spaces.get(index) : 0);
        }
    }

    private void resizeCanvas() {
        var dimensions = renderTarget == null ? Dimensions.LARGE_SQUARE
                : new Dimension(
                        renderTarget.getWidth() * zoom + 2 * padding,
                        renderTarget.getHeight() * zoom + 2 * padding
                );
        setMinimumSize(dimensions);
        setMaximumSize(dimensions);
        setPreferredSize(dimensions);
        setSize(dimensions);
        revalidate();
    }

    @Override
    protected void paintComponent(final Graphics graphics) {
        final var g2d = (Graphics2D) graphics.create();
        if (characters.isEmpty() || renderTarget == null) {
            g2d.setColor(backgroundColor);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        } else {
            // Composite based drawing fails with NotImplementedException on Linux.
            // This method is more naive, but at least works.
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.drawImage(
                    renderTarget,
                    padding,
                    padding,
                    renderTarget.getWidth() * zoom,
                    renderTarget.getHeight() * zoom,
                    backgroundColor,
                    null
            );
        }
        g2d.dispose();
    }
}
