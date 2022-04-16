package io.github.mimoguz.pixelj.actions;

import io.github.mimoguz.pixelj.models.CharacterModel;
import io.github.mimoguz.pixelj.models.DisplayListModel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
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
    private @Nullable Dimension canvasSize;
    private int defaultCharacterWidth;
    private boolean enabled = true;
    private @Nullable DisplayListModel<CharacterModel> listModel;
    private @Nullable ListSelectionModel selectionModel;
    private final ListSelectionListener selectionListener = e -> {
        showRemoveDialogAction.setEnabled(selectionModel.getMinSelectionIndex() >= 0);
    };

    public CharacterListActions(
            @NotNull DisplayListModel<CharacterModel> listModel,
            @NotNull ListSelectionModel selectionModel
    ) {
        this.listModel = listModel;
        this.selectionModel = selectionModel;
        selectionModel.addListSelectionListener(selectionListener);
    }

    public CharacterListActions() {
    }

    public @Nullable Dimension getCanvasSize() {
        return canvasSize;
    }

    public void setCanvasSize(final @Nullable Dimension canvasSize) {
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

    public void setEnabled(boolean value) {
        enabled = value;
        Actions.setEnabled(all, enabled);
    }

    public void setListModel(@Nullable DisplayListModel<CharacterModel> value) {
        listModel = value;
    }

    public void setSelectionModel(@Nullable ListSelectionModel value) {
        if (selectionModel != null) {
            selectionModel.removeListSelectionListener(selectionListener);
        }
        selectionModel = value;
        if (selectionModel != null) {
            selectionModel.addListSelectionListener(selectionListener);
        }
    }
}
