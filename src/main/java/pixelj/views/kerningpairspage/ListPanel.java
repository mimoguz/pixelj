package pixelj.views.kerningpairspage;

import java.util.function.Predicate;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JComponent;
import javax.swing.ListSelectionModel;

import pixelj.actions.Actions;
import pixelj.actions.KerningPairListActions;
import pixelj.models.BlockRecord;
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

    public ListPanel(final Project project, final JComponent root) {
        actions = new KerningPairListActions(project, selectionModel, root);
        actions.showRemoveDialogAction.setEnabled(false);
        Actions.registerShortcuts(actions.all, root);
        addButton.setAction(actions.showAddDialogAction);
        removeButton.setAction(actions.showRemoveDialogAction);

        listModel = new FilteredList<>(project.getKerningPairs());
        list.setSelectionModel(selectionModel);
        list.setModel(listModel);
        selectionModel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        leftFilterBox.addActionListener(e -> {
            if (leftFilterBox.getSelectedItem() instanceof final BlockRecord block) {
                setFilterLeft(block.starts(), block.ends());
            } else {
                setFilterLeft(0, Integer.MAX_VALUE);
            }
        });

        rightFilterBox.addActionListener(e -> {
            if (rightFilterBox.getSelectedItem() instanceof final BlockRecord block) {
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
