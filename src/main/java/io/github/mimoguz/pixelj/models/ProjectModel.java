package io.github.mimoguz.pixelj.models;

import javax.swing.event.EventListenerList;

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

    private final CharacterListModel characters;
    private final KerningPairListModel kerningPairs;
    private final EventListenerList listeners = new EventListenerList();

    private Metrics metrics;

    private String title;

    public ProjectModel(
            final String title,
            final CharacterListModel characters,
            final KerningPairListModel kerningPairs,
            final Metrics metrics
    ) {
        this.title = title;
        this.characters = characters;
        this.kerningPairs = kerningPairs;
        this.metrics = metrics;
        characters.pair(kerningPairs);
    }

    public CharacterListModel getCharacters() {
        return characters;
    }

    public KerningPairListModel getKerningPairs() {
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

    public void setMetrics(Metrics value) {
        metrics = value;
        fireChangeEvent(this, new ProjectChangeEvent.MetricsChanged(value));
    }

    public void setTitle(String value) {
        title = value;
        fireChangeEvent(this, new ProjectChangeEvent.TitleChanged(value));
    }
}
