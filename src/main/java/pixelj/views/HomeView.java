package pixelj.views;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;

import com.formdev.flatlaf.FlatClientProperties;

import pixelj.actions.ApplicationAction;
import pixelj.actions.HomeActions;
import pixelj.resources.Resources;
import pixelj.views.shared.Borders;
import pixelj.views.shared.Components;
import pixelj.views.shared.Dimensions;

public class HomeView extends JFrame {

    public HomeView() {
        final var res = Resources.get();
        final var root = new JPanel(new BorderLayout());
        final var actions = new HomeActions(root);

        setupNorth(root, actions);
        setupCenter(root, actions);

        setContentPane(root);
        pack();
        setSize(new Dimension(720, 600));
        setTitle(res.getString("applicationName"));
        setIconImages(res.applicationIcons);
        getRootPane().putClientProperty(FlatClientProperties.TITLE_BAR_SHOW_ICON, false);
        setLocationRelativeTo(null);
    }

    private void setupCenter(final JPanel root, final HomeActions actions) {
        final var recentList = new JList<>(
                new Vector<>(
                        java.util.List.of(
                                new RecentItem("Project Panama", "c:\\path\\to\\panama.ext"),
                                new RecentItem("Project Valhalla", "c:\\path\\to\\valhalla.ext")
                        )
                )
        );
        recentList.setCellRenderer(new RecentItemCellRenderer());
        recentList.setBorder(Borders.EMPTY);
        final var scrollPanel = new JScrollPane(recentList);
        scrollPanel.setBorder(Borders.EMPTY);
        root.add(scrollPanel, BorderLayout.CENTER);

        final var contextMenu = new JPopupMenu();
        final var removeRecent = new JMenuItem(actions.removeRecentItemAction);
        final var openContainingFolder = new JMenuItem(actions.openContainingFolderAction);
        if (
                actions.removeRecentItemAction.getValue(Action.SMALL_ICON) == null
                    || actions.openContainingFolderAction.getValue(Action.SMALL_ICON) == null
        ) {
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
    }

    private void setupNorth(final JPanel root, final HomeActions actions) {
        final var newProjectButton = makeButton(actions.newProjectAction);
        final var openProjectButton = makeButton(actions.openSelectedAction);
        final var showOpenDialogButton = makeButton(actions.loadProjectAction);

        final var top = new JPanel(new BorderLayout());
        top.setBorder(Borders.LARGE_EMPTY);

        final var buttonBox = new JPanel();
        buttonBox.setLayout(new BoxLayout(buttonBox, BoxLayout.X_AXIS));
        buttonBox.add(newProjectButton);
        buttonBox.add(Box.createHorizontalStrut(Dimensions.MEDIUM_PADDING));
        buttonBox.add(openProjectButton);
        buttonBox.add(Box.createHorizontalStrut(Dimensions.MEDIUM_PADDING));
        buttonBox.add(showOpenDialogButton);
        buttonBox.add(Box.createHorizontalStrut(Dimensions.MEDIUM_PADDING));
        buttonBox.add(Box.createHorizontalStrut(Dimensions.MEDIUM_PADDING));
        top.add(buttonBox, BorderLayout.WEST);

        final var toolBar = new JToolBar();
        toolBar.add(actions.showOptionsDialogAction);
        toolBar.add(actions.quitAction);
        top.add(toolBar, BorderLayout.EAST);

        root.add(top, BorderLayout.NORTH);
    }

    private static JButton makeButton(final ApplicationAction action) {
        final var button = new JButton(action);
        button.setIcon(null);
        Components.setFixedSize(button, Dimensions.HOME_BUTTON_SIZE);
        return button;
    }

    private record RecentItem(String title, String path) {
    }

    private static class RecentItemCellRenderer implements ListCellRenderer<RecentItem> {
        private final JPanel component = new JPanel(new BorderLayout());
        private final JLabel path = new JLabel(" ");
        private final JLabel title = new JLabel(" ");

        RecentItemCellRenderer() {
            component.add(title, BorderLayout.NORTH);
            path.putClientProperty(FlatClientProperties.STYLE_CLASS, "small");
            component.add(path, BorderLayout.SOUTH);
            component.setBorder(
                    BorderFactory.createEmptyBorder(
                            Dimensions.MEDIUM_PADDING,
                            Dimensions.LARGE_PADDING,
                            Dimensions.MEDIUM_PADDING,
                            Dimensions.LARGE_PADDING
                    )
            );
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
