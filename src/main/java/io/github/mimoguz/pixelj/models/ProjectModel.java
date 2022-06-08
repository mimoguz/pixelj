package io.github.mimoguz.pixelj.models;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.EventListenerList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import io.github.mimoguz.pixelj.util.ChangeListener;
import io.github.mimoguz.pixelj.util.Changeable;

public class ProjectModel
        implements
        Changeable<ProjectModel, ProjectModel.ProjectChangeEvent, ProjectModel.ProjectChangeListener> {

    public sealed interface ProjectChangeEvent permits ProjectChangeEvent.MetricsChanged, ProjectChangeEvent.TitleChanged {
        record MetricsChanged(Metrics metrics) implements ProjectChangeEvent {
            // Empty
        }

        record TitleChanged(String title) implements ProjectChangeEvent {
            // Empty
        }
    }

    public interface ProjectChangeListener extends ChangeListener<ProjectModel, ProjectChangeEvent> {
        // Empty
    }

    private final HashListModel<CharacterModel> characters;
    private final ListDataListener kerningPairRemover;
    private final HashListModel<KerningPairModel> kerningPairs;

    private final EventListenerList listeners = new EventListenerList();

    private Metrics metrics;

    private String title;

    public ProjectModel(
            final String title,
            final HashListModel<CharacterModel> characters,
            final HashListModel<KerningPairModel> kerningPairs,
            final Metrics metrics
    ) {
        this.title = title;
        this.characters = characters;
        this.kerningPairs = kerningPairs;
        this.metrics = metrics;

        kerningPairRemover = new ListDataListener() {
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
                final var marked = new ArrayList<KerningPairModel>();
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

    public int countDependent(final CharacterModel model) {
        return kerningPairs.countWhere(p -> p.getLeft().equals(model) || p.getRight().equals(model));
    }

    public List<KerningPairModel> findDependent(final CharacterModel model) {
        return kerningPairs.find(p -> p.getLeft().equals(model) || p.getRight().equals(model));
    }

    public HashListModel<CharacterModel> getCharacters() {
        return characters;
    }

    public HashListModel<KerningPairModel> getKerningPairs() {
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
