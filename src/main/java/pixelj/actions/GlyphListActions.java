package pixelj.actions;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;

import pixelj.graphics.BinaryImage;
import pixelj.models.Glyph;
import pixelj.models.KerningPair;
import pixelj.models.Project;
import pixelj.resources.Resources;
import pixelj.views.glyphspage.AddDialog;

public final class GlyphListActions {
    /**
     * A collection of all actions.
     */
    public final Collection<ApplicationAction> all;
    /**
     * Display a dialog to add new glyphs to the project.
     */
    public final ApplicationAction addGlyphsAction;
    /**
     * Remove the selected glyphs.
     */
    public final ApplicationAction removeGlyphsAction;

    private Dimension canvasSize;
    private int defaultWidth;
    private boolean enabled = true;
    private final AddDialog addDialog;
    private final Project project;
    private final JFrame window;
    private final ListSelectionModel selectionModel;

    public GlyphListActions(
            final Project project,
            final ListSelectionModel selectionModel,
            final JFrame window
    ) {
        this.project = project;
        this.selectionModel = selectionModel;
        this.window = window;
        addDialog = new AddDialog(window);

        addGlyphsAction = new ApplicationAction("addGlyphsAction", this::showAddDialog)
                .withText()
                .setAccelerator(KeyEvent.VK_PLUS, InputEvent.ALT_DOWN_MASK);

        removeGlyphsAction = new ApplicationAction("removeGlyphsAction", this::showRemoveDialog)
                .withText()
                .setAccelerator(KeyEvent.VK_MINUS, InputEvent.ALT_DOWN_MASK);

        all = List.of(addGlyphsAction, removeGlyphsAction);

        final var documentSettings = project.getDocumentSettings();
        canvasSize = new Dimension(documentSettings.canvasWidth(), documentSettings.canvasHeight());
        defaultWidth = documentSettings.defaultWidth();
        project.documentSettingsProperty.addChangeListener((source, settings) -> {
            canvasSize = new Dimension(settings.canvasWidth(), settings.canvasHeight());
            defaultWidth = settings.defaultWidth();
        });

        selectionModel.addListSelectionListener(e -> 
                removeGlyphsAction.setEnabled(selectionModel.getMinSelectionIndex() >= 0)
        );
    }

    @SuppressWarnings("unused")
    private void addCharacters(final int... codePoints) {
        for (final var codePoint : codePoints) {
            project.getGlyphs().add(
                    new Glyph(codePoint, defaultWidth, BinaryImage.of(canvasSize.width, canvasSize.height))
            );
        }
        project.setDirty(true);
    }

    private int countAffectedKerningPairs(final Collection<Glyph> characters) {
        final var kerningPairs = new HashSet<KerningPair>();
        for (final var character : characters) {
            kerningPairs.addAll(project.findDependent(character));
        }
        return kerningPairs.size();
    }

    public int getDefaultCharacterWidth() {
        return defaultWidth;
    }

    public void setDefaultCharacterWidth(final int defaultCharacterWidth) {
        this.defaultWidth = defaultCharacterWidth;
    }

    public boolean isEnabled() {
        return enabled;
    }

    /**
     * @param value Is enabled
     */
    public void setEnabled(final boolean value) {
        enabled = value;
        Actions.setEnabled(all, enabled);
    }

    private void showAddDialog(final ActionEvent event, final Action action) {
        addDialog.setVisible(true);
        final var result = addDialog.getResult();
        if (result.isEmpty()) {
            return;
        }
        for (final var characterData : result) {
            project.getGlyphs().add(
                    new Glyph(
                        characterData.codePoint(),
                        defaultWidth,
                        BinaryImage.of(canvasSize.width, canvasSize.height, true)
                    )
            );
        }
        project.setDirty(true);
    }

    private void showRemoveDialog(final ActionEvent event, final Action action) {
        final var indices = selectionModel.getSelectedIndices();
        if (indices.length == 0) {
            return;
        }

        final var listModel = project.getGlyphs();
        final var removed = Arrays.stream(indices).mapToObj(listModel::getElementAt).toList();
        final var affected = countAffectedKerningPairs(removed);
        final var res = Resources.get();
        final var message = affected == 0 
                ? res.formatString("removingGlyphsMessage", indices.length)
                : res.formatString("removingGlyphsAndKerningPairsMessage", indices.length, affected);

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

        // Project model should take care of removing the affected kerning pairs.
        listModel.removeAll(removed);
        project.setDirty(true);
    }
}
