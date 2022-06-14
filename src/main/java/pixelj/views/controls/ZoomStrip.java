package pixelj.views.controls;

import pixelj.resources.Icons;
import pixelj.resources.Resources;
import pixelj.views.shared.Borders;
import pixelj.views.shared.Components;

import javax.swing.*;
import java.awt.*;

public class ZoomStrip extends JPanel {
    private final JSlider slider;
    private final JLabel zoomOut = new JLabel();
    private final JLabel zoomIn = new JLabel();

    public ZoomStrip(final int minimum, final int maximum, final int value) {
        slider = new JSlider(minimum, maximum, value);

        final var res = Resources.get();

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(Borders.SMALL_EMPTY_CUP_CENTER);

        add(Box.createHorizontalGlue());

        Components.setFixedSize(zoomOut, new Dimension(16, 16));
        zoomOut.setIcon(res.getIcon(Icons.ZOOM_OUT, res.colors.icon(), res.colors.disabledIcon()));
        add(zoomOut);

        slider.setMinimumSize(new Dimension(96, 24));
        slider.setMaximumSize(new Dimension(256, 24));
        add(slider);

        Components.setFixedSize(zoomIn, new Dimension(16, 16));
        zoomIn.setIcon(res.getIcon(Icons.ZOOM_IN, res.colors.icon(), res.colors.disabledIcon()));
        add(zoomIn);

        add(Box.createHorizontalGlue());
    }

    public JSlider getSlider() {
        return slider;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        zoomIn.setEnabled(enabled);
        zoomOut.setEnabled(enabled);
        slider.setEnabled(enabled);
    }
}
