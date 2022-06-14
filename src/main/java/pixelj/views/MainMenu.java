package pixelj.views;

import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import pixelj.actions.MainActions;

public class MainMenu extends JPopupMenu {
    public MainMenu(MainActions actions) {
        add(actions.saveAction);
        add(actions.saveAsAction);
        add(actions.exportAction);
        add(new JSeparator(SwingConstants.HORIZONTAL));
        add(actions.showMetricsAction);
        add(actions.showSettingsAction);
        add(new JSeparator(SwingConstants.HORIZONTAL));
        add(actions.showHelpAction);
        add(new JSeparator(SwingConstants.HORIZONTAL));
        add(actions.returnToProjectManagerAction);
        add(actions.quitAction);
    }
}