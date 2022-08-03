package pixelj.views.projectwindow;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.ToolTipManager;
import javax.swing.WindowConstants;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import com.formdev.flatlaf.FlatClientProperties;

import pixelj.actions.ApplicationAction;
import pixelj.graphics.FontIcon;
import pixelj.resources.Icons;
import pixelj.resources.Resources;
import pixelj.views.projectwindow.glyphspage.GlyphsPage;
import pixelj.views.projectwindow.kerningpairspage.KerningPairsPage;
import pixelj.views.projectwindow.previewpage.PreviewPage;
import pixelj.views.shared.Borders;
import pixelj.views.shared.Components;
import pixelj.views.shared.Dimensions;

/** Project window design. */
abstract class ProjectWindowBase extends JFrame {
    protected final JTabbedPane content = new JTabbedPane();
    protected final JToggleButton menuButton = new JToggleButton();
    protected final JButton optionsButton = new JButton();
    protected final JButton helpButton = new JButton();
    protected final JPopupMenu mainMenu = new JPopupMenu();

    private GlyphsPage glyphsPage;
    private KerningPairsPage kerningPairsPage;
    private PreviewPage previewPage;

    public void setup(
                final GlyphsPage glyphsPg,
                final KerningPairsPage kerningPairsPg,
                final PreviewPage previewPg
    ) {
        this.glyphsPage = glyphsPg;
        this.kerningPairsPage = kerningPairsPg;
        this.previewPage = previewPg;

        final var res = Resources.get();

        addTabs();
        addLeading();
        addTrailing();
        applyUITweaks();

        setContentPane(content);
        setIconImages(res.applicationIcons);
        pack();
        setSize(1200, 720);
        getRootPane().putClientProperty(FlatClientProperties.TITLE_BAR_SHOW_ICON, false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public GlyphsPage getGlyphsPage() {
        return glyphsPage;
    }

    public KerningPairsPage getKerningPairsPage() {
        return kerningPairsPage;
    }

    public PreviewPage getPreviewPage() {
        return previewPage;
    }

    private void addTabs() {
        final var res = Resources.get();

        content.putClientProperty(
                FlatClientProperties.TABBED_PANE_TAB_TYPE,
                FlatClientProperties.TABBED_PANE_TAB_TYPE_CARD
        );

        content.addTab(
                null,
                res.getIcon(Icons.LIST, res.colors.active(), null),
                glyphsPage,
                res.getString("glyphsScreenTabTooltip")
        );

        content.addTab(
                null,
                res.getIcon(Icons.KERNING_WIDE, res.colors.inactive(), null),
                kerningPairsPage,
                res.getString("kerningPairsScreenTabTooltip")
        );

        content.addTab(
                null,
                res.getIcon(Icons.EYE, res.colors.inactive(), null),
                previewPage,
                res.getString("previewScreenTabTooltip")
        );

        final Collection<ApplicationAction> tabActions = List.of(
                new ApplicationAction(
                        "projectViewTab0Action",
                        (event, action) -> content.setSelectedIndex(0)
                ).setAccelerator(KeyEvent.VK_1, ActionEvent.ALT_MASK),
                new ApplicationAction(
                        "projectViewTab1Action",
                        (event, action) -> content.setSelectedIndex(1)
                ).setAccelerator(KeyEvent.VK_2, ActionEvent.ALT_MASK),
                new ApplicationAction(
                        "projectViewTab2Action",
                        (event, action) -> content.setSelectedIndex(2)
                ).setAccelerator(KeyEvent.VK_3, ActionEvent.ALT_MASK)
        );
        for (var action : tabActions) {
            Components.registerShortcut(content, action);
        }

        content.addChangeListener(e -> setCurrentScreen(content.getSelectedIndex()));
    }

    private void addLeading() {
        final var leadingContainer = new JPanel();
        final var res = Resources.get();

        // Show menu using a shortcut or the menu button
        final var menuKey = KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.ALT_DOWN_MASK);
        final var menuAction = new ApplicationAction("menuAction", this::handleMenu)
                .setTooltipWithAccelerator(res.getString("menuButtonActionTooltip"), menuKey)
                .setIcon(Icons.BURGER, res.colors.accent(), res.colors.disabledIcon());
        menuButton.setAction(menuAction);
        Components.registerShortcut(getRootPane(), menuAction);

        // Deselect menu button when the menu is becoming invisible
        mainMenu.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuCanceled(final PopupMenuEvent e) {
                // Ignored
            }

            @Override
            public void popupMenuWillBecomeInvisible(final PopupMenuEvent e) {
                menuButton.setSelected(false);
            }

            @Override
            public void popupMenuWillBecomeVisible(final PopupMenuEvent e) {
                // Ignored
            }
        });

        styleTabBarButton(menuButton);
        leadingContainer.setLayout(new BoxLayout(leadingContainer, BoxLayout.Y_AXIS));
        leadingContainer.add(menuButton);
        content.putClientProperty(FlatClientProperties.TABBED_PANE_LEADING_COMPONENT, leadingContainer);
    }

    private void addTrailing() {
        styleTabBarButton(helpButton);
        styleTabBarButton(optionsButton);

        final var trailingContainer = new JPanel();
        trailingContainer.setLayout(new GridBagLayout());
        trailingContainer.setBorder(Borders.EMPTY);
        final var constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weighty = 1.0;
        constraints.gridx = 0;
        constraints.gridy = 0;
        final var emptyBox = new JPanel();
        emptyBox.setMaximumSize(Dimensions.MAXIMUM);
        emptyBox.setBackground(content.getBackground());
        trailingContainer.add(emptyBox, constraints);
        constraints.weighty = 0.0;
        constraints.gridy = GridBagConstraints.RELATIVE;
        trailingContainer.add(optionsButton, constraints);
        trailingContainer.add(helpButton, constraints);

        content.putClientProperty(FlatClientProperties.TABBED_PANE_TRAILING_COMPONENT, trailingContainer);
    }

    private void applyUITweaks() {
        ToolTipManager.sharedInstance().setInitialDelay(600);
        ToolTipManager.sharedInstance().setDismissDelay(3000);

        content.putClientProperty(
                FlatClientProperties.TABBED_PANE_MINIMUM_TAB_WIDTH,
                Dimensions.TAB_BAR_BUTTON_SIZE.width
        );
        content.putClientProperty(
                FlatClientProperties.TABBED_PANE_MAXIMUM_TAB_WIDTH,
                Dimensions.TAB_BAR_BUTTON_SIZE.width
        );
        content.putClientProperty(
                FlatClientProperties.TABBED_PANE_TAB_HEIGHT,
                Dimensions.TAB_BAR_BUTTON_SIZE.height
        );
        content.putClientProperty(FlatClientProperties.TABBED_PANE_TAB_ICON_PLACEMENT, SwingConstants.TOP);
        content.setTabPlacement(SwingConstants.LEFT);
        content.setFocusable(false);
    }

    private void handleMenu(final ActionEvent e, final Action action) {
        if (menuButton.isSelected() || e.getSource() != menuButton) {
            mainMenu.show(menuButton, menuButton.getWidth() + Dimensions.MEDIUM_PADDING, 0);
        } else {
            mainMenu.setVisible(false);
        }
    }

    private void setCurrentScreen(final int index) {
        switch (index) {
            case 0 -> {
                glyphsPage.setEnabled(true);
                kerningPairsPage.setEnabled(false);
                previewPage.setEnabled(false);
            }
            case 1 -> {
                glyphsPage.setEnabled(false);
                kerningPairsPage.setEnabled(true);
                previewPage.setEnabled(false);
            }
            default -> {
                glyphsPage.setEnabled(false);
                kerningPairsPage.setEnabled(false);
                previewPage.setEnabled(true);
            }
        }

        // Fix icon color
        final var res = Resources.get();
        for (var tab = 0; tab < 3; tab++) {
            if (content.getIconAt(tab) instanceof final FontIcon icn) {
                icn.setForeground(
                        tab == index ? res.colors.active() : res.colors.inactive()
                );
            }
        }
    }

    private static void styleTabBarButton(final AbstractButton button) {
        button.setBorder(Borders.EMPTY);
        button.putClientProperty(
                FlatClientProperties.BUTTON_TYPE,
                FlatClientProperties.BUTTON_TYPE_BORDERLESS
        );
        button.putClientProperty(FlatClientProperties.BUTTON_TYPE_SQUARE, true);
        button.setFocusable(false);
        Components.setFixedSize(button, Dimensions.TAB_BAR_BUTTON_SIZE);
    }
}
