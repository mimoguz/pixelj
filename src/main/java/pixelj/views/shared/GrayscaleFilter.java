package pixelj.views.shared;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import javax.swing.*;
import java.awt.*;

public final class GrayscaleFilter extends FlatSVGIcon.ColorFilter {
    public GrayscaleFilter() {
        super((color -> {
            final var lum = (int)Math.floor(color.getRed() * 0.299 + color.getGreen() * 0.587 + color.getBlue() * 0.114);
            return new Color(Math.max(lum - 5, 0), Math.max(lum - 5, 0), Math.min(lum + 10, 255), color.getAlpha());
        }));
    }
}
