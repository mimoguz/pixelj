package pixelj.util;

public interface Receiver {
    public Class messageType();

    public void receive(Object message);
}
