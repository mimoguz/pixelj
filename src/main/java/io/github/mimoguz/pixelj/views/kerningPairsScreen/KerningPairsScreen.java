package io.github.mimoguz.pixelj.views.kerningPairsScreen;

import io.github.mimoguz.pixelj.models.KerningPairListModel;
import io.github.mimoguz.pixelj.models.Metrics;
import io.github.mimoguz.pixelj.models.ProjectModel;
import io.github.mimoguz.pixelj.util.Detachable;
import io.github.mimoguz.pixelj.views.shared.Dimensions;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.*;

@ParametersAreNonnullByDefault
public class KerningPairsScreen extends JSplitPane implements Detachable {
    private final EditorPanel editorPanel;
    private final KerningPairListModel listModel;
    private final ListPanel listPanel;
    private final ListSelectionModel selectionModel;

    public KerningPairsScreen(final ProjectModel project, final JComponent root) {
        selectionModel = new DefaultListSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        listModel = project.getKerningPairs();
        listPanel = new ListPanel(listModel, selectionModel, project.getMetrics(), root);

        editorPanel = new EditorPanel();

        // Connect the listModel to the editor
        selectionModel.addListSelectionListener(e -> {
            if (selectionModel.getMinSelectionIndex() == selectionModel.getMaxSelectionIndex()
                    && selectionModel.getMinSelectionIndex() >= 0) {
                editorPanel.setModel(listModel.getElementAt(selectionModel.getMinSelectionIndex()));
            } else {
                editorPanel.setModel(null);
            }
        });

        updateMetrics(project.getMetrics());

        setMaximumSize(Dimensions.maximum);
        setLeftComponent(editorPanel);
        setRightComponent(listPanel);
        setResizeWeight(1.0);
    }

    @Override
    public void detach() {
        listPanel.detach();
        editorPanel.detach();
    }

    @Override
    public void setEnabled(final boolean value) {
        super.setEnabled(value);
        listPanel.setEnabled(value);
        editorPanel.setEnabled(value);
    }

    public void updateMetrics(final Metrics metrics) {
        editorPanel.setSpacing(metrics.spacing());
    }
}
