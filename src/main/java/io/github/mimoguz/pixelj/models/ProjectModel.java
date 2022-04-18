package io.github.mimoguz.pixelj.models;

import io.github.mimoguz.pixelj.util.ChangeListener;
import io.github.mimoguz.pixelj.util.Changeable;

import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.event.EventListenerList;

@ParametersAreNonnullByDefault
public class ProjectModel
        implements Changeable<ProjectModel, ProjectModel.ProjectChangeEvent, ProjectModel.ProjectChangeListener> {
    private final CharacterListModel characters;
    private final KerningPairListModel kerningPairs;
    private final EventListenerList listeners = new EventListenerList();
    private @NotNull Metrics metrics;
    private @NotNull String title;

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

    public @NotNull CharacterListModel getCharacters() {
        return characters;
    }

    public @NotNull KerningPairListModel getKerningPairs() {
        return kerningPairs;
    }

    @Override
    public @NotNull Class<ProjectChangeListener> getListenerClass() {
        return ProjectChangeListener.class;
    }

    @Override
    public @NotNull EventListenerList getListenerList() {
        return listeners;
    }

    public @NotNull Metrics getMetrics() {
        return metrics;
    }

    public void setMetrics(Metrics value) {
        metrics = value;
        fireChangeEvent(this, new ProjectChangeEvent.MetricsChanged(value));
    }

    public @NotNull String getTitle() {
        return title;
    }

    public void setTitle(String value) {
        title = value;
        fireChangeEvent(this, new ProjectChangeEvent.TitleChanged(value));
    }

    public sealed interface ProjectChangeEvent permits ProjectChangeEvent.MetricsChanged, ProjectChangeEvent.TitleChanged {
        record MetricsChanged(Metrics metrics) implements ProjectChangeEvent {
        }

        record TitleChanged(String title) implements ProjectChangeEvent {
        }
    }

    public interface ProjectChangeListener extends ChangeListener<ProjectModel, ProjectChangeEvent> {
    }
}
