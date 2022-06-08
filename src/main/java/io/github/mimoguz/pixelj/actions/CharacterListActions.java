package io.github.mimoguz.pixelj.actions;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.swing.*;

import io.github.mimoguz.pixelj.graphics.BinaryImage;
import io.github.mimoguz.pixelj.models.CharacterItem;
import io.github.mimoguz.pixelj.models.KerningPair;
import io.github.mimoguz.pixelj.models.Metrics;
import io.github.mimoguz.pixelj.models.Project;
import io.github.mimoguz.pixelj.resources.Resources;
import io.github.mimoguz.pixelj.views.characters_screen.AddDialog;

public class CharacterListActions {
    public final Collection<ApplicationAction> all;
    public final ApplicationAction showAddDialogAction;
    public final ApplicationAction showRemoveDialogAction;

    private final AddDialog addDialog;
    private Dimension canvasSize;
    private int defaultCharacterWidth;
    private boolean enabled = true;
    private final Project project;
    private final JComponent root;
    private final ListSelectionModel selectionModel;

    public CharacterListActions(
            final Project project,
            final ListSelectionModel selectionModel,
            final Metrics metrics,
            final JComponent root
    ) {
        this.project = project;
        this.selectionModel = selectionModel;
        this.root = root;

        addDialog = new AddDialog((JFrame) SwingUtilities.getWindowAncestor(root));

        showAddDialogAction = new ApplicationAction("charactersShowAddDialogAction", this::showAddDialog)
                .setTextKey("charactersShowAddDialogAction")
                .setAccelerator(KeyEvent.VK_PLUS, InputEvent.ALT_DOWN_MASK);

        showRemoveDialogAction = new ApplicationAction(
                "charactersShowRemoveDialogAction",
                this::showRemoveDialog
        ).setTextKey("charactersShowRemoveDialogAction")
                .setAccelerator(KeyEvent.VK_MINUS, InputEvent.ALT_DOWN_MASK);

        all = java.util.Collections
                .unmodifiableCollection(List.of(showAddDialogAction, showRemoveDialogAction));

        canvasSize = new Dimension(metrics.canvasWidth(), metrics.canvasHeight());
        defaultCharacterWidth = metrics.defaultCharacterWidth();

        selectionModel.addListSelectionListener(
                e -> showRemoveDialogAction.setEnabled(selectionModel.getMinSelectionIndex() >= 0)
        );
    }

    public int getDefaultCharacterWidth() {
        return defaultCharacterWidth;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setDefaultCharacterWidth(final int defaultCharacterWidth) {
        this.defaultCharacterWidth = defaultCharacterWidth;
    }

    public void setEnabled(final boolean value) {
        enabled = value;
        Actions.setEnabled(all, enabled);
    }

    public void updateMetrics(final Metrics metrics) {
        canvasSize = new Dimension(metrics.canvasWidth(), metrics.canvasHeight());
        defaultCharacterWidth = metrics.defaultCharacterWidth();
    }

    @SuppressWarnings("unused")
    private void addCharacters(final int... codePoints) {
        for (final var codePoint : codePoints) {
            project.getCharacters()
                    .add(
                            new CharacterItem(
                                    codePoint,
                                    defaultCharacterWidth,
                                    BinaryImage.of(canvasSize.width, canvasSize.height)
                            )
                    );
        }
    }

    private int countAffectedKerningPairs(final Collection<CharacterItem> characters) {
        final var kerningPairs = new HashSet<KerningPair>();
        for (final var character : characters) {
            kerningPairs.addAll(project.findDependent(character));
        }
        return kerningPairs.size();
    }

    private void showAddDialog(final ActionEvent event, final Action action) {
        addDialog.setVisible(true);
        final var result = addDialog.getResult();
        if (result.isEmpty()) {
            return;
        }
        for (final var characterData : result) {
            project.getCharacters()
                    .add(
                            new CharacterItem(
                                    characterData.codePoint(),
                                    defaultCharacterWidth,
                                    BinaryImage.of(canvasSize.width, canvasSize.height, true)
                            )
                    );
        }
    }

    private void showRemoveDialog(final ActionEvent event, final Action action) {
        final var indices = selectionModel.getSelectedIndices();
        if (indices.length == 0) {
            return;
        }

        final var listModel = project.getCharacters();
        final var characters = Arrays.stream(indices).mapToObj(listModel::getElementAt).toList();
        final var kerningPairs = countAffectedKerningPairs(characters);

        final var res = Resources.get();
        final var message = kerningPairs == 0 ? res.formatString("removingCharactersMessage", indices.length)
                : res.formatString("removingCharactersAndKerningPairsMessage", indices.length, kerningPairs);
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
        listModel.removeAll(characters);
    }
}
