package io.github.mimoguz.pixelj.views;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;

import io.github.mimoguz.pixelj.actions.Actions;
import io.github.mimoguz.pixelj.actions.ApplicationAction;
import io.github.mimoguz.pixelj.actions.MainActions;
import io.github.mimoguz.pixelj.graphics.FontIcon;
import io.github.mimoguz.pixelj.models.Project;
import io.github.mimoguz.pixelj.models.Project.ProjectChangeEvent;
import io.github.mimoguz.pixelj.resources.Icons;
import io.github.mimoguz.pixelj.resources.Resources;
import io.github.mimoguz.pixelj.views.characters_screen.CharactersScreen;
import io.github.mimoguz.pixelj.views.kerning_pairs_screen.KerningPairsScreen;
import io.github.mimoguz.pixelj.views.preview_screen.PreviewScreen;
import io.github.mimoguz.pixelj.views.shared.Borders;
import io.github.mimoguz.pixelj.views.shared.Components;
import io.github.mimoguz.pixelj.views.shared.Dimensions;

public class ProjectView extends JFrame {
    private final CharactersScreen charactersScreen;
    private final KerningPairsScreen kerningPairsScreen;
    private final PreviewScreen previewScreen;
    private final JTabbedPane root;

    public ProjectView(final Project project) {
        super();

        final var res = Resources.get();

        root = new JTabbedPane();

        charactersScreen = new CharactersScreen(project, root);
        kerningPairsScreen = new KerningPairsScreen(project, root);
        previewScreen = new PreviewScreen(project, root);
        MainActions mainActions = new MainActions(project, root);
        final var mainMenu = new MainMenu(mainActions);

        Actions.registerShortcuts(mainActions.all, root);
        // Invisible screens should be disabled to prevent shortcut collisions.
        kerningPairsScreen.setEnabled(false);
        previewScreen.setEnabled(false);

        setTitle(project.getTitle() + " - " + res.getString("applicationName"));
        setIconImages(
                Stream.of(16, 32, 48, 64, 128, 256)
                        .map(
                                size -> new FlatSVGIcon(
                                        "io/github/mimoguz/pixelj/resources/application_icon.svg",
                                        size,
                                        getClass().getClassLoader()
                                ).getImage()
                        )
                        .toList()
        );

        project.addChangeListener(this::onChange);

        root.putClientProperty(
                FlatClientProperties.TABBED_PANE_TAB_TYPE,
                FlatClientProperties.TABBED_PANE_TAB_TYPE_CARD
        );

        root.addTab(
                null,
                res.getIcon(Icons.LIST, res.colors.active(), null),
                charactersScreen,
                res.getString("charactersScreenTabTooltip")
        );

        root.addTab(
                null,
                res.getIcon(Icons.KERNING, res.colors.inactive(), null),
                kerningPairsScreen,
                res.getString("kerningPairsScreenTabTooltip")
        );

        root.addTab(
                null,
                res.getIcon(Icons.EYE, res.colors.inactive(), null),
                previewScreen,
                res.getString("previewScreenTabTooltip")
        );

        Collection<ApplicationAction> tabActions = List.of(
                new ApplicationAction("projectViewTab0Action", (event, action) -> root.setSelectedIndex(0))
                        .setAccelerator(KeyEvent.VK_1, ActionEvent.ALT_MASK),
                new ApplicationAction("projectViewTab1Action", (event, action) -> root.setSelectedIndex(1))
                        .setAccelerator(KeyEvent.VK_2, ActionEvent.ALT_MASK),
                new ApplicationAction("projectViewTab2Action", (event, action) -> root.setSelectedIndex(2))
                        .setAccelerator(KeyEvent.VK_3, ActionEvent.ALT_MASK)
        );
        Actions.registerShortcuts(tabActions, root);

        root.addChangeListener(e -> {
            // Activate-deactivate actions
            switch (root.getSelectedIndex()) {
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
            for (var index = 0; index < 3; index++) {
                if (root.getIconAt(index) instanceof final FontIcon icn) {
                    icn.setForeground(
                            index == root.getSelectedIndex() ? res.colors.active() : res.colors.inactive()
                    );
                }
            }
        });

        /* ---------------------------- Leading component --------------------------- */
        final var leadingContainer = new JPanel();
        final var buttonSize = new Dimension(48, 48);
        final var menuButton = new JToggleButton();
        final var menuButtonAction = new ApplicationAction("menuButtonAction", (event, action) -> {
            if (menuButton.isSelected()) {
                mainMenu.show(menuButton, menuButton.getWidth() + Dimensions.MEDIUM_PADDING, 0);
            } else {
                mainMenu.setVisible(false);
            }
        })
                .setTooltipWithAccelerator(
                        res.getString("menuButtonAction"),
                        KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.ALT_DOWN_MASK)
                )
                .setIcon(Icons.ELLIPSIS, res.colors.accent(), res.colors.disabledIcon());

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

        Actions.registerShortcuts(java.util.List.of(menuButtonAction), root);
        tabBarButton(menuButton, menuButtonAction, buttonSize);
        leadingContainer.setLayout(new BoxLayout(leadingContainer, BoxLayout.Y_AXIS));
        leadingContainer.add(menuButton);
        root.putClientProperty(FlatClientProperties.TABBED_PANE_LEADING_COMPONENT, leadingContainer);

        /* --------------------------- Trailing component --------------------------- */
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

        final var settingsButton = tabBarButton(new JButton(), mainActions.showSettingsAction, buttonSize);
        constraints.weighty = 0.0;
        constraints.gridy += 1;
        trailingContainer.add(settingsButton, constraints);

        final var helpButton = tabBarButton(new JButton(), mainActions.showHelpAction, buttonSize);
        constraints.gridy += 1;
        trailingContainer.add(helpButton, constraints);
        root.putClientProperty(FlatClientProperties.TABBED_PANE_TRAILING_COMPONENT, trailingContainer);

        /* --------------------------- Other tweaks --------------------------- */

        ToolTipManager.sharedInstance().setInitialDelay(100);
        ToolTipManager.sharedInstance().setDismissDelay(3000);

        root.putClientProperty(FlatClientProperties.TABBED_PANE_MINIMUM_TAB_WIDTH, buttonSize.width);
        root.putClientProperty(FlatClientProperties.TABBED_PANE_MAXIMUM_TAB_WIDTH, buttonSize.width);
        root.putClientProperty(FlatClientProperties.TABBED_PANE_TAB_HEIGHT, buttonSize.height);
        root.putClientProperty(FlatClientProperties.TABBED_PANE_TAB_ICON_PLACEMENT, SwingConstants.TOP);
        root.setTabPlacement(SwingConstants.LEFT);
        root.setFocusable(false);
        add(root);

        pack();
        setSize(1200, 720);
        getRootPane().putClientProperty(FlatClientProperties.TITLE_BAR_SHOW_ICON, false);
        setLocationRelativeTo(null);
    }

    private void onChange(final Project source, final ProjectChangeEvent event) {
        switch (event) {
            case final ProjectChangeEvent.MetricsChanged metricsChanged -> {
                charactersScreen.updateMetrics(metricsChanged.metrics());
                kerningPairsScreen.updateMetrics(metricsChanged.metrics());
                if (previewScreen.isEnabled()) {
                    previewScreen.refresh();
                }
            }
            case final ProjectChangeEvent.TitleChanged titleChanged ->
                setTitle(titleChanged.title() + " - " + Resources.get().getString("applicationName"));
            default -> throw new IllegalArgumentException("Unexpected value: " + event);
        }
    }

    private static <T extends AbstractButton> T tabBarButton(
            final T button,
            final Action action,
            final Dimension size
    ) {
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
        Components.setFixedSize(button, size);
        return button;
    }
}
