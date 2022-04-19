package io.github.mimoguz.pixelj.actions;

import io.github.mimoguz.pixelj.models.KerningPairListModel;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.List;

@ParametersAreNonnullByDefault
public class KerningPairListActions {
    public final ApplicationAction showAddDialogAction = new ApplicationAction(
            "showAddDialog",
            (e, action) -> System.out.println("Show add dialog action")
    )
            .setTextKey("showAddDialogAction")
            .setAccelerator(KeyEvent.VK_PLUS, InputEvent.ALT_DOWN_MASK);
    ;
    public final ApplicationAction showRemoveDialogAction = new ApplicationAction(
            "showRemoveDialog",
            (e, action) -> removeSelected()
    )
            .setTextKey("showRemoveDialogAction")
            .setAccelerator(KeyEvent.VK_MINUS, InputEvent.ALT_DOWN_MASK);

    public final Collection<ApplicationAction> all = List.of(showAddDialogAction, showRemoveDialogAction);
    private KerningPairListModel listModel;
    private ListSelectionModel selectionModel;

    public KerningPairListActions(
            final KerningPairListModel listModel,
            final ListSelectionModel selectionModel
    ) {

        this.listModel = listModel;
        this.selectionModel = selectionModel;
    }

    ;

    private void removeSelected() {
    }
}
