package pixelj.util.bmreader2;

import java.util.ArrayList;
import java.util.List;

public final class Scanner {

    private static final char NIL = '\0';

    private final ArrayList<Token> tokens = new ArrayList<>();
    private final String source;

    private int mark;
    private int cursor;

    public Scanner(final String source) {
        this.source = source;
    }

    /**
     * @return Token list
     * @throws ReadError
     */
    public List<Token> scan() throws ReadError {
        tokens.clear();
        while (!eol()) {
            token();
            mark = cursor;
        }
        tokens.add(Token.Eol.INSTANCE);
        return tokens;
    }

    private void token() throws ReadError {
        final var current = step();
        switch (current) {
            case '"': string(); break;
            case '=': tokens.add(Token.Equals.INSTANCE); break;
            case ',': tokens.add(Token.Comma.INSTANCE); break;
            default:
                if (isDecimalDigit(current) || current == '-') {
                    number();
                } else if (isAsciiLetter(current)) {
                    identifier();
                } else if (Character.isWhitespace(current)) {
                    // Ignore.
                } else {
                    throw new ReadError("Unexpected character: '" + current + "'");
                }
        }
    }

    private char current() {
        return eol() ? NIL : source.charAt(cursor);
    }

    private boolean eol() {
        return cursor >= source.length();
    }

    private void identifier() {
        while (isAsciiLetter(current())) {
            step();
        }
        tokens.add(new Token.Identifier(source.substring(mark, cursor)));
    }

    private void number() throws ReadError {
        if (current() == '-') {
            step();
        }
        while (isDecimalDigit(current())) {
            step();
        }
        try {
            tokens.add(new Token.Number(Integer.parseInt(source, mark, cursor, 10)));
        } catch (NumberFormatException e) {
            throw new ReadError(source.substring(mark, cursor) + "is not an integer");
        }
    }

    private char step() {
        return eol() ? NIL : source.charAt(cursor++);
    }

    private void string() throws ReadError {
        while (current() != '"') {
            if (eol()) {
                throw new ReadError("Non-terminated string in \"" + source + '"');
            }
            step();
        }
        step();
        tokens.add(new Token.Text(source.substring(mark + 1, cursor - 1)));
    }

    private static boolean isDecimalDigit(final char c) {
        return c >= '0' && c <= '9';
    }

    private static boolean isAsciiLetter(final char c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
    }
}
