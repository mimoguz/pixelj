package pixelj.util.bmreader2;

public sealed interface Token
    permits
    Token.Identifier,
    Token.Number,
    Token.Text,
    Token.Comma,
    Token.Equals,
    Token.Eol {

    record Identifier(String value) implements Token { }

    record Number(int value) implements Token { }

    record Text(String value) implements Token { }

    final class Equals implements Token {

        /** Instance. */
        public static final Equals INSTANCE = new Equals();

        private Equals() { }
    }

    final class Comma implements Token {

        /** Instance. */
        public static final Comma INSTANCE = new Comma();

        private Comma() { }
    }

    final class Eol implements Token {

        /** Instance. */
        public static final Eol INSTANCE = new Eol();

        private Eol() { }
    }
}
