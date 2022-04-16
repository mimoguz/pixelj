package io.github.mimoguz.pixelj.actions;

import io.github.mimoguz.pixelj.models.CharacterModel;
import io.github.mimoguz.pixelj.models.DisplayListModel;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.List;

public class CharacterListActions {
    public final ApplicationAction showAddDialogAction = new ApplicationAction(
            "showAddDialog",
            (e, action) -> System.out.println("Show add dialog action")
    )
            .setTextKey("showAddDialogAction")
            .setAccelerator(KeyEvent.VK_PLUS, InputEvent.ALT_DOWN_MASK);

    public final ApplicationAction showRemoveDialogAction = new ApplicationAction(
            "showRemoveDialog",
            (e, action) -> System.out.println("Show remove dialog action")
    )
            .setTextKey("showRemoveDialogAction")
            .setAccelerator(KeyEvent.VK_MINUS, InputEvent.ALT_DOWN_MASK);

    public final Collection<ApplicationAction> all = List.of(showAddDialogAction, showRemoveDialogAction);
    private Dimension canvasSize;
    private int defaultCharacterWidth;
    private boolean enabled = true;

    public CharacterListActions(
            final @NotNull DisplayListModel<CharacterModel> listModel,
            final @NotNull ListSelectionModel selectionModel
    ) {
        selectionModel.addListSelectionListener(e -> {
            showRemoveDialogAction.setEnabled(selectionModel.getMinSelectionIndex() >= 0);
        });
    }

    public @NotNull Dimension getCanvasSize() {
        return canvasSize;
    }

    public void setCanvasSize(final @NotNull Dimension canvasSize) {
        this.canvasSize = canvasSize;
    }

    public int getDefaultCharacterWidth() {
        return defaultCharacterWidth;
    }

    public void setDefaultCharacterWidth(final int defaultCharacterWidth) {
        this.defaultCharacterWidth = defaultCharacterWidth;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(final boolean value) {
        enabled = value;
        Actions.setEnabled(all, enabled);
    }
}
