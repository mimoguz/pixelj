package pixelj.actions;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;

import pixelj.models.Project;
import pixelj.resources.Resources;
import pixelj.views.projectwindow.kerningpairspage.AddDialog;

public final class KerningPairListActions implements Actions {

    /**
     * Show add kerning pair dialog.
     */
    public final ApplicationAction addAction;
    /**
     * Show remove kerning pairs dialog.
     */
    public final ApplicationAction removeAction;

    private final Collection<ApplicationAction> all;
    private final AddDialog addDialog;
    private final Project project;
    private final JFrame window;
    private final ListSelectionModel selectionModel;

    public KerningPairListActions(
        final Project project,
        final ListSelectionModel selectionModel,
        final JFrame window
    ) {

        this.project = project;
        this.selectionModel = selectionModel;
        this.window = window;

        addDialog = new AddDialog(window, project);

        addAction = new ApplicationAction("addKerningPairsAction", this::showAddDialog)
            .withText()
            .setTooltipWithAccelerator(
                    null,
                    KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.ALT_DOWN_MASK)
            );

        removeAction = new ApplicationAction("removeKerningPairsAction", this::showRemoveDialog)
            .withText()
            .setTooltipWithAccelerator(null, KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.ALT_DOWN_MASK));

        selectionModel.addListSelectionListener(
            e -> removeAction.setEnabled(selectionModel.getMinSelectionIndex() >= 0)
        );

        all = List.of(addAction, removeAction);
    }

    @Override
    public void detach() {
        addDialog.dispose();
    }

    @Override
    public Collection<ApplicationAction> getAll() {
        return all;
    }

    @Override
    public void setEnabled(final boolean enabled) {
        addAction.setEnabled(enabled);
        removeAction.setEnabled(enabled && selectionModel.getMaxSelectionIndex() >= 0);
    }

    private void showAddDialog(final ActionEvent event, final Action action) {
        addDialog.setVisible(true);
        final var result = addDialog.getResult();
        if (result != null) {
            project.getKerningPairs().add(result);
            project.setDirty(true);
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
            window,
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
        project.setDirty(true);
    }
}
