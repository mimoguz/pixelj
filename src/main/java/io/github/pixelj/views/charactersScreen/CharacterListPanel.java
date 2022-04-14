package io.github.pixelj.views.charactersScreen;

import io.github.pixelj.actions.Actions;
import io.github.pixelj.actions.CharacterListActions;
import io.github.pixelj.models.CharacterListModel;
import io.github.pixelj.models.CharacterModel;
import io.github.pixelj.util.Components;
import io.github.pixelj.views.shared.Dimensions;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class CharacterListPanel extends JPanel {
    private final CharacterListActions actions;
    private final JButton addButton;
    private final JList<CharacterModel> list;
    private final @NotNull CharacterListModel listModel;
    private final JButton removeButton;
    private final @NotNull ListSelectionModel selectionModel;


    public CharacterListPanel(
            @NotNull CharacterListModel listModel,
            @NotNull ListSelectionModel selectionModel,
            @NotNull JComponent root
    ) {
        this.listModel = listModel;
        this.selectionModel = selectionModel;
        actions = new CharacterListActions(listModel, selectionModel);
        actions.showRemoveDialogAction.setEnabled(false);
        Actions.registerShortcuts(actions.all, root);

        addButton = new JButton();
        addButton.setAction(actions.showAddDialogAction);
        Components.setFixedSize(addButton, Dimensions.textButtonSize);

        removeButton = new JButton();
        removeButton.setAction(actions.showRemoveDialogAction);
        Components.setFixedSize(removeButton, Dimensions.textButtonSize);

        list = new JList<>(listModel);
        list.setSelectionModel(selectionModel);
    }

    @Override
    public void setEnabled(boolean value) {
        super.setEnabled(value);
        Actions.setEnabled(actions.all, value);
    }
}
