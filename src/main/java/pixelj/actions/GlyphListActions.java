package pixelj.actions;

import pixelj.messaging.DependentPairsQuestion;
import pixelj.messaging.Messenger;
import pixelj.messaging.RemoveGlyphsMessage;
import pixelj.models.FilteredList;
import pixelj.models.Glyph;
import pixelj.models.Project;
import pixelj.resources.Icon;
import pixelj.resources.Resources;
import pixelj.views.projectwindow.glyphspage.AddCodePointDialog;
import pixelj.views.projectwindow.glyphspage.AddDialog;
import pixelj.views.projectwindow.glyphspage.CopyDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("unused")
public final class GlyphListActions implements Actions {

    /**
     * Display a dialog to add new glyphs to the project.
     */
    public final ApplicationAction addGlyphsAction;
    /**
     * Display a dialog to add a glyph using its code point.
     */
    public final ApplicationAction addCodePointAction;
    /**
     * Display a dialog to transfer image from another glyph to selected glyphs.
     */
    public final ApplicationAction copyFromAction;
    /**
     * Remove the selected glyphs.
     */
    public final ApplicationAction removeGlyphsAction;

    private final Collection<ApplicationAction> all;
    private final AddDialog addDialog;
    private final AddCodePointDialog addCodePointDialog;
    private final JFrame window;
    private final ListSelectionModel selectionModel;
    private final FilteredList<Glyph> listModel;
    private final CopyDialog copyDialog;

    public GlyphListActions(
        final Project project,
        final ListSelectionModel selectionModel,
        final FilteredList<Glyph> listModel,
        final JFrame window
    ) {
        this.selectionModel = selectionModel;
        this.listModel = listModel;
        this.window = window;
        addDialog = new AddDialog(window);
        addCodePointDialog = new AddCodePointDialog(window, project);
        copyDialog = new CopyDialog(window, project);

        addGlyphsAction =
            new ApplicationAction("addGlyphsAction", this::showAddDialog)
                .withText()
                .setTooltipWithAccelerator(null, KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.ALT_DOWN_MASK));

        addCodePointAction =
            new ApplicationAction("addCodePointAction", this::showAddCodePointDialog)
                .setIcon(Icon.NUMBER)
                .setTooltipWithAccelerator(
                    Resources.get().getString("addCodePointActionTooltip"),
                    KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.ALT_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK)
                );

        copyFromAction =
            new ApplicationAction("copyFromAction", this::showCopyFromDialog)
                .setIcon(Icon.CLIPBOARD_PASTE_MULTIPLE)
                .setTooltip(Resources.get().getString("copyFromActionTooltip"));

        removeGlyphsAction =
            new ApplicationAction("removeGlyphsAction", this::showRemoveDialog)
                .withText()
                .setTooltipWithAccelerator(null, KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.ALT_DOWN_MASK));

        all = List.of(addGlyphsAction, addCodePointAction, copyFromAction, removeGlyphsAction);

        final var documentSettings = project.getDocumentSettings();
        selectionModel.addListSelectionListener(e -> {
            final var isSelected = selectionModel.getMinSelectionIndex() >= 0;
            removeGlyphsAction.setEnabled(isSelected);
            copyFromAction.setEnabled(isSelected);
        });
    }

    @Override
    public void detach() {
        addDialog.dispose();
        addCodePointDialog.dispose();
    }

    private void showAddDialog(final ActionEvent event, final Action action) {
        addDialog.setVisible(true);
    }

    private void showAddCodePointDialog(final ActionEvent event, final Action action) {
        addCodePointDialog.setVisible(true);
    }

    private void showCopyFromDialog(final ActionEvent actionEvent, final Action action) {
        final var targets = Arrays.stream(selectionModel.getSelectedIndices()).map(i -> listModel.getElementAt(i).getCodePoint()).toArray();
        copyDialog.setTargets(targets);
        copyDialog.setVisible(true);
    }

    private void showRemoveDialog(final ActionEvent event, final Action action) {
        final var indices = selectionModel.getSelectedIndices();
        if (indices.length == 0) {
            return;
        }

        final var removed = Arrays.stream(indices).mapToObj(listModel::getElementAt).toList();
        final int affected = Messenger
            .get(DependentPairsQuestion.class, Integer.class)
            .askOne(new DependentPairsQuestion(removed));

        final var res = Resources.get();
        final var
            message =
            affected == 0
                ? res.formatString("removingGlyphsMessage", indices.length)
                : res.formatString("removingGlyphsAndKerningPairsMessage", indices.length, affected);

        final var
            result =
            JOptionPane.showConfirmDialog(
                window,
                message,
                res.getString("nonUndoable"),
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE
            );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        // Project model should take care of removing the affected kerning pairs.
        Messenger.get(RemoveGlyphsMessage.class).send(new RemoveGlyphsMessage(removed));
    }

    @Override
    public Collection<ApplicationAction> getAll() {
        return all;
    }

    @Override
    public void setEnabled(final boolean enabled) {
        for (var action : all) {
            action.setEnabled(enabled);
        }
    }
}
