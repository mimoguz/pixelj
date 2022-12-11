package pixelj.messaging;

@SuppressWarnings("InstantiationOfUtilityClass")
public class DocumentSettingsQuestion {
    private static final DocumentSettingsQuestion INSTANCE = new DocumentSettingsQuestion();

    public static DocumentSettingsQuestion get() {
        return INSTANCE;
    }

    private DocumentSettingsQuestion() {}
}
