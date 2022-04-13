package io.github.pixelj.actions;

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
    private boolean active = true;
    private @Nullable Painter painter;
    public final ApplicationAction historyUndoAction = new ApplicationAction(
            "painterHistoryUndoAction",
            (e) -> {
                if (painter != null) System.out.println("Painter undo");
            },
            null,
            "painterHistoryUndoActionTooltip",
            Icons.HISTORY_UNDO,
            null,
            null,
            KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK)
    );
    public final ApplicationAction historyRedoAction = new ApplicationAction(
            "painterHistoryRedoAction",
            (e) -> {
                if (painter != null) System.out.println("Painter redo");
            },
            null,
            "painterHistoryRedoActionTooltip",
            Icons.HISTORY_UNDO,
            null,
            null,
            KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK)
    );
    public final ApplicationAction clipboardCutAction = new ApplicationAction(
            "painterClipboardCutAction",
            (e) -> {
                if (painter != null) System.out.println("Painter cut");
            },
            null,
            "painterClipboardCutActionTooltip",
            Icons.HISTORY_UNDO,
            null,
            null,
            KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK)
    );
    public final ApplicationAction clipboardCopyAction = new ApplicationAction(
            "painterClipboardCopyAction",
            (e) -> {
                if (painter != null) System.out.println("Painter copy");
            },
            null,
            "painterClipboardCopyActionTooltip",
            Icons.HISTORY_UNDO,
            null,
            null,
            KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK)
    );
    public final ApplicationAction clipboardPasteAction = new ApplicationAction(
            "painterClipboardPasteAction",
            (e) -> {
                if (painter != null) System.out.println("Painter paste");
            },
            null,
            "painterClipboardPasteActionTooltip",
            Icons.HISTORY_UNDO,
            null,
            null,
            KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK)
    );
    public final ApplicationAction clipboardImportAction = new ApplicationAction(
            "painterClipboardImportAction",
            (e) -> {
                if (painter != null) System.out.println("Painter import clip");
            },
            null,
            "painterClipboardImportActionTooltip",
            Icons.HISTORY_UNDO,
            null,
            null,
            KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK)
    );
    public final ApplicationAction flipVerticallyAction = new ApplicationAction(
            "flipVerticallyAction",
            (e) -> {
                if (painter != null) System.out.println("Painter flip vertically");
            },
            null,
            "flipVerticallyActionTooltip",
            Icons.HISTORY_UNDO,
            null,
            null,
            KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.ALT_DOWN_MASK)
    );
    public final ApplicationAction flipHorizontallyAction = new ApplicationAction(
            "flipHorizontallyAction",
            (e) -> {
                if (painter != null) System.out.println("Painter flip horizontally");
            },
            null,
            "flipHorizontallyActionTooltip",
            Icons.HISTORY_UNDO,
            null,
            null,
            KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.ALT_DOWN_MASK)
    );
    public final ApplicationAction rotateLeftAction = new ApplicationAction(
            "rotateLeftAction",
            (e) -> {
                if (painter != null) System.out.println("Painter rotate left");
            },
            null,
            "rotateLeftActionTooltip",
            Icons.HISTORY_UNDO,
            null,
            null,
            KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.ALT_DOWN_MASK)
    );
    public final ApplicationAction rotateRightAction = new ApplicationAction(
            "rotateRightAction",
            (e) -> {
                if (painter != null) System.out.println("Painter rotate right");
            },
            null,
            "rotateRightActionTooltip",
            Icons.HISTORY_UNDO,
            null,
            null,
            KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.ALT_DOWN_MASK)
    );
    public final ApplicationAction moveLeftAction = new ApplicationAction(
            "moveLeftAction",
            (e) -> {
                if (painter != null) System.out.println("Painter move left");
            },
            null,
            "moveLeftActionTooltip",
            Icons.HISTORY_UNDO,
            null,
            null,
            KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.ALT_DOWN_MASK)
    );
    public final ApplicationAction moveRightAction = new ApplicationAction(
            "moveRightAction",
            (e) -> {
                if (painter != null) System.out.println("Painter right left");
            },
            null,
            "moveRightActionTooltip",
            Icons.HISTORY_UNDO,
            null,
            null,
            KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.ALT_DOWN_MASK)
    );
    public final ApplicationAction moveUpAction = new ApplicationAction(
            "moveUpAction",
            (e) -> {
                if (painter != null) System.out.println("Painter right up");
            },
            null,
            "moveUpActionTooltip",
            Icons.HISTORY_UNDO,
            null,
            null,
            KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.ALT_DOWN_MASK)
    );
    public final ApplicationAction moveDownAction = new ApplicationAction(
            "moveDownAction",
            (e) -> {
                if (painter != null) System.out.println("Painter right down");
            },
            null,
            "moveDownActionTooltip",
            Icons.HISTORY_UNDO,
            null,
            null,
            KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.ALT_DOWN_MASK)
    );
    public final ApplicationAction eraseAction = new ApplicationAction(
            "eraseAction",
            (e) -> {
                if (painter != null) System.out.println("Painter erase");
            },
            null,
            "eraseActionTooltip",
            Icons.HISTORY_UNDO,
            null,
            null,
            KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.ALT_DOWN_MASK)
    );
    public final ApplicationAction symmetryToggleAction = new ApplicationAction(
            "symmetryToggleAction",
            (e) -> {
                if (painter != null) System.out.println("Painter toggle symmetry");
            },
            null,
            "symmetryToggleActionTooltip",
            Icons.HISTORY_UNDO,
            null,
            null,
            KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.ALT_DOWN_MASK)
    );
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

    public PainterActions(@NotNull Painter painter) {
        this.painter = painter;
        symmetryToggleAction.putValue(Action.SELECTED_KEY, false);
    }

    public PainterActions() {
    }

    public @Nullable Painter getPainter() {
        return painter;
    }

    public void setPainter(@Nullable Painter value) {
        painter = value;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean value) {
        active = value;
        Actions.setEnabled(this.all, active);
    }
}
