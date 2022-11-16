package pixelj.util;

public interface Receiver<T> {
    public Class<T> messageType();

    public void receive(T message);
}
