package pixelj.views.kerningpairspage;

import javax.swing.JFrame;

import pixelj.models.Project;

public final class KerningPairsPage extends KerningPairsPageBase {

    public KerningPairsPage(final Project project, final JFrame window) {
        super(new EditorPanel(project), new ListPanel(project, window));

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
