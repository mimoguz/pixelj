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

import pixelj.messaging.ProjectModifiedMessage;
import pixelj.messaging.RemoveKerningPairsMessage;
import pixelj.models.FilteredList;
import pixelj.models.KerningPair;
import pixelj.models.Project;
import pixelj.resources.Resources;
import pixelj.messaging.Messenger;
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
    private final JFrame window;
    private final ListSelectionModel selectionModel;
    private final FilteredList<KerningPair> displayListModel;

    public KerningPairListActions(
        final Project project,
        final ListSelectionModel selectionModel,
        final FilteredList<KerningPair> displayListModel,
        final JFrame window
    ) {

        this.selectionModel = selectionModel;
        this.displayListModel = displayListModel;
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
        final var pairs = Arrays.stream(indices).mapToObj(displayListModel::getElementAt).toList();
        Messenger.sendTo(new RemoveKerningPairsMessage(pairs), RemoveKerningPairsMessage.class);

    }
}
