package pixelj.models;

public enum GeneralCategory {
    LU("Uppercase_Letter"),
    LL("Lowercase_Letter"),
    LT("Titlecase_Letter"),
    LM("Modifier_Letter"),
    LO("Other_Letter"),
    MN("Nonspacing_Mark"),
    MC("Spacing_Mark"),
    ME("Enclosing_Mark"),
    ND("Decimal_Number"),
    NL("Letter_Number"),
    NO("Other_Number"),
    PC("Connector_Punctuation"),
    PD("Dash_Punctuation"),
    PS("Open_Punctuation"),
    PE("Close_Punctuation"),
    PI("Initial_Punctuation"),
    PF("Final_Punctuation"),
    PO("Other_Punctuation"),
    SM("Math_Symbol"),
    SC("Currency_Symbol"),
    SK("Modifier_Symbol"),
    SO("Other_Symbol"),
    ZS("Space_Separator"),
    ZL("Line_Separator"),
    ZP("Paragraph_Separator"),
    CC("Control"),
    CF("Format"),
    CS("Surrogate"),
    CO("Private_Use"),
    CN("Unassigned");

    private final String name;

    GeneralCategory(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
