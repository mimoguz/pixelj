package pixelj.services;

import java.util.List;

import pixelj.models.Project;
import pixelj.util.packer.GridPacker;
import pixelj.util.packer.Rectangle;

public final class GridExportService extends AbstractExportService {
    @Override
    protected List<List<Rectangle>> pack(
            final Project project,
            final int textureWidth,
            final int textureHeight
    ) {
        final var settings = project.getDocumentSettings();
        final var innerHeight = settings.ascender() + settings.descender();
        final var height = innerHeight + 1;
        final var rectangles = project.getGlyphs().getElements().stream().map(glyph -> {
            final var innerWidth = settings.isMonospaced()
                    ? Math.min(glyph.getWidth(), settings.defaultWidth())
                    : glyph.getWidth();
            return new Rectangle(glyph.getCodePoint(), innerWidth + 1, height, innerWidth, innerHeight);
        }).toList();
        return new GridPacker().pack(rectangles, textureWidth, textureHeight);
    }
}
