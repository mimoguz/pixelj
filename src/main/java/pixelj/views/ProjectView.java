package pixelj.views;

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
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.ToolTipManager;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import com.formdev.flatlaf.FlatClientProperties;

import pixelj.actions.Actions;
import pixelj.actions.ApplicationAction;
import pixelj.actions.MainActions;
import pixelj.graphics.FontIcon;
import pixelj.models.Project;
import pixelj.resources.Icons;
import pixelj.resources.Resources;
import pixelj.util.ChangeableValue;
import pixelj.views.glyphsscreen.GlyphsScreen;
import pixelj.views.kerningpairsscreen.KerningPairsScreen;
import pixelj.views.previewscreen.PreviewScreen;
import pixelj.views.shared.Borders;
import pixelj.views.shared.Components;
import pixelj.views.shared.Dimensions;

public class ProjectView extends JFrame {
    private final GlyphsScreen charactersScreen;
    private final KerningPairsScreen kerningPairsScreen;
    private final PreviewScreen previewScreen;
    private final JTabbedPane root;

    public ProjectView(final Project project) {
        root = new JTabbedPane();

        final var res = Resources.get();

        charactersScreen = new GlyphsScreen(project, root);
        kerningPairsScreen = new KerningPairsScreen(project, root);
        previewScreen = new PreviewScreen(project, root);
        // Invisible screens should be disabled to prevent shortcut collisions.
        kerningPairsScreen.setEnabled(false);
        previewScreen.setEnabled(false);

        final var mainActions = new MainActions(project, root);
        Actions.registerShortcuts(mainActions.all, root);

        project.titleProperty.addChangeListener((sender, value) -> setFrameTitle(value, project.isDirty()));
        project.dirtyProperty.addChangeListener((sender, value) -> setFrameTitle(project.getTitle(), value));

        setupTabs();
        setupLeading(mainActions, root);
        setupTrailing(mainActions);
        applyUITweaks();

        setContentPane(root);
        setFrameTitle(project.getTitle(), false);
        setIconImages(res.applicationIcons);
        pack();
        setSize(1200, 720);
        getRootPane().putClientProperty(FlatClientProperties.TITLE_BAR_SHOW_ICON, false);
        setLocationRelativeTo(null);
    }

    private void applyUITweaks() {
        ToolTipManager.sharedInstance().setInitialDelay(100);
        ToolTipManager.sharedInstance().setDismissDelay(3000);

        root.putClientProperty(
                FlatClientProperties.TABBED_PANE_MINIMUM_TAB_WIDTH,
                Dimensions.TAB_BAR_BUTTON_SIZE.width
        );
        root.putClientProperty(
                FlatClientProperties.TABBED_PANE_MAXIMUM_TAB_WIDTH,
                Dimensions.TAB_BAR_BUTTON_SIZE.width
        );
        root.putClientProperty(
                FlatClientProperties.TABBED_PANE_TAB_HEIGHT,
                Dimensions.TAB_BAR_BUTTON_SIZE.height
        );
        root.putClientProperty(FlatClientProperties.TABBED_PANE_TAB_ICON_PLACEMENT, SwingConstants.TOP);
        root.setTabPlacement(SwingConstants.LEFT);
        root.setFocusable(false);
    }

    private void setupLeading(final MainActions mainActions, final JComponent component) {
        final var res = Resources.get();
        final var mainMenu = makeMainMenu(mainActions);
        final var leadingContainer = new JPanel();
        final var menuButton = new JToggleButton();

        final var menuButtonAction = new ApplicationAction("menuButtonAction", (event, action) -> {
            if (menuButton.isSelected()) {
                mainMenu.show(menuButton, menuButton.getWidth() + Dimensions.MEDIUM_PADDING, 0);
            } else {
                mainMenu.setVisible(false);
            }
        })
                .setTooltipWithAccelerator(
                        res.getString("menuButtonActionTooltip"),
                        KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.ALT_DOWN_MASK)
                )
                .setIcon(Icons.BURGER, res.colors.accent(), res.colors.disabledIcon());

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

        Actions.registerShortcuts(java.util.List.of(menuButtonAction), component);
        setupTabBarButton(menuButton, menuButtonAction);
        leadingContainer.setLayout(new BoxLayout(leadingContainer, BoxLayout.Y_AXIS));
        leadingContainer.add(menuButton);
        component.putClientProperty(FlatClientProperties.TABBED_PANE_LEADING_COMPONENT, leadingContainer);
    }

    private void setupTabs() {
        final var res = Resources.get();

        root.putClientProperty(
                FlatClientProperties.TABBED_PANE_TAB_TYPE,
                FlatClientProperties.TABBED_PANE_TAB_TYPE_CARD
        );

        root.addTab(
                null,
                res.getIcon(Icons.LIST, res.colors.active(), null),
                charactersScreen,
                res.getString("glyphsScreenTabTooltip")
        );

        root.addTab(
                null,
                res.getIcon(Icons.KERNING_WIDE, res.colors.inactive(), null),
                kerningPairsScreen,
                res.getString("kerningPairsScreenTabTooltip")
        );

        root.addTab(
                null,
                res.getIcon(Icons.EYE, res.colors.inactive(), null),
                previewScreen,
                res.getString("previewScreenTabTooltip")
        );

        final Collection<ApplicationAction> tabActions = List.of(
                new ApplicationAction("projectViewTab0Action", (event, action) -> root.setSelectedIndex(0))
                        .setAccelerator(KeyEvent.VK_1, ActionEvent.ALT_MASK),
                new ApplicationAction("projectViewTab1Action", (event, action) -> root.setSelectedIndex(1))
                        .setAccelerator(KeyEvent.VK_2, ActionEvent.ALT_MASK),
                new ApplicationAction("projectViewTab2Action", (event, action) -> root.setSelectedIndex(2))
                        .setAccelerator(KeyEvent.VK_3, ActionEvent.ALT_MASK)
        );
        Actions.registerShortcuts(tabActions, root);

        root.addChangeListener(e -> setCurrentScreen(root.getSelectedIndex()));
    }

    private void setupTrailing(final MainActions mainActions) {
        final var trailingContainer = new JPanel();
        trailingContainer.setLayout(new GridBagLayout());
        trailingContainer.setBorder(Borders.EMPTY);

        final var constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;

        final var emptyBox = new JPanel();
        emptyBox.setBackground(root.getBackground());
        emptyBox.setMaximumSize(Dimensions.MAXIMUM);
        constraints.weighty = 1.0;
        constraints.gridy = 0;
        trailingContainer.add(emptyBox, constraints);

        final var settingsButton = setupTabBarButton(new JButton(), mainActions.showOptionsAction);
        constraints.weighty = 0.0;
        constraints.gridy += 1;
        trailingContainer.add(settingsButton, constraints);

        final var helpButton = setupTabBarButton(new JButton(), mainActions.showHelpAction);
        constraints.gridy += 1;
        trailingContainer.add(helpButton, constraints);
        root.putClientProperty(FlatClientProperties.TABBED_PANE_TRAILING_COMPONENT, trailingContainer);
    }

    private void setFrameTitle(final String titleText, final boolean isDirty) {
        if (isDirty) {
            setTitle(Resources.get().formatString("projectViewTitleUnsaved", titleText));
        } else {
            setTitle(Resources.get().formatString("projectViewTitle", titleText));
        }
    }

    private void setCurrentScreen(final int index) {
        switch (index) {
            case 0 -> {
                charactersScreen.setEnabled(true);
                kerningPairsScreen.setEnabled(false);
                previewScreen.setEnabled(false);
            }
            case 1 -> {
                charactersScreen.setEnabled(false);
                kerningPairsScreen.setEnabled(true);
                previewScreen.setEnabled(false);
            }
            default -> {
                charactersScreen.setEnabled(false);
                kerningPairsScreen.setEnabled(false);
                previewScreen.setEnabled(true);
            }
        }

        // Fix icon color
        final var res = Resources.get();
        for (var tab = 0; tab < 3; tab++) {
            if (root.getIconAt(tab) instanceof final FontIcon icn) {
                icn.setForeground(
                        tab == index ? res.colors.active() : res.colors.inactive()
                );
            }
        }
    }

    private static JPopupMenu makeMainMenu(final MainActions actions) {
        final var menu = new JPopupMenu();
        menu.add(actions.saveAction);
        menu.add(actions.saveAsAction);
        menu.add(actions.exportAction);
        menu.add(new JSeparator(SwingConstants.HORIZONTAL));
        menu.add(actions.showDocumentSettingsAction);
        menu.add(actions.showOptionsAction);
        menu.add(new JSeparator(SwingConstants.HORIZONTAL));
        menu.add(actions.showHelpAction);
        menu.add(new JSeparator(SwingConstants.HORIZONTAL));
        menu.add(actions.returnHomeAction);
        menu.add(actions.quitAction);
        return menu;
    }

    private static <T extends AbstractButton> T setupTabBarButton(final T button, final Action action) {
        if (action != null) {
            button.setAction(action);
        }
        button.setText(null);
        button.setBorder(Borders.EMPTY);
        button.putClientProperty(
                FlatClientProperties.BUTTON_TYPE,
                FlatClientProperties.BUTTON_TYPE_BORDERLESS
        );
        button.putClientProperty(FlatClientProperties.BUTTON_TYPE_SQUARE, true);
        button.setFocusable(false);
        Components.setFixedSize(button, Dimensions.TAB_BAR_BUTTON_SIZE);
        return button;
    }
}
