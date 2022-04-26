package io.github.mimoguz.pixelj.actions;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.Action;
import javax.swing.JToggleButton;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

import io.github.mimoguz.pixelj.controls.GlyphPainter;
import io.github.mimoguz.pixelj.controls.painter.Painter;
import io.github.mimoguz.pixelj.resources.Icons;

@NonNullByDefault
public class PainterActions {
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

    private boolean enabled = true;
    @Nullable
    private GlyphPainter painter;

    public PainterActions() {
        symmetryToggleAction = new ApplicationAction("symmetryToggleAction", (e, action) -> {
            ifPainterNotNull(p -> {
                p.setSymmetrical(!p.isSymmetrical());
                // Fix selected state if the action performed not because of a button press but
                // its shortcut:
                if (e.getSource() instanceof JToggleButton) {
                    return;
                }
                action.putValue(Action.SELECTED_KEY, p.isSymmetrical());
            });
        }).setTooltipKey("symmetryToggleActionTooltip")
                .setIcon(Icons.SYMMETRY, null, null)
                .setAccelerator(KeyEvent.VK_S, InputEvent.ALT_DOWN_MASK);

        historyUndoAction = new ApplicationAction("painterHistoryUndoAction", (e, action) -> {
            if (painter != null) {
                System.out.println("Painter undo");
            }
        }).setTooltipKey("painterHistoryUndoActionTooltip")
                .setIcon(Icons.HISTORY_UNDO, null, null)
                .setAccelerator(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK);

        historyRedoAction = new ApplicationAction("painterHistoryRedoAction", (e, action) -> {
            if (painter != null) {
                System.out.println("Painter redo");
            }
        }).setTooltipKey("painterHistoryRedoActionTooltip")
                .setIcon(Icons.HISTORY_REDO, null, null)
                .setAccelerator(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK);

        clipboardCutAction = new ApplicationAction("painterClipboardCutAction", (e, action) -> {
            if (painter != null) {
                System.out.println("Painter cut");
            }
        }).setTooltipKey("painterClipboardCutActionTooltip")
                .setIcon(Icons.CLIPBOARD_CUT, null, null)
                .setAccelerator(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK);

        clipboardCopyAction = new ApplicationAction("painterClipboardCopyAction", (e, action) -> {
            if (painter != null) {
                System.out.println("Painter copy");
            }
        }).setTooltipKey("painterClipboardCopyActionTooltip")
                .setIcon(Icons.CLIPBOARD_COPY, null, null)
                .setAccelerator(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK);

        clipboardPasteAction = new ApplicationAction("painterClipboardPasteAction", (e, action) -> {
            if (painter != null) {
                System.out.println("Painter paste");
            }
        }).setTooltipKey("painterClipboardPasteActionTooltip")
                .setIcon(Icons.CLIPBOARD_PASTE, null, null)
                .setAccelerator(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK);

        clipboardImportAction = new ApplicationAction("painterClipboardImportAction", (e, action) -> {
            if (painter != null) {
                System.out.println("Painter import clip");
            }
        }).setTooltipKey("painterClipboardImportActionTooltip")
                .setIcon(Icons.CLIPBOARD_IMPORT, null, null)
                .setAccelerator(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK);

        flipVerticallyAction = new ApplicationAction("flipVerticallyAction", (e, action) -> {
            ifPainterNotNull(p -> p.flipVertically());
        }).setTooltipKey("flipVerticallyActionTooltip")
                .setIcon(Icons.FLIP_VERTICAL, null, null)
                .setAccelerator(KeyEvent.VK_V, InputEvent.ALT_DOWN_MASK);

        flipHorizontallyAction = new ApplicationAction("flipHorizontallyAction", (e, action) -> {
            ifPainterNotNull(p -> p.flipHorizontally());
        }).setTooltipKey("flipHorizontallyActionTooltip")
                .setIcon(Icons.FLIP_HORIZONTAL, null, null)
                .setAccelerator(KeyEvent.VK_H, InputEvent.ALT_DOWN_MASK);

        rotateLeftAction = new ApplicationAction("rotateLeftAction", (e, action) -> {
            ifPainterNotNull(p -> p.rotateLeft());
        }).setTooltipKey("rotateLeftActionTooltip")
                .setIcon(Icons.ROTATE_LEFT, null, null)
                .setAccelerator(KeyEvent.VK_L, InputEvent.ALT_DOWN_MASK);

        rotateRightAction = new ApplicationAction("rotateRightAction", (e, action) -> {
            ifPainterNotNull(p -> p.rotateRight());
        }).setTooltipKey("rotateRightActionTooltip")
                .setIcon(Icons.ROTATE_RIGHT, null, null)
                .setAccelerator(KeyEvent.VK_R, InputEvent.ALT_DOWN_MASK);

        moveLeftAction = new ApplicationAction("moveLeftAction", (e, action) -> {
            ifPainterNotNull(p -> p.moveOnePixelLeft());
        }).setTooltipKey("moveLeftActionTooltip")
                .setIcon(Icons.MOVE_LEFT, null, null)
                .setAccelerator(KeyEvent.VK_LEFT, InputEvent.ALT_DOWN_MASK);

        moveRightAction = new ApplicationAction("moveRightAction", (e, action) -> {
            ifPainterNotNull(p -> p.moveOnePixelRight());
        }).setTooltipKey("moveRightActionTooltip")
                .setIcon(Icons.MOVE_RIGHT, null, null)
                .setAccelerator(KeyEvent.VK_RIGHT, InputEvent.ALT_DOWN_MASK);

        moveUpAction = new ApplicationAction("moveUpAction", (e, action) -> {
            ifPainterNotNull(p -> p.moveOnePixelUp());
        }).setTooltipKey("moveUpActionTooltip")
                .setIcon(Icons.MOVE_UP, null, null)
                .setAccelerator(KeyEvent.VK_UP, InputEvent.ALT_DOWN_MASK);

        moveDownAction = new ApplicationAction("moveDownAction", (e, action) -> {
            ifPainterNotNull(p -> p.moveOnePixelDown());
        }).setTooltipKey("moveDownActionTooltip")
                .setIcon(Icons.MOVE_DOWN, null, null)
                .setAccelerator(KeyEvent.VK_DOWN, InputEvent.ALT_DOWN_MASK);

        eraseAction = new ApplicationAction("eraseAction", (e, action) -> {
            ifPainterNotNull(p -> p.erase());
        }).setTooltipKey("eraseActionTooltip")
                .setIcon(Icons.ERASE, null, null)
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

    @Nullable
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

    public void setPainter(@Nullable GlyphPainter value) {
        painter = value;
    }

    private void ifPainterNotNull(Consumer<@NonNull GlyphPainter> consumer) {
        final var painter = this.painter;
        if (painter != null) {
            consumer.accept(painter);
        }
    }
}
