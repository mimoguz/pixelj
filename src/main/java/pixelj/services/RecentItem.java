package pixelj.services;

import java.nio.file.Path;
import java.time.OffsetDateTime;

public record RecentItem(String title, Path path, OffsetDateTime lastOpened) {

    @Override
    public boolean equals(final Object that) {
        if (this == that) {
            return true;
        }
        if (that instanceof RecentItem other) {
            return title.equals(other.title)
                && path.equals(other.path)
                && lastOpened.toInstant().compareTo(other.lastOpened.toInstant()) == 0;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (title == null ? 0 : title.hashCode());
        hash = 31 * hash + (path == null ? 0 : path.hashCode());
        hash = 31 * hash + (lastOpened == null ? 0 : lastOpened.toInstant().hashCode());
        return hash;
    }
}
