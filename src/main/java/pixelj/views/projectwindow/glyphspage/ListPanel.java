package pixelj.views.projectwindow.glyphspage;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JFrame;
import javax.swing.ListSelectionModel;

import pixelj.actions.GlyphListActions;
import pixelj.models.Block;
import pixelj.models.FilteredList;
import pixelj.models.Glyph;
import pixelj.models.Project;
import pixelj.models.SortedList;
import pixelj.resources.Icons;
import pixelj.resources.Resources;

public final class ListPanel extends ListPanelBase {

    private final GlyphListActions actions;
    private final ListSelectionModel selectionModel = new DefaultListSelectionModel();
    private final SortedList<Glyph> listModel;

    public ListPanel(final Project project, final JFrame window) {
        actions = new GlyphListActions(project, selectionModel, window);
        actions.removeGlyphsAction.setEnabled(false);
        actions.registerShortcuts(window.getRootPane());
        addButton.setPrimaryAction(actions.addGlyphsAction);
        addButton.setSecondaryAction(actions.addCodePointAction);
        removeButton.setAction(actions.removeGlyphsAction);

        final var filteredListModel = new FilteredList<>(project.getGlyphs());
        listModel = filteredListModel;
        list.setModel(filteredListModel);
        list.setSelectionModel(selectionModel);
        selectionModel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        filterBox.addActionListener(event -> {
            if (filterBox.getSelectedItem() instanceof final Block block) {
                filteredListModel.setFilter(
                        chr -> chr.getCodePoint() >= block.starts() && chr.getCodePoint() <= block.ends()
                );
            } else {
                filteredListModel.setFilter(chr -> true);
            }
        });
    }

    public ListSelectionModel getSelectionModel() {
        return selectionModel;
    }

    public SortedList<Glyph> getListModel() {
        return listModel;
    }

    @Override
    public void setEnabled(final boolean value) {
        super.setEnabled(value);
        actions.addGlyphsAction.setEnabled(value);
        actions.removeGlyphsAction.setEnabled(value && (selectionModel.getMinSelectionIndex() >= 0));
    }
}
