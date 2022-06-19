package pixelj.services;

import java.nio.file.Path;
import java.util.List;

import org.h2.store.fs.FilePath;

import pixelj.models.CompressedGlyph;
import pixelj.models.KerningPairRecord;
import pixelj.models.Project;

public class FileService {
    public static final String EXTENSION = Queries.EXTENSION;

    static {
        final var wrapper = new CustomExtensionWrapper();
        FilePath.register(wrapper);
    }

    public static boolean writeFile(Project project, Path path) {
        List<CompressedGlyph> glyphs;
        List<KerningPairRecord> kerningPairs;
        String title;
        final var metrics = project.getMetrics();

        synchronized (project) {
            title = project.getTitle();
            glyphs = project.getGlyphs().getElements().parallelStream().map(CompressedGlyph::from).toList();
            kerningPairs = project.getKerningPairs()
                    .getElements()
                    .stream()
                    .map(KerningPairRecord::from)
                    .toList();
        }

        try {
            final var worker = new SaveWorker(path, glyphs, kerningPairs, metrics, title);
            worker.addPropertyChangeListener(e -> {
                if (e.getPropertyName() == "progress") {
                    switch (worker.getProgress()) {
                        case 100 -> System.out.println("Done");
                        case -1 -> System.err.println("Save failed! Path: " + path);
                    }
                }
            });
            worker.doInBackground();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static Project loadFile(Path path) {
        return null;
    }
}
