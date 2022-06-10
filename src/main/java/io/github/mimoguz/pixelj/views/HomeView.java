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

public class HomeView extends JFrame {
    private final HomeActions actions;

    public HomeView() {
        super();
        final var res = Resources.get();
        final var root = new JPanel(new BorderLayout());

        actions = new HomeActions(root);

        final var newProjectButton = makeButton(actions.newProjectAction);
        final var openProjectButton = makeButton(actions.openProjectAction);
        final var showOpenDialogButton = makeButton(actions.showOpenDialogAction);

        final var left = new JPanel(new BorderLayout());
        left.setBorder(Borders.SMALL_EMPTY_CUP);

        final var buttonBox = new JPanel();
        buttonBox.setLayout(new BoxLayout(buttonBox, BoxLayout.Y_AXIS));
        buttonBox.add(newProjectButton);
        buttonBox.add(Box.createVerticalStrut(Dimensions.SMALL_PADDING));
        buttonBox.add(openProjectButton);
        buttonBox.add(Box.createVerticalStrut(Dimensions.SMALL_PADDING));
        buttonBox.add(showOpenDialogButton);
        buttonBox.add(Box.createVerticalGlue());
        left.add(buttonBox, BorderLayout.NORTH);

        final var toolBar = new JToolBar();
        toolBar.add(actions.showOptionsDialogAction);
        toolBar.addSeparator();
        toolBar.add(actions.quitAction);
        left.add(toolBar, BorderLayout.SOUTH);

        root.add(left, BorderLayout.WEST);

        final var recentList = new JList<String>();
        recentList.setBorder(Borders.EMPTY);
        final var scrollPanel = new JScrollPane(recentList);
        scrollPanel.setBorder(Borders.EMPTY);
        root.add(scrollPanel, BorderLayout.CENTER);

        setContentPane(root);
        pack();
        setSize(new Dimension(600, 600));
        setTitle(res.getString("applicationName"));
        getRootPane().putClientProperty(FlatClientProperties.TITLE_BAR_SHOW_ICON, false);
        setLocationRelativeTo(null);
    }

    private static JButton makeButton(ApplicationAction action) {
        final var button = new JButton();
        button.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_BORDERLESS);
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
                    0
            ));
            content.add(label, BorderLayout.CENTER);
        }
        button.add(content);
        return button;
    }
}
