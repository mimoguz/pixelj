package pixelj.views.kerning_pairs_screen;

import javax.swing.JComponent;

import pixelj.models.Project;

public final class KerningPairsScreen extends KerningPairsScreenBase {

    public KerningPairsScreen(final Project project, final JComponent root) {
        super(new EditorPanel(project), new ListPanel(project, root));

        // Connect the listModel to the editor
        final var selectionModel = listPanel.getSelectionModel();
        selectionModel.addListSelectionListener(e -> {
            final var index = selectionModel.getMinSelectionIndex();
            if (index == selectionModel.getMaxSelectionIndex() && index >= 0) {
                // Set the editor panel model when one and only one item is selected: 
                editorPanel.setModel(listPanel.getListModel().getElementAt(index));
            } else {
                editorPanel.setModel(null);
            }
        });

        editorPanel.valueSpinner.addChangeListener(e -> project.setDirty(true));
    }
}
