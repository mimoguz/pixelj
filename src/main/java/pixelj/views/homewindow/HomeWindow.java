package pixelj.views.homewindow;

import javax.swing.DefaultListModel;

import pixelj.actions.HomeActions;

public class HomeWindow extends HomeWindowBase {

    public HomeWindow() {
        final var actions = new HomeActions(content);
        contextMenu.add(actions.removeRecentItemAction);
        contextMenu.add(actions.openContainingFolderAction);
        newProjectButton.setAction(actions.newProjectAction);
        loadSelectedButton.setAction(actions.loadSelectedAction);
        openProjectButton.setAction(actions.openProjectAction);
        toolBar.add(actions.showOptionsDialogAction);
        toolBar.add(actions.quitAction);

        final var recentItems = new DefaultListModel<RecentItem>();
        recentItems.addElement(
                new RecentItem("Project Panama", "c:\\path\\to\\panama.ext")
        );
        recentItems.addElement(
            new RecentItem("Project Valhalla", "c:\\path\\to\\valhalla.ext")
        );
        recentList.setModel(recentItems);
    }
}
