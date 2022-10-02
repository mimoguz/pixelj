package pixelj.views.homewindow;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.Box;

import net.miginfocom.swing.MigLayout;
import pixelj.resources.Resources;
import pixelj.services.RecentItem;
import pixelj.views.shared.Borders;
import pixelj.views.shared.Components;
import pixelj.views.shared.Dimensions;

/**
 * Home window design.
 */
abstract class HomeWindowBase extends JFrame {
    protected final JPanel content = new JPanel(new BorderLayout());
    protected final JButton newProjectButton = new JButton();
    protected final JButton loadSelectedButton = new JButton();
    protected final JButton openProjectButton = new JButton();
    protected final JToolBar toolBar = new JToolBar();
    protected final JList<RecentItem> recentList = new JList<>();
    protected final JPopupMenu contextMenu = new JPopupMenu();

    HomeWindowBase() {
        recentList.setCellRenderer(new RecentItemCellRenderer());

        /* ---------------------------------- North --------------------------------- */
        Components.setFixedSize(newProjectButton, Dimensions.HOME_BUTTON_SIZE);
        Components.setFixedSize(loadSelectedButton, Dimensions.HOME_BUTTON_SIZE);
        Components.setFixedSize(openProjectButton, Dimensions.HOME_BUTTON_SIZE);
        toolBar.putClientProperty(FlatClientProperties.STYLE_CLASS, "header");
        
        final var pad = Dimensions.MEDIUM_PADDING;
        final var northLayout = new MigLayout(
            "",
            String.format("[]%dlp[]%dlp[]%dlp[grow][]", pad, pad, pad),
            "[center]"
        );
        final var north = new JPanel(northLayout);
        north.putClientProperty(FlatClientProperties.STYLE_CLASS, "header");
        north.setBorder(Borders.LARGE_EMPTY);
        north.add(newProjectButton);
        north.add(loadSelectedButton);
        north.add(openProjectButton);
        north.add(Box.createRigidArea(Dimensions.SMALL_SQUARE));
        north.add(toolBar);
        content.add(north, BorderLayout.NORTH);

        /* --------------------------------- Center --------------------------------- */
        recentList.setBorder(Borders.EMPTY);
        final var center = new JScrollPane(recentList);
        center.setBorder(Borders.EMPTY);
        content.add(center, BorderLayout.CENTER);
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

        final var res = Resources.get();
        setTitle(res.getString("applicationName"));
        setIconImages(res.applicationIcons);
        setContentPane(content);
        pack();
        setSize(new Dimension(720, 600));
        setLocationRelativeTo(null);
    }

    private static class RecentItemCellRenderer implements ListCellRenderer<RecentItem> {

        private static final String BLANK = " ";
        private static final String SUBTITLE_STYLE = "small";

        private final JPanel component = new JPanel(new BorderLayout());
        private final JLabel path = new JLabel(BLANK);
        private final JLabel title = new JLabel(BLANK);

        RecentItemCellRenderer() {
            component.add(title, BorderLayout.NORTH);
            path.putClientProperty(FlatClientProperties.STYLE_CLASS, SUBTITLE_STYLE);
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
            title.setText(value.title());
            path.setText(value.path().toAbsolutePath().toString());
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
