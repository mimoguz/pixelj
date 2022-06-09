package io.github.mimoguz.pixelj.actions;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.Action;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;

import io.github.mimoguz.pixelj.controls.GlyphPainter;
import io.github.mimoguz.pixelj.controls.painter.Painter;
import io.github.mimoguz.pixelj.graphics.Snapshot;
import io.github.mimoguz.pixelj.resources.Icons;
import io.github.mimoguz.pixelj.resources.Resources;

/**
 * In sake of simplicity, all actions assume painter is not null when they are
 * called.
 */
public class PainterActions {
    private static class TransferableImage implements Transferable {
        private final Image image;

        public TransferableImage(final Image image) {
            this.image = image;
        }

        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
            if (flavor.equals(DataFlavor.imageFlavor)) {
                return image;
            }
            throw new UnsupportedFlavorException(flavor);
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[] { DataFlavor.imageFlavor };
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return Arrays.stream(getTransferDataFlavors()).anyMatch(f -> f.equals(flavor));
        }
    }

    private static final int MAX_UNDO = 64;
    public final Collection<ApplicationAction> all = new ArrayList<>();
    public final ApplicationAction clipboardCopyAction;
    public final ApplicationAction clipboardCutAction;
    public final ApplicationAction clipboardImportAction;
    public final ApplicationAction clipboardPasteAction;
    public final ApplicationAction eraseAction;
    public final ApplicationAction flipHorizontallyAction;
    public final ApplicationAction flipVerticallyAction;
    public final ApplicationAction historyRedoAction;
    public final ApplicationAction historyUndoAction;
    public final ApplicationAction moveDownAction;
    public final ApplicationAction moveLeftAction;
    public final ApplicationAction moveRightAction;
    public final ApplicationAction moveUpAction;
    public final ApplicationAction rotateLeftAction;
    public final ApplicationAction rotateRightAction;
    public final Consumer<Snapshot> snapshotConsumer;
    public final ApplicationAction symmetryToggleAction;
    private Snapshot clipboard = null;
    private boolean enabled = true;
    private GlyphPainter painter;
    private final ArrayList<Snapshot> redoBuffer = new ArrayList<>(MAX_UNDO);
    private final ArrayList<Snapshot> undoBuffer = new ArrayList<>(MAX_UNDO);

    public PainterActions() {
        final var res = Resources.get();

        snapshotConsumer = this::addToUndoBuffer;

        symmetryToggleAction = new ApplicationAction("symmetryToggleAction", (e, action) -> act(p -> {
            p.setSymmetrical(!p.isSymmetrical());
            // Fix selected state if the action performed not because of a button
            // press but
            // its shortcut:
            if (e.getSource() instanceof JToggleButton) {
                return;
            }
            action.putValue(Action.SELECTED_KEY, p.isSymmetrical());
        }))
                .setTooltipWithAccelerator(
                        res.getString("symmetryToggleActionTooltip"),
                        KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.ALT_DOWN_MASK)
                )
                .setIcon(Icons.SYMMETRY, res.colors.icon(), res.colors.disabledIcon());

        historyUndoAction = new ApplicationAction("painterHistoryUndoAction", this::undo)
                .setTooltipWithAccelerator(
                        res.getString("painterHistoryUndoActionTooltip"),
                        KeyStroke.getKeyStroke(
                                KeyEvent.VK_Z,
                                Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()
                        )
                )
                .setIcon(Icons.HISTORY_UNDO, res.colors.icon(), res.colors.disabledIcon());

        historyRedoAction = new ApplicationAction("painterHistoryRedoAction", this::redo)
                .setTooltipWithAccelerator(
                        res.getString("painterHistoryRedoActionTooltip"),
                        KeyStroke.getKeyStroke(
                                KeyEvent.VK_Y,
                                Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()
                        )
                )
                .setIcon(Icons.HISTORY_REDO, res.colors.icon(), res.colors.disabledIcon());

        clipboardCutAction = new ApplicationAction("painterClipboardCutAction", this::cut)
                .setTooltipWithAccelerator(
                        res.getString("painterClipboardCutActionTooltip"),
                        KeyStroke.getKeyStroke(
                                KeyEvent.VK_X,
                                Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()
                        )
                )
                .setIcon(Icons.CLIPBOARD_CUT, res.colors.icon(), res.colors.disabledIcon());

        clipboardCopyAction = new ApplicationAction("painterClipboardCopyAction", this::copy)
                .setTooltipWithAccelerator(
                        res.getString("painterClipboardCopyActionTooltip"),
                        KeyStroke.getKeyStroke(
                                KeyEvent.VK_C,
                                Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()
                        )
                )
                .setIcon(Icons.CLIPBOARD_COPY, res.colors.icon(), res.colors.disabledIcon());

        clipboardPasteAction = new ApplicationAction("painterClipboardPasteAction", this::paste)
                .setTooltipWithAccelerator(
                        res.getString("painterClipboardPasteActionTooltip"),
                        KeyStroke.getKeyStroke(
                                KeyEvent.VK_V,
                                Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()
                        )
                )
                .setIcon(Icons.CLIPBOARD_PASTE, res.colors.icon(), res.colors.disabledIcon());

        clipboardImportAction = new ApplicationAction("painterClipboardImportAction", this::importClip)
                .setTooltipWithAccelerator(
                        res.getString("painterClipboardImportActionTooltip"),
                        KeyStroke.getKeyStroke(
                                KeyEvent.VK_V,
                                Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()
                                        | InputEvent.SHIFT_DOWN_MASK
                        )
                )
                .setIcon(Icons.CLIPBOARD_IMPORT, res.colors.icon(), res.colors.disabledIcon());

        flipVerticallyAction = new ApplicationAction(
                "flipVerticallyAction",
                (e, action) -> painter.flipVertically()
        ).setTooltipWithAccelerator(
                res.getString("flipVerticallyActionTooltip"),
                KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.ALT_DOWN_MASK)
        ).setIcon(Icons.FLIP_VERTICAL, res.colors.icon(), res.colors.disabledIcon());

        flipHorizontallyAction = new ApplicationAction(
                "flipHorizontallyAction",
                (e, action) -> painter.flipHorizontally()
        ).setTooltipWithAccelerator(
                res.getString("flipHorizontallyActionTooltip"),
                KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.ALT_DOWN_MASK)
        ).setIcon(Icons.FLIP_HORIZONTAL, res.colors.icon(), res.colors.disabledIcon());

        rotateLeftAction = new ApplicationAction("rotateLeftAction", (e, action) -> painter.rotateLeft())
                .setTooltipWithAccelerator(
                        res.getString("rotateLeftActionTooltip"),
                        KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.ALT_DOWN_MASK)
                )
                .setIcon(Icons.ROTATE_LEFT, res.colors.icon(), res.colors.disabledIcon());

        rotateRightAction = new ApplicationAction("rotateRightAction", (e, action) -> painter.rotateRight())
                .setTooltipWithAccelerator(
                        res.getString("rotateRightActionTooltip"),
                        KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.ALT_DOWN_MASK)
                )
                .setIcon(Icons.ROTATE_RIGHT, res.colors.icon(), res.colors.disabledIcon());

        moveLeftAction = new ApplicationAction("moveLeftAction", (e, action) -> painter.moveOnePixelLeft())
                .setTooltipWithAccelerator(
                        res.getString("moveLeftActionTooltip"),
                        KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.ALT_DOWN_MASK)
                )
                .setIcon(Icons.MOVE_LEFT, res.colors.icon(), res.colors.disabledIcon());

        moveRightAction = new ApplicationAction("moveRightAction", (e, action) -> painter.moveOnePixelRight())
                .setTooltipWithAccelerator(
                        res.getString("moveRightActionTooltip"),
                        KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.ALT_DOWN_MASK)
                )
                .setIcon(Icons.MOVE_RIGHT, res.colors.icon(), res.colors.disabledIcon());

        moveUpAction = new ApplicationAction("moveUpAction", (e, action) -> painter.moveOnePixelUp())
                .setTooltipWithAccelerator(
                        res.getString("moveUpActionTooltip"),
                        KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.ALT_DOWN_MASK)
                )
                .setIcon(Icons.MOVE_UP, res.colors.icon(), res.colors.disabledIcon());

        moveDownAction = new ApplicationAction("moveDownAction", (e, action) -> painter.moveOnePixelDown())
                .setTooltipWithAccelerator(
                        res.getString("moveDownActionTooltip"),
                        KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.ALT_DOWN_MASK)
                )
                .setIcon(Icons.MOVE_DOWN, res.colors.icon(), res.colors.disabledIcon());

        eraseAction = new ApplicationAction("eraseAction", this::erase)
                .setTooltipWithAccelerator(
                        res.getString("eraseActionTooltip"),
                        KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.ALT_DOWN_MASK)
                )
                .setIcon(Icons.ERASE, res.colors.icon(), res.colors.disabledIcon());

        all.addAll(
                List.of(
                        historyUndoAction,
                        historyRedoAction,
                        clipboardCutAction,
                        clipboardCopyAction,
                        clipboardPasteAction,
                        clipboardImportAction,
                        flipVerticallyAction,
                        flipHorizontallyAction,
                        rotateLeftAction,
                        rotateRightAction,
                        moveLeftAction,
                        moveRightAction,
                        moveUpAction,
                        moveDownAction,
                        eraseAction,
                        symmetryToggleAction
                )
        );

        symmetryToggleAction.putValue(Action.SELECTED_KEY, false);
    }

    public PainterActions(GlyphPainter painter) {
        this();
        this.painter = painter;
    }

    public Painter getPainter() {
        return painter;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean value) {
        enabled = value;
        Actions.setEnabled(this.all, enabled);
    }

    public void setPainter(GlyphPainter value) {
        painter = value;
    }

    private void act(Consumer<GlyphPainter> consumer) {
        consumer.accept(painter);
    }

    private void addToUndoBuffer(Snapshot snapshot) {
        if (undoBuffer.size() >= MAX_UNDO) {
            undoBuffer.remove(0);
        }
        undoBuffer.add(snapshot);
    }

    private void copy(ActionEvent event, Action action) {
        final var model = painter.getModel();
        if (model == null) {
            return;
        }
        final var glyph = model.getGlyph();
        clipboard = glyph.getSnapshot(model.getCodePoint());
        // Send to system clipboard:
        final var rgbImage = new BufferedImage(
                glyph.getWidth(),
                glyph.getHeight(),
                BufferedImage.TYPE_INT_RGB
        );
        final var g2d = (Graphics2D) rgbImage.getGraphics();
        glyph.draw(g2d, 0, 0, glyph.getWidth(), glyph.getHeight());
        final var transferable = new TransferableImage(rgbImage);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(transferable, null);
        g2d.dispose();
    }

    private void cut(ActionEvent event, Action action) {
        copy(event, action);
        erase(event, action);
    }

    private void erase(ActionEvent event, Action action) {
        final var model = painter.getModel();
        if (model == null) {
            return;
        }
        addToUndoBuffer(model.getGlyph().getSnapshot(model.getCodePoint()));
        painter.erase();
    }

    private void importClip(ActionEvent event, Action action) {
        final var model = painter.getModel();
        if (model == null) {
            return;
        }
        final var glyph = model.getGlyph();
        final var transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
        if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.imageFlavor)) {
            try {
                final var source = (BufferedImage) transferable.getTransferData(DataFlavor.imageFlavor);
                final var w = Math.min(source.getWidth(null), glyph.getWidth());
                final var h = Math.min(source.getHeight(null), glyph.getHeight());
                final var raster = source.getRaster();
                addToUndoBuffer(glyph.getSnapshot(model.getCodePoint()));
                final var buffer = new int[raster.getNumDataElements()];
                for (var y = 0; y < h; y++) {
                    for (var x = 0; x < w; x++) {
                        raster.getPixel(x, y, buffer);
                        glyph.set(x, y, (buffer[0] & 1) != 0);
                    }
                }
            } catch (Exception e) {
                // Ignore errors
            }
        }
    }

    private void paste(ActionEvent event, Action action) {
        final var clip = clipboard;
        final var model = painter.getModel();
        if (model == null || clip == null) {
            return;
        }
        addToUndoBuffer(model.getGlyph().getSnapshot(model.getCodePoint()));
        model.getGlyph().setDataElements(clip.x(), clip.y(), clip.width(), clip.height(), clip.data());
    }

    private void redo(ActionEvent event, Action action) {
        timeTravel(redoBuffer, undoBuffer);
    }

    private void timeTravel(ArrayList<Snapshot> from, ArrayList<Snapshot> to) {
        final var model = painter.getModel();
        if (model == null) {
            return;
        }
        for (var index = from.size() - 1; index >= 0; index--) {
            final var snapshot = from.get(index);
            if (snapshot.id() == model.getCodePoint()) {
                if (to.size() >= MAX_UNDO) {
                    to.remove(0);
                }
                from.remove(index);
                to.add(model.getGlyph().getSnapshot(model.getCodePoint()));
                model.getGlyph()
                        .setDataElements(
                                snapshot.x(),
                                snapshot.y(),
                                snapshot.width(),
                                snapshot.height(),
                                snapshot.data()
                        );
                return;
            }
        }
    }

    private void undo(ActionEvent event, Action action) {
        timeTravel(undoBuffer, redoBuffer);
    }
}
