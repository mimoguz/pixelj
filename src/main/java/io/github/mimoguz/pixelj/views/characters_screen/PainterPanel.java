package io.github.mimoguz.pixelj.views.characters_screen;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import com.formdev.flatlaf.FlatClientProperties;

import io.github.mimoguz.pixelj.actions.Actions;
import io.github.mimoguz.pixelj.actions.PainterActions;
import io.github.mimoguz.pixelj.controls.GlyphPainter;
import io.github.mimoguz.pixelj.controls.Line;
import io.github.mimoguz.pixelj.controls.Orientation;
import io.github.mimoguz.pixelj.graphics.Snapshot;
import io.github.mimoguz.pixelj.models.CharacterModel;
import io.github.mimoguz.pixelj.models.Metrics;
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
    private final JLabel title;
    private final ArrayList<Snapshot> undoBuffer = new ArrayList<>();
    private final JSlider zoomSlider;

    public PainterPanel(final JComponent root) {
        painter = new GlyphPainter(Resources.get().colors.disabledIcon());
        painter.setZoom(INITIAL_ZOOM);
        painter.setSnapshotConsumer(snapshot -> {
            undoBuffer.add(snapshot);
            if (undoBuffer.size() > MAX_UNDO) {
                undoBuffer.remove(0);
            }
        });

        actions = new PainterActions();
        actions.setPainter(painter);
        Actions.registerShortcuts(actions.all, root);
        Actions.setEnabled(actions.all, false);

        title = new JLabel(" ");
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
        toolBar.add(actions.symmetryToggleAction);
        toolBar.addSeparator();
        toolBar.add(actions.eraseAction);
        toolBar.setOrientation(SwingConstants.VERTICAL);
        toolBar.setBorder(Borders.smallEmpty);
        add(toolBar, BorderLayout.WEST);

        // ****************************** CENTER ******************************

        final var bottomPanel = new JPanel();
        bottomPanel.setBorder(Borders.smallEmpty);
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        bottomPanel.add(Box.createHorizontalGlue());
        bottomPanel.add(zoomSlider);
        bottomPanel.add(Box.createHorizontalGlue());

        final var titlePanel = new JPanel();
        titlePanel.setBorder(Borders.smallEmpty);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
        titlePanel.add(Box.createHorizontalGlue());
        titlePanel.add(title);
        titlePanel.add(Box.createHorizontalGlue());

        final var painterPanel = new JPanel();
        painterPanel.setLayout(new GridBagLayout());
        painterPanel.setMaximumSize(Dimensions.maximum);
        painterPanel.add(painter);

        final var scrollPanel = new JScrollPane(painterPanel);
        scrollPanel.setBorder(Borders.empty);
        scrollPanel.setFocusable(true);
        scrollPanel.setMaximumSize(Dimensions.maximum);

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
        centerPanel.add(bottomPanel);
        add(centerPanel, BorderLayout.CENTER);

        // ****************************** EAST ******************************

        final var eastPanel = new JPanel();
        eastPanel.setMinimumSize(new Dimension(200, 1));
        eastPanel.setMaximumSize(new Dimension(200, Integer.MAX_VALUE));
        eastPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Resources.get().colors.divider()));
        add(eastPanel, BorderLayout.EAST);
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
        Actions.setEnabled(actions.all, value);
        super.setEnabled(value);
    }

    /**
     * @param metrics May be null.
     */
    public void setMetrics(final Metrics metrics) {
        painter.removeLines();

        if (metrics == null) {
            return;
        }

        final var colors = Resources.get().colors;
        painter.addLines(
                new Line(Orientation.HORIZONTAL, metrics.descender(), colors.accent()),
                new Line(Orientation.HORIZONTAL, metrics.descender() + metrics.xHeight(), colors.accent()),
                new Line(Orientation.HORIZONTAL, metrics.descender() + metrics.ascender(), colors.accent())
        );
    }

    /**
     * @param value May be null
     */
    public void setModel(final CharacterModel value) {
        painter.setModel(value);

        if (value != null) {
            Actions.setEnabled(actions.all, true);
            title.setText(Integer.toString(value.getCodePoint()));
            zoomSlider.setEnabled(true);
        } else {
            Actions.setEnabled(actions.all, false);
            title.setText(" ");
            zoomSlider.setEnabled(false);
        }
    }
}
