package pixelj.views.projectwindow.kerningpairspage;

import java.util.function.Predicate;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JFrame;
import javax.swing.ListSelectionModel;

import pixelj.actions.KerningPairListActions;
import pixelj.models.Block;
import pixelj.models.FilteredList;
import pixelj.models.KerningPair;
import pixelj.models.Project;
import pixelj.models.SortedList;

public final class ListPanel extends ListPanelBase {

    private final KerningPairListActions actions;
    private final FilteredList<KerningPair> listModel;
    private final ListSelectionModel selectionModel = new DefaultListSelectionModel();
    private Predicate<KerningPair> filterRight = model -> true;
    private Predicate<KerningPair> filterLeft = model -> true;

    public ListPanel(final Project project, final JFrame window) {
        actions = new KerningPairListActions(project, selectionModel, window);
        actions.showRemoveDialogAction.setEnabled(false);
        actions.registerShortcuts(window.getRootPane());
        addButton.setAction(actions.showAddDialogAction);
        removeButton.setAction(actions.showRemoveDialogAction);

        listModel = new FilteredList<>(project.getKerningPairs());
        list.setSelectionModel(selectionModel);
        list.setModel(listModel);
        selectionModel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        leftFilterBox.addActionListener(e -> {
            if (leftFilterBox.getSelectedItem() instanceof final Block block) {
                setFilterLeft(block.starts(), block.ends());
            } else {
                setFilterLeft(0, Integer.MAX_VALUE);
            }
        });

        rightFilterBox.addActionListener(e -> {
            if (rightFilterBox.getSelectedItem() instanceof final Block block) {
                setFilterRight(block.starts(), block.ends());
            } else {
                setFilterRight(0, Integer.MAX_VALUE);
            }
        });
    }

    public SortedList<KerningPair> getListModel() {
        return listModel;
    }

    public ListSelectionModel getSelectionModel() {
        return selectionModel;
    }

    @Override
    public void setEnabled(final boolean enabled) {
        super.setEnabled(enabled);
        actions.showAddDialogAction.setEnabled(enabled);
        actions.showRemoveDialogAction.setEnabled(enabled && (selectionModel.getMinSelectionIndex() >= 0));
    }

    private void setFilterLeft(final int from, final int to) {
        filterLeft = model ->
                model.getLeft().getCodePoint() >= from && model.getLeft().getCodePoint() <= to;
        listModel.setFilter(model -> filterLeft.test(model) && filterRight.test(model));
    }

    private void setFilterRight(final int from, final int to) {
        filterLeft = model ->
                model.getRight().getCodePoint() >= from && model.getRight().getCodePoint() <= to;
        listModel.setFilter(model -> filterLeft.test(model) && filterRight.test(model));
    }
}
