package io.github.mimoguz.pixelj.views.characters_screen;

import io.github.mimoguz.pixelj.actions.Actions;
import io.github.mimoguz.pixelj.actions.PainterActions;
import io.github.mimoguz.pixelj.views.controls.GlyphPainter;
import io.github.mimoguz.pixelj.views.controls.Line;
import io.github.mimoguz.pixelj.views.controls.Orientation;
import io.github.mimoguz.pixelj.graphics.Snapshot;
import io.github.mimoguz.pixelj.models.CharacterItem;
import io.github.mimoguz.pixelj.models.Metrics;
import io.github.mimoguz.pixelj.models.Project;
import io.github.mimoguz.pixelj.resources.Resources;
import io.github.mimoguz.pixelj.util.Detachable;
import io.github.mimoguz.pixelj.views.controls.ZoomStrip;
import io.github.mimoguz.pixelj.views.shared.Borders;
import io.github.mimoguz.pixelj.views.shared.Dimensions;

import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class PainterPanel extends JPanel implements Detachable {
    private static final Color BASELINE = new Color(45, 147, 173);
    private static final int BLACK_SQUARE = 0x10_00_00_00;
    private static final Color CAP_HEIGHT = new Color(41, 191, 18);
    private static final int INITIAL_ZOOM = 12;
    private static final int MAX_UNDO = 64;
    private static final int WHITE_SQUARE = 0x30_ff_ff_ff;
    private static final Color X_HEIGHT = CAP_HEIGHT;

    private final PainterActions actions = new PainterActions();
    private final InfoPanel infoPanel;
    private final GlyphPainter painter = new GlyphPainter(Resources.get().colors.disabledIcon());
    private final ArrayList<Snapshot> undoBuffer = new ArrayList<>();
    private final ZoomStrip zoomStrip = new ZoomStrip(1, 48, 12);
    private BufferedImage overlay;

    public PainterPanel(final Project project, final JComponent root) {
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

        actions.setPainter(painter);
        painter.setSnapshotConsumer(actions.snapshotConsumer);
        Actions.registerShortcuts(actions.all, root);

        infoPanel = new InfoPanel(project);
        infoPanel.getShowGridCheckBox()
                .addChangeListener(
                        e -> painter.setOverlayVisible(infoPanel.getShowGridCheckBox().isSelected())
                );
        infoPanel.getShowLinesCheckBox().addChangeListener(e -> {
            final var visible = infoPanel.getShowLinesCheckBox().isSelected();
            painter.setLinesVisible(visible);
            painter.setShaded(visible);
        });

        final var zoomSlider = zoomStrip.getSlider();
        zoomSlider.addChangeListener(e -> {
            if (zoomSlider.getValueIsAdjusting()) {
                painter.setZoom(zoomSlider.getValue());
            }
        });
        zoomStrip.setEnabled(false);

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
        toolBar.setBorder(Borders.SMALL_EMPTY_CUP);
        add(toolBar, BorderLayout.WEST);

        // ****************************** CENTER ******************************

        final var title = new JLabel(Resources.get().getString("painterTitle"));
        title.putClientProperty(FlatClientProperties.STYLE_CLASS, "h3");
        final var titlePanel = new JPanel();
        titlePanel.setBorder(Borders.TITLE_CENTER);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
        titlePanel.add(Box.createHorizontalStrut(Dimensions.LARGE_PADDING));
        titlePanel.add(title);
        titlePanel.add(Box.createHorizontalGlue());

        final var painterPanel = new JPanel();
        painterPanel.setLayout(new GridBagLayout());
        painterPanel.setMaximumSize(Dimensions.MAXIMUM);
        painterPanel.add(painter);

        final var scrollPanel = new JScrollPane(painterPanel);
        scrollPanel.setBorder(Borders.SMALL_EMPTY_CUP_CENTER);
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
        centerPanel.setLayout(new BorderLayout());
        final var editorPanel = new JPanel(new BorderLayout());
        editorPanel.add(titlePanel, BorderLayout.NORTH);
        editorPanel.add(toolBar, BorderLayout.WEST);
        editorPanel.add(scrollPanel, BorderLayout.CENTER);
        editorPanel.add(zoomStrip, BorderLayout.SOUTH);
        centerPanel.add(editorPanel, BorderLayout.CENTER);
        centerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Resources.get().colors.divider()));
        add(centerPanel, BorderLayout.CENTER);

        // ****************************** EAST ******************************
        add(infoPanel, BorderLayout.EAST);

        setEnabled(false);
    }

    @Override
    public void detach() {
        painter.detach();
    }

    public CharacterItem getModel() {
        return painter.getModel();
    }

    /**
     * @param value May be null
     */
    public void setModel(final CharacterItem value) {
        painter.setModel(value);
        infoPanel.setModel(value);

        if (value != null) {
            Actions.setEnabled(actions.all, true);
            zoomStrip.setEnabled(true);
            if (
                    overlay == null || overlay.getWidth() != value.getGlyph().getWidth()
                            || overlay.getHeight() != value.getGlyph().getHeight()
            ) {
                overlay = checkerBoard(value.getGlyph().getWidth(), value.getGlyph().getHeight());
            }
            painter.setOverlay(overlay);
        } else {
            Actions.setEnabled(actions.all, false);
            zoomStrip.setEnabled(false);
            painter.setOverlay(null);
        }
    }

    public GlyphPainter getPainter() {
        return painter;
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
                        CAP_HEIGHT
                ),
                // x height
                new Line(
                        Orientation.HORIZONTAL,
                        metrics.canvasHeight() - metrics.descender() - metrics.xHeight(),
                        X_HEIGHT
                ),
                // Baseline
                new Line(Orientation.HORIZONTAL, metrics.canvasHeight() - metrics.descender(), BASELINE)
        );
    }

    private static BufferedImage checkerBoard(int w, int h) {
        final var image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        for (var y = 0; y < h; y++) {
            for (var x = 0; x < w; x++) {
                //noinspection SuspiciousNameCombination
                image.setRGB(
                        x,
                        y,
                        ((odd(x) && even(y)) || (odd(y) && even(x))) ? WHITE_SQUARE : BLACK_SQUARE
                );
            }
        }
        return image;
    }

    private static boolean even(int x) {
        return (x & 1) == 0;
    }

    private static boolean odd(int x) {
        return (x & 1) == 1;
    }
}
