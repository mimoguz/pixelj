package pixelj.graphics;

@SuppressWarnings("java:S6218")
public record Snapshot(int id, int x, int y, int width, int height, byte[] data) {
}
