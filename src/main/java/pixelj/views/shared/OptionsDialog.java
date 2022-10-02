package pixelj.views.shared;

import java.awt.Frame;
import java.awt.event.KeyEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import pixelj.services.AppState;

public final class OptionsDialog extends OptionsDialogBase {

    private Result result;

    public OptionsDialog(final Frame owner) {
        super(owner);

        colorThemeIn.setModel(new DefaultComboBoxModel<>(AppState.ColorTheme.values()));
        colorThemeIn.setSelectedIndex(0);
        
        iconThemeIn.setModel(new DefaultComboBoxModel<>(AppState.IconTheme.values()));
        iconThemeIn.setSelectedIndex(0);

        saveButton.addActionListener(e -> {
            result = new Result(
                    AppState.ColorTheme.values()[colorThemeIn.getSelectedIndex()],
                    AppState.IconTheme.values()[iconThemeIn.getSelectedIndex()]
            );
            setVisible(false);
        });

        cancelButton.addActionListener(e -> setVisible(false));

        getRootPane().registerKeyboardAction(
            (e) -> setVisible(false),
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );
    }

    public void setTheme(final AppState.ColorTheme colorTheme, final AppState.IconTheme iconTheme) {
        colorThemeIn.setSelectedIndex(colorTheme.ordinal());
        iconThemeIn.setSelectedIndex(iconTheme.ordinal());
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
    
    public record Result(AppState.ColorTheme colorTheme, AppState.IconTheme iconTheme) { }
}
