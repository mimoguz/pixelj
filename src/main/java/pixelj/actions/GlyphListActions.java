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
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import pixelj.graphics.BinaryImage;
import pixelj.models.Glyph;
import pixelj.models.KerningPair;
import pixelj.models.Metrics;
import pixelj.models.Project;
import pixelj.resources.Resources;
import pixelj.views.glyphs_screen.AddDialog;

public class GlyphListActions {
    private final AddDialog addDialog;
    public final Collection<ApplicationAction> all;
    private Dimension canvasSize;

    private int defaultWidth;
    private boolean enabled = true;
    private final Project project;
    private final JComponent root;
    private final ListSelectionModel selectionModel;
    public final ApplicationAction showAddDialogAction;
    public final ApplicationAction showRemoveDialogAction;

    public GlyphListActions(
            final Project project,
            final ListSelectionModel selectionModel,
            final Metrics metrics,
            final JComponent root
    ) {
        this.project = project;
        this.selectionModel = selectionModel;
        this.root = root;

        addDialog = new AddDialog((JFrame) SwingUtilities.getWindowAncestor(root));

        showAddDialogAction = new ApplicationAction("addGlyphsAction", this::showAddDialog)
                .setTextKey("addGlyphsAction")
                .setAccelerator(KeyEvent.VK_PLUS, InputEvent.ALT_DOWN_MASK);

        showRemoveDialogAction = new ApplicationAction("removeGlyphsAction", this::showRemoveDialog)
                .setTextKey("removeGlyphsAction")
                .setAccelerator(KeyEvent.VK_MINUS, InputEvent.ALT_DOWN_MASK);

        all = List.of(showAddDialogAction, showRemoveDialogAction);

        canvasSize = new Dimension(metrics.canvasWidth(), metrics.canvasHeight());
        defaultWidth = metrics.defaultWidth();

        selectionModel.addListSelectionListener(
                e -> showRemoveDialogAction.setEnabled(selectionModel.getMinSelectionIndex() >= 0)
        );
    }

    @SuppressWarnings("unused")
    private void addCharacters(final int... codePoints) {
        for (final var codePoint : codePoints) {
            project.getGlyphs()
                    .add(
                            new Glyph(
                                    codePoint,
                                    defaultWidth,
                                    BinaryImage.of(canvasSize.width, canvasSize.height)
                            )
                    );
        }
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setDefaultCharacterWidth(final int defaultCharacterWidth) {
        this.defaultWidth = defaultCharacterWidth;
    }

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
            project.getGlyphs()
                    .add(
                            new Glyph(
                                    characterData.codePoint(),
                                    defaultWidth,
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

        final var listModel = project.getGlyphs();
        final var removed = Arrays.stream(indices).mapToObj(listModel::getElementAt).toList();
        final var affected = countAffectedKerningPairs(removed);

        final var res = Resources.get();
        final var message = affected == 0 ? res.formatString("removingGlyphsMessage", indices.length)
                : res.formatString("removingGlyphsAndKerningPairsMessage", indices.length, affected);
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
        // Project model should take care of removing the affected kerning pairs.
        listModel.removeAll(removed);
    }

    public void updateMetrics(final Metrics metrics) {
        canvasSize = new Dimension(metrics.canvasWidth(), metrics.canvasHeight());
        defaultWidth = metrics.defaultWidth();
    }
}
