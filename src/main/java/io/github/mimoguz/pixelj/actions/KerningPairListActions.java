package io.github.mimoguz.pixelj.actions;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

import javax.swing.*;

import io.github.mimoguz.pixelj.models.Project;
import io.github.mimoguz.pixelj.resources.Resources;
import io.github.mimoguz.pixelj.views.kerning_pairs_screen.AddDialog;

public class KerningPairListActions {
    public final Collection<ApplicationAction> all;
    public final ApplicationAction showAddDialogAction;
    public final ApplicationAction showRemoveDialogAction;

    private final AddDialog addDialog;
    @SuppressWarnings("unused")
    private final Logger logger;
    private final Project project;
    private final JComponent root;
    @SuppressWarnings("unused")
    private final ListSelectionModel selectionModel;

    public KerningPairListActions(
            final Project project,
            final ListSelectionModel selectionModel,
            final JComponent root
    ) {

        this.project = project;
        this.selectionModel = selectionModel;
        this.root = root;

        addDialog = new AddDialog(project.getCharacters(), (Frame) SwingUtilities.getWindowAncestor(root));

        logger = Logger.getLogger(this.getClass().getName());
        logger.addHandler(new ConsoleHandler());

        showAddDialogAction = new ApplicationAction("showAddDialog", this::showAddDialog)
                .setTextKey("kerningPairsShowAddDialogAction")
                .setAccelerator(KeyEvent.VK_PLUS, InputEvent.ALT_DOWN_MASK);

        showRemoveDialogAction = new ApplicationAction("showRemoveDialog", this::showRemoveDialog)
                .setTextKey("kerningPairsShowRemoveDialogAction")
                .setAccelerator(KeyEvent.VK_MINUS, InputEvent.ALT_DOWN_MASK);

        selectionModel.addListSelectionListener(
                e -> showRemoveDialogAction.setEnabled(selectionModel.getMinSelectionIndex() >= 0)
        );

        all = java.util.Collections
                .unmodifiableCollection(List.of(showAddDialogAction, showRemoveDialogAction));
    }

    private void showAddDialog(final ActionEvent event, final Action action) {
        addDialog.setVisible(true);
        final var result = addDialog.getResult();
        if (result != null) {
            project.getKerningPairs().add(result);
        }
    }

    private void showRemoveDialog(final ActionEvent event, final Action action) {
        final var indices = selectionModel.getSelectedIndices();
        if (indices.length == 0) {
            return;
        }
        final var res = Resources.get();
        final var message = res.formatString("removingKerningPairsMessage", indices.length);
        final var result = JOptionPane.showConfirmDialog(
                root,
                message,
                res.getString("nonUndoable"),
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        if (result != JOptionPane.OK_OPTION) {
            return;
        }
        final var listModel = project.getKerningPairs();
        listModel.removeAll(Arrays.stream(indices).mapToObj(listModel::getElementAt).toList());
    }
}
