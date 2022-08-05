package pixelj.actions;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;

import pixelj.models.Glyph;
import pixelj.models.KerningPair;
import pixelj.models.Project;
import pixelj.services.AppState;
import pixelj.views.controls.StringView;

public final class PreviewPageActions implements Actions {
    private static final int SPACE = 32;

    /**
     * Clear the preview panel and the input field.
     */
    public final ApplicationAction clearAction;
    /**
     * Refresh the preview panel.
     */
    public final ApplicationAction refreshAction;

    private final Collection<ApplicationAction> all = new ArrayList<>();
    private final JPanel container;
    private final JTextArea input;
    private final Project project;
    private final AppState appState;
    private int zoom = 1;

    public PreviewPageActions(
        final Project project,
        final AppState appState,
        final JTextArea input,
        final JPanel container
    ) {
        this.project = project;
        this.appState = appState;
        this.input = input;
        this.container = container;

        clearAction = new ApplicationAction("previewClearAction", this::clearPreview).withText();

        refreshAction = new ApplicationAction("previewRefreshAction", this::refreshPreview).withText()
                .setAccelerator(KeyEvent.VK_F5, 0);

        all.add(clearAction);
        all.add(refreshAction);

        project.documentSettingsProperty.addChangeListener((source, value) -> refresh());
    }

    public void setZoom(final int value) {
        zoom = value;
        for (final var child : container.getComponents()) {
            if (child instanceof final StringView view) {
                view.setZoom(value);
            }
        }
        container.revalidate();
    }

    @Override
    public void detach() {
        // Empty
    }

    @Override
    public Collection<ApplicationAction> getAll() {
        return all;
    }

    @Override
    public void setEnabled(final boolean enabled) {
        for (var action : all) {
            action.setEnabled(enabled);
        }
    }

    private void clearContainer() {
        final var currentViews = container.getComponents();
        for (final var view : currentViews) {
            container.remove(view);
        }
    }

    private void clearPreview(final ActionEvent event, final Action action) {
        clearContainer();
        container.revalidate();
        container.repaint();
        input.setText(null);
        appState.setPreviewText(null);
    }

    private List<Glyph> getCharactersOfLine(final String line) {
        final var characters = project.getGlyphs();
        return line.codePoints()
            .mapToObj(cp -> cp == SPACE ? new Glyph(SPACE, 0, null) : characters.findHash(cp))
            .filter(Objects::nonNull)
            .toList();
    }

    private List<Integer> getSpaces(final List<Glyph> characters) {
        if (characters.isEmpty()) {
            return java.util.Collections.emptyList();
        }

        final var settings = project.getDocumentSettings();
        final var letterSpacing = settings.letterSpacing();
        final var spaceSize = settings.isMonospaced()
            ? settings.defaultWidth() + settings.letterSpacing()
            : settings.spaceSize();
        final var kerningPairs = project.getKerningPairs();
        final var pairs = characters.size() - 1;
        final var spaces = new ArrayList<Integer>(pairs);
        for (var index = 0; index < pairs; index++) {
            final var left = characters.get(index);
            if (left.getCodePoint() == SPACE) {
                spaces.add(spaceSize);
                continue;
            }
            final var right = characters.get(index + 1);
            final var pair = kerningPairs.findHash(KerningPair.getHash(left, right));
            spaces.add(pair == null || settings.isMonospaced()
                ? letterSpacing
                : letterSpacing + pair.getKerningValue()
            );
        }
        return spaces;
    }

    private StringView getView(final String line) {
        final var view = new StringView(Color.WHITE);
        view.setMaxY(project.getDocumentSettings().descender() + project.getDocumentSettings().ascender());
        final var characters = getCharactersOfLine(line);
        final var spaces = getSpaces(characters);
        view.set(characters, spaces);
        return view;
    }

    private void refreshPreview(final ActionEvent event, final Action action) {
        refresh();
    }

    private void refresh() {
        clearContainer();
        final var text = input.getText();
        appState.setPreviewText(text);
        if (text != null && !input.getText().isEmpty()) {
            final var lines = input.getLineCount();
            for (var lineIndex = 0; lineIndex < lines; lineIndex++) {
                try {
                    final var from = input.getLineStartOffset(lineIndex);
                    final var to = input.getLineEndOffset(lineIndex);
                    final var line = input.getText(from, to - from);
                    final var view = getView(line);
                    view.setZoom(zoom);
                    view.setPadding(0);
                    if (lineIndex < lines - 1) {
                        view.setLineSpacing(project.getDocumentSettings().lineSpacing());
                    }
                    view.setAlignmentX(0f);
                    container.add(view);
                } catch (final BadLocationException exception) {
                    break;
                }
            }
        }

        container.revalidate();
        container.repaint();
    }
}
