package pixelj.views.controls;

import pixelj.resources.Icon;
import pixelj.resources.Resources;
import pixelj.views.shared.Borders;
import pixelj.views.shared.Components;
import pixelj.views.shared.Dimensions;

import javax.swing.*;
import java.awt.*;

public final class ZoomStrip extends JPanel {
    private final JSlider slider;
    private final JLabel zoomOut = new JLabel();
    private final JLabel zoomIn = new JLabel();

    public ZoomStrip(final int minimum, final int maximum, final int value) {
        slider = new JSlider(minimum, maximum, value);

        final var res = Resources.get();

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(Borders.SMALL_EMPTY_CUP_CENTER);

        add(Box.createHorizontalGlue());

        Components.setFixedSize(zoomOut, new Dimension(Dimensions.ICON_SIZE, Dimensions.ICON_SIZE));
        zoomOut.setIcon(res.getThemeIcon(Icon.ZOOM_OUT));
        add(zoomOut);

        slider.setMinimumSize(new Dimension(96, 24));
        slider.setMaximumSize(new Dimension(256, 24));
        add(slider);

        Components.setFixedSize(zoomIn, new Dimension(Dimensions.ICON_SIZE, Dimensions.ICON_SIZE));
        zoomIn.setIcon(res.getThemeIcon(Icon.ZOOM_IN));
        add(zoomIn);

        add(Box.createHorizontalGlue());
    }

    public JSlider getSlider() {
        return slider;
    }

    @Override
    public void setEnabled(final boolean enabled) {
        super.setEnabled(enabled);
        zoomIn.setEnabled(enabled);
        zoomOut.setEnabled(enabled);
        slider.setEnabled(enabled);
    }
}
