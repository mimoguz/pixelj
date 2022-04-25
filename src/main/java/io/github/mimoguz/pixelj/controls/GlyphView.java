package io.github.mimoguz.pixelj.controls;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import javax.swing.JPanel;
import javax.swing.event.EventListenerList;

import org.eclipse.jdt.annotation.Nullable;

import io.github.mimoguz.pixelj.graphics.BinaryImage;
import io.github.mimoguz.pixelj.models.CharacterModel;
import io.github.mimoguz.pixelj.util.ChangeListener;
import io.github.mimoguz.pixelj.util.Changeable;
import io.github.mimoguz.pixelj.util.Detachable;

public class GlyphView extends JPanel
        implements
        Changeable<GlyphView, GlyphView.ViewChangeEvent, GlyphView.ViewChangeListener>,
        Detachable {
    public enum ViewChangeEvent {
        GLYPH_MODIFIED, MODEL_LOADED, MODEL_UNLOADED
    }

    public interface ViewChangeListener extends ChangeListener<GlyphView, ViewChangeEvent> {
    }

    private static final long serialVersionUID = 7841183984464270304L;

    private static final Color SHADE = new Color(20, 0, 0, 0);

    private final Color backgroundColor;
    private final BinaryImage.ImageChangeListener imageChangeListener;
    private final ArrayList<Line> lines = new ArrayList<>();
    private final EventListenerList listeners = new EventListenerList();
    @Nullable
    private CharacterModel model;
    @Nullable
    private Image overlay;

    private boolean showLines = false;

    private boolean showOverlay = false;

    private int zoom = 1;

    public GlyphView(Color backgroundColor) {
        this.backgroundColor = backgroundColor;

        imageChangeListener = (source, event) -> {
            final var model = this.model;
            if (model == null || source != model.getGlyph()) {
                return;
            }
            // TODO: Repaint only changed region
            repaint();
            fireChangeEvent(this, ViewChangeEvent.GLYPH_MODIFIED);
        };
    }

    public void addLines(Line... lines) {
        this.lines.addAll(Arrays.stream(lines).filter(Objects::nonNull).toList());
    }

    @Override
    public void detach() {
        final var model = this.model;
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

    @Nullable
    public CharacterModel getModel() {
        return model;
    }

    @Nullable
    public Image getOverlay() {
        return overlay;
    }

    public int getZoom() {
        return zoom;
    }

    public boolean isLinesVisible() {
        return showLines;
    }

    public boolean isOverlayVisible() {
        return showOverlay;
    }

    @Override
    public void paintComponent(@Nullable final Graphics graphics) {
        final var model = this.model;

        if (graphics == null) {
            return;
        }

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

    public void setLinesVisible(boolean value) {
        if (value == showLines) {
            return;
        }
        this.showLines = value;
        repaint();
    }

    public void setModel(@Nullable CharacterModel value) {
        setModel(value, true);
    }

    public void setModel(@Nullable CharacterModel value, boolean listen) {
        if (model == value) {
            return;
        }

        final var oldValue = model;

        if (oldValue != null) {
            oldValue.getGlyph().removeChangeListener(imageChangeListener);
            fireChangeEvent(this, ViewChangeEvent.MODEL_UNLOADED);
        }

        this.model = value;

        if (value != null) {
            if (listen) {
                value.getGlyph().addChangeListener(imageChangeListener);
            }
            fireChangeEvent(this, ViewChangeEvent.MODEL_LOADED);
        }

        autoSize();
    }

    public void setOverlay(@Nullable Image value) {
        if (overlay == value) {
            return;
        }
        overlay = value;
        repaint();
    }

    public void setOverlayVisible(boolean value) {
        if (value == showOverlay) {
            return;
        }
        this.showOverlay = value;
        repaint();
    }

    public void setZoom(int value) {
        zoom = value;
        autoSize();
    }

    private void autoSize() {
        final var model = this.model;
        if (zoom > 0) {
            final var dimension = model != null
                    ? new Dimension(model.getGlyph().getWidth() * zoom, model.getGlyph().getHeight() * zoom)
                    : new Dimension(zoom, zoom);
            setMinimumSize(dimension);
            setMaximumSize(dimension);
            setSize(dimension);
        }
        revalidate();
        repaint();
    }

    private void drawLines(Graphics2D g) {
        final var model = this.model;

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
            switch (line.orientation()) {
                case HORIZONTAL -> {
                    final var y = (int) Math.round(line.point() * dy);
                    g.drawLine(0, y, getWidth(), y);
                }
                default -> {
                    final var x = (int) Math.round(line.point() * dx);
                    g.drawLine(x, 0, x, getHeight());
                }
            }
        }
    }

    private void drawOverlay(Graphics2D g) {
        if (overlay == null | !showOverlay) {
            return;
        }
        g.drawImage(overlay, 0, 0, getWidth(), getHeight(), null);
    }

    private void drawShade(Graphics2D g) {
        final var model = this.model;

        if (model == null || model.getWidth() >= model.getGlyph().getWidth()) {
            return;
        }

        final var x = (int) Math
                .round((((double) getWidth()) / model.getGlyph().getWidth()) * model.getWidth());
        g.setColor(SHADE);
        g.fillRect(x, 0, x, getHeight());
    }
}
