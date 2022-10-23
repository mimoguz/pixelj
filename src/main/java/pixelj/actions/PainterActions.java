package pixelj.actions;

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
import javax.swing.KeyStroke;

import pixelj.graphics.Snapshot;
import pixelj.resources.Icon;
import pixelj.resources.Resources;
import pixelj.util.ChangeableValue;
import pixelj.views.controls.GlyphPainter;
import pixelj.views.controls.painter.Painter;

/** In sake of simplicity, all actions assume painter is not null when they are  called. */
public final class PainterActions implements Actions {

    private static final int MAX_UNDO = 64;

    /** Copy image to both the application and the system clipboards. */
    public final ApplicationAction clipboardCopyAction;
    /** Cut image, put it to both the application and the system clipboards. */
    public final ApplicationAction clipboardCutAction;
    /** Paste from the system clipboard. This is relatively costly, and may fail. */
    public final ApplicationAction clipboardImportAction;
    /** Paste from the application clipboard. */
    public final ApplicationAction clipboardPasteAction;
    /** Clear the image contents. */
    public final ApplicationAction eraseAction;
    /** Flip the image horizontally. */
    public final ApplicationAction flipHorizontallyAction;
    /** Flip the image vertically. */
    public final ApplicationAction flipVerticallyAction;
    /** Redo. */
    public final ApplicationAction historyRedoAction;
    /** Undo. */
    public final ApplicationAction historyUndoAction;
    /** Move image contents one pixel down. The overflowing line will be lost. */
    public final ApplicationAction moveDownAction;
    /** Move image contents one pixel left. The O-overflowing line will be lost. */
    public final ApplicationAction moveLeftAction;
    /** Move image contents one pixel right. The overflowing line will be lost. */
    public final ApplicationAction moveRightAction;
    /** Move image contents one pixel up. The overflowing line will be lost. */
    public final ApplicationAction moveUpAction;
    /** Rotate the image 90 degrees left. */
    public final ApplicationAction rotateLeftAction;
    /** Rotate the image 90 right left. */
    public final ApplicationAction rotateRightAction;
    /** When an image modified, its previous state will be sent to this consumer. */
    public final Consumer<Snapshot> snapshotConsumer;
    /** Toggle horizontal symmetry. */
    public final ApplicationAction symmetryToggleAction;

    private final Collection<ApplicationAction> all = new ArrayList<>();
    private boolean enabled = true;
    private GlyphPainter painter;
    private final ArrayList<Snapshot> undoBuffer = new ArrayList<>(MAX_UNDO);
    private final ArrayList<Snapshot> redoBuffer = new ArrayList<>(MAX_UNDO);
    private final ChangeableValue<Snapshot> clipboard = new ChangeableValue<>(null);

    public PainterActions() {
        final var res = Resources.get();
        final var menuShortcutMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
        snapshotConsumer = this::addToUndoBuffer;

        symmetryToggleAction = new ApplicationAction("symmetryToggleAction", (e, action) -> act(p -> {
            p.setSymmetrical(!p.isSymmetrical());
            // Fix selected state if the action performed not because of a button press but its shortcut:
            if (e.getSource() instanceof JToggleButton) {
                return;
            }
            action.putValue(Action.SELECTED_KEY, p.isSymmetrical());
        }))
            .setTooltipWithAccelerator(
                res.getString("painterSymmetryToggleActionTooltip"),
                KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.ALT_DOWN_MASK)
            )
            .setIcon(Icon.SYMMETRY);

        historyUndoAction = new ApplicationAction("painterHistoryUndoAction", this::undo)
            .setTooltipWithAccelerator(
                res.getString("painterHistoryUndoActionTooltip"),
                KeyStroke.getKeyStroke(KeyEvent.VK_Z, menuShortcutMask)
            )
            .setIcon(Icon.HISTORY_UNDO);

        historyRedoAction = new ApplicationAction("painterHistoryRedoAction", this::redo)
            .setTooltipWithAccelerator(
                res.getString("painterHistoryRedoActionTooltip"),
                KeyStroke.getKeyStroke(KeyEvent.VK_Y, menuShortcutMask)
            )
            .setIcon(Icon.HISTORY_REDO);

        clipboardCutAction = new ApplicationAction("painterClipboardCutAction", this::cut)
            .setTooltipWithAccelerator(
                res.getString("painterClipboardCutActionTooltip"),
                KeyStroke.getKeyStroke(KeyEvent.VK_X, menuShortcutMask)
            )
            .setIcon(Icon.CLIPBOARD_CUT);

        clipboardCopyAction = new ApplicationAction("painterClipboardCopyAction", this::copy)
            .setTooltipWithAccelerator(
                res.getString("painterClipboardCopyActionTooltip"),
                KeyStroke.getKeyStroke(KeyEvent.VK_C, menuShortcutMask)
            )
            .setIcon(Icon.CLIPBOARD_COPY);

        clipboardPasteAction = new ApplicationAction("painterClipboardPasteAction", this::paste)
            .setTooltipWithAccelerator(
                res.getString("painterClipboardPasteActionTooltip"),
                KeyStroke.getKeyStroke(KeyEvent.VK_V, menuShortcutMask)
            )
            .setIcon(Icon.CLIPBOARD_PASTE);

        clipboardImportAction = new ApplicationAction("painterClipboardImportAction", this::importClip)
            .setTooltipWithAccelerator(
                res.getString("painterClipboardImportActionTooltip"),
                KeyStroke.getKeyStroke(KeyEvent.VK_V, menuShortcutMask | InputEvent.SHIFT_DOWN_MASK)
            )
            .setIcon(Icon.CLIPBOARD_IMPORT);

        flipVerticallyAction = new ApplicationAction(
            "flipVerticallyAction",
            (e, action) -> painter.flipVertically()
        )
            .setTooltipWithAccelerator(
                res.getString("painterFlipVerticalActionTooltip"),
                KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.ALT_DOWN_MASK)
            )
            .setIcon(Icon.FLIP_VERTICAL);

        flipHorizontallyAction = new ApplicationAction(
            "flipHorizontallyAction",
            (e, action) -> painter.flipHorizontally()
        )
            .setTooltipWithAccelerator(
                    res.getString("painterFlipHorizontalActionTooltip"),
                    KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.ALT_DOWN_MASK)
            )
            .setIcon(Icon.FLIP_HORIZONTAL);

        rotateLeftAction = new ApplicationAction("rotateLeftAction", (e, action) -> painter.rotateLeft())
            .setTooltipWithAccelerator(
                res.getString("painterRotateLeftActionTooltip"),
                KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.ALT_DOWN_MASK)
            )
            .setIcon(Icon.ROTATE_LEFT);

        rotateRightAction = new ApplicationAction("rotateRightAction", (e, action) -> painter.rotateRight())
            .setTooltipWithAccelerator(
                res.getString("painterRotateRightActionTooltip"),
                KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.ALT_DOWN_MASK)
            )
            .setIcon(Icon.ROTATE_RIGHT);

        moveLeftAction = new ApplicationAction("moveLeftAction", (e, action) -> painter.moveOnePixelLeft())
            .setTooltipWithAccelerator(
                res.getString("painterMoveLeftActionTooltip"),
                KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.ALT_DOWN_MASK)
            )
            .setIcon(Icon.MOVE_LEFT);

        moveRightAction = new ApplicationAction("moveRightAction", (e, action) -> painter.moveOnePixelRight())
            .setTooltipWithAccelerator(
                res.getString("painterMoveRightActionTooltip"),
                KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.ALT_DOWN_MASK)
            )
            .setIcon(Icon.MOVE_RIGHT);

        moveUpAction = new ApplicationAction("moveUpAction", (e, action) -> painter.moveOnePixelUp())
            .setTooltipWithAccelerator(
                res.getString("painterMoveUpActionTooltip"),
                KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.ALT_DOWN_MASK)
            )
            .setIcon(Icon.MOVE_UP);

        moveDownAction = new ApplicationAction("moveDownAction", (e, action) -> painter.moveOnePixelDown())
            .setTooltipWithAccelerator(
                res.getString("painterMoveDownActionTooltip"),
                KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.ALT_DOWN_MASK)
            )
            .setIcon(Icon.MOVE_DOWN);

        eraseAction = new ApplicationAction("eraseAction", this::erase)
            .setTooltipWithAccelerator(
                res.getString("painterClearActionTooltip"),
                KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.ALT_DOWN_MASK)
            )
            .setIcon(Icon.ERASE);

        all.addAll(List.of(
            historyUndoAction, historyRedoAction, clipboardCutAction, clipboardCopyAction,
            clipboardPasteAction, clipboardImportAction, flipVerticallyAction,
            flipHorizontallyAction, rotateLeftAction, rotateRightAction, moveLeftAction,
            moveRightAction, moveUpAction, moveDownAction, eraseAction, symmetryToggleAction
        ));

        clipboardPasteAction.setEnabled(false);
        clipboard.addChangeListener((src, clip) -> clipboardPasteAction.setEnabled(enabled && clip != null));
        symmetryToggleAction.putValue(Action.SELECTED_KEY, false);
    }

    public PainterActions(final GlyphPainter painter) {
        this();
        this.painter = painter;
    }

    public Painter getPainter() {
        return painter;
    }

    public void setPainter(final GlyphPainter value) {
        painter = value;
    }

    @Override
    public void detach() {
        // Empty
    }

    @Override
    public Collection<ApplicationAction> getAll() {
        return all;
    }

    @Override
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
        for (var action : all) {
            action.setEnabled(enabled);
        }
        clipboardPasteAction.setEnabled(enabled && clipboard.getValue() != null);
    }

    private void act(final Consumer<GlyphPainter> consumer) {
        consumer.accept(painter);
    }

    private void addToUndoBuffer(final Snapshot snapshot) {
        if (undoBuffer.size() >= MAX_UNDO) {
            undoBuffer.remove(0);
        }
        undoBuffer.add(snapshot);
    }

    private void copy(final ActionEvent event, final Action action) {
        final var model = painter.getModel();
        if (model == null) {
            return;
        }
        final var image = model.getImage();
        clipboard.setValue(image.getSnapshot(model.getCodePoint()));
        // Send to system clipboard:
        final var rgbImage = new BufferedImage(
            image.getImageWidth(),
            image.getImageHeight(),
            BufferedImage.TYPE_INT_RGB
        );
        final var g2d = (Graphics2D) rgbImage.getGraphics();
        image.draw(g2d, 0, 0, image.getImageWidth(), image.getImageHeight());
        final var transferable = new TransferableImage(rgbImage);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(transferable, null);
        g2d.dispose();
    }

    private void cut(final ActionEvent event, final Action action) {
        copy(event, action);
        erase(event, action);
    }

    private void erase(final ActionEvent event, final Action action) {
        final var snapshot = painter.takeSnapshot();
        if (snapshot == null) {
            return;
        }
        addToUndoBuffer(snapshot);
        painter.erase();
    }

    private void importClip(final ActionEvent event, final Action action) {
        final var model = painter.getModel();
        if (model == null) {
            return;
        }
        final var image = model.getImage();
        final var transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
        if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.imageFlavor)) {
            try {
                final var source = (BufferedImage) transferable.getTransferData(DataFlavor.imageFlavor);
                final var w = Math.min(source.getWidth(null), image.getImageWidth());
                final var h = Math.min(source.getHeight(null), image.getImageHeight());
                final var raster = source.getRaster();
                addToUndoBuffer(image.getSnapshot(model.getCodePoint()));
                final var buffer = new int[raster.getNumDataElements()];
                for (var y = 0; y < h; y++) {
                    for (var x = 0; x < w; x++) {
                        raster.getPixel(x, y, buffer);
                        image.set(x, y, (buffer[0] & 1) != 0);
                    }
                }
            } catch (IOException | UnsupportedFlavorException | IndexOutOfBoundsException e) {
                // Ignore errors
            }
        }
    }

    private void paste(final ActionEvent event, final Action action) {
        final var clip = clipboard.getValue();
        final var model = painter.getModel();
        if (model == null || clip == null) {
            return;
        }
        // TODO: Better way to set the document dirty???
        addToUndoBuffer(painter.takeSnapshot());
        model.getImage().setDataElements(clip.x(), clip.y(), clip.width(), clip.height(), clip.data());
    }

    private void redo(final ActionEvent event, final Action action) {
        timeTravel(redoBuffer, undoBuffer);
    }

    private void timeTravel(final ArrayList<Snapshot> from, final ArrayList<Snapshot> to) {
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
                to.add(model.getImage().getSnapshot(model.getCodePoint()));
                model.getImage().setDataElements(
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

    private void undo(final ActionEvent event, final Action action) {
        timeTravel(undoBuffer, redoBuffer);
    }

    private static final class TransferableImage implements Transferable {
        private final Image image;

        TransferableImage(final Image image) {
            this.image = image;
        }

        @Override
        public Object getTransferData(final DataFlavor flavor) throws UnsupportedFlavorException {
            if (flavor.equals(DataFlavor.imageFlavor)) {
                return image;
            }
            throw new UnsupportedFlavorException(flavor);
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[] {
                DataFlavor.imageFlavor
            };
        }

        @Override
        public boolean isDataFlavorSupported(final DataFlavor flavor) {
            return Arrays.stream(getTransferDataFlavors()).anyMatch(f -> f.equals(flavor));
        }
    }
}
