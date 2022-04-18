package io.github.mimoguz.pixelj.models;

import io.github.mimoguz.pixelj.util.ChangeListener;
import io.github.mimoguz.pixelj.util.Changeable;

import org.jetbrains.annotations.NotNull;

import javax.swing.event.EventListenerList;

public class ProjectModel
        implements Changeable<ProjectModel, ProjectModel.ProjectChangeEvent, ProjectModel.ProjectChangeListener> {
    private final CharacterListModel characters;
    private final KerningPairListModel kerningPairs;
    private final EventListenerList listeners = new EventListenerList();
    private Metrics metrics;
    private String title;

    public ProjectModel(
            final @NotNull String title,
            final @NotNull CharacterListModel characters,
            final @NotNull KerningPairListModel kerningPairs,
            final @NotNull Metrics metrics
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

    public void setMetrics(final @NotNull Metrics value) {
        metrics = value;
        fireChangeEvent(this, new ProjectChangeEvent.MetricsChanged(value));
    }

    public @NotNull String getTitle() {
        return title;
    }

    public void setTitle(final @NotNull String value) {
        title = value;
        fireChangeEvent(this, new ProjectChangeEvent.TitleChanged(value));
    }

    public sealed interface ProjectChangeEvent permits ProjectChangeEvent.MetricsChanged, ProjectChangeEvent.TitleChanged {
        record MetricsChanged(@NotNull Metrics metrics) implements ProjectChangeEvent {
        }

        record TitleChanged(@NotNull String title) implements ProjectChangeEvent {
        }
    }

    public interface ProjectChangeListener extends ChangeListener<ProjectModel, ProjectChangeEvent> {
    }
}
