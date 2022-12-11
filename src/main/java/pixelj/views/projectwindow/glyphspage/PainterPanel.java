package pixelj.views.projectwindow.glyphspage;

import pixelj.actions.PainterActions;
import pixelj.messaging.DocumentSettingsChangedMessage;
import pixelj.messaging.GlyphChangedMessage;
import pixelj.messaging.Messenger;
import pixelj.messaging.Receiver;
import pixelj.models.DocumentSettings;
import pixelj.models.Glyph;
import pixelj.models.Project;
import pixelj.util.Checkerboard;
import pixelj.util.Detachable;
import pixelj.views.controls.GlyphPainter;
import pixelj.views.controls.Line;
import pixelj.views.controls.Orientation;
import pixelj.views.shared.ZoomAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public final class PainterPanel extends PainterPanelBase implements Detachable {

    private static final Color BASELINE = new Color(45, 147, 173);
    private static final Color CAP_HEIGHT = new Color(41, 191, 18);
    private static final int INITIAL_ZOOM = 12;
    private static final Color X_HEIGHT = CAP_HEIGHT;
    private final PainterActions actions = new PainterActions();
    private final Receiver<DocumentSettingsChangedMessage, Void> documentSettingsChangedReceiver;
    private BufferedImage overlay;

    public PainterPanel(final Project project, final JFrame window) {
        super(new InfoPanel(project));

        painter.setZoom(INITIAL_ZOOM);
        painter.setOverlayVisible(true);
        painter.setLinesVisible(true);
        painter.setShaded(true);

        final var zoomSlider = zoomStrip.getSlider();
        zoomSlider.addChangeListener(e -> painter.setZoom(zoomSlider.getValue()));
        zoomStrip.setEnabled(false);

        infoPanel.gridVisibleProperty.addChangeListener(painter::setOverlayVisible);

        infoPanel.guidesVisibleProperty.addChangeListener(isVisible -> {
            painter.setLinesVisible(isVisible);
            painter.setShaded(isVisible);
        });

        actions.setPainter(painter);
        painter.setSnapshotConsumer(s -> {
            actions.snapshotConsumer.accept(s);
            Messenger.get(GlyphChangedMessage.class).send(new GlyphChangedMessage(painter.getModel().getCodePoint()));
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

        documentSettingsChangedReceiver = msg -> {
            setMetrics(msg.settings());
            return null;
        };
        Messenger.get(DocumentSettingsChangedMessage.class).register(documentSettingsChangedReceiver);

        setEnabled(false);
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

    public PainterActions getActions() {
        return actions;
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
                overlay = Checkerboard.create(value.getImage().getImageWidth(), value.getImage().getImageHeight());
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
        final var
            capHeight =
            new Line(
                Orientation.HORIZONTAL,
                settings.canvasHeight() - settings.descender() - settings.capHeight(),
                CAP_HEIGHT
            );
        final var
            xHeight =
            new Line(
                Orientation.HORIZONTAL,
                settings.canvasHeight() - settings.descender() - settings.xHeight(),
                X_HEIGHT
            );
        final var baseLine = new Line(Orientation.HORIZONTAL, settings.canvasHeight() - settings.descender(), BASELINE);
        painter.addLines(capHeight, xHeight, baseLine);
    }
}
