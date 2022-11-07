package pixelj.views.projectwindow.glyphspage;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.event.MouseEvent;

import javax.swing.*;

import com.formdev.flatlaf.FlatClientProperties;
import pixelj.actions.ApplicationAction;
import pixelj.actions.PainterActions;
import pixelj.models.DocumentSettings;
import pixelj.models.Glyph;
import pixelj.models.Project;
import pixelj.resources.Icon;
import pixelj.util.ChangeableInt;
import pixelj.util.Checkerboard;
import pixelj.util.Detachable;
import pixelj.views.controls.GlyphPainter;
import pixelj.views.controls.Line;
import pixelj.views.controls.Orientation;
import pixelj.views.shared.Dimensions;
import pixelj.views.shared.ToolLayout;
import pixelj.views.shared.ZoomAdapter;

public final class PainterPanel extends PainterPanelBase implements Detachable {

    private static final Color BASELINE = new Color(45, 147, 173);
    private static final Color CAP_HEIGHT = new Color(41, 191, 18);
    private static final int INITIAL_ZOOM = 12;
    private static final Color X_HEIGHT = CAP_HEIGHT;

    private final PainterActions actions = new PainterActions();
    private BufferedImage overlay;
    private JPopupMenu overflowMenu = new JPopupMenu();
    private ChangeableInt.Listener cutoffChangeListener;

    public PainterPanel(final Project project, final JFrame window) {
        super(new InfoPanel(project));

        painter.setZoom(INITIAL_ZOOM);
        painter.setOverlayVisible(true);
        painter.setLinesVisible(true);
        painter.setShaded(true);

        cutoffChangeListener = this::onCutoffIndexChanged;

        final var toolBarLayout = new ToolLayout();
        toolBarLayout.cutoffProperty.addChangeListener(cutoffChangeListener);
        toolBar.setLayout(toolBarLayout);

        final var overflowButtonAction = new ApplicationAction(
                "painterToolBarOverflowAction",
                (event, action) -> {
                    overflowMenu.show(overflowButton, overflowButton.getWidth() + Dimensions.SMALL_PADDING, 0);
                }
        ).setIcon(Icon.ELLIPSIS);
        overflowButton.setAction(overflowButtonAction);


        final var zoomSlider = zoomStrip.getSlider();
        zoomSlider.addChangeListener(e -> painter.setZoom(zoomSlider.getValue()));
        zoomStrip.setEnabled(false);

        infoPanel.gridVisibleProperty.addChangeListener(isVisible -> painter.setOverlayVisible(isVisible));

        infoPanel.guidesVisibleProperty.addChangeListener(isVisible -> {
            painter.setLinesVisible(isVisible);
            painter.setShaded(isVisible);
        });

        actions.setPainter(painter);
        painter.setSnapshotConsumer(s -> {
            actions.snapshotConsumer.accept(s);
            project.setDirty(true);
        });
        actions.registerShortcuts(window.getRootPane());
        fillToolbar(toolBar, actions);

        final var mouseAdapter = new ZoomAdapter(zoomSlider) {
            @Override
            public void mousePressed(final MouseEvent e) {
                // When clicked, move focus to scrollPane
                scrollPane.requestFocus();
            }
        };

        scrollPane.addMouseListener(mouseAdapter);
        scrollPane.addMouseWheelListener(mouseAdapter);
        painter.addMouseListener(mouseAdapter);

        setMetrics(project.getDocumentSettings());
        project.documentSettingsProperty.addChangeListener((source, settings) -> setMetrics(settings));
        setEnabled(false);
    }

    public Glyph getModel() {
        return painter.getModel();
    }

    /**
     * @param value May be null
     */
    public void setModel(final Glyph value) {
        painter.setModel(value);
        infoPanel.setModel(value);
        if (value != null) {
            actions.setEnabled(true);
            zoomStrip.setEnabled(true);
            final var gw = value.getImage().getImageWidth();
            final var gh = value.getImage().getImageHeight();
            if (overlay == null || overlay.getWidth() != gw || overlay.getHeight() != gh) {
                overlay = Checkerboard.create(
                    value.getImage().getImageWidth(),
                    value.getImage().getImageHeight()
                );
            }
            painter.setOverlay(overlay);
        } else {
            actions.setEnabled(false);
            zoomStrip.setEnabled(false);
            painter.setOverlay(null);
        }
    }

    public GlyphPainter getPainter() {
        return painter;
    }

    @Override
    public void detach() {
        actions.detach();
        if (toolBar.getLayout() instanceof ToolLayout toolLayout) {
            toolLayout.cutoffProperty.removeChangeListener(cutoffChangeListener);
        }
    }

    @Override
    public void setEnabled(final boolean value) {
        super.setEnabled(value);
        actions.setEnabled(value && painter.getModel() != null);
    }

    /**
     * Create the guides.
     */
    private void setMetrics(final DocumentSettings settings) {
        painter.removeLines();
        if (settings == null) {
            return;
        }
        painter.setTop(settings.descender() + settings.ascender());
        final var capHeight = new Line(
            Orientation.HORIZONTAL,
            settings.canvasHeight() - settings.descender() - settings.capHeight(),
            CAP_HEIGHT
        );
        final var xHeight = new Line(
            Orientation.HORIZONTAL,
            settings.canvasHeight() - settings.descender() - settings.xHeight(),
            X_HEIGHT
        );
        final var baseLine = new Line(
            Orientation.HORIZONTAL,
            settings.canvasHeight() - settings.descender(),
            BASELINE
        );
        painter.addLines(capHeight, xHeight, baseLine);
    }

    private void onCutoffIndexChanged(final int newValue) {
        final var components = toolBar.getComponents();
        if (newValue < components.length) {
            overflowMenu.removeAll();
            for (var i = newValue; i < components.length; i++) {
                final var component = toolBar.getComponent(i);
                if (component instanceof JButton btn) {
                    final var item = new JMenuItem(btn.getAction());
                    overflowMenu.add(item);
                } else if (component instanceof JToggleButton btn) {
                    final var item = new JCheckBoxMenuItem();
                    item.setSelected(btn.isSelected());
                    item.setAction(btn.getAction());
                    overflowMenu.add(item);
                } else if (component instanceof JSeparator)
                {
                    overflowMenu.addSeparator();
                }
            }
            overflowButton.setEnabled(true);
        } else {
            overflowMenu.removeAll();
            overflowButton.setEnabled(false);
        }
    }

    private static void fillToolbar(final JToolBar toolBar, final PainterActions actions) {
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
    }
}
