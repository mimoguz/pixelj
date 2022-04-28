package io.github.mimoguz.pixelj.views;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.ToolTipManager;

import io.github.mimoguz.pixelj.graphics.FontIcon;
import io.github.mimoguz.pixelj.models.ProjectModel;
import io.github.mimoguz.pixelj.resources.Icons;
import io.github.mimoguz.pixelj.resources.Resources;
import io.github.mimoguz.pixelj.views.characters_screen.CharactersScreen;
import io.github.mimoguz.pixelj.views.kerning_pairs_screen.KerningPairsScreen;
import io.github.mimoguz.pixelj.views.preview_screen.PreviewScreen;

public class ProjectView extends JFrame {
    private static final long serialVersionUID = -8552411151437621157L;

    public ProjectView(final ProjectModel project) {
        final var root = new JTabbedPane();
        final var charactersScreen = new CharactersScreen(project, root);
        final var kerningPairsScreen = new KerningPairsScreen(project, root);
        final var previewScreen = new PreviewScreen(project, root);

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

        ToolTipManager.sharedInstance().setInitialDelay(100);
        ToolTipManager.sharedInstance().setDismissDelay(3000);
        add(root);
        pack();
    }
}
