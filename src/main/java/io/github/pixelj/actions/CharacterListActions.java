package io.github.pixelj.actions;

import io.github.pixelj.models.CharacterModel;
import io.github.pixelj.models.DisplayListModel;
import io.github.pixelj.resources.Icons;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.List;

public class CharacterListActions {
    private final ApplicationAction showAddDialogAction = new ApplicationAction(
            "showAddDialog",
            (e) -> System.out.println("Show add dialog action"),
            "showAddDialogAction",
            null,
            Icons.FILE_NEW,
            null,
            null,
            KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK)
    );
    private final ApplicationAction showRemoveDialogAction = new ApplicationAction(
            "showRemoveDialog",
            (e) -> System.out.println("Show remove dialog action"),
            "showRemoveDialogAction",
            null,
            Icons.FILE_NEW,
            null,
            null,
            KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK)
    );
    private final Collection<ApplicationAction> all = List.of(showAddDialogAction, showRemoveDialogAction);
    private @Nullable DisplayListModel<CharacterModel> listModel;
    private @Nullable ListSelectionModel selectionModel;
    private final ListSelectionListener selectionListener = e -> {
        showRemoveDialogAction.setEnabled(selectionModel.getMinSelectionIndex() >= 0);
    };

    public CharacterListActions(
            @Nullable DisplayListModel<CharacterModel> listModel,
            @Nullable ListSelectionModel selectionModel
    ) {
        this.listModel = listModel;
        this.selectionModel = selectionModel;
        if (selectionModel != null) selectionModel.addListSelectionListener(selectionListener);
    }

    public CharacterListActions() {
    }

    public Collection<ApplicationAction> getAll() {
        return all;
    }

    public ApplicationAction getShowAddDialogAction() {
        return showAddDialogAction;
    }

    public ApplicationAction getShowRemoveDialogAction() {
        return showRemoveDialogAction;
    }

    public void setListModel(@Nullable DisplayListModel<CharacterModel> value) {
        listModel = value;
    }

    public void setSelectionModel(@Nullable ListSelectionModel value) {
        if (selectionModel != null) selectionModel.removeListSelectionListener(selectionListener);
        selectionModel = value;
        if (selectionModel != null) selectionModel.addListSelectionListener(selectionListener);
    }
}
