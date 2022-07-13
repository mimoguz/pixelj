package pixelj.views.projectwindow.glyphspage;

import javax.swing.JFrame;
import javax.swing.ListSelectionModel;

import pixelj.models.Project;
import pixelj.views.controls.GlyphView;

public final class GlyphsPage extends GlyphsScreenBase {
    private final ListSelectionModel selectionModel;

    public GlyphsPage(final Project project, final JFrame window) {
        super(new ListPanel(project, window), new PainterPanel(project, window));
        selectionModel = listPanel.getSelectionModel();

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
