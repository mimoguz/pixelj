package io.github.mimoguz.pixelj.views.charactersScreen;

import io.github.mimoguz.pixelj.actions.Actions;
import io.github.mimoguz.pixelj.actions.PainterActions;
import io.github.mimoguz.pixelj.controls.GlyphPainter;
import io.github.mimoguz.pixelj.graphics.Snapshot;
import io.github.mimoguz.pixelj.models.CharacterModel;
import io.github.mimoguz.pixelj.resources.Resources;
import io.github.mimoguz.pixelj.views.shared.Borders;

import com.formdev.flatlaf.FlatClientProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PainterPanel extends JPanel {
    private static final int INITIAL_ZOOM = 12;
    private static final int MAX_UNDO = 64;
    private final PainterActions actions;
    private final GlyphPainter painter;
    private final JLabel title;
    private final ArrayList<Snapshot> undoBuffer = new ArrayList<>();
    private final JSlider zoomSlider;

    public PainterPanel(final @NotNull JComponent root) {
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

        setLayout(new BorderLayout());

        add(toolBar, BorderLayout.WEST);

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

    }

    public void setModel(final @Nullable CharacterModel model) {
        painter.setModel(model);
        if (model != null) {
            Actions.setEnabled(actions.all, true);
            title.setText(Integer.toString(model.getCodePoint()));
            zoomSlider.setEnabled(true);
        } else {
            Actions.setEnabled(actions.all, false);
            title.setText(" ");
            zoomSlider.setEnabled(false);
        }
    }
}
