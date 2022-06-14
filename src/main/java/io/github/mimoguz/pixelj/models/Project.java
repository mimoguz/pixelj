package io.github.mimoguz.pixelj.models;

import io.github.mimoguz.pixelj.util.ChangeableValue;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.List;

public class Project {

    public final ChangeableValue<Metrics> metricsProperty;
    public final ChangeableValue<String> titleProperty;
    private final SortedList<CharacterItem> characters;
    private final SortedList<KerningPair> kerningPairs;

    public Project(
            final String title,
            final SortedList<CharacterItem> characters,
            final SortedList<KerningPair> kerningPairs,
            final Metrics metrics
    ) {
        titleProperty = new ChangeableValue<>(title);
        this.characters = characters;
        this.kerningPairs = kerningPairs;
        metricsProperty = new ChangeableValue<>(metrics);

        // Ignore
        // Kerning pairs which depend on non-existing characters
        ListDataListener kerningPairRemover = new ListDataListener() {
            @Override
            public void intervalAdded(final ListDataEvent e) { // Ignore
            }

            @Override
            public void intervalRemoved(final ListDataEvent e) {
                sync();
            }

            @Override
            public void contentsChanged(final ListDataEvent e) {
                sync();
            }

            private void sync() {

                if (kerningPairs == null) {
                    return;
                }

                // Kerning pairs which depend on non-existing characters
                final var marked = new ArrayList<KerningPair>();
                for (var index = 0; index < kerningPairs.getSize(); index++) {
                    final var model = kerningPairs.getElementAt(index);
                    if (
                            !characters.sourceContains(model.getLeft())
                                    || !characters.sourceContains(model.getRight())
                    ) {
                        marked.add(model);
                    }
                }

                kerningPairs.removeAll(marked);
            }
        };

        characters.addListDataListener(kerningPairRemover);
    }

    public int countDependent(final CharacterItem model) {
        return kerningPairs.countWhere(p -> p.getLeft().equals(model) || p.getRight().equals(model));
    }

    public List<KerningPair> findDependent(final CharacterItem model) {
        return kerningPairs.find(p -> p.getLeft().equals(model) || p.getRight().equals(model));
    }

    public SortedList<CharacterItem> getCharacters() {
        return characters;
    }

    public SortedList<KerningPair> getKerningPairs() {
        return kerningPairs;
    }

    public Metrics getMetrics() {
        return metricsProperty.getValue();
    }

    public void setMetrics(final Metrics value) {
        metricsProperty.setValue(value);
    }

    public String getTitle() {
        return titleProperty.getValue();
    }

    public void setTitle(final String value) {
        titleProperty.setValue(value);
    }
}
