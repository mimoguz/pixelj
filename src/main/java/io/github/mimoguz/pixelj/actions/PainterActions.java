package io.github.mimoguz.pixelj.actions;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.Action;
import javax.swing.JToggleButton;

import io.github.mimoguz.pixelj.controls.GlyphPainter;
import io.github.mimoguz.pixelj.controls.painter.CanFlipImage;
import io.github.mimoguz.pixelj.controls.painter.CanRotateImage;
import io.github.mimoguz.pixelj.controls.painter.CanTranslateImage;
import io.github.mimoguz.pixelj.controls.painter.Painter;
import io.github.mimoguz.pixelj.graphics.Snapshot;
import io.github.mimoguz.pixelj.resources.Icons;
import io.github.mimoguz.pixelj.resources.Resources;

public class PainterActions {
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
        public final ApplicationAction symmetryToggleAction;

        public final Consumer<Snapshot> snapshotConsumer;

        private boolean enabled = true;
        private final ArrayList<Snapshot> undoBuffer = new ArrayList<>(MAX_UNDO);
        private final ArrayList<Snapshot> redoBuffer = new ArrayList<>(MAX_UNDO);
        private GlyphPainter painter;

        public PainterActions() {
                snapshotConsumer = this::consumeSnapshot;

                final var res = Resources.get();

                symmetryToggleAction = new ApplicationAction(
                                "symmetryToggleAction",
                                (e, action) -> ifPainterNotNull(p -> {
                                        p.setSymmetrical(!p.isSymmetrical());
                                        // Fix selected state if the action performed not because of a button
                                        // press but
                                        // its shortcut:
                                        if (e.getSource() instanceof JToggleButton) {
                                                return;
                                        }
                                        action.putValue(Action.SELECTED_KEY, p.isSymmetrical());
                                })
                ).setTooltipKey("symmetryToggleActionTooltip")
                                .setIcon(Icons.SYMMETRY, res.colors.icon(), res.colors.disabledIcon())
                                .setAccelerator(KeyEvent.VK_S, InputEvent.ALT_DOWN_MASK);

                historyUndoAction = new ApplicationAction("painterHistoryUndoAction", this::undo)
                                .setTooltipKey("painterHistoryUndoActionTooltip")
                                .setIcon(Icons.HISTORY_UNDO, res.colors.icon(), res.colors.disabledIcon())
                                .setAccelerator(
                                                KeyEvent.VK_Z,
                                                Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()
                                );

                historyRedoAction = new ApplicationAction("painterHistoryRedoAction", this::redo)
                                .setTooltipKey("painterHistoryRedoActionTooltip")
                                .setIcon(Icons.HISTORY_REDO, res.colors.icon(), res.colors.disabledIcon())
                                .setAccelerator(
                                                KeyEvent.VK_Y,
                                                Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()
                                );

                clipboardCutAction = new ApplicationAction("painterClipboardCutAction", (e, action) -> {
                        if (painter != null) {
                                System.out.println("Painter cut");
                        }
                }).setTooltipKey("painterClipboardCutActionTooltip")
                                .setIcon(Icons.CLIPBOARD_CUT, res.colors.icon(), res.colors.disabledIcon())
                                .setAccelerator(
                                                KeyEvent.VK_X,
                                                Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()
                                );

                clipboardCopyAction = new ApplicationAction("painterClipboardCopyAction", (e, action) -> {
                        if (painter != null) {
                                System.out.println("Painter copy");
                        }
                }).setTooltipKey("painterClipboardCopyActionTooltip")
                                .setIcon(Icons.CLIPBOARD_COPY, res.colors.icon(), res.colors.disabledIcon())
                                .setAccelerator(
                                                KeyEvent.VK_C,
                                                Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()
                                );

                clipboardPasteAction = new ApplicationAction("painterClipboardPasteAction", (e, action) -> {
                        if (painter != null) {
                                System.out.println("Painter paste");
                        }
                }).setTooltipKey("painterClipboardPasteActionTooltip")
                                .setIcon(Icons.CLIPBOARD_PASTE, res.colors.icon(), res.colors.disabledIcon())
                                .setAccelerator(
                                                KeyEvent.VK_V,
                                                Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()
                                );

                clipboardImportAction = new ApplicationAction("painterClipboardImportAction", (e, action) -> {
                        if (painter != null) {
                                System.out.println("Painter import clip");
                        }
                }).setTooltipKey("painterClipboardImportActionTooltip")
                                .setIcon(Icons.CLIPBOARD_IMPORT, res.colors.icon(), res.colors.disabledIcon())
                                .setAccelerator(
                                                KeyEvent.VK_V,
                                                Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()
                                                                | InputEvent.SHIFT_DOWN_MASK
                                );

                flipVerticallyAction = new ApplicationAction(
                                "flipVerticallyAction",
                                (e, action) -> ifPainterNotNull(CanFlipImage::flipVertically)
                ).setTooltipKey("flipVerticallyActionTooltip")
                                .setIcon(Icons.FLIP_VERTICAL, res.colors.icon(), res.colors.disabledIcon())
                                .setAccelerator(KeyEvent.VK_V, InputEvent.ALT_DOWN_MASK);

                flipHorizontallyAction = new ApplicationAction(
                                "flipHorizontallyAction",
                                (e, action) -> ifPainterNotNull(CanFlipImage::flipHorizontally)
                ).setTooltipKey("flipHorizontallyActionTooltip")
                                .setIcon(Icons.FLIP_HORIZONTAL, res.colors.icon(), res.colors.disabledIcon())
                                .setAccelerator(KeyEvent.VK_H, InputEvent.ALT_DOWN_MASK);

                rotateLeftAction = new ApplicationAction(
                                "rotateLeftAction",
                                (e, action) -> ifPainterNotNull(CanRotateImage::rotateLeft)
                ).setTooltipKey("rotateLeftActionTooltip")
                                .setIcon(Icons.ROTATE_LEFT, res.colors.icon(), res.colors.disabledIcon())
                                .setAccelerator(KeyEvent.VK_L, InputEvent.ALT_DOWN_MASK);

                rotateRightAction = new ApplicationAction(
                                "rotateRightAction",
                                (e, action) -> ifPainterNotNull(CanRotateImage::rotateRight)
                ).setTooltipKey("rotateRightActionTooltip")
                                .setIcon(Icons.ROTATE_RIGHT, res.colors.icon(), res.colors.disabledIcon())
                                .setAccelerator(KeyEvent.VK_R, InputEvent.ALT_DOWN_MASK);

                moveLeftAction = new ApplicationAction(
                                "moveLeftAction",
                                (e, action) -> ifPainterNotNull(CanTranslateImage::moveOnePixelLeft)
                ).setTooltipKey("moveLeftActionTooltip")
                                .setIcon(Icons.MOVE_LEFT, res.colors.icon(), res.colors.disabledIcon())
                                .setAccelerator(KeyEvent.VK_LEFT, InputEvent.ALT_DOWN_MASK);

                moveRightAction = new ApplicationAction(
                                "moveRightAction",
                                (e, action) -> ifPainterNotNull(CanTranslateImage::moveOnePixelRight)
                ).setTooltipKey("moveRightActionTooltip")
                                .setIcon(Icons.MOVE_RIGHT, res.colors.icon(), res.colors.disabledIcon())
                                .setAccelerator(KeyEvent.VK_RIGHT, InputEvent.ALT_DOWN_MASK);

                moveUpAction = new ApplicationAction(
                                "moveUpAction",
                                (e, action) -> ifPainterNotNull(CanTranslateImage::moveOnePixelUp)
                ).setTooltipKey("moveUpActionTooltip")
                                .setIcon(Icons.MOVE_UP, res.colors.icon(), res.colors.disabledIcon())
                                .setAccelerator(KeyEvent.VK_UP, InputEvent.ALT_DOWN_MASK);

                moveDownAction = new ApplicationAction(
                                "moveDownAction",
                                (e, action) -> ifPainterNotNull(CanTranslateImage::moveOnePixelDown)
                ).setTooltipKey("moveDownActionTooltip")
                                .setIcon(Icons.MOVE_DOWN, res.colors.icon(), res.colors.disabledIcon())
                                .setAccelerator(KeyEvent.VK_DOWN, InputEvent.ALT_DOWN_MASK);

                eraseAction = new ApplicationAction(
                                "eraseAction",
                                (e, action) -> ifPainterNotNull(GlyphPainter::erase)
                ).setTooltipKey("eraseActionTooltip")
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

        /**
         * @param painter May be null
         */
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

        private void ifPainterNotNull(Consumer<GlyphPainter> consumer) {
                if (painter != null) {
                        consumer.accept(painter);
                }
        }

        private void consumeSnapshot(Snapshot snapshot) {
                for (var index = redoBuffer.size() - 1; index >= 0; index--) {
                        if (redoBuffer.get(index).id() == snapshot.id()) {
                                redoBuffer.remove(index);
                        }
                }
                if (undoBuffer.size() >= MAX_UNDO) {
                        undoBuffer.remove(0);
                }
                undoBuffer.add(snapshot);
        }

        private void undo(ActionEvent event, Action action) {
                timeTravel(undoBuffer, redoBuffer);
        }

        private void redo(ActionEvent event, Action action) {
                timeTravel(redoBuffer, undoBuffer);
        }

        private void timeTravel(ArrayList<Snapshot> from, ArrayList<Snapshot> to) {
                if (painter == null) {
                        return;
                }
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
}
