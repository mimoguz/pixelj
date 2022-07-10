package pixelj.views.kerning_pairs_screen;

import javax.swing.JComponent;
import javax.swing.JSplitPane;

import pixelj.models.Project;
import pixelj.util.Detachable;
import pixelj.views.shared.Dimensions;

public final class KerningPairsScreen extends JSplitPane implements Detachable {
    private final EditorPanel editorPanel;
    private final ListPanel listPanel;

    public KerningPairsScreen(final Project project, final JComponent root) {
        listPanel = new ListPanel(project, root);
        editorPanel = new EditorPanel(project);

        final var selectionModel = listPanel.getSelectionModel();
        // Connect the listModel to the editor
        selectionModel.addListSelectionListener(e -> {
            final var index = selectionModel.getMinSelectionIndex();
            if (index == selectionModel.getMaxSelectionIndex() && index >= 0) {
                // Set the editor panel model when one and only one item is selected: 
                editorPanel.setModel(listPanel.getListModel().getElementAt(index));
            } else {
                editorPanel.setModel(null);
            }
        });

        setMaximumSize(Dimensions.MAXIMUM);
        setLeftComponent(editorPanel);
        setRightComponent(listPanel);
        setResizeWeight(1.0);
    }

    @Override
    public void detach() {
        editorPanel.detach();
    }

    @Override
    public void setEnabled(final boolean enabled) {
        super.setEnabled(enabled);
        listPanel.setEnabled(enabled);
        editorPanel.setEnabled(enabled);
        // Take focus away from inputs:
        if (enabled) {
            listPanel.requestFocus();
        }
    }
}
