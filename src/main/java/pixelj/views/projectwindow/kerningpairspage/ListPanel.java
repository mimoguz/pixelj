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
import pixelj.util.Detachable;

public final class ListPanel extends ListPanelBase implements Detachable {

    private final KerningPairListActions actions;
    private final FilteredList<KerningPair> listModel;
    private final ListSelectionModel selectionModel = new DefaultListSelectionModel();
    private final JFrame window;
    private Predicate<KerningPair> filterRight = model -> true;
    private Predicate<KerningPair> filterLeft = model -> true;

    public ListPanel(final Project project, final JFrame window) {
        this.window = window;

        listModel = new FilteredList<>(project.getKerningPairs());
        list.setSelectionModel(selectionModel);
        list.setModel(listModel);
        selectionModel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        actions = new KerningPairListActions(project, selectionModel, listModel, window);
        actions.removeAction.setEnabled(false);
        addButton.setAction(actions.addAction);
        removeButton.setAction(actions.removeAction);

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
    public void detach() {
        actions.detach();
    }

    @Override
    public void setEnabled(final boolean enabled) {
        super.setEnabled(enabled);
        actions.addAction.setEnabled(enabled);
        actions.removeAction.setEnabled(enabled && (selectionModel.getMinSelectionIndex() >= 0));
        if (enabled) {
            actions.registerShortcuts(window.getRootPane());
        } else {
            actions.unregisterShortcuts(window.getRootPane());
        }
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
