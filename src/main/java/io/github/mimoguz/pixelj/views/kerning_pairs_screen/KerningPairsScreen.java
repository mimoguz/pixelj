package io.github.mimoguz.pixelj.views.kerning_pairs_screen;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JComponent;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;

import io.github.mimoguz.pixelj.models.Metrics;
import io.github.mimoguz.pixelj.models.ProjectModel;
import io.github.mimoguz.pixelj.util.Detachable;
import io.github.mimoguz.pixelj.views.shared.Dimensions;

public class KerningPairsScreen extends JSplitPane implements Detachable {
    private static final long serialVersionUID = -8264436768125439149L;

    private final EditorPanel editorPanel;
    private final ListPanel listPanel;
    private final transient ListSelectionModel selectionModel;

    public KerningPairsScreen(final ProjectModel project, final JComponent root) {
        selectionModel = new DefaultListSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        listPanel = new ListPanel(project, selectionModel, root);
        editorPanel = new EditorPanel();

        // Connect the listModel to the editor
        selectionModel.addListSelectionListener(e -> {
            if (
                selectionModel.getMinSelectionIndex() == selectionModel.getMaxSelectionIndex()
                        && selectionModel.getMinSelectionIndex() >= 0
            ) {
                editorPanel.setModel(
                        listPanel.getListModel().getElementAt(selectionModel.getMinSelectionIndex())
                );
            } else {
                editorPanel.setModel(null);
            }
        });

        updateMetrics(project.getMetrics());

        setMaximumSize(Dimensions.MAXIMUM);
        setLeftComponent(editorPanel);
        setRightComponent(listPanel);
        setResizeWeight(1.0);
    }

    @Override
    public void detach() {
        listPanel.detach();
        editorPanel.detach();
    }

    public EditorPanel getEditorPanel() {
        return editorPanel;
    }

    public ListPanel getListPanel() {
        return listPanel;
    }

    @Override
    public void setEnabled(final boolean value) {
        super.setEnabled(value);
        listPanel.setEnabled(value);
        editorPanel.setEnabled(value);
        if (value) {
            listPanel.getList().requestFocus();
        }
    }

    public void updateMetrics(final Metrics metrics) {
        editorPanel.setSpacing(metrics.spacing());
        editorPanel.getPreview().setMaxY(metrics.descender() + metrics.ascender());
    }
}
