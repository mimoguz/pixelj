package pixelj.views.shared;

import java.awt.Frame;

public final class OptionsDialog extends OptionsDialogBase {

    private Boolean result;

    public OptionsDialog(final Frame owner) {
        super(owner);
        saveButton.addActionListener(e -> {
            result = themeIn.isSelected();
            setVisible(false);
        });
        cancelButton.addActionListener(e -> setVisible(false));
    }

    public void setDarkTheme(final boolean useDarkTheme) {
        themeIn.setSelected(useDarkTheme);
    }

    public Boolean getResult() {
        return result;
    }

    @Override
    public void setVisible(final boolean visible) {
        if (visible) {
            result = null;
        }
        super.setVisible(visible);
    }
}
