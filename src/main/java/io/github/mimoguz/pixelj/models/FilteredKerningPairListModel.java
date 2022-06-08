package io.github.mimoguz.pixelj.models;

import java.util.function.Predicate;

public class FilteredKerningPairListModel extends FilteredListModel<KerningPairModel> {
    private Predicate<KerningPairModel> leftFilter = model -> true;
    private Predicate<KerningPairModel> rightFilter = model -> true;

    public FilteredKerningPairListModel(final HashListModel<KerningPairModel> delegate) {
        super(delegate);
    }

    public void setLeftRange(final int from, final int to) {
        leftFilter = model -> {
            final var leftCodePoint = model.getLeft().getCodePoint();
            return leftCodePoint >= from && leftCodePoint <= to;
        };
        setFilter(model -> leftFilter.test(model) && rightFilter.test(model));
    }

    public void setRightRange(final int from, final int to) {
        rightFilter = model -> {
            final var rightCodePoint = model.getRight().getCodePoint();
            return rightCodePoint >= from && rightCodePoint <= to;
        };
        setFilter(model -> leftFilter.test(model) && rightFilter.test(model));
    }
}
