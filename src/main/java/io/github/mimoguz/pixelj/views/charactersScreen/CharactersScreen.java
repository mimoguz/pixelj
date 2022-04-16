package io.github.mimoguz.pixelj.views.charactersScreen;

import io.github.mimoguz.pixelj.controls.GlyphView;
import io.github.mimoguz.pixelj.models.CharacterListModel;
import io.github.mimoguz.pixelj.models.Metrics;
import io.github.mimoguz.pixelj.models.ProjectModel;
import io.github.mimoguz.pixelj.views.shared.Dimensions;

import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class CharactersScreen extends JSplitPane {
    private final CharacterListPanel listPanel;
    private final PainterPanel painterPanel;
    private final ListSelectionModel selectionModel;
    private @Nullable CharacterListModel listModel;

    public CharactersScreen(JComponent root) {
        selectionModel = new DefaultListSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        listPanel = new CharacterListPanel(listModel, selectionModel, root);
        painterPanel = new PainterPanel(root);

        // Connect the listModel to the painter
        selectionModel.addListSelectionListener(e -> {
            if (selectionModel.getMinSelectionIndex() == selectionModel.getMaxSelectionIndex()
                    && selectionModel.getMinSelectionIndex() >= 0
                    && this.listModel != null) {
                painterPanel.setModel(listModel.getElementAt(selectionModel.getMinSelectionIndex()));
            } else {
                painterPanel.setModel(null);
            }
        });

        // Connect the painter to the listModel
        painterPanel.getPainter().addListener((sender, event) -> {
            if (event == GlyphView.ViewChangeEvent.GLYPH_MODIFIED) {
                final var index = selectionModel.getMinSelectionIndex();
                if (index >= 0 && listModel != null
                        && painterPanel.getModel() == this.listModel.getElementAt(index)) {
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
    public void setEnabled(final boolean value) {
        listPanel.setEnabled(value);
        painterPanel.setEnabled(value);
        super.setEnabled(value);
    }

    public void setProject(final @Nullable ProjectModel project) {
        painterPanel.setModel(null);
        if (project != null) {
            listModel = project.getCharacters();
            listPanel.setListModel(project.getCharacters());
            updateMetrics(project.getMetrics());
        } else {
            listModel = null;
            listPanel.setListModel(null);
            updateMetrics(null);
        }
    }

    public void updateMetrics(@Nullable Metrics metrics) {
        painterPanel.setMetrics(metrics);
        if (metrics != null) {
            listPanel.getActions().setCanvasSize(new Dimension(metrics.canvasWidth(), metrics.canvasHeight()));
            listPanel.getActions().setDefaultCharacterWidth(metrics.defaultCharacterWidth());
        } else {
            listPanel.getActions().setCanvasSize(null);
            listPanel.getActions().setDefaultCharacterWidth(0);
        }
    }
}
