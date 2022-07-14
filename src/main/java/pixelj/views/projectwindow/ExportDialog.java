package pixelj.views.projectwindow;

import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.DefaultComboBoxModel;
import javax.swing.SpinnerNumberModel;

public final class ExportDialog extends ExportDialogBase {

    private static final int MAXIMUM_SIZE = 4096;
    private static final int STEP_SIZE = 8;

    private Result result;

    public ExportDialog(final Frame owner, final Dimension defaultSize, final Dimension minimumSize) {
        super(owner);
        final var layouts = new DefaultComboBoxModel<LayoutStrategy>();
        layouts.addElement(LayoutStrategy.GRID_LAYOUT);
        layoutStrategyIn.setModel(layouts);
        layoutStrategyIn.setSelectedIndex(0);

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
