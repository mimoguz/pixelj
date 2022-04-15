package io.github.mimoguz.pixelj.views.charactersScreen;

import io.github.mimoguz.pixelj.controls.GlyphView;
import io.github.mimoguz.pixelj.models.CharacterListModel;
import io.github.mimoguz.pixelj.views.shared.Dimensions;

import org.jetbrains.annotations.Nullable;

import javax.swing.*;

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
            painterPanel.setModel(
                    (selectionModel.getMinSelectionIndex() == selectionModel.getMaxSelectionIndex()
                            && selectionModel.getMinSelectionIndex() >= 0
                            && listModel != null
                    )
                            ? listModel.getElementAt(selectionModel.getMinSelectionIndex())
                            : null
            );
        });

        // Connect the painter to the listModel
        painterPanel.getPainter().addListener((sender, event) -> {
            if (event == GlyphView.ViewChangeEvent.GLYPH_MODIFIED) {
                final var index = selectionModel.getMinSelectionIndex();
                if (index >= 0 && listModel != null && painterPanel.getModel() == listModel.getElementAt(index)) {
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

    public void setListModel(final @Nullable CharacterListModel listModel) {
        this.listModel = listModel;
    }
}
