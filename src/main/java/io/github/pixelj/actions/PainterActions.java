package io.github.pixelj.actions;

import io.github.pixelj.controls.GlyphPainter;
import io.github.pixelj.controls.painter.Painter;
import io.github.pixelj.resources.Icons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.List;

public class PainterActions {
    private boolean enabled = true;
    private @Nullable GlyphPainter painter;
    public final ApplicationAction symmetryToggleAction = new ApplicationAction(
            "symmetryToggleAction",
            (e, action) -> {
                if (painter != null) {
                    painter.setSymmetrical(!painter.isSymmetrical());
                    // Fix selected state if the action performed not because of a button press but
                    // its shortcut:
                    if (e.getSource() instanceof JToggleButton) {
                        return;
                    }
                    action.putValue(Action.SELECTED_KEY, painter.isSymmetrical());
                }
            }
    )
            .setTooltipKey("symmetryToggleActionTooltip")
            .setIcon(Icons.HISTORY_UNDO, null, null)
            .setAccelerator(KeyEvent.VK_S, InputEvent.ALT_DOWN_MASK);

    public final ApplicationAction historyUndoAction = new ApplicationAction(
            "painterHistoryUndoAction",
            (e, action) -> {
                if (painter != null) {
                    System.out.println("Painter undo");
                }
            }
    )
            .setTooltipKey("painterHistoryUndoActionTooltip")
            .setIcon(Icons.HISTORY_UNDO, null, null)
            .setAccelerator(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK);

    public final ApplicationAction historyRedoAction = new ApplicationAction(
            "painterHistoryRedoAction",
            (e, action) -> {
                if (painter != null) {
                    System.out.println("Painter redo");
                }
            }
    )
            .setTooltipKey("painterHistoryRedoActionTooltip")
            .setIcon(Icons.HISTORY_UNDO, null, null)
            .setAccelerator(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK);

    public final ApplicationAction clipboardCutAction = new ApplicationAction(
            "painterClipboardCutAction",
            (e, action) -> {
                if (painter != null) {
                    System.out.println("Painter cut");
                }
            }
    )
            .setTooltipKey("painterClipboardCutActionTooltip")
            .setIcon(Icons.HISTORY_UNDO, null, null)
            .setAccelerator(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK);

    public final ApplicationAction clipboardCopyAction = new ApplicationAction(
            "painterClipboardCopyAction",
            (e, action) -> {
                if (painter != null) {
                    System.out.println("Painter copy");
                }
            }
    )
            .setTooltipKey("painterClipboardCopyActionTooltip")
            .setIcon(Icons.HISTORY_UNDO, null, null)
            .setAccelerator(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK);

    public final ApplicationAction clipboardPasteAction = new ApplicationAction(
            "painterClipboardPasteAction",
            (e, action) -> {
                if (painter != null) {
                    System.out.println("Painter paste");
                }
            }
    )
            .setTooltipKey("painterClipboardPasteActionTooltip")
            .setIcon(Icons.HISTORY_UNDO, null, null)
            .setAccelerator(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK);

    public final ApplicationAction clipboardImportAction = new ApplicationAction(
            "painterClipboardImportAction",
            (e, action) -> {
                if (painter != null) {
                    System.out.println("Painter import clip");
                }
            }
    )
            .setTooltipKey("painterClipboardImportActionTooltip")
            .setIcon(Icons.HISTORY_UNDO, null, null)
            .setAccelerator(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK);

    public final ApplicationAction flipVerticallyAction = new ApplicationAction(
            "flipVerticallyAction",
            (e, action) -> {
                if (painter != null) {
                    painter.flipVertically();
                }
            }
    )
            .setTooltipKey("flipVerticallyActionTooltip")
            .setIcon(Icons.HISTORY_UNDO, null, null)
            .setAccelerator(KeyEvent.VK_V, InputEvent.ALT_DOWN_MASK);

    public final ApplicationAction flipHorizontallyAction = new ApplicationAction(
            "flipHorizontallyAction",
            (e, action) -> {
                if (painter != null) {
                    painter.flipHorizontally();
                }
            }
    )
            .setTooltipKey("flipHorizontallyActionTooltip")
            .setIcon(Icons.HISTORY_UNDO, null, null)
            .setAccelerator(KeyEvent.VK_H, InputEvent.ALT_DOWN_MASK);

    public final ApplicationAction rotateLeftAction = new ApplicationAction(
            "rotateLeftAction",
            (e, action) -> {
                if (painter != null) {
                    painter.rotateLeft();
                }
            }
    )
            .setTooltipKey("rotateLeftActionTooltip")
            .setIcon(Icons.HISTORY_UNDO, null, null)
            .setAccelerator(KeyEvent.VK_L, InputEvent.ALT_DOWN_MASK);

    public final ApplicationAction rotateRightAction = new ApplicationAction(
            "rotateRightAction",
            (e, action) -> {
                if (painter != null) {
                    painter.rotateRight();
                }
            }
    )
            .setTooltipKey("rotateRightActionTooltip")
            .setIcon(Icons.HISTORY_UNDO, null, null)
            .setAccelerator(KeyEvent.VK_R, InputEvent.ALT_DOWN_MASK);

    public final ApplicationAction moveLeftAction = new ApplicationAction(
            "moveLeftAction",
            (e, action) -> {
                if (painter != null) {
                    painter.moveOnePixelLeft();
                }
            }
    )
            .setTooltipKey("moveLeftActionTooltip")
            .setIcon(Icons.HISTORY_UNDO, null, null)
            .setAccelerator(KeyEvent.VK_LEFT, InputEvent.ALT_DOWN_MASK);

    public final ApplicationAction moveRightAction = new ApplicationAction(
            "moveRightAction",
            (e, action) -> {
                if (painter != null) {
                    painter.moveOnePixelRight();
                }
            }
    )
            .setTooltipKey("moveRightActionTooltip")
            .setIcon(Icons.HISTORY_UNDO, null, null)
            .setAccelerator(KeyEvent.VK_RIGHT, InputEvent.ALT_DOWN_MASK);

    public final ApplicationAction moveUpAction = new ApplicationAction(
            "moveUpAction",
            (e, action) -> {
                if (painter != null) {
                    painter.moveOnePixelUp();
                }
            }
    )
            .setTooltipKey("moveUpActionTooltip")
            .setIcon(Icons.HISTORY_UNDO, null, null)
            .setAccelerator(KeyEvent.VK_UP, InputEvent.ALT_DOWN_MASK);

    public final ApplicationAction moveDownAction = new ApplicationAction(
            "moveDownAction",
            (e, action) -> {
                if (painter != null) {
                    painter.moveOnePixelDown();
                }
            }
    )
            .setTooltipKey("moveDownActionTooltip")
            .setIcon(Icons.HISTORY_UNDO, null, null)
            .setAccelerator(KeyEvent.VK_DOWN, InputEvent.ALT_DOWN_MASK);

    public final ApplicationAction eraseAction = new ApplicationAction(
            "eraseAction",
            (e, action) -> {
                if (painter != null) {
                    painter.erase();
                }
            }
    )
            .setTooltipKey("eraseActionTooltip")
            .setIcon(Icons.HISTORY_UNDO, null, null)
            .setAccelerator(KeyEvent.VK_X, InputEvent.ALT_DOWN_MASK);

    public final Collection<ApplicationAction> all = List.of(
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
    );

    public PainterActions(@NotNull GlyphPainter painter) {
        this.painter = painter;
        symmetryToggleAction.putValue(Action.SELECTED_KEY, false);
    }

    public PainterActions() {
    }

    public @Nullable Painter getPainter() {
        return painter;
    }

    public void setPainter(@Nullable GlyphPainter value) {
        painter = value;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean value) {
        enabled = value;
        Actions.setEnabled(this.all, enabled);
    }
}
