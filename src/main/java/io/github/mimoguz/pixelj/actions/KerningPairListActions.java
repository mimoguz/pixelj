package io.github.mimoguz.pixelj.actions;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Action;
import javax.swing.ListSelectionModel;

import io.github.mimoguz.pixelj.models.KerningPairListModel;

public class KerningPairListActions {
    public final Collection<ApplicationAction> all;
    public final ApplicationAction showAddDialogAction;
    public final ApplicationAction showRemoveDialogAction;

    @SuppressWarnings("unused")
    private final KerningPairListModel listModel;
    @SuppressWarnings("unused")
    private final ListSelectionModel selectionModel;
    private final Logger logger;

    public KerningPairListActions(
            final KerningPairListModel listModel,
            final ListSelectionModel selectionModel
    ) {

        this.listModel = listModel;
        this.selectionModel = selectionModel;

        logger = Logger.getLogger(this.getClass().getName());
        logger.addHandler(new ConsoleHandler());

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
        logger.log(Level.INFO, "{0}", action.getValue(Action.NAME));
    }

    private void showRemoveDialog(final ActionEvent event, final Action action) {
        logger.log(Level.INFO, "{0}", action.getValue(Action.NAME));
    }
}
