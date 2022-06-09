package io.github.mimoguz.pixelj.models;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.EventListenerList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import io.github.mimoguz.pixelj.util.ChangeListener;
import io.github.mimoguz.pixelj.util.Changeable;

public class Project
        implements
        Changeable<Project, Project.ProjectChangeEvent, Project.ProjectChangeListener> {

    public sealed interface ProjectChangeEvent permits ProjectChangeEvent.MetricsChanged, ProjectChangeEvent.TitleChanged {
        record MetricsChanged(Metrics metrics) implements ProjectChangeEvent {
            // Empty
        }

        record TitleChanged(String title) implements ProjectChangeEvent {
            // Empty
        }
    }

    public interface ProjectChangeListener extends ChangeListener<Project, ProjectChangeEvent> {
        // Empty
    }

    private final SortedList<CharacterItem> characters;
    private final SortedList<KerningPair> kerningPairs;
    private final EventListenerList listeners = new EventListenerList();
    private Metrics metrics;
    private String title;

    public Project(
            final String title,
            final SortedList<CharacterItem> characters,
            final SortedList<KerningPair> kerningPairs,
            final Metrics metrics
    ) {
        this.title = title;
        this.characters = characters;
        this.kerningPairs = kerningPairs;
        this.metrics = metrics;

        // Ignore
        // Kerning pairs which depend on non-existing characters
        ListDataListener kerningPairRemover = new ListDataListener() {
            @Override
            public void contentsChanged(final ListDataEvent e) {
                sync();
            }

            @Override
            public void intervalAdded(final ListDataEvent e) { // Ignore
            }

            @Override
            public void intervalRemoved(final ListDataEvent e) {
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

    @Override
    public Class<ProjectChangeListener> getListenerClass() {
        return ProjectChangeListener.class;
    }

    @Override
    public EventListenerList getListenerList() {
        return listeners;
    }

    public Metrics getMetrics() {
        return metrics;
    }

    public String getTitle() {
        return title;
    }

    public void setMetrics(final Metrics value) {
        metrics = value;
        fireChangeEvent(this, new ProjectChangeEvent.MetricsChanged(value));
    }

    public void setTitle(final String value) {
        title = value;
        fireChangeEvent(this, new ProjectChangeEvent.TitleChanged(value));
    }
}
