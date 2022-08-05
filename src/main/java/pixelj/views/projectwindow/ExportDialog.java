package pixelj.views.projectwindow;

import java.awt.Dimension;
import java.awt.Frame;
import java.util.Arrays;

import javax.swing.DefaultComboBoxModel;
import javax.swing.SpinnerNumberModel;

import pixelj.actions.ApplicationAction;
import pixelj.resources.Icons;
import pixelj.resources.Resources;
import pixelj.views.shared.Help;

public final class ExportDialog extends ExportDialogBase {

    private static final int MAXIMUM_SIZE = 4096;
    private static final int STEP_SIZE = 8;

    private Result result;

    public ExportDialog(
        final Frame owner,
        final Dimension defaultSize,
        final Dimension minimumSize,
        final LayoutStrategy strategy
    ) {
        super(owner);
        final var layouts = new DefaultComboBoxModel<LayoutStrategy>();
        layouts.addAll(Arrays.asList(LayoutStrategy.values()));
        layoutStrategyIn.setModel(layouts);
        layoutStrategyIn.setSelectedIndex(strategy.ordinal());

        widthIn.setModel(new SpinnerNumberModel(
            defaultSize.width,
            minimumSize.width,
            MAXIMUM_SIZE,
            STEP_SIZE
        ));

        heightIn.setModel(new SpinnerNumberModel(
            defaultSize.height,
            minimumSize.height,
            MAXIMUM_SIZE,
            STEP_SIZE
        ));

        exportButton.addActionListener(e -> {
            result = new Result(
                    ((SpinnerNumberModel) widthIn.getModel()).getNumber().intValue(),
                    ((SpinnerNumberModel) heightIn.getModel()).getNumber().intValue(),
                    (LayoutStrategy) layoutStrategyIn.getSelectedItem()
            );
            setVisible(false);
        });

        cancelButton.addActionListener(e -> setVisible(false));

        helpButton.setAction(
            new ApplicationAction("exportHelpAction", (e, action) -> Help.showPage(Help.Page.EXPORT))
                .setIcon(Icons.HELP)
                .setTooltip(Resources.get().getString("help"))
        );
    }

    public Result getResult() {
        return result;
    }

    @Override
    public void setVisible(final boolean visible) {
        if (visible) {
            result = null;
        }
        super.setVisible(visible);
    }

    public record Result(int width, int height, LayoutStrategy strategy) {
    }
}
