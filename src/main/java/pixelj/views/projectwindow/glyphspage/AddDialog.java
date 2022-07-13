package pixelj.views.projectwindow.glyphspage;

import java.awt.Frame;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ListSelectionModel;

import pixelj.models.BlockRecord;
import pixelj.models.ScalarRecord;
import pixelj.resources.Resources;
import pixelj.views.shared.ScalarCellRenderer;

/**
 * A dialog to select scalars to add. Application modal.
 */
public final class AddDialog extends AddDialogBase {
    private final DefaultListModel<ScalarRecord> listModel = new DefaultListModel<>();
    private final ArrayList<ScalarRecord> result = new ArrayList<>();
    private final ListSelectionModel selectionModel = new DefaultListSelectionModel();

    public AddDialog(final Frame owner) {
        super(owner);

        selectionModel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        scalarList.setModel(listModel);
        scalarList.setSelectionModel(selectionModel);
        scalarList.setCellRenderer(new ScalarCellRenderer());

        final var res = Resources.get();

        filterBox.setSelectedIndex(0);
        if (filterBox.getSelectedItem() instanceof final BlockRecord block) {
            listModel.addAll(res.getScalars(block.id()));
        }
        filterBox.addActionListener(event -> {
            if (filterBox.getSelectedItem() instanceof final BlockRecord block) {
                listModel.clear();
                listModel.addAll(res.getScalars(block.id()));
            }
        });

        addButton.setEnabled(false);
        addButton.addActionListener(event -> {
            fillResult();
            setVisible(false);
        });
        selectionModel.addListSelectionListener(
                e -> addButton.setEnabled(selectionModel.getMinSelectionIndex() >= 0)
        );

        cancelButton.addActionListener(event -> setVisible(false));
    }

    /**
     * @return List of scalars the user selected. If the user presses the cancel button to dismiss this
     *         dialog, the result will be empty.
     */
    public Collection<ScalarRecord> getResult() {
        return Collections.unmodifiableCollection(result);
    }

    /**
     *  @param visible Is visible. Note that since this dialog is modal, passing true here block the caller.
     */
    @Override
    public void setVisible(final boolean visible) {
        if (visible) {
            result.clear();
            selectionModel.clearSelection();
        }
        super.setVisible(visible);
    }

    private void fillResult() {
        final var indices = selectionModel.getSelectedIndices();
        for (final var i : indices) {
            result.add(listModel.get(i));
        }
    }
}
