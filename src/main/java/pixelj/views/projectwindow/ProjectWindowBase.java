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
import javax.swing.Box;
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
import com.formdev.flatlaf.extras.FlatSVGIcon;

import pixelj.actions.ApplicationAction;
import pixelj.resources.Icon;
import pixelj.resources.Resources;
import pixelj.views.projectwindow.glyphspage.GlyphsPage;
import pixelj.views.projectwindow.kerningpairspage.KerningPairsPage;
import pixelj.views.projectwindow.previewpage.PreviewPage;
import pixelj.views.shared.Borders;
import pixelj.views.shared.Components;
import pixelj.views.shared.Dimensions;

/** Project window design. */
abstract class ProjectWindowBase extends JFrame {

    private GlyphsPage glyphsPage;
    private KerningPairsPage kerningPairsPage;
    private PreviewPage previewPage;

    private final FlatSVGIcon.ColorFilter inactiveFilter;
    private final FlatSVGIcon.ColorFilter activeFilter;

    protected final JTabbedPane content = new JTabbedPane();
    protected final JToggleButton menuButton = new JToggleButton();
    protected final JButton optionsButton = new JButton();
    protected final JButton helpButton = new JButton();
    protected final JPopupMenu mainMenu = new JPopupMenu();

    public ProjectWindowBase()
    {
        final var res = Resources.get();
        inactiveFilter = new FlatSVGIcon.ColorFilter(color -> res.colors.icon());
        activeFilter = new FlatSVGIcon.ColorFilter(color -> res.colors.activeTab());
    }

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
            FlatClientProperties.TABBED_PANE_TAB_TYPE_UNDERLINED
        );

        final var listIcon = res.getIcon(Icon.LIST);
        final var kerningIcon = res.getIcon(Icon.KERNING_WIDE);
        final var eyeIcon = res.getIcon(Icon.EYE);
        
        listIcon.setColorFilter(activeFilter);
        kerningIcon.setColorFilter(inactiveFilter);
        eyeIcon.setColorFilter(inactiveFilter);

        content.addTab(
            null,
            listIcon,
            glyphsPage,
            res.getString("glyphsScreenTabTooltip")
        );

        content.addTab(null,
            kerningIcon,
            kerningPairsPage,
            res.getString("kerningPairsScreenTabTooltip")
        );

        content.addTab(null,
            eyeIcon,
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
        leadingContainer.setBackground(content.getBackground());
        leadingContainer.setLayout(new BoxLayout(leadingContainer, BoxLayout.X_AXIS));

        final var res = Resources.get();

        // Show menu using a shortcut or the menu button
        final var menuKey = KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.ALT_DOWN_MASK);
        final var menuAction = new ApplicationAction("menuAction", this::handleMenu)
            .setTooltipWithAccelerator(res.getString("menuButtonActionTooltip"), menuKey)
            .setIcon(Icon.ELLIPSIS);
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

        menuButton.setAction(menuAction);
        styleTabBarButton(menuButton);
        leadingContainer.add(menuButton);

        // Balance the leading and the trailing containers.
        leadingContainer.add(Box.createRigidArea(Dimensions.TAB_BAR_BUTTON_SIZE));

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
        constraints.weightx = 1.0;
        constraints.gridx = 0;
        constraints.gridy = 0;
        final var emptyBox = new JPanel();
        emptyBox.setMaximumSize(Dimensions.MAXIMUM);
        emptyBox.setBackground(content.getBackground());
        trailingContainer.add(emptyBox, constraints);
        constraints.weightx = 0.0;
        constraints.gridx = GridBagConstraints.RELATIVE;
        trailingContainer.add(optionsButton, constraints);
        trailingContainer.add(helpButton, constraints);

        content.putClientProperty(FlatClientProperties.TABBED_PANE_TRAILING_COMPONENT, trailingContainer);
    }

    private void applyUITweaks() {
        ToolTipManager.sharedInstance().setInitialDelay(600);
        ToolTipManager.sharedInstance().setDismissDelay(3000);

        content.putClientProperty(
            FlatClientProperties.TABBED_PANE_MINIMUM_TAB_WIDTH,
            Dimensions.TAB_BAR_BUTTON_SIZE.width + Dimensions.LARGE_PADDING
        );
        content.putClientProperty(
            FlatClientProperties.TABBED_PANE_MAXIMUM_TAB_WIDTH,
            Dimensions.TAB_BAR_BUTTON_SIZE.width + Dimensions.LARGE_PADDING
        );
        content.putClientProperty(
            FlatClientProperties.TABBED_PANE_TAB_HEIGHT,
            Dimensions.TAB_BAR_BUTTON_SIZE.height
        );
        content.putClientProperty(FlatClientProperties.TABBED_PANE_TAB_ICON_PLACEMENT, SwingConstants.TOP);
        content.putClientProperty(FlatClientProperties.TABBED_PANE_TAB_AREA_ALIGNMENT, SwingConstants.CENTER);
        content.putClientProperty(FlatClientProperties.TABBED_PANE_SHOW_CONTENT_SEPARATOR, false);
        content.setTabPlacement(SwingConstants.TOP);
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
        // Related actions handle registering/un-registering shortcuts. Enabled page must be the last item,
        // otherwise its registered shortcut may be unregistered by the disabled pages.
        switch (index) {
            case 0 -> {
                kerningPairsPage.setEnabled(false);
                previewPage.setEnabled(false);
                glyphsPage.setEnabled(true);
            }
            case 1 -> {
                glyphsPage.setEnabled(false);
                previewPage.setEnabled(false);
                kerningPairsPage.setEnabled(true);
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
            if (content.getIconAt(tab) instanceof final FlatSVGIcon icn) {
                icn.setColorFilter(tab == index ? activeFilter : inactiveFilter);
            }
        }
    }

    private static void styleTabBarButton(final AbstractButton button) {
        button.setBorder(Borders.EMPTY);
        button.putClientProperty(FlatClientProperties.STYLE_CLASS, "tabBarButton");
        // button.putClientProperty(FlatClientProperties.BUTTON_TYPE_SQUARE, true);
        button.setFocusable(false);
        Components.setFixedSize(button, Dimensions.TAB_BAR_BUTTON_SIZE);
    }
}
