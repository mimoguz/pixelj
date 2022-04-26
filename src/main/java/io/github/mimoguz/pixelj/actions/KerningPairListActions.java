package io.github.mimoguz.pixelj.actions;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.Action;
import javax.swing.ListSelectionModel;

import org.eclipse.jdt.annotation.NonNullByDefault;

import io.github.mimoguz.pixelj.models.KerningPairListModel;

@NonNullByDefault
public class KerningPairListActions {
    public final Collection<ApplicationAction> all = new ArrayList<>();
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
                .setTextKey("showAddDialogAction")
                .setAccelerator(KeyEvent.VK_PLUS, InputEvent.ALT_DOWN_MASK);

        showRemoveDialogAction = new ApplicationAction("showRemoveDialog", this::showRemoveDialog)
                .setTextKey("showRemoveDialogAction")
                .setAccelerator(KeyEvent.VK_MINUS, InputEvent.ALT_DOWN_MASK);

        all.add(showAddDialogAction);
        all.add(showRemoveDialogAction);
    }

    private void showAddDialog(final ActionEvent event, final Action action) {
        System.out.println(action.getValue(Action.NAME));
    }

    private void showRemoveDialog(final ActionEvent event, final Action action) {
        System.out.println(action.getValue(Action.NAME));
    }
}
