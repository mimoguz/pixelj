package io.github.pixelj.actions;

import io.github.pixelj.models.CharacterModel;
import io.github.pixelj.models.DisplayListModel;
import io.github.pixelj.resources.Icons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
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
            .setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, InputEvent.ALT_DOWN_MASK));

    public final ApplicationAction showRemoveDialogAction = new ApplicationAction(
            "showRemoveDialog",
            (e, action) -> System.out.println("Show remove dialog action")
    )
            .setTextKey("showRemoveDialogAction")
            .setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, InputEvent.ALT_DOWN_MASK));

    public final Collection<ApplicationAction> all = List.of(showAddDialogAction, showRemoveDialogAction);
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
