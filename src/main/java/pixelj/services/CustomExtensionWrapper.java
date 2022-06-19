package pixelj.services;

import org.h2.store.fs.FilePath;
import org.h2.store.fs.FilePathWrapper;

/**
 * <a href=https://stackoverflow.com/a/33293800>Stackoverflow answer</a>
 */
public class CustomExtensionWrapper extends FilePathWrapper {
    private static final String MV_DB = ".mv.db";
    private static final String EXTENSION = "." + Queries.EXTENSION;
    private static final String SCHEME = Queries.PIXELJ;

    @Override
    public String getScheme() {
        return SCHEME;
    }

    @Override
    protected FilePath unwrap(final String path) {
        final var pathStr = path.substring(getScheme().length() + 1);
        final var withNewExtension = switchExtension(pathStr, MV_DB, EXTENSION);
        return FilePath.get(withNewExtension);
    }

    @Override
    public FilePathWrapper wrap(final FilePath path) {
        final var wrapper = (CustomExtensionWrapper) super.wrap(path);
        wrapper.name = getPrefix() + switchExtension(path.toString(), EXTENSION, MV_DB);
        return wrapper;
    }

    private String switchExtension(final String fileName, final String oldExt, final String newExt) {
        if (fileName.endsWith(oldExt)) {
            return fileName.substring(0, fileName.length() - oldExt.length()) + newExt;
        }
        return fileName;
    }
}
