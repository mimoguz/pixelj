package io.github.mimoguz.pixelj.models;

import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collection;
import java.util.function.Predicate;

@ParametersAreNonnullByDefault
public class KerningPairListModel extends DisplayListModel<KerningPairModel> {
    private @NotNull Predicate<KerningPairModel> leftFilter = model -> true;
    private @NotNull Predicate<KerningPairModel> rightFilter = model -> true;

    public KerningPairListModel() {
    }

    public KerningPairListModel(Collection<KerningPairModel> elements) {
        super(elements);
    }

    public void setLeftRange(int from, int to) {
        leftFilter = model -> {
            final var leftCodePoint = model.getLeft().getCodePoint();
            return leftCodePoint >= from && leftCodePoint <= to;
        };
        setFilter(model -> leftFilter.test(model) && rightFilter.test(model));
    }

    public void setRightRange(int from, int to) {
        rightFilter = model -> {
            final var rightCodePoint = model.getRight().getCodePoint();
            return rightCodePoint >= from && rightCodePoint <= to;
        };
        setFilter(model -> leftFilter.test(model) && rightFilter.test(model));
    }
}
