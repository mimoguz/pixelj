package io.github.mimoguz.pixelj.actions;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;

import io.github.mimoguz.pixelj.controls.StringView;
import io.github.mimoguz.pixelj.models.CharacterModel;
import io.github.mimoguz.pixelj.models.KerningPairModel;
import io.github.mimoguz.pixelj.models.ProjectModel;

public class PreviewScreenActions {
    public final Collection<ApplicationAction> all = new ArrayList<>();
    public final ApplicationAction clearAction;
    public final ApplicationAction refreshAction;
    private final JPanel container;
    private final JTextArea input;
    private final ProjectModel project;

    public PreviewScreenActions(final ProjectModel project, final JTextArea input, final JPanel container) {
        this.project = project;
        this.input = input;
        this.container = container;

        clearAction = new ApplicationAction("previewClearAction", this::clearPreview)
                .setTextKey("previewClearAction");

        refreshAction = new ApplicationAction("previewRefreshAction", this::refreshPreview)
                .setTextKey("previewRefreshAction")
                .setAccelerator(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK);

        all.add(clearAction);
        all.add(refreshAction);
    }

    private void clearContainer() {
        var currentViews = container.getComponents();
        for (var view : currentViews) {
            container.remove(view);
        }
    }

    private void clearPreview(ActionEvent event, Action action) {
        clearContainer();
        container.revalidate();
        container.repaint();
    }

    private List<CharacterModel> getCharactersOfLine(String line) {
        final var characters = project.getCharacters();
        return line.codePoints()
                .mapToObj(characters::findHash)
        		.filter(Objects::nonNull).toList();
    }

    private List<Integer> getSpaces(List<CharacterModel> characters) {
        if (characters.isEmpty()) {
            return java.util.Collections.emptyList();
        }

        final var spacing = project.getMetrics().spacing();
        final var kerningPairs = project.getKerningPairs();
        final var pairs = characters.size() - 1;
        final var spaces = new ArrayList<Integer>(pairs);
        for (var index = 0; index < pairs; index++) {
            final var left = characters.get(index);
            final var right = characters.get(index + 1);
            final var pair = kerningPairs.findHash(KerningPairModel.getHash(left, right));
            spaces.add(pair == null ? spacing : spacing + pair.getKerningValue());
        }
        return spaces;
    }

    private StringView getView(String line) {
        final var view = new StringView(Color.WHITE);
        final var characters = getCharactersOfLine(line);
        final var spaces = getSpaces(characters);
        view.set(characters, spaces);
        return view;
    }

    private void refreshPreview(ActionEvent event, Action action) {
        clearContainer();

        final var lines = input.getLineCount();
        for (var lineIndex = 0; lineIndex < lines; lineIndex++) {
            try {
                final var from = input.getLineStartOffset(lineIndex);
                final var to = input.getLineEndOffset(lineIndex);
                final var line = input.getText(from, to - from);
                container.add(getView(line));
            } catch (BadLocationException exception) {
                break;
            }
        }
        
        container.revalidate();
        container.repaint();
    }
}
