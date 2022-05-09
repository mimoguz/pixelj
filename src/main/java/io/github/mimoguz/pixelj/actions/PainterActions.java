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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.Action;
import javax.swing.JToggleButton;

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
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
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
        })).setTooltipKey("symmetryToggleActionTooltip")
                .setIcon(Icons.SYMMETRY, res.colors.icon(), res.colors.disabledIcon())
                .setAccelerator(KeyEvent.VK_S, InputEvent.ALT_DOWN_MASK);

        historyUndoAction = new ApplicationAction("painterHistoryUndoAction", this::undo)
                .setTooltipKey("painterHistoryUndoActionTooltip")
                .setIcon(Icons.HISTORY_UNDO, res.colors.icon(), res.colors.disabledIcon())
                .setAccelerator(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx());

        historyRedoAction = new ApplicationAction("painterHistoryRedoAction", this::redo)
                .setTooltipKey("painterHistoryRedoActionTooltip")
                .setIcon(Icons.HISTORY_REDO, res.colors.icon(), res.colors.disabledIcon())
                .setAccelerator(KeyEvent.VK_Y, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx());

        clipboardCutAction = new ApplicationAction("painterClipboardCutAction", this::cut)
                .setTooltipKey("painterClipboardCutActionTooltip")
                .setIcon(Icons.CLIPBOARD_CUT, res.colors.icon(), res.colors.disabledIcon())
                .setAccelerator(KeyEvent.VK_X, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx());

        clipboardCopyAction = new ApplicationAction("painterClipboardCopyAction", this::copy)
                .setTooltipKey("painterClipboardCopyActionTooltip")
                .setIcon(Icons.CLIPBOARD_COPY, res.colors.icon(), res.colors.disabledIcon())
                .setAccelerator(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx());

        clipboardPasteAction = new ApplicationAction("painterClipboardPasteAction", this::paste)
                .setTooltipKey("painterClipboardPasteActionTooltip")
                .setIcon(Icons.CLIPBOARD_PASTE, res.colors.icon(), res.colors.disabledIcon())
                .setAccelerator(KeyEvent.VK_V, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx());

        clipboardImportAction = new ApplicationAction("painterClipboardImportAction", this::importClip)
                .setTooltipKey("painterClipboardImportActionTooltip")
                .setIcon(Icons.CLIPBOARD_IMPORT, res.colors.icon(), res.colors.disabledIcon())
                .setAccelerator(
                        KeyEvent.VK_V,
                        Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx() | InputEvent.SHIFT_DOWN_MASK
                );

        flipVerticallyAction = new ApplicationAction(
                "flipVerticallyAction",
                (e, action) -> painter.flipVertically()
        ).setTooltipKey("flipVerticallyActionTooltip")
                .setIcon(Icons.FLIP_VERTICAL, res.colors.icon(), res.colors.disabledIcon())
                .setAccelerator(KeyEvent.VK_V, InputEvent.ALT_DOWN_MASK);

        flipHorizontallyAction = new ApplicationAction(
                "flipHorizontallyAction",
                (e, action) -> painter.flipHorizontally()
        ).setTooltipKey("flipHorizontallyActionTooltip")
                .setIcon(Icons.FLIP_HORIZONTAL, res.colors.icon(), res.colors.disabledIcon())
                .setAccelerator(KeyEvent.VK_H, InputEvent.ALT_DOWN_MASK);

        rotateLeftAction = new ApplicationAction("rotateLeftAction", (e, action) -> painter.rotateLeft())
                .setTooltipKey("rotateLeftActionTooltip")
                .setIcon(Icons.ROTATE_LEFT, res.colors.icon(), res.colors.disabledIcon())
                .setAccelerator(KeyEvent.VK_L, InputEvent.ALT_DOWN_MASK);

        rotateRightAction = new ApplicationAction("rotateRightAction", (e, action) -> painter.rotateRight())
                .setTooltipKey("rotateRightActionTooltip")
                .setIcon(Icons.ROTATE_RIGHT, res.colors.icon(), res.colors.disabledIcon())
                .setAccelerator(KeyEvent.VK_R, InputEvent.ALT_DOWN_MASK);

        moveLeftAction = new ApplicationAction("moveLeftAction", (e, action) -> painter.moveOnePixelLeft())
                .setTooltipKey("moveLeftActionTooltip")
                .setIcon(Icons.MOVE_LEFT, res.colors.icon(), res.colors.disabledIcon())
                .setAccelerator(KeyEvent.VK_LEFT, InputEvent.ALT_DOWN_MASK);

        moveRightAction = new ApplicationAction("moveRightAction", (e, action) -> painter.moveOnePixelRight())
                .setTooltipKey("moveRightActionTooltip")
                .setIcon(Icons.MOVE_RIGHT, res.colors.icon(), res.colors.disabledIcon())
                .setAccelerator(KeyEvent.VK_RIGHT, InputEvent.ALT_DOWN_MASK);

        moveUpAction = new ApplicationAction("moveUpAction", (e, action) -> painter.moveOnePixelUp())
                .setTooltipKey("moveUpActionTooltip")
                .setIcon(Icons.MOVE_UP, res.colors.icon(), res.colors.disabledIcon())
                .setAccelerator(KeyEvent.VK_UP, InputEvent.ALT_DOWN_MASK);

        moveDownAction = new ApplicationAction("moveDownAction", (e, action) -> painter.moveOnePixelDown())
                .setTooltipKey("moveDownActionTooltip")
                .setIcon(Icons.MOVE_DOWN, res.colors.icon(), res.colors.disabledIcon())
                .setAccelerator(KeyEvent.VK_DOWN, InputEvent.ALT_DOWN_MASK);

        eraseAction = new ApplicationAction("eraseAction", this::erase).setTooltipKey("eraseActionTooltip")
                .setIcon(Icons.ERASE, res.colors.icon(), res.colors.disabledIcon())
                .setAccelerator(KeyEvent.VK_X, InputEvent.ALT_DOWN_MASK);

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
