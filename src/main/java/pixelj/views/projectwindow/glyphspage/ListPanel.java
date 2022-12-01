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
import pixelj.resources.Resources;
import pixelj.util.Detachable;
import pixelj.views.shared.Borders;
import pixelj.views.shared.Dimensions;

import java.awt.*;

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
                synchronized (listModel) {
                    gridView.removeAll();

                    final var count = listModel.getSize();
                    final var location = gridViewButton.getLocationOnScreen();

                    if (count == 0) {
                        gridView.setLayout(new BoxLayout(gridView, BoxLayout.X_AXIS));
                        final var msg = new JLabel(Resources.get().getString("emptyListMessage"));
                        msg.setBorder(Borders.LARGE_EMPTY);
                        gridView.add(msg);

                        // Fix popup size and location
                        SwingUtilities.invokeLater(() -> {
                            gridViewPopup.setPopupSize(msg.getWidth() + 2, msg.getHeight() + 2);
                            gridViewPopup.setLocation(
                                location.x,
                                location.y + gridViewButton.getHeight() + Dimensions.MEDIUM_PADDING
                            );
                        });
                    } else {
                        final var layout = new GridLayout((count + 1) / 10, Math.min(10, count), 1, 1);
                        gridView.setLayout(layout);
                        for (var i = 0; i < listModel.getSize(); i++) {
                            gridView.add(new GridCell(listModel.getElementAt(i)));
                        }

                        // Fix popup size and location
                        SwingUtilities.invokeLater(() -> {
                            final var maxHeight = ListPanel.this.getGraphicsConfiguration().getBounds().height / 3 * 2;
                            final var size = layout.minimumLayoutSize(gridView);
                            var popupWidth = size.width + 2;
                            var popupHeight = size.height + 2;
                            if (popupHeight > maxHeight) {
                                popupHeight = maxHeight;
                                popupWidth += 10;
                            }
                            gridViewPopup.setPopupSize(popupWidth, popupHeight);
                            gridViewPopup.setLocation(
                                location.x,
                                location.y + gridViewButton.getHeight() + Dimensions.MEDIUM_PADDING
                            );
                        });
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
        actions.copyFromAction.setEnabled(value && (selectionModel.getMinSelectionIndex() >= 0));
    }
}
