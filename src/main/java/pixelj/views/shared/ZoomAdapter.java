package pixelj.views.shared;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;

import javax.swing.JSlider;

import pixelj.util.MathUtils;

public class ZoomAdapter extends MouseAdapter {

    private final JSlider zoomSlider;

    public ZoomAdapter(final JSlider zoomSlider) {
        this.zoomSlider = zoomSlider;
    }

    /** Handles mouse wheel zoom. */
    @Override
    public void mouseWheelMoved(final MouseWheelEvent e) {
        if (!(zoomSlider.isEnabled() && e.isControlDown())) {
            return;
        }
        final var units = e.getWheelRotation();
        final var zoom = MathUtils.clamp(
            zoomSlider.getValue() - units,
            zoomSlider.getMinimum(), zoomSlider.getMaximum()
        );
        zoomSlider.setValue(zoom);
    }
};