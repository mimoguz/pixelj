package pixelj.views.glyphs_screen;

import javax.swing.JComponent;
import javax.swing.ListSelectionModel;

import pixelj.models.Project;
import pixelj.views.controls.GlyphView;

public final class GlyphsScreen extends GlyphsScreenBase {
    private final ListSelectionModel selectionModel;

    public GlyphsScreen(final Project project, final JComponent root) {
        super(project, root, new ListPanel(project, root), new PainterPanel(project, root));
        selectionModel = listPanel.getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        // Connect the listModel to the painter
        selectionModel.addListSelectionListener(e -> {
            final var index = selectionModel.getMinSelectionIndex();
            if (index == selectionModel.getMaxSelectionIndex() && index >= 0) {
                painterPanel.setModel(listPanel.getListModel().getElementAt(index));
            } else {
                painterPanel.setModel(null);
            }
        });

        // Connect the painter to the listModel
        painterPanel.getPainter().addChangeListener((sender, event) -> {
            if (event == GlyphView.ViewChangeEvent.GLYPH_MODIFIED) {
                final var index = selectionModel.getMinSelectionIndex();
                if (index >= 0 && painterPanel.getModel() == listPanel.getListModel().getElementAt(index)) {
                    listPanel.getListModel().requestEvent(index);
                }
            }
        });
    }
}
