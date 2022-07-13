package pixelj.views.projectwindow;

import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import pixelj.actions.Actions;
import pixelj.actions.MainActions;
import pixelj.models.Project;
import pixelj.resources.Resources;
import pixelj.views.projectwindow.glyphspage.GlyphsPage;
import pixelj.views.projectwindow.kerningpairspage.KerningPairsPage;
import pixelj.views.projectwindow.previewpage.PreviewPage;

public class ProjectWindow extends ProjectWindowBase {

    public ProjectWindow(final Project project) {
        setup(
                new GlyphsPage(project, this),
                new KerningPairsPage(project, this),
                new PreviewPage(project, this)
        );

        // Invisible screens should be disabled to prevent shortcut collisions.
        getKerningPairsPage().setEnabled(false);
        getPreviewPage().setEnabled(false);

        final var mainActions = new MainActions(project, this);
        Actions.registerShortcuts(mainActions.all, this.getRootPane());
        fillMenu(mainActions);
        helpButton.setAction(mainActions.showHelpAction);
        optionsButton.setAction(mainActions.showOptionsAction);

        final var res = Resources.get();
        helpButton.setText(null);
        helpButton.setToolTipText(res.getString("showHelpAction"));
        optionsButton.setText(null);
        optionsButton.setToolTipText(res.getString("showOptionsAction"));

        final var closeListener = new CloseListener(project, mainActions.saveAction, this);
        this.addWindowListener(closeListener);

        project.titleProperty.addChangeListener((sender, value) -> setFrameTitle(value, project.isDirty()));
        project.dirtyProperty.addChangeListener((sender, value) -> setFrameTitle(project.getTitle(), value));
    }

    private void setFrameTitle(final String titleText, final boolean isDirty) {
        if (isDirty) {
            setTitle(Resources.get().formatString("projectViewTitleUnsaved", titleText));
        } else {
            setTitle(Resources.get().formatString("projectViewTitle", titleText));
        }
    }

    private void fillMenu(final MainActions actions) {
        mainMenu.add(actions.saveAction);
        mainMenu.add(actions.saveAsAction);
        mainMenu.add(actions.exportAction);
        mainMenu.add(new JSeparator(SwingConstants.HORIZONTAL));
        mainMenu.add(actions.showDocumentSettingsAction);
        mainMenu.add(actions.showOptionsAction);
        mainMenu.add(new JSeparator(SwingConstants.HORIZONTAL));
        mainMenu.add(actions.showHelpAction);
        mainMenu.add(new JSeparator(SwingConstants.HORIZONTAL));
        mainMenu.add(actions.returnHomeAction);
        mainMenu.add(actions.quitAction);
    }
}
