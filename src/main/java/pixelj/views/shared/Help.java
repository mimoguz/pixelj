package pixelj.views.shared;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public final class Help {

    // TODO: Replace with individual pages
    private static final String WIKI = "https://github.com/mimoguz/pixelj/wiki";

    private Help() {
    }

    /**
     * Open the requested help page.
     *
     * @param page
     */
    public static void showPage(final Page page) {
        try {
            Desktop.getDesktop().browse(page.getUri());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public enum Page {

        MAIN(WIKI),
        HOME(WIKI),
        DOCUMENT_SETTINGS(WIKI),
        EXPORT(WIKI),
        GLYPHS(WIKI),
        KERNING_PAIRS(WIKI),
        PREVIEW(WIKI);

        private URI uri;

        Page(final String uri) {
            try {
                this.uri = new URI(uri);
            } catch (URISyntaxException e) {
                // Ignore
            }
        }

        public URI getUri() {
            return uri;
        }
    }
}
