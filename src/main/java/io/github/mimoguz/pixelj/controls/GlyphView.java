package io.github.mimoguz.pixelj.controls;

import io.github.mimoguz.pixelj.graphics.BinaryImage;
import io.github.mimoguz.pixelj.models.CharacterModel;
import io.github.mimoguz.pixelj.util.ChangeListener;
import io.github.mimoguz.pixelj.util.Changeable;
import io.github.mimoguz.pixelj.util.Detachable;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class GlyphView extends JPanel
        implements
        Changeable<GlyphView, GlyphView.ViewChangeEvent, GlyphView.ViewChangeListener>,
        Detachable {
    private static final Color SHADE = new Color(20, 0, 0, 0);
    private final Color backgroundColor;
    private final ArrayList<Line> lines = new ArrayList<>();
    private final EventListenerList listeners = new EventListenerList();
    @Nullable
    private CharacterModel model;
    private final BinaryImage.ImageChangeListener imageChangeListener = (source, event) -> {
        if (model == null || source != model.getGlyph()) {
            return;
        }
        // TODO: Repaint only changed region
        repaint();
        fireChangeEvent(this, ViewChangeEvent.GLYPH_MODIFIED);
    };
    @Nullable
    private Image overlay;
    private boolean showLines = false;
    private boolean showOverlay = false;
    private int zoom = 1;

    public GlyphView(@NotNull Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void addLines(Line... lines) {
        this.lines.addAll(Arrays.stream(lines).toList());
    }

    @Override
    public void detach() {
        if (model != null) {
            model.getGlyph().removeChangeListener(imageChangeListener);
        }
    }

    @Override
    @NotNull
    public Class<ViewChangeListener> getListenerClass() {
        return ViewChangeListener.class;
    }

    @Override
    @NotNull
    public EventListenerList getListenerList() {
        return listeners;
    }

    @Nullable
    public CharacterModel getModel() {
        return model;
    }

    public void setModel(CharacterModel value) {
        setModel(value, true);
    }

    @Nullable
    public Image getOverlay() {
        return overlay;
    }

    public void setOverlay(Image value) {
        if (overlay == value) {
            return;
        }
        overlay = value;
        repaint();
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

    @Override
    public void paintComponent(Graphics graphics) {
        final var g = (Graphics2D) graphics.create();
        if (model != null) {
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            model.getGlyph().draw(g, 0, 0, getWidth(), getHeight());
            drawShade(g);
            drawOverlay(g);
            drawLines(g);
        } else {
            g.setColor(backgroundColor);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        g.dispose();
    }

    public void removeLines(Line... lines) {
        this.lines.removeAll(Arrays.stream(lines).toList());
        repaint();
    }

    public void removeLines() {
        lines.clear();
        repaint();
    }

    public void setModel(CharacterModel value, boolean listen) {
        if (model == value) {
            return;
        }

        if (model != null) {
            model.getGlyph().removeChangeListener(imageChangeListener);
            fireChangeEvent(this, ViewChangeEvent.MODEL_UNLOADED);
        }

        this.model = value;

        if (model != null) {
            if (listen) {
                model.getGlyph().addChangeListener(imageChangeListener);
            }
            fireChangeEvent(this, ViewChangeEvent.MODEL_LOADED);
        }

        autoSize();
    }

    private void autoSize() {
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
        if (lines.isEmpty() | !showLines | model == null) {
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
                case VERTICAL -> {
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
        if (model == null || model.getWidth() >= model.getGlyph().getWidth()) {
            return;
        }
        final var x = (int) Math.round((((double) getWidth()) / model.getGlyph().getWidth()) * model.getWidth());
        g.setColor(SHADE);
        g.fillRect(x, 0, x, getHeight());
    }

    public enum ViewChangeEvent {
        MODEL_LOADED, MODEL_UNLOADED, GLYPH_MODIFIED
    }

    public interface ViewChangeListener extends ChangeListener<GlyphView, ViewChangeEvent> {
    }
}
