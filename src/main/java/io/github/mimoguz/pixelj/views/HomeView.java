package io.github.mimoguz.pixelj.views;

import io.github.mimoguz.pixelj.actions.ApplicationAction;
import io.github.mimoguz.pixelj.actions.HomeActions;
import io.github.mimoguz.pixelj.resources.Resources;
import io.github.mimoguz.pixelj.views.shared.Borders;
import io.github.mimoguz.pixelj.views.shared.Components;
import io.github.mimoguz.pixelj.views.shared.Dimensions;

import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

public class HomeView extends JFrame {
    public HomeView() {
        super();
        final var res = Resources.get();
        final var root = new JPanel(new BorderLayout());
        final var actions = new HomeActions(root);

        final var newProjectButton = makeButton(actions.showNewProjectDialogAction);
        final var openProjectButton = makeButton(actions.openSelectedProjectAction);
        final var showOpenDialogButton = makeButton(actions.showOpenDialogAction);

        final var top = new JPanel(new BorderLayout());
        top.setBorder(Borders.MEDIUM_EMPTY);

        final var buttonBox = new JPanel();
        buttonBox.setLayout(new BoxLayout(buttonBox, BoxLayout.X_AXIS));
        buttonBox.add(newProjectButton);
        buttonBox.add(Box.createHorizontalStrut(Dimensions.SMALL_PADDING));
        buttonBox.add(openProjectButton);
        buttonBox.add(Box.createHorizontalStrut(Dimensions.SMALL_PADDING));
        buttonBox.add(showOpenDialogButton);
        buttonBox.add(Box.createHorizontalStrut(Dimensions.SMALL_PADDING));
        top.add(buttonBox, BorderLayout.WEST);

        final var toolBar = new JToolBar();
        toolBar.add(actions.showOptionsDialogAction);
        toolBar.add(actions.quitAction);
        top.add(toolBar, BorderLayout.EAST);

        root.add(top, BorderLayout.NORTH);

        final var recentList = new JList<>(new Vector<>(java.util.List.of(
                new RecentItem("Project Panama", "c:\\path\\to\\panama.ext"),
                new RecentItem("Project Valhalla", "c:\\path\\to\\valhalla.ext")
        )));
        recentList.setCellRenderer(new RecentItemCellRenderer());
        recentList.setBorder(Borders.EMPTY);
        final var scrollPanel = new JScrollPane(recentList);
        scrollPanel.setBorder(Borders.EMPTY);
        root.add(scrollPanel, BorderLayout.CENTER);

        final var contextMenu = new JPopupMenu();
        final var removeRecent = new JMenuItem(actions.removeRecentItemAction);
        final var openContainingFolder = new JMenuItem(actions.openContainingFolderAction);
        if (actions.removeRecentItemAction.getValue(Action.SMALL_ICON) == null
                || actions.openContainingFolderAction.getValue(Action.SMALL_ICON) == null) {
            removeRecent.setIconTextGap(0);
            openContainingFolder.setIconTextGap(0);
        }
        contextMenu.add(removeRecent);
        contextMenu.add(openContainingFolder);

        // https://stackoverflow.com/a/28954204
        recentList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                if (!SwingUtilities.isRightMouseButton(e)) {
                    return;
                }
                final var index = recentList.locationToIndex(e.getPoint());
                recentList.setSelectedIndex(index);
                if (index >= 0) {
                    contextMenu.show(recentList, e.getX(), e.getY());
                }
            }
        });

        setContentPane(root);
        pack();
        recentList.requestFocusInWindow();
        setSize(new Dimension(720, 600));
        setTitle(res.getString("applicationName"));
        getRootPane().putClientProperty(FlatClientProperties.TITLE_BAR_SHOW_ICON, false);
        setLocationRelativeTo(null);
    }

    private static JButton makeButton(ApplicationAction action) {
        final var button = new JButton();
//        button.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_BORDERLESS);
        Components.setFixedSize(button, Dimensions.HOME_BUTTON_SIZE);
        button.setAction(action);
        button.setIcon(null);
        button.setText(null);
        final var content = new JPanel(new BorderLayout());
        content.setOpaque(false);
        if (action.getValue(Action.SMALL_ICON) instanceof Icon icon) {
            final var iconLabel = new JLabel();
            iconLabel.setIcon(icon);
            content.add(iconLabel, BorderLayout.WEST);
        }
        if (action.getValue(Action.NAME) instanceof String name) {
            final var label = new JLabel(name);
            label.setBorder(BorderFactory.createEmptyBorder(
                    0,
                    Dimensions.MEDIUM_PADDING,
                    0,
                    Dimensions.MEDIUM_PADDING
            ));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            content.add(label, BorderLayout.CENTER);
        }
        button.add(content);
        return button;
    }

    private record RecentItem(String title, String path) {
    }

    private static class RecentItemCellRenderer implements ListCellRenderer<RecentItem> {
        private final JPanel component = new JPanel(new BorderLayout());
        private final JLabel path = new JLabel(" ");
        private final JLabel title = new JLabel(" ");

        public RecentItemCellRenderer() {
            component.add(title, BorderLayout.NORTH);
            path.putClientProperty(FlatClientProperties.STYLE_CLASS, "small");
            component.add(path, BorderLayout.SOUTH);
            component.setBorder(BorderFactory.createEmptyBorder(
                    Dimensions.MEDIUM_PADDING,
                    Dimensions.LARGE_PADDING,
                    Dimensions.MEDIUM_PADDING,
                    Dimensions.LARGE_PADDING
            ));
        }

        @Override
        public Component getListCellRendererComponent(
                final JList<? extends RecentItem> list,
                final RecentItem value,
                final int index,
                final boolean isSelected,
                final boolean cellHasFocus
        ) {
            if (value == null) {
                return null;
            }
            title.setText(value.title);
            path.setText(value.path);
            if (isSelected) {
                title.setForeground(list.getSelectionForeground());
                path.setForeground(list.getSelectionForeground());
                component.setBackground(list.getSelectionBackground());
            } else {
                title.setForeground(list.getForeground());
                path.setForeground(list.getForeground());
                component.setBackground(list.getBackground());
            }
            return component;
        }
    }
}
