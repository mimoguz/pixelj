package pixelj.services;

public record FontMetadata(
    String fontName,
    String familyName,
    String weight,
    String version,
    String copyright,
    String comments
) {
    public FontMetadata() {
        this(null, null, null, null, null, null);
    }
}
