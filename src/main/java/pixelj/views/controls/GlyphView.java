package pixelj.views.controls;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;

import pixelj.graphics.BinaryImage;
import pixelj.models.CharacterItem;
import pixelj.util.ChangeListener;
import pixelj.util.Changeable;
import pixelj.util.ChangeableInt;
import pixelj.util.Detachable;
import pixelj.views.shared.Dimensions;

public class GlyphView extends JPanel
        implements
        Changeable<GlyphView, GlyphView.ViewChangeEvent, GlyphView.ViewChangeListener>,
        Detachable {
    private static final Color SHADE = new Color(242, 27, 63, 50);
    private final Color backgroundColor;
    private final ChangeableInt.Listener characterWidthChangeListener;
    private final BinaryImage.ImageListener imageChangeListener;
    private final ArrayList<Line> lines = new ArrayList<>();
    private final Set<ViewChangeListener> listeners = new HashSet<>();
    private boolean drawShade;
    /**
     * May be null;
     */
    private CharacterItem model;
    /**
     * May be null;
     */
    private Image overlay;
    private boolean showLines = false;
    private boolean showOverlay = false;
    private int top;
    private int zoom = 1;

    public GlyphView(final Color backgroundColor) {
        this.backgroundColor = backgroundColor;

        characterWidthChangeListener = (source, event) -> repaint();

        imageChangeListener = (source, event) -> {
            if (model == null || source != model.getGlyph()) {
                return;
            }
            // TODO: Repaint only changed region
            repaint();
            fireChangeEvent(this, ViewChangeEvent.GLYPH_MODIFIED);
        };
    }

    public void addLines(final Line... lines) {
        Arrays.stream(lines).forEach(line -> {
            if (line != null) {
                this.lines.add(line);
            }
        });
    }

    @Override
    public void detach() {
        if (model != null) {
            model.getGlyph().removeChangeListener(imageChangeListener);
        }
    }

    @Override
    public Set<ViewChangeListener> getListeners() {
        return listeners;
    }

    /**
     * @return Character model or null
     */
    public CharacterItem getModel() {
        return model;
    }

    /**
     * @param value May be null.
     */
    public void setModel(final CharacterItem value) {
        setModel(value, true);
    }

    /**
     * @return Overlay image or null
     */
    public Image getOverlay() {
        return overlay;
    }

    /**
     * @param value May be null
     */
    public void setOverlay(final Image value) {
        if (overlay == value) {
            return;
        }
        overlay = value;
        repaint();
    }

    public int getTop() {
        return top;
    }

    public void setTop(final int top) {
        this.top = top;
    }

    public int getZoom() {
        return zoom;
    }

    public void setZoom(final int value) {
        zoom = value;
        autoSize();
    }

    public boolean isLinesVisible() {
        return showLines;
    }

    public void setLinesVisible(final boolean value) {
        if (value == showLines) {
            return;
        }
        this.showLines = value;
        repaint();
    }

    public boolean isOverlayVisible() {
        return showOverlay;
    }

    public void setOverlayVisible(final boolean value) {
        if (value == showOverlay) {
            return;
        }
        this.showOverlay = value;
        repaint();
    }

    public boolean isShaded() {
        return drawShade;
    }

    public void setShaded(final boolean value) {
        this.drawShade = value;
    }

    @Override
    public void paintComponent(final Graphics graphics) {
        final var g2d = (Graphics2D) graphics.create();
        if (model != null) {
            g2d.setRenderingHint(
                    RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR
            );
            model.getGlyph().draw(g2d, 0, 0, getWidth(), getHeight());
            drawShade(g2d);
            drawOverlay(g2d);
            drawLines(g2d);
        } else {
            g2d.setColor(backgroundColor);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
        g2d.dispose();
    }

    public void removeLines() {
        lines.clear();
        repaint();
    }

    public void removeLines(final Line... lines) {
        this.lines.removeAll(Arrays.stream(lines).toList());
        repaint();
    }

    /**
     * @param value  May be null.
     * @param listen If true, the view will add an imageChangeListener to the glyph.
     */
    public void setModel(final CharacterItem value, final boolean listen) {
        if (model == value) {
            return;
        }
        if (model != null) {
            model.getGlyph().removeChangeListener(imageChangeListener);
            model.widthProperty.removeChangeListener(characterWidthChangeListener);
            fireChangeEvent(this, ViewChangeEvent.MODEL_UNLOADED);
            if (top <= 0) {
                top = model.getGlyph().getHeight();
            }
        }

        this.model = value;

        if (value != null) {
            if (listen) {
                value.getGlyph().addChangeListener(imageChangeListener);
                value.widthProperty.addChangeListener(characterWidthChangeListener);
            }
            fireChangeEvent(this, ViewChangeEvent.MODEL_LOADED);
        }

        autoSize();
    }

    private void autoSize() {
        if (zoom > 0) {
            final var dimension = model != null
                    ? new Dimension(model.getGlyph().getWidth() * zoom, model.getGlyph().getHeight() * zoom)
                    : Dimensions.LARGE_SQUARE;
            setMinimumSize(dimension);
            setMaximumSize(dimension);
            setPreferredSize(dimension);
            setSize(dimension);
        }
        revalidate();
        repaint();
    }

    private void drawLines(final Graphics2D g) {
        if (lines.isEmpty() || !showLines || model == null) {
            return;
        }

        final var glyph = model.getGlyph();
        final var w = glyph.getWidth();
        final var h = glyph.getHeight();
        final var dx = getWidth() / (double) w;
        final var dy = getHeight() / (double) h;
        for (final var line : lines) {
            g.setColor(line.color());
            if (line.orientation() == Orientation.HORIZONTAL) {
                final var y = (int) Math.round(line.point() * dy);
                g.drawLine(0, y, getWidth(), y);
            } else {
                final var x = (int) Math.round(line.point() * dx);
                g.drawLine(x, 0, x, getHeight());
            }
        }
    }

    private void drawOverlay(final Graphics2D g) {
        if (overlay == null || !showOverlay) {
            return;
        }
        g.drawImage(overlay, 0, 0, getWidth(), getHeight(), null);
    }

    private void drawShade(final Graphics2D g) {
        if (model == null || !drawShade) {
            return;
        }

        g.setColor(SHADE);

        final var x = (int) Math
                .round((((double) getWidth()) / model.getGlyph().getWidth()) * model.getWidth());
        g.fillRect(x, 0, getWidth() - x, getHeight());

        final var y = (int) Math.round(
                (((double) getHeight()) / model.getGlyph().getHeight()) * (model.getGlyph().getHeight() - top)
        );
        g.fillRect(0, 0, x, y);
    }

    public enum ViewChangeEvent {
        GLYPH_MODIFIED, MODEL_LOADED, MODEL_UNLOADED
    }

    public interface ViewChangeListener extends ChangeListener<GlyphView, ViewChangeEvent> {
        // Empty
    }
}
