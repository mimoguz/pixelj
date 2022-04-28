package io.github.mimoguz.pixelj.views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.*;

import com.formdev.flatlaf.FlatClientProperties;

import io.github.mimoguz.pixelj.actions.Actions;
import io.github.mimoguz.pixelj.actions.ApplicationAction;
import io.github.mimoguz.pixelj.actions.MainActions;
import io.github.mimoguz.pixelj.graphics.FontIcon;
import io.github.mimoguz.pixelj.models.ProjectModel;
import io.github.mimoguz.pixelj.resources.Icons;
import io.github.mimoguz.pixelj.resources.Resources;
import io.github.mimoguz.pixelj.views.characters_screen.CharactersScreen;
import io.github.mimoguz.pixelj.views.kerning_pairs_screen.KerningPairsScreen;
import io.github.mimoguz.pixelj.views.preview_screen.PreviewScreen;
import io.github.mimoguz.pixelj.views.shared.Borders;
import io.github.mimoguz.pixelj.views.shared.Components;

public class ProjectView extends JFrame {
    private static final long serialVersionUID = -8552411151437621157L;

    public ProjectView(final ProjectModel project) {
        final var root = new JTabbedPane();
        final var charactersScreen = new CharactersScreen(project, root);
        final var kerningPairsScreen = new KerningPairsScreen(project, root);
        final var previewScreen = new PreviewScreen(project, root);
        final var globalActions = new MainActions(project);
        final var mainMenu = new MainMenu(globalActions);

        Actions.registerShortcuts(globalActions.all, root);
        // Invisible screens should be disabled to prevent shortcut collisions.
        kerningPairsScreen.setEnabled(false);
        previewScreen.setEnabled(false);

        final var res = Resources.get();

        root.addTab(
                null,
                res.getIcon(Icons.LIST, res.colors.active()),
                charactersScreen,
                res.getString("charactersScreenTabTooltip")
        );

        root.addTab(
                null,
                res.getIcon(Icons.KERNING, res.colors.faintIcon()),
                kerningPairsScreen,
                res.getString("kerningPairsScreenTabTooltip")
        );

        root.addTab(
                null,
                res.getIcon(Icons.EYE, res.colors.faintIcon()),
                previewScreen,
                res.getString("previewScreenTabTooltip")
        );

        root.addChangeListener(e -> {
            // Activate-deactivate actions
            switch (root.getSelectedIndex()) {
                case 0 -> {
                    charactersScreen.setEnabled(true);
                    kerningPairsScreen.setEnabled(false);
                    kerningPairsScreen.setEnabled(false);
                }
                case 1 -> {
                    charactersScreen.setEnabled(false);
                    kerningPairsScreen.setEnabled(true);
                    kerningPairsScreen.setEnabled(false);
                }
                default -> {
                    charactersScreen.setEnabled(false);
                    kerningPairsScreen.setEnabled(false);
                    kerningPairsScreen.setEnabled(true);
                }
            }

            // Fix icon color
            for (var index = 0; index < 3; index++) {
                if (root.getIconAt(index) instanceof FontIcon icn) {
                    icn.setForeground(
                            index == root.getSelectedIndex() ? res.colors.active() : res.colors.faintIcon()
                    );
                }
            }
        });

        /* ---------------------------- Leading component --------------------------- */
        final var menuButtonContainer = new JPanel();
        final var buttonSize = new Dimension(48, 48);
        final var menuButtonAction = new ApplicationAction(
                "menuButtonAction",
                (event, action) -> mainMenu.show(
                        menuButtonContainer,
                        menuButtonContainer.getX() + menuButtonContainer.getWidth() + 12,
                        menuButtonContainer.getY()
                )
        ).setTooltipKey("menuButtonAction")
                .setIcon(Icons.ELLIPSIS, res.colors.accent(), res.colors.disabledIcon())
                .setAccelerator(KeyEvent.VK_M, ActionEvent.CTRL_MASK);
        Actions.registerShortcuts(java.util.List.of(menuButtonAction), root);
        final var menuButton = tabBarButton(menuButtonAction, buttonSize);
        menuButtonContainer.setLayout(new BorderLayout());
        menuButtonContainer.add(menuButton, BorderLayout.CENTER);
        menuButtonContainer.add(divider(buttonSize.width), BorderLayout.SOUTH);
        root.putClientProperty(FlatClientProperties.TABBED_PANE_LEADING_COMPONENT, menuButtonContainer);

        /* --------------------------- Trailing component --------------------------- */
        final var trailingContainer = new JPanel();
        trailingContainer.setLayout(new GridBagLayout());
        final var cons = new GridBagConstraints();
        cons.weighty = 1;
        trailingContainer.add(Box.createHorizontalGlue(), cons);
        cons.weighty = 0;
        cons.gridy = cons.gridy + 1;
        trailingContainer.add(divider(buttonSize.width), cons);
        final var settingsButton = tabBarButton(globalActions.showSettingsAction, buttonSize);
        cons.gridy = cons.gridy + 1;
        trailingContainer.add(settingsButton, cons);
        final var helpButton = tabBarButton(globalActions.showHelpAction, buttonSize);
        cons.gridy = cons.gridy + 1;
        trailingContainer.add(helpButton, cons);
        root.putClientProperty(FlatClientProperties.TABBED_PANE_TRAILING_COMPONENT, trailingContainer);

        ToolTipManager.sharedInstance().setInitialDelay(100);
        ToolTipManager.sharedInstance().setDismissDelay(3000);
        root.putClientProperty(FlatClientProperties.TABBED_PANE_MINIMUM_TAB_WIDTH, buttonSize.width);
        root.putClientProperty(FlatClientProperties.TABBED_PANE_MAXIMUM_TAB_WIDTH, buttonSize.width);
        root.putClientProperty(FlatClientProperties.TABBED_PANE_TAB_HEIGHT, buttonSize.height);
        root.putClientProperty(FlatClientProperties.TABBED_PANE_TAB_ICON_PLACEMENT, SwingConstants.TOP);
        root.putClientProperty(FlatClientProperties.STYLE_CLASS, "divided");
        root.setTabPlacement(SwingConstants.LEFT);
        root.setFocusable(false);
        add(root);
        pack();
    }

    private static JButton tabBarButton(Action action, Dimension size) {
        final var button = new JButton();
        button.setAction(action);
        Components.setFixedSize(button, size);
        button.setBorder(Borders.empty);
        button.putClientProperty(
                FlatClientProperties.BUTTON_TYPE,
                FlatClientProperties.BUTTON_TYPE_BORDERLESS
        );
        button.setFocusable(false);
        return button;
    }

    private JPanel divider(int width) {
        final var divider = new JPanel();
        divider.setBackground(Resources.get().colors.divider());
        Components.setFixedSize(divider, new Dimension(width, 1));
        return divider;
    }
}
