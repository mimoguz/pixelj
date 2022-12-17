package pixelj.views.projectwindow.glyphspage;

import pixelj.actions.ApplicationAction;
import pixelj.actions.GlyphListActions;
import pixelj.graphics.BinaryImage;
import pixelj.models.Block;
import pixelj.models.FilteredList;
import pixelj.models.Glyph;
import pixelj.models.Project;
import pixelj.models.SortedList;
import pixelj.resources.Icon;
import pixelj.resources.Resources;
import pixelj.util.Detachable;
import pixelj.views.shared.Dimensions;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

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
        gridView.setModel(filteredListModel);

        final var settings = project.getDocumentSettings();
        gridView.setSample(new Glyph(
            0,
            settings.defaultWidth(),
            BinaryImage.of(settings.canvasWidth(), settings.canvasHeight())
        ));

        actions = new GlyphListActions(project, selectionModel, listModel, window);
        actions.removeGlyphsAction.setEnabled(false);
        addButton.setPrimaryAction(actions.addGlyphsAction);
        addButton.setSecondaryAction(actions.addCodePointAction);
        copyFromButton.setAction(actions.copyFromAction);
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

        gridViewButton.setAction(
            new ApplicationAction(
                "gridViewAction",
                (event, action) -> gridViewPopup.show(
                    gridViewButton,
                    0,
                    gridViewButton.getHeight() + Dimensions.MEDIUM_PADDING
                )
            )
                .setIcon(Icon.GRID)
                .setTooltip(Resources.get().getString("gridViewActionTooltip"))
        );

        gridViewPopup.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(final PopupMenuEvent e) {

                final var location = gridViewButton.getLocationOnScreen();

                // Fix popup size and location
                final var maxHeight = ListPanel.this.getGraphicsConfiguration().getBounds().height / 3 * 2;
                final var gridSize = gridView.getMinimumSize();
                final var popupInsets = gridViewPopup.getInsets();
                final var scrollInsets = gridScroll.getInsets();

                var w = gridSize.width
                    + gridScroll.getVerticalScrollBar().getWidth()
                    + popupInsets.left + popupInsets.right
                    + scrollInsets.left + scrollInsets.right;

                final var h = Math.min(maxHeight, gridSize.height)
                    + popupInsets.top + popupInsets.bottom
                    + scrollInsets.top + scrollInsets.bottom;

                gridView.setPreferredSize(gridSize);
                gridViewPopup.setPopupSize(w, h);
                gridViewPopup.setLocation(
                    location.x,
                    location.y + gridViewButton.getHeight() + Dimensions.MEDIUM_PADDING
                );
            }

            @Override
            public void popupMenuWillBecomeInvisible(final PopupMenuEvent e) {
                // Ignore
            }

            @Override
            public void popupMenuCanceled(final PopupMenuEvent e) {
                // Ignore
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
        actions.copyFromAction.setEnabled(value && (selectionModel.getMinSelectionIndex() >= 0));
    }
}
