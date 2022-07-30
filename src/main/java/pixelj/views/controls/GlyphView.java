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
import pixelj.models.Glyph;
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
    /** May be null. */
    private Glyph model;
    /** May be null. */
    private Image overlay;
    private boolean showLines;
    private boolean showOverlay;
    private int top;
    private int zoom = 1;

    public GlyphView(final Color backgroundColor) {
        this.backgroundColor = backgroundColor;

        characterWidthChangeListener = (source, event) -> repaint();

        imageChangeListener = (source, event) -> {
            if (model == null || source != model.getImage()) {
                return;
            }
            // TODO: Repaint only changed region
            repaint();
            fireChangeEvent(this, ViewChangeEvent.GLYPH_MODIFIED);
        };
    }

    /**
     * @param newLines The lines to be added
     */
    public final void addLines(final Line... newLines) {
        Arrays.stream(newLines).forEach(line -> {
            if (line != null) {
                lines.add(line);
            }
        });
    }

    /**
     * @return Character model or null
     */
    public Glyph getModel() {
        return model;
    }

    /**
     * @param value May be null.
     */
    public void setModel(final Glyph value) {
        setModel(value, true);
    }

    /**
     * @param value  May be null.
     * @param listen If true, the view will add an imageChangeListener to the glyph.
     */
    public void setModel(final Glyph value, final boolean listen) {
        if (model == value) {
            return;
        }
        if (model != null) {
            model.getImage().removeChangeListener(imageChangeListener);
            model.widthProperty.removeChangeListener(characterWidthChangeListener);
            fireChangeEvent(this, ViewChangeEvent.MODEL_UNLOADED);
            if (top <= 0) {
                top = model.getImage().getImageHeight();
            }
        }

        this.model = value;

        if (value != null) {
            if (listen) {
                value.getImage().addChangeListener(imageChangeListener);
                value.widthProperty.addChangeListener(characterWidthChangeListener);
            }
            fireChangeEvent(this, ViewChangeEvent.MODEL_LOADED);
        }

        autoSize();
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

    /**
     * @return Top of the glyph area
     */
    public int getTop() {
        return top;
    }

    /**
     * @param top Top of the glyph. That value is used to calculate the shaded area.
     */
    public void setTop(final int top) {
        this.top = top;
    }

    /**
     * @return Zoom value of the view. If it's zero, auto-resizing is disabled.
     */
    public int getZoom() {
        return zoom;
    }

    /**
     * @param value Zoom value of the view. If it's zero, auto-resizing will be disabled.
     */
    public void setZoom(final int value) {
        zoom = value;
        autoSize();
    }

    /**
     * @return The visibility of the guide lines
     */
    public boolean isLinesVisible() {
        return showLines;
    }

    /**
     * @param value  The visibility of the guide lines
     */
    public void setLinesVisible(final boolean value) {
        if (value == showLines) {
            return;
        }
        this.showLines = value;
        repaint();
    }

    /**
     * @return The visibility of the overlay image, if it exists.
     */
    public boolean isOverlayVisible() {
        return showOverlay;
    }

    /**
     * @param value The visibility of the overlay image, if it exists.
     */
    public void setOverlayVisible(final boolean value) {
        if (value == showOverlay) {
            return;
        }
        this.showOverlay = value;
        repaint();
    }

    /**
     * @return The visibility of the shade, the "outside" of the area.
     */
    public boolean isShaded() {
        return drawShade;
    }

    /**
     * @param value The visibility of the shade, the "outside" of the area.
     */
    public void setShaded(final boolean value) {
        this.drawShade = value;
    }

    /** Removes all guide lines. */
    public void removeLines() {
        lines.clear();
        repaint();
    }

    /**
     * @param linesToRemove Guide lines to be removed
     */
    public void removeLines(final Line... linesToRemove) {
        this.lines.removeAll(Arrays.stream(linesToRemove).toList());
        repaint();
    }

    /** Remove hard references. */
    @Override
    public void detach() {
        if (model != null) {
            model.getImage().removeChangeListener(imageChangeListener);
        }
    }

    @Override
    public final Set<ViewChangeListener> getListeners() {
        return listeners;
    }

    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    public void paintComponent(final Graphics graphics) {
        final var g2d = (Graphics2D) graphics.create();
        if (model != null) {
            g2d.setRenderingHint(
                    RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR
            );
            model.getImage().draw(g2d, 0, 0, getWidth(), getHeight());
            drawShade(g2d);
            drawOverlay(g2d);
            drawLines(g2d);
        } else {
            g2d.setColor(backgroundColor);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
        g2d.dispose();
    }

    private void autoSize() {
        if (zoom > 0 && model != null) {
            final var image = model.getImage();
            final var dimension = model != null
                    ? new Dimension(image.getImageWidth() * zoom, image.getImageHeight() * zoom)
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

        final var glyph = model.getImage();
        final var w = glyph.getImageWidth();
        final var h = glyph.getImageHeight();
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
        final var x = (int) Math.round(
                (((double) getWidth()) / model.getImage().getImageWidth())
                * model.getWidth()
        );
        final var y = (int) Math.round(
                (((double) getHeight()) / model.getImage().getImageHeight())
                    * (model.getImage().getImageHeight() - top)
        );
        g.fillRect(x, 0, getWidth() - x, getHeight());
        g.fillRect(0, 0, x, y);
    }

    public enum ViewChangeEvent {
        GLYPH_MODIFIED, MODEL_LOADED, MODEL_UNLOADED
    }

    public interface ViewChangeListener extends ChangeListener<GlyphView, ViewChangeEvent> {
        // Empty
    }
}
