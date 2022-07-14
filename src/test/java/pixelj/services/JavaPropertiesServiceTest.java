package pixelj.services;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JavaPropertiesServiceTest {

    /** Serialize then deserialize a list of recent items. */
    @Test
    public void recentItems() {
        final var items = List.of(
            new RecentItem("Item 1", System.getProperty("user.home")),
            new RecentItem("Item 2", "Path 2")
        );
        final var objectMapper = new ObjectMapper();
        try {
            final var json = objectMapper.writeValueAsString(items);
            final var itemsBack = JavaPropertiesService.parseRecentItems(json).stream().toArray();
            assertArrayEquals(items.toArray(), itemsBack);
        } catch (JsonProcessingException e) {
            throw new AssertionFailedError(e.getMessage());
        }
    }

    /** Serialize then deserialize an empty list of recent items. */
    @Test
    public void noRecentItems() {
        final var items = List.of();
        final var objectMapper = new ObjectMapper();
        try {
            final var json = objectMapper.writeValueAsString(items);
            final var itemsBack = JavaPropertiesService.parseRecentItems(json).stream().toArray();
            assertArrayEquals(items.toArray(), itemsBack);
        } catch (JsonProcessingException e) {
            throw new AssertionFailedError(e.getMessage());
        }
    }

    /** Try deserializing an illegal string. */
    @Test
    public void noEntry() {
        assertThrows(
                JsonProcessingException.class,
                () -> JavaPropertiesService.parseRecentItems("*./")
        );
    }
}
