package io.github.mimoguz.pixelj.views;

import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import com.formdev.flatlaf.FlatClientProperties;

import io.github.mimoguz.pixelj.actions.MainActions;

public class MainMenu extends JPopupMenu {
    private static final long serialVersionUID = 1L;

    public MainMenu(MainActions actions) {
        add(actions.saveAction);
        add(actions.saveAsAction);
        add(actions.exportAction);
        add(separator());
        add(actions.showMetricsAction);
        add(actions.showSettingsAction);
        add(separator());
        add(actions.showHelpAction);
        add(separator());
        add(actions.closeProjectAction);
        add(actions.quitAction);
    }

    private static JSeparator separator() {
        final var separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.putClientProperty(FlatClientProperties.STYLE_CLASS, "divider");
        return separator;
    }
}