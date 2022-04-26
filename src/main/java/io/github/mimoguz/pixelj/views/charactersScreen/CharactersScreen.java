package io.github.mimoguz.pixelj.views.charactersScreen;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JComponent;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;

import io.github.mimoguz.pixelj.controls.GlyphView;
import io.github.mimoguz.pixelj.models.CharacterListModel;
import io.github.mimoguz.pixelj.models.Metrics;
import io.github.mimoguz.pixelj.models.ProjectModel;
import io.github.mimoguz.pixelj.util.Detachable;
import io.github.mimoguz.pixelj.views.shared.Dimensions;

public class CharactersScreen extends JSplitPane implements Detachable {
    private static final long serialVersionUID = -7882245338313749432L;

    private final CharacterListModel listModel;
    private final ListPanel listPanel;
    private final PainterPanel painterPanel;
    private final ListSelectionModel selectionModel;

    public CharactersScreen(final ProjectModel project, final JComponent root) {
        selectionModel = new DefaultListSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        listModel = project.getCharacters();
        listPanel = new ListPanel(listModel, selectionModel, project.getMetrics(), root);

        painterPanel = new PainterPanel(root);
        painterPanel.setMetrics(project.getMetrics());

        // Connect the listModel to the painter
        selectionModel.addListSelectionListener(e -> {
            if (
                selectionModel.getMinSelectionIndex() == selectionModel.getMaxSelectionIndex()
                        && selectionModel.getMinSelectionIndex() >= 0
            ) {
                painterPanel.setModel(listModel.getElementAt(selectionModel.getMinSelectionIndex()));
            } else {
                painterPanel.setModel(null);
            }
        });

        // Connect the painter to the listModel
        painterPanel.getPainter().addChangeListener((sender, event) -> {
            if (event == GlyphView.ViewChangeEvent.GLYPH_MODIFIED) {
                final var index = selectionModel.getMinSelectionIndex();
                if (index >= 0 && painterPanel.getModel() == this.listModel.getElementAt(index)) {
                    listModel.requestEvent(index);
                }
            }
        });

        setMaximumSize(Dimensions.maximum);
        setLeftComponent(painterPanel);
        setRightComponent(listPanel);
        setResizeWeight(1.0);
    }

    @Override
    public void detach() {
        painterPanel.detach();
        listPanel.detach();
    }

    public ListPanel getListPanel() {
        return listPanel;
    }

    public PainterPanel getPainterPanel() {
        return painterPanel;
    }

    @Override
    public void setEnabled(final boolean value) {
        listPanel.setEnabled(value);
        painterPanel.setEnabled(value);
        super.setEnabled(value);
    }

    public void updateMetrics(final Metrics metrics) {
        painterPanel.setMetrics(metrics);
        listPanel.getActions().updateMetrics(metrics);
    }
}
