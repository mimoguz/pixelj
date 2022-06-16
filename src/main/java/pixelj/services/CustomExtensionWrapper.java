package pixelj.services;

import org.h2.store.fs.FilePath;
import org.h2.store.fs.FilePathWrapper;

/**
 * <a href=https://stackoverflow.com/a/33293800>Stackoverflow answer</a>
 */
public class CustomExtensionWrapper extends FilePathWrapper {
    private static final String H2_DB = ".h2.db";
    private static final String LOCK_DB = ".lock.db";

    private final String scheme;
    private final String extension;
    private final String lock;

    public CustomExtensionWrapper(final String schemeName, final String extension) {
        scheme = schemeName;
        this.extension = extension.startsWith(".") ? extension : "." + extension;
        lock = this.extension + ".lock";
    }

    @Override
    public String getScheme() {
        return scheme;
    }

    @Override
    protected FilePath unwrap(final String path) {
        final var name = path.substring(getScheme().length() + 1);
        final var withNewExtension = switchExtension(name, H2_DB, extension, LOCK_DB, lock);
        return FilePath.get(withNewExtension);
    }

    @Override
    public FilePathWrapper wrap(final FilePath path) {
        final var wrapper = (CustomExtensionWrapper) super.wrap(path);
        wrapper.name = getPrefix() + switchExtension(path.toString(), extension, H2_DB, lock, LOCK_DB);
        return wrapper;
    }

    private String switchExtension(
            final String fileName,
            final String oldExt,
            final String newExt,
            final String oldLock,
            final String newLock
    ) {
        if (fileName.endsWith(H2_DB)) {
            return replaceExtension(fileName, oldExt, newExt);
        } else if (fileName.endsWith(LOCK_DB)) {
            return replaceExtension(fileName, oldLock, newLock);
        }
        return fileName;
    }

    private static String replaceExtension(final String fileName, final String oldExt, final String newExt) {
        return fileName.substring(0, fileName.length() - oldExt.length()) + newExt;
    }

}
