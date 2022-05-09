package io.github.mimoguz.pixelj.views.characters_screen;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.*;

import com.formdev.flatlaf.FlatClientProperties;

import io.github.mimoguz.pixelj.actions.Actions;
import io.github.mimoguz.pixelj.actions.PainterActions;
import io.github.mimoguz.pixelj.controls.GlyphPainter;
import io.github.mimoguz.pixelj.controls.Line;
import io.github.mimoguz.pixelj.controls.Orientation;
import io.github.mimoguz.pixelj.graphics.Snapshot;
import io.github.mimoguz.pixelj.models.CharacterModel;
import io.github.mimoguz.pixelj.models.IntValueChangeListener;
import io.github.mimoguz.pixelj.models.Metrics;
import io.github.mimoguz.pixelj.models.ProjectModel;
import io.github.mimoguz.pixelj.resources.Resources;
import io.github.mimoguz.pixelj.util.Detachable;
import io.github.mimoguz.pixelj.views.shared.Borders;
import io.github.mimoguz.pixelj.views.shared.Dimensions;

public class PainterPanel extends JPanel implements Detachable {
    private static final int INITIAL_ZOOM = 12;
    private static final int MAX_UNDO = 64;
    private static final long serialVersionUID = -2196271415900003483L;

    private final transient PainterActions actions;
    private final GlyphPainter painter;
    private final InfoPanel infoPanel;
    private final JLabel title;
    private final ArrayList<Snapshot> undoBuffer = new ArrayList<>();
    private final JSlider zoomSlider;
    private transient BufferedImage overlay;

    public PainterPanel(final ProjectModel project, final JComponent root) {
        painter = new GlyphPainter(Resources.get().colors.disabledIcon());
        painter.setZoom(INITIAL_ZOOM);
        painter.setOverlayVisible(true);
        painter.setLinesVisible(true);
        painter.setShaded(true);
        painter.setSnapshotConsumer(snapshot -> {
            undoBuffer.add(snapshot);
            if (undoBuffer.size() > MAX_UNDO) {
                undoBuffer.remove(0);
            }
        });

        actions = new PainterActions();
        actions.setPainter(painter);
        painter.setSnapshotConsumer(actions.snapshotConsumer);
        Actions.registerShortcuts(actions.all, root);

        title = new JLabel(Resources.get().getString("painterTitle"));
        title.putClientProperty(FlatClientProperties.STYLE_CLASS, "h4");

        zoomSlider = new JSlider(1, 48, INITIAL_ZOOM);
        zoomSlider.setMinimumSize(new Dimension(96, 24));
        zoomSlider.setMaximumSize(new Dimension(256, 24));
        zoomSlider.setEnabled(false);
        zoomSlider.addChangeListener(e -> {
            if (zoomSlider.getValueIsAdjusting()) {
                painter.setZoom(zoomSlider.getValue());
            }
        });

        setLayout(new BorderLayout());

        // ****************************** WEST ******************************

        final var toolBar = new JToolBar();
        toolBar.add(actions.historyUndoAction);
        toolBar.add(actions.historyRedoAction);
        toolBar.addSeparator();
        toolBar.add(actions.clipboardCutAction);
        toolBar.add(actions.clipboardCopyAction);
        toolBar.add(actions.clipboardPasteAction);
        toolBar.add(actions.clipboardImportAction);
        toolBar.addSeparator();
        toolBar.add(actions.flipHorizontallyAction);
        toolBar.add(actions.flipVerticallyAction);
        toolBar.add(actions.rotateLeftAction);
        toolBar.add(actions.rotateRightAction);
        toolBar.addSeparator();
        toolBar.add(actions.moveLeftAction);
        toolBar.add(actions.moveRightAction);
        toolBar.add(actions.moveUpAction);
        toolBar.add(actions.moveDownAction);
        toolBar.addSeparator();
        toolBar.add(new JToggleButton(actions.symmetryToggleAction));
        toolBar.addSeparator();
        toolBar.add(actions.eraseAction);
        toolBar.setOrientation(SwingConstants.VERTICAL);
        toolBar.setBorder(Borders.smallEmpty);
        add(toolBar, BorderLayout.WEST);

        // ****************************** CENTER ******************************

        final var zoomPanel = new JPanel();
        zoomPanel.setBorder(Borders.smallEmptyCupCenter);
        zoomPanel.setLayout(new BoxLayout(zoomPanel, BoxLayout.X_AXIS));
        zoomPanel.add(Box.createHorizontalGlue());
        zoomPanel.add(zoomSlider);
        zoomPanel.add(Box.createHorizontalGlue());

        final var titlePanel = new JPanel();
        titlePanel.setBorder(Borders.titleCenter);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
        titlePanel.add(Box.createHorizontalGlue());
        titlePanel.add(title);
        titlePanel.add(Box.createHorizontalGlue());

        final var painterPanel = new JPanel();
        painterPanel.setLayout(new GridBagLayout());
        painterPanel.setMaximumSize(Dimensions.MAXIMUM);
        painterPanel.add(painter);

        final var scrollPanel = new JScrollPane(painterPanel);
        scrollPanel.setBorder(Borders.smallEmptyCupCenter);
        scrollPanel.setFocusable(true);
        scrollPanel.setMaximumSize(Dimensions.MAXIMUM);

        final var moveFocus = new MouseListener() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                // Ignored
            }

            @Override
            public void mouseEntered(final MouseEvent e) {
                // Ignored
            }

            @Override
            public void mouseExited(final MouseEvent e) {
                // Ignored
            }

            @Override
            public void mousePressed(final MouseEvent e) {
                scrollPanel.requestFocus();
            }

            @Override
            public void mouseReleased(final MouseEvent e) {
                // Ignored
            }
        };

        scrollPanel.addMouseListener(moveFocus);
        painter.addMouseListener(moveFocus);

        final var centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(titlePanel);
        centerPanel.add(scrollPanel);
        centerPanel.add(zoomPanel);
        add(centerPanel, BorderLayout.CENTER);

        // ****************************** EAST ******************************

        infoPanel = new InfoPanel(project);
        infoPanel.setMinimumSize(new Dimension(200, 1));
        infoPanel.setMaximumSize(new Dimension(200, Integer.MAX_VALUE));
        infoPanel.setPreferredSize(new Dimension(200, 300));
        infoPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Resources.get().colors.divider()));
        add(infoPanel, BorderLayout.EAST);

        setEnabled(false);
    }

    @Override
    public void detach() {
        painter.detach();
    }

    public CharacterModel getModel() {
        return painter.getModel();
    }

    public GlyphPainter getPainter() {
        return painter;
    }

    public JLabel getTitle() {
        return title;
    }

    public JSlider getZoomSlider() {
        return zoomSlider;
    }

    @Override
    public void setEnabled(final boolean value) {
        super.setEnabled(value);
        Actions.setEnabled(actions.all, value && (painter.getModel() != null));
    }

    /**
     * @param metrics May be null.
     */
    public void setMetrics(final Metrics metrics) {
        painter.removeLines();

        if (metrics == null) {
            return;
        }

        infoPanel.setMetrics(metrics);

        painter.setTop(metrics.descender() + metrics.ascender());

        painter.addLines(
                // Cap height
                new Line(
                        Orientation.HORIZONTAL,
                        metrics.canvasHeight() - metrics.descender() - metrics.capHeight(),
                        Color.YELLOW
                ),
                // x height
                new Line(
                        Orientation.HORIZONTAL,
                        metrics.canvasHeight() - metrics.descender() - metrics.xHeight(),
                        Color.YELLOW
                ),
                // Baseline
                new Line(Orientation.HORIZONTAL, metrics.canvasHeight() - metrics.descender(), Color.BLUE)
        );
    }

    /**
     * @param value May be null
     */
    public void setModel(final CharacterModel value) {
        painter.setModel(value);
        infoPanel.setModel(value);

        if (value != null) {
            Actions.setEnabled(actions.all, true);
            title.setText(Integer.toString(value.getCodePoint()));
            zoomSlider.setEnabled(true);
            if (
                overlay == null || overlay.getWidth() != value.getGlyph().getWidth()
                        || overlay.getHeight() != value.getGlyph().getHeight()
            ) {
                overlay = checkerBoard(value.getGlyph().getWidth(), value.getGlyph().getHeight());
            }
            painter.setOverlay(overlay);
        } else {
            Actions.setEnabled(actions.all, false);
            title.setText(Resources.get().getString("painterTitle"));
            zoomSlider.setEnabled(false);
            painter.setOverlay(null);
        }
    }

    private static BufferedImage checkerBoard(int w, int h) {
        final var image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        for (var y = 0; y < h; y++) {
            for (var x = 0; x < w; x++) {
                image.setRGB(
                        x,
                        y,
                        ((odd(x) && even(y)) || (odd(y) && even(x))) ? WHITE_SQUARE : BLACK_SQUARE
                );
            }
        }
        return image;
    }

    private static boolean odd(int x) {
        return (x & 1) == 1;
    }

    private static boolean even(int x) {
        return (x & 1) == 0;
    }

    private static final int WHITE_SQUARE = 0x30_ff_ff_ff;
    private static final int BLACK_SQUARE = 0x10_00_00_00;
}
