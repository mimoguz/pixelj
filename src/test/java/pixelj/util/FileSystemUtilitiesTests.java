package pixelj.util;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

public class FileSystemUtilitiesTests {

    /** Print config directory. */
    @Test
    public void configPath() {
        assertDoesNotThrow(() -> {
            final var osPair = FileSystemUtilities.getConfigDirectory();
            System.out.println("OS: " + osPair.os() + " Path: " + osPair.value());
        });
    }
}
