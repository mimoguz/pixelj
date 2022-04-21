package io.github.mimoguz.pixelj.views.previewScreen;

import io.github.mimoguz.pixelj.controls.StringView;
import io.github.mimoguz.pixelj.models.CharacterModel;
import io.github.mimoguz.pixelj.models.ProjectModel;
import io.github.mimoguz.pixelj.util.Detachable;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ParametersAreNonnullByDefault
public class PreviewScreen extends JPanel implements Detachable {
    private final List<StringView> lines = new ArrayList<>(1);
    private final ProjectModel project;
    private final JTextArea textInput;

    public PreviewScreen(final ProjectModel project) {
        this.project = project;

        textInput = new JTextArea();
    }

    @Override
    public void detach() {

    }

    private List<CharacterModel> getCharactersOfLine(String line) {
        final var characters = project.getCharacters();

        return line.codePoints()
                .mapToObj(cp -> characters.findFirst(chr -> chr.getCodePoint() == cp))
                .filter(Objects::nonNull)
                .toList();
    }

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

    private void setView(String line, StringView view) {
        final var characters = getCharactersOfLine(line);
        final var spaces = getSpaces(characters);
        view.set(characters, spaces);
    }
}
