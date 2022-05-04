package io.github.mimoguz.pixelj.actions;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.List;

import javax.swing.Action;
import javax.swing.ListSelectionModel;

import io.github.mimoguz.pixelj.models.KerningPairListModel;

public class KerningPairListActions {
    public final Collection<ApplicationAction> all;
    public final ApplicationAction showAddDialogAction;
    public final ApplicationAction showRemoveDialogAction;

    private final KerningPairListModel listModel;
    private final ListSelectionModel selectionModel;

    public KerningPairListActions(
            final KerningPairListModel listModel,
            final ListSelectionModel selectionModel
    ) {

        this.listModel = listModel;
        this.selectionModel = selectionModel;

        showAddDialogAction = new ApplicationAction("showAddDialog", this::showAddDialog)
                .setTextKey("kerningPairsShowAddDialogAction")
                .setAccelerator(KeyEvent.VK_PLUS, InputEvent.ALT_DOWN_MASK);

        showRemoveDialogAction = new ApplicationAction("showRemoveDialog", this::showRemoveDialog)
                .setTextKey("kerningPairsShowRemoveDialogAction")
                .setAccelerator(KeyEvent.VK_MINUS, InputEvent.ALT_DOWN_MASK);

        all = java.util.Collections
                .unmodifiableCollection(List.of(showAddDialogAction, showRemoveDialogAction));
    }

    private void showAddDialog(final ActionEvent event, final Action action) {
        System.out.println(action.getValue(Action.NAME));
    }

    private void showRemoveDialog(final ActionEvent event, final Action action) {
        System.out.println(action.getValue(Action.NAME));
    }
}
