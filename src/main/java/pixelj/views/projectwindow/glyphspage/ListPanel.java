package pixelj.views.projectwindow.glyphspage;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import pixelj.actions.ApplicationAction;
import pixelj.actions.GlyphListActions;
import pixelj.models.Block;
import pixelj.models.FilteredList;
import pixelj.models.Glyph;
import pixelj.models.Project;
import pixelj.models.SortedList;
import pixelj.resources.Icon;
import pixelj.util.Detachable;
import pixelj.views.shared.Dimensions;

public final class ListPanel extends ListPanelBase implements Detachable {

    private final GlyphListActions actions;
    private final ListSelectionModel selectionModel = new DefaultListSelectionModel();
    private final FilteredList<Glyph> listModel;
    private final JFrame window;

    public ListPanel(final Project project, final JFrame window) {
        this.window = window;

        final var filteredListModel = new FilteredList<>(project.getGlyphs());
        listModel = filteredListModel;
        list.setModel(filteredListModel);
        list.setSelectionModel(selectionModel);
        selectionModel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        actions = new GlyphListActions(project, selectionModel, listModel, window);
        actions.removeGlyphsAction.setEnabled(false);
        addButton.setPrimaryAction(actions.addGlyphsAction);
        addButton.setSecondaryAction(actions.addCodePointAction);
        removeButton.setAction(actions.removeGlyphsAction);

        filterBox.addActionListener(event -> {
            if (filterBox.getSelectedItem() instanceof final Block block) {
                filteredListModel.setFilter(
                    chr -> chr.getCodePoint() >= block.starts() && chr.getCodePoint() <= block.ends()
                );
            } else {
                filteredListModel.setFilter(chr -> true);
            }
        });

        gridViewButton.setAction(new ApplicationAction("gridViewAction", (event, action) -> {
            gridViewPopup.show(gridViewButton, 0, gridViewButton.getHeight() + Dimensions.SMALL_PADDING);
        }).setIcon(Icon.GRID));

        gridViewPopup.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(final PopupMenuEvent e) {
                synchronized (listModel) {
                    for (var i = 0; i < listModel.getSize(); i++) {
                        gridView.add(new GridCell(listModel.getElementAt(i)));
                    }
                }
            }

            @Override
            public void popupMenuWillBecomeInvisible(final PopupMenuEvent e) {
                gridView.removeAll();
            }

            @Override
            public void popupMenuCanceled(final PopupMenuEvent e) {
                gridView.removeAll();
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
    public void detach() {
        actions.detach();
    }

    @Override
    public void setEnabled(final boolean value) {
        super.setEnabled(value);
        if (value) {
            actions.registerShortcuts(window.getRootPane());
        } else {
            actions.unregisterShortcuts(window.getRootPane());
        }
        actions.addGlyphsAction.setEnabled(value);
        actions.removeGlyphsAction.setEnabled(value && (selectionModel.getMinSelectionIndex() >= 0));
    }
}
