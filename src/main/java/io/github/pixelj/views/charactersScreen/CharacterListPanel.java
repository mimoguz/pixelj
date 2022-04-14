package io.github.pixelj.views.charactersScreen;

import io.github.pixelj.actions.Actions;
import io.github.pixelj.actions.CharacterListActions;
import io.github.pixelj.controls.SearchableComboBox;
import io.github.pixelj.models.CharacterListModel;
import io.github.pixelj.models.CharacterModel;
import io.github.pixelj.util.Components;
import io.github.pixelj.views.shared.Dimensions;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.List;

public class CharacterListPanel extends JPanel {
    private final CharacterListActions actions;
    private final JButton addButton;
    private final SearchableComboBox<String> filterBox;
    private final JList<CharacterModel> list;
    private final JButton removeButton;
    private final ListSelectionModel selectionModel;

    public CharacterListPanel(
            @NotNull CharacterListModel listModel,
            @NotNull ListSelectionModel selectionModel,
            @NotNull JComponent root
    ) {
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
        filterBox = new SearchableComboBox<>(List.of("Foo"));
    }

    public JButton getAddButton() {
        return addButton;
    }

    public SearchableComboBox<String> getFilterBox() {
        return filterBox;
    }

    public JList<CharacterModel> getList() {
        return list;
    }

    public JButton getRemoveButton() {
        return removeButton;
    }

    @Override
    public void setEnabled(boolean value) {
        super.setEnabled(value);
        Actions.setEnabled(actions.all, value);
    }

    public void setListModel(CharacterListModel listModel) {
        selectionModel.clearSelection();
        list.setModel(listModel);
    }
}
