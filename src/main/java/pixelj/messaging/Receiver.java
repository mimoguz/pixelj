package pixelj.messaging;

public interface Receiver<M, R> {
    R receive(M message);
}
