package pixelj.views.homewindow;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListSelectionModel;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;

import pixelj.actions.HomeWindowActions;
import pixelj.services.AppState;

public final class HomeWindow extends HomeWindowBase {

    private final HomeWindowActions actions;

    public HomeWindow(final AppState appState) {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        recentList.setModel(appState.getRecentItemsListModel());
        final var selectionModel = new DefaultListSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        recentList.setSelectionModel(selectionModel);

        actions = new HomeWindowActions(this, appState, selectionModel);
        contextMenu.add(actions.removeRecentItemAction);
        contextMenu.add(actions.openContainingFolderAction);
        newProjectButton.setAction(actions.newProjectAction);
        loadSelectedButton.setAction(actions.loadSelectedAction);
        openProjectButton.setAction(actions.openProjectAction);
        toolBar.add(actions.showHelpAction);
        toolBar.add(actions.showOptionsDialogAction);
        toolBar.add(actions.quitAction);

        // Open in double click
        recentList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                if (e.getClickCount() == 2) {
                    actions.loadSelectedAction.actionPerformed(null);
                }
            }
        });

        // Disable load button if nothing is selected
        loadSelectedButton.setEnabled(false);
        selectionModel.addListSelectionListener(e ->
            loadSelectedButton.setEnabled(selectionModel.getMinSelectionIndex() >= 0)
        );

        final var closeListener = new CloseListener(appState, this);
        this.addWindowListener(closeListener);
    }

    @Override
    public void dispose() {
        super.dispose();
        actions.detach();
    }
}
