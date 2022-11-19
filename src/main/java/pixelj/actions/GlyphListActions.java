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

import javax.swing.*;
import java.awt.*;
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
     * Remove the selected glyphs.
     */
    public final ApplicationAction removeGlyphsAction;

    private final Collection<ApplicationAction> all;
    private Dimension canvasSize;
    private int defaultWidth;
    private final AddDialog addDialog;
    private final AddCodePointDialog addCodePointDialog;
    private final JFrame window;
    private final ListSelectionModel selectionModel;
    private final FilteredList<Glyph> listModel;

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

        removeGlyphsAction =
            new ApplicationAction("removeGlyphsAction", this::showRemoveDialog)
                .withText()
                .setTooltipWithAccelerator(null, KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.ALT_DOWN_MASK));

        all = List.of(addGlyphsAction, addCodePointAction, removeGlyphsAction);

        final var documentSettings = project.getDocumentSettings();
        canvasSize = new Dimension(documentSettings.canvasWidth(), documentSettings.canvasHeight());
        defaultWidth = documentSettings.defaultWidth();
        project.documentSettingsProperty.addChangeListener((source, settings) -> {
            canvasSize = new Dimension(settings.canvasWidth(), settings.canvasHeight());
            defaultWidth = settings.defaultWidth();
        });

        selectionModel.addListSelectionListener(e -> removeGlyphsAction.setEnabled(selectionModel.getMinSelectionIndex() >=
            0));
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
