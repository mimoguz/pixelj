package pixelj.views.projectwindow.glyphspage;

import java.awt.Frame;
import java.awt.event.KeyEvent;

import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;

import pixelj.actions.ApplicationAction;
import pixelj.graphics.BinaryImage;
import pixelj.models.Block;
import pixelj.models.Glyph;
import pixelj.models.Project;
import pixelj.models.Scalar;
import pixelj.resources.Icons;
import pixelj.resources.Resources;
import pixelj.views.shared.Help;
import pixelj.views.shared.ScalarCellRenderer;

/**
 * A dialog to select scalars to add. Application modal.
 */
public final class AddDialog extends AddDialogBase {

    private final DefaultListModel<Scalar> listModel = new DefaultListModel<>();
    private final ListSelectionModel selectionModel = new DefaultListSelectionModel();
    private final Project project;

    public AddDialog(final Frame owner, final Project project) {
        super(owner);
        this.project = project;

        selectionModel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        scalarList.setModel(listModel);
        scalarList.setSelectionModel(selectionModel);
        scalarList.setCellRenderer(new ScalarCellRenderer());

        final var res = Resources.get();

        filterBox.setSelectedIndex(0);
        if (filterBox.getSelectedItem() instanceof final Block block) {
            listModel.addAll(res.getScalars(block.id()));
        }
        filterBox.addActionListener(event -> {
            if (filterBox.getSelectedItem() instanceof final Block block) {
                listModel.clear();
                listModel.addAll(res.getScalars(block.id()));
            }
        });

        addButton.setEnabled(false);
        // TODO: Separate actions
        addButton.addActionListener(event -> addSelected());
        selectionModel.addListSelectionListener(
                e -> addButton.setEnabled(selectionModel.getMinSelectionIndex() >= 0)
        );

        closeButton.addActionListener(event -> setVisible(false));

        helpButton.setAction(
                new ApplicationAction(
                        "documentSettingsHelpAction",
                        (e, action) -> Help.showPage(Help.Page.GLYPHS)
                )
                        .setIcon(Icons.HELP)
                        .setTooltip(Resources.get().getString("help"))
        );

        getRootPane().registerKeyboardAction(
                (e) -> setVisible(false),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );
    }

    /**
     *  @param visible Is visible. Note that since this dialog is modal, passing true here block the caller.
     */
    @Override
    public void setVisible(final boolean visible) {
        if (visible) {
            selectionModel.clearSelection();
            scalarList.requestFocusInWindow();
        }
        super.setVisible(visible);
    }

    private void addSelected() {
        final var indices = selectionModel.getSelectedIndices();
        final var defaultWidth = project.getDocumentSettings().defaultWidth();
        final var canvasWidth = project.getDocumentSettings().canvasWidth();
        final var canvasHeight = project.getDocumentSettings().canvasHeight();
        for (final var i : indices) {
            final var scalarData = listModel.get(i);
            project.getGlyphs().add(
                    new Glyph(
                        scalarData.codePoint(),
                        defaultWidth,
                        BinaryImage.of(canvasWidth, canvasHeight, true)
                    )
            );
        }
        selectionModel.clearSelection();
        project.setDirty(true);
    }
}
