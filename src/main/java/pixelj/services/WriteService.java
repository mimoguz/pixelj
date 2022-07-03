package pixelj.services;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import pixelj.models.CompressedGlyph;
import pixelj.models.KerningPairRecord;
import pixelj.models.DocumentSettings;
import pixelj.models.Project;
import pixelj.services.Queries.GlyphsColumn;
import pixelj.services.Queries.KerningPairsColumn;
import pixelj.services.Queries.SettingsColumn;
import pixelj.services.Queries.TitleColumn;

final class WriteService {
    public static final int VERSION = 1;

    public static void save(final Path path, final Project project) throws IOException {
        List<CompressedGlyph> glyphs;
        List<KerningPairRecord> kerningPairs;
        String title;
        DocumentSettings settings;

        synchronized (project) {
            title = project.getTitle();
            settings = project.getDocumentSettings();
            glyphs = project.getGlyphs().getElements().parallelStream().map(CompressedGlyph::from)
                    .toList();
            kerningPairs = project.getKerningPairs().getElements().stream()
                    .map(KerningPairRecord::from).toList();
        }

        final var pathStr = path.toAbsolutePath().toString();
        final var url = pathStr.endsWith("." + Queries.EXTENSION)
                ? pathStr.substring(0, pathStr.length() - Queries.EXTENSION.length() - 1)
                : pathStr;

        try (var connection =
                DriverManager.getConnection(Queries.URL_PREFIX + url, Queries.PIXELJ, "")) {
            connection.setAutoCommit(false);

            final var statement = connection.createStatement();

            statement.executeUpdate(Queries.DROP_PROJECT_TABLE);
            statement.executeUpdate(Queries.CREATE_PROJECT_TABLE);
            final var insertTitle = connection.prepareStatement(Queries.INSERT_PROJECT);
            insertTitle.setString(TitleColumn.TITLE.getIndex(), title);
            insertTitle.setInt(TitleColumn.SAVE_VERSION.getIndex(), VERSION);
            insertTitle.executeUpdate();

            statement.executeUpdate(Queries.DROP_GLYPHS_TABLE);
            statement.executeUpdate(Queries.CREATE_GLYPHS_TABLE_QUERY);
            final var insertGlyph = connection.prepareStatement(Queries.INSERT_GLYPH);
            for (var glyph : glyphs) {
                insertGlyph.setInt(GlyphsColumn.CODE_POINT.getIndex(), glyph.codePoint());
                insertGlyph.setInt(GlyphsColumn.WIDTH.getIndex(), glyph.width());
                insertGlyph.setBytes(GlyphsColumn.IMAGE_BYTES.getIndex(), glyph.imageBytes());
                insertGlyph.executeUpdate();
            }

            statement.executeUpdate(Queries.DROP_K_PAIRS_TABLE);
            statement.executeUpdate(Queries.CREATE_KERNING_PAIRS_TABLE);
            final var insertKerningPair = connection.prepareStatement(Queries.INSERT_KERNING_PAIR);
            for (var pair : kerningPairs) {
                insertKerningPair.setInt(KerningPairsColumn.ID.getIndex(), pair.id());
                insertKerningPair.setInt(KerningPairsColumn.LEFT_CODE_POINT.getIndex(),
                        pair.left());
                insertKerningPair.setInt(KerningPairsColumn.RIGHT_CODE_POINT.getIndex(),
                        pair.right());
                insertKerningPair.setInt(KerningPairsColumn.KERNING_VALUE.getIndex(), pair.value());
                insertKerningPair.executeUpdate();
            }

            statement.executeUpdate(Queries.DROP_SETTINGS_TABLE);
            statement.executeUpdate(Queries.CREATE_SETTINGS_TABLE);
            final var insertSettings = connection.prepareStatement(Queries.INSERT_SETTINGS);
            insertSettings.setInt(SettingsColumn.CANVAS_WIDTH.getIndex(), settings.canvasWidth());
            insertSettings.setInt(SettingsColumn.CANVAS_HEIGHT.getIndex(), settings.canvasHeight());
            insertSettings.setInt(SettingsColumn.ASCENDER.getIndex(), settings.ascender());
            insertSettings.setInt(SettingsColumn.DESCENDER.getIndex(), settings.descender());
            insertSettings.setInt(SettingsColumn.CAP_HEIGHT.getIndex(), settings.capHeight());
            insertSettings.setInt(SettingsColumn.X_HEIGHT.getIndex(), settings.xHeight());
            insertSettings.setInt(SettingsColumn.DEFAULT_WIDTH.getIndex(), settings.defaultWidth());
            insertSettings.setInt(SettingsColumn.LETTER_SPACING.getIndex(),
                    settings.letterSpacing());
            insertSettings.setInt(SettingsColumn.SPACE_SIZE.getIndex(), settings.spaceSize());
            insertSettings.setInt(SettingsColumn.LINE_SPACING.getIndex(), settings.lineSpacing());
            insertSettings.setBoolean(SettingsColumn.IS_MONOSPACED.getIndex(),
                    settings.isMonospaced());
            insertSettings.setBoolean(SettingsColumn.IS_BOLD.getIndex(), settings.isBold());
            insertSettings.setBoolean(SettingsColumn.IS_ITALIC.getIndex(), settings.isMonospaced());
            insertSettings.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            // TODO: Move the message to the resources.
            throw new IOException(
                    String.format("Can't save file %s:\n%s", pathStr, e.getMessage()));
        }
    }

    private WriteService() {}
}
