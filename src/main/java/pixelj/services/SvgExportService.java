package pixelj.services;

import pixelj.models.DocumentSettings;
import pixelj.models.Glyph;
import pixelj.models.KerningPair;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

public interface SvgExportService {
    void write(final Path outDir, final Collection<Glyph> glyphs, final DocumentSettings settings) throws IOException;

    void writeWithScript(
        final Path outDir,
        final Collection<Glyph> glyphs,
        final Collection<KerningPair> kerningPairs,
        final DocumentSettings settings,
        final String fontForgeScriptName
    ) throws IOException;
}
