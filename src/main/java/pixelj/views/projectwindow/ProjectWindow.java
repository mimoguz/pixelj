package pixelj.views.projectwindow;

import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import pixelj.actions.ProjectWindowActions;
import pixelj.models.Project;
import pixelj.resources.Resources;
import pixelj.services.AppState;
import pixelj.views.projectwindow.glyphspage.GlyphsPage;
import pixelj.views.projectwindow.kerningpairspage.KerningPairsPage;
import pixelj.views.projectwindow.previewpage.PreviewPage;

public final class ProjectWindow extends ProjectWindowBase {

    public ProjectWindow(final Project project, final AppState appState) {
        setup(
            new GlyphsPage(project, this),
            new KerningPairsPage(project, this),
            new PreviewPage(project, appState, this)
        );

        // Invisible screens should be disabled to prevent shortcut collisions.
        // Related actions handle registering/un-registering shortcuts. Enabled page must be the last item,
        // otherwise its registered shortcut may be unregistered by the disabled pages.
        getKerningPairsPage().setEnabled(false);
        getPreviewPage().setEnabled(false);
        getGlyphsPage().setEnabled(true);

        final var mainActions = new ProjectWindowActions(project, this, appState);
        mainActions.registerShortcuts(getRootPane());
        fillMenu(mainActions);
        helpButton.setAction(mainActions.showHelpAction);
        optionsButton.setAction(mainActions.showOptionsAction);

        final var res = Resources.get();
        helpButton.setText(null);
        helpButton.setToolTipText(res.getString("showHelpAction"));
        optionsButton.setText(null);
        optionsButton.setToolTipText(res.getString("showOptionsAction"));

        final var closeListener = new CloseListener(project, appState, mainActions.saveAction, this);
        this.addWindowListener(closeListener);

        setFrameTitle(project.getTitle(), false);
        project.titleProperty.addChangeListener((sender, title) -> setFrameTitle(title, project.isDirty()));
        project.dirtyProperty.addChangeListener(dirty -> setFrameTitle(project.getTitle(), dirty));
    }

    @Override
    public void dispose() {
        super.dispose();
        getKerningPairsPage().detach();
        getGlyphsPage().detach();
    }

    private void setFrameTitle(final String titleText, final boolean isDirty) {
        if (isDirty) {
            setTitle(Resources.get().formatString("projectViewTitleUnsaved", titleText));
        } else {
            setTitle(Resources.get().formatString("projectViewTitle", titleText));
        }
    }

    private void fillMenu(final ProjectWindowActions actions) {
        mainMenu.add(actions.saveAction);
        mainMenu.add(actions.saveAsAction);
        mainMenu.add(new JSeparator(SwingConstants.HORIZONTAL));
        mainMenu.add(actions.fntExportAction);
        mainMenu.add(actions.svgExportAction);
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
