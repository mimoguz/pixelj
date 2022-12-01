package pixelj.messaging;

public record CopyFromMessage(int source, int[] targets) {
}
