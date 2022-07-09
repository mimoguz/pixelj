package pixelj.views.glyphs_screen;

import java.awt.Font;

import javax.swing.SpinnerNumberModel;

import pixelj.models.DocumentSettings;
import pixelj.models.Glyph;
import pixelj.models.Project;
import pixelj.resources.Resources;
import pixelj.util.ChangeableBoolean;
import pixelj.util.ReadOnlyBoolean;

public final class InfoPanel extends InfoPanelBase {

    protected final ChangeableBoolean gridVisible = new ChangeableBoolean(true);
    protected final ChangeableBoolean guidesVisible = new ChangeableBoolean(true);
    /**
     * Show/hide grid.
     */
    public final ReadOnlyBoolean gridVisibleProperty = new ReadOnlyBoolean(gridVisible);
    /**
     * Show/hide guides.
     */
    public final ReadOnlyBoolean guidesVisibleProperty = new ReadOnlyBoolean(guidesVisible);

    private Glyph model;

    public InfoPanel(final Project project) {
        showGridCheckBox.addItemListener(e -> gridVisible.setValue(showGridCheckBox.isSelected()));
        showGridCheckBox.setSelected(true);

        showGuidesCheckBox.addItemListener(e -> guidesVisible.setValue(showGuidesCheckBox.isSelected()));
        showGuidesCheckBox.setSelected(true);
        
        widthSpinner.addChangeListener(e -> {
            if (model != null && widthSpinner.getModel() instanceof final SpinnerNumberModel numberModel) {
                final var value = numberModel.getNumber().intValue();
                if (value != model.getWidth()) {
                    model.setWidth(value);
                }
            }
        });

        setMetrics(project.getDocumentSettings());
        project.documentSettingsProperty.addChangeListener((source, settings) -> setMetrics(settings));

        setModel(null);
    }

    /**
     * @return Current glyph. May be null.
     */
    public Glyph getModel() {
        return model;
    }

    /**
     * @param value Current glyph. May be null.
     */
    public void setModel(final Glyph value) {
        this.model = value;
        if (value != null) {
            final var res = Resources.get();
            final var scalar = res.getScalar(value.getCodePoint());
            final var name = String.format(
                    "<html><body style=\"text-align: left; \">%s</body></html>", scalar.name()
            );
            nameLabel.setText(name);
            codePointLabel.setText(
                    res.formatString("codePointLabel", Integer.toHexString(scalar.codePoint()))
            );
            blockNameLabel.setText(
                    res.formatString("blockNameLabel", res.getBlockData(scalar.blockId()).name())
            );
            glyphLabel.setText(Character.toString((char) model.getCodePoint()));
            if (widthSpinner.getModel() instanceof final SpinnerNumberModel numberModel) {
                numberModel.setValue(value.getWidth());
                widthSpinner.setEnabled(true);
                widthLabel.setEnabled(true);
            }
        } else {
            nameLabel.setText(BLANK);
            codePointLabel.setText(BLANK);
            blockNameLabel.setText(BLANK);
            glyphLabel.setText(BLANK);
            widthSpinner.setEnabled(false);
            widthLabel.setEnabled(false);
        }
    }

    private void setMetrics(final DocumentSettings settings) {
        widthSpinner.setModel(
                new SpinnerNumberModel(
                        model != null ? model.getWidth() : settings.defaultWidth(),
                        0,
                        settings.canvasWidth(),
                        1
                )
        );
        setGlyphLabelFont(settings);
    }

    private void setGlyphLabelFont(final DocumentSettings settings) {
        final var style = settings.isBold()
                ? (settings.isItalic() ? Font.BOLD | Font.ITALIC : Font.BOLD)
                : (settings.isItalic() ? Font.ITALIC : Font.PLAIN);
        glyphLabel.setFont(glyphLabel.getFont().deriveFont(style, GLYPH_LABEL_SIZE));
    }
}
