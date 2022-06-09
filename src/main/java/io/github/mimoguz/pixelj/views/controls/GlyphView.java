package io.github.mimoguz.pixelj.views.controls;

import io.github.mimoguz.pixelj.graphics.BinaryImage;
import io.github.mimoguz.pixelj.models.CharacterItem;
import io.github.mimoguz.pixelj.models.IntValueChangeListener;
import io.github.mimoguz.pixelj.util.ChangeListener;
import io.github.mimoguz.pixelj.util.Changeable;
import io.github.mimoguz.pixelj.util.Detachable;
import io.github.mimoguz.pixelj.views.shared.Dimensions;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class GlyphView extends JPanel
        implements
        Changeable<GlyphView, GlyphView.ViewChangeEvent, GlyphView.ViewChangeListener>,
        Detachable {
    private static final Color SHADE = new Color(242, 27, 63, 50);
    private final Color backgroundColor;
    private final IntValueChangeListener characterWidthChangeListener;
    private final BinaryImage.ImageChangeListener imageChangeListener;
    private final ArrayList<Line> lines = new ArrayList<>();
    private final EventListenerList listeners = new EventListenerList();
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
    public GlyphView(Color backgroundColor) {
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

    public void addLines(Line... lines) {
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
    public Class<ViewChangeListener> getListenerClass() {
        return ViewChangeListener.class;
    }

    @Override
    public EventListenerList getListenerList() {
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
    public void setOverlay(Image value) {
        if (overlay == value) {
            return;
        }
        overlay = value;
        repaint();
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getZoom() {
        return zoom;
    }

    public void setZoom(int value) {
        zoom = value;
        autoSize();
    }

    public boolean isLinesVisible() {
        return showLines;
    }

    public void setLinesVisible(boolean value) {
        if (value == showLines) {
            return;
        }
        this.showLines = value;
        repaint();
    }

    public boolean isOverlayVisible() {
        return showOverlay;
    }

    public void setOverlayVisible(boolean value) {
        if (value == showOverlay) {
            return;
        }
        this.showOverlay = value;
        repaint();
    }

    public boolean isShaded() {
        return drawShade;
    }

    public void setShaded(boolean value) {
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

    public void removeLines(Line... lines) {
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
            model.removeChangeListener(characterWidthChangeListener);
            fireChangeEvent(this, ViewChangeEvent.MODEL_UNLOADED);
            if (top <= 0) {
                top = model.getGlyph().getHeight();
            }
        }

        this.model = value;

        if (value != null) {
            if (listen) {
                value.getGlyph().addChangeListener(imageChangeListener);
                value.addChangeListener(characterWidthChangeListener);
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

    private void drawLines(Graphics2D g) {
        if (lines.isEmpty() || !showLines || model == null) {
            return;
        }

        final var glyph = model.getGlyph();
        final var w = glyph.getWidth();
        final var h = glyph.getHeight();
        final var dx = getWidth() / (double) w;
        final var dy = getHeight() / (double) h;
        for (var line : lines) {
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

    private void drawOverlay(Graphics2D g) {
        if (overlay == null || !showOverlay) {
            return;
        }
        g.drawImage(overlay, 0, 0, getWidth(), getHeight(), null);
    }

    private void drawShade(Graphics2D g) {
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
