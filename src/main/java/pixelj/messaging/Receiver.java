package pixelj.messaging;

public interface Receiver<T> {
    void receive(T message);
}
