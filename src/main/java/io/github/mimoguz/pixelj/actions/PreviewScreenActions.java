package io.github.mimoguz.pixelj.actions;

import io.github.mimoguz.pixelj.controls.StringView;
import io.github.mimoguz.pixelj.models.CharacterModel;
import io.github.mimoguz.pixelj.models.ProjectModel;
import io.github.mimoguz.pixelj.resources.Resources;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class PreviewScreenActions {
    public final Collection<ApplicationAction> all;
    public final ApplicationAction clearAction;
    public final ApplicationAction refreshAction;
    private final JPanel container;
    private final JTextArea input;
    private final ProjectModel project;

    public PreviewScreenActions(final ProjectModel project, final JTextArea input, final JPanel container) {
        this.project = project;
        this.input = input;
        this.container = container;

        clearAction = new ApplicationAction("previewClearAction", (e, action) -> clearPreview())
                .setTextKey("previewClearAction");

        refreshAction = new ApplicationAction("previewRefreshAction", (e, action) -> refreshPreview())
                .setTextKey("previewRefreshAction")
                .setAccelerator(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK);
        
        all = List.of(clearAction, refreshAction);
    }

    private void clearPreview() {
        var currentViews = container.getComponents();
        for (var view : currentViews) {
            container.remove(view);
        }
    }

    @NotNull
    private List<CharacterModel> getCharactersOfLine(String line) {
        final var characters = project.getCharacters();
        return line.codePoints()
                .mapToObj(codePoint -> characters.findFirst(chr -> chr.getCodePoint() == codePoint))
                .filter(Objects::nonNull)
                .toList();
    }

    @NotNull
    private List<Integer> getSpaces(List<CharacterModel> characters) {
        final var spacing = project.getMetrics().spacing();
        final var kerningPairs = project.getKerningPairs();
        final var pairs = characters.size() - 1;
        final var spaces = new ArrayList<Integer>(pairs);
        for (var index = 0; index < pairs; index++) {
            final var left = characters.get(index);
            final var right = characters.get(index + 1);
            final var pair = kerningPairs.findFirst(p -> p.getLeft().equals(left) && p.getRight().equals(right));
            spaces.add(pair == null ? spacing : spacing + pair.getKerningValue());
        }
        return spaces;
    }

    @NotNull
    private StringView getView(String line) {
        final var view = new StringView(Resources.get().colors.disabledIcon());
        final var characters = getCharactersOfLine(line);
        final var spaces = getSpaces(characters);
        view.set(characters, spaces);
        return view;
    }

    private void refreshPreview() {
        clearPreview();

        final var lines = input.getLineCount();
        for (var lineIndex = 0; lineIndex < lines; lineIndex++) {
            try {
                final var from = input.getLineStartOffset(lineIndex);
                final var to = input.getLineEndOffset(lineIndex);
                final var line = input.getText(from, to - from + 1);
                container.add(getView(line));
            } catch (BadLocationException exception) {
                break;
            }
        }
    }
}
