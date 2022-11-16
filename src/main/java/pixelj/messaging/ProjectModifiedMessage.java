package pixelj.messaging;

import pixelj.models.Project;

public class ProjectModifiedMessage {
    private static final ProjectModifiedMessage INSTANCE = new ProjectModifiedMessage();

    public static ProjectModifiedMessage get() {
        return INSTANCE;
    }

    private ProjectModifiedMessage() {
    }
}
