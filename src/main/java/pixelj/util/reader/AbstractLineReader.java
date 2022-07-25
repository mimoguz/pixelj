package pixelj.util.reader;

import org.eclipse.collections.api.list.primitive.IntList;
import org.eclipse.collections.impl.list.mutable.primitive.IntArrayList;

/**
 * Abstract LineReader2 implementation with convenience methods.
 *
 * @param <T> Resulting line type
*/
public abstract class AbstractLineReader<T extends Line> implements LineReader2<T> {

    private static final char END = '\0';
    private static final char COMMA = ',';
    private static final char DOUBLE_QUOTES = '"';
    private static final char MINUS = '-';
    private static final char EQUALS = '=';

    private final String input;
    private int start;
    private int end;

    public AbstractLineReader(final String input) {
        this.input = input;
    }

    protected final IntList readNumbers() throws ReaderException {
        final var result = new IntArrayList();
        result.add(readNumber());
        skipWhitespace();
        while (peek() == COMMA) {
            advance();
            start = end;
            result.add(readNumber());
            skipWhitespace();
        }
        return result;
    }

    protected final int readNumber() throws ReaderException {
        if (peek() == MINUS) {
            advance();
        }
        while (Character.isDigit(peek())) {
            advance();
        }
        try {
            final var result = Integer.parseInt(input, start, end, 10);
            start = end;
            return result;
        } catch (NumberFormatException exception) {
            throw new ReaderException(exception);
        }

    }

    protected final String readString() throws ReaderException {
        if (peek() != DOUBLE_QUOTES) {
            throw new ReaderException("Error reading string: Expected double quotes");
        }
        advance();
        while (peek() != DOUBLE_QUOTES) {
            advance();
        }
        if (peek() == END) {
            throw new ReaderException("Error reading string: Unpaired double quotes");
        }
        advance();
        final var result = input.substring(start + 1, end - 1);
        start = end;
        return result;
    }

    protected final String readKey() {
        while (Character.isAlphabetic(peek())) {
            advance();
        }
        final var result = input.substring(start, end);
        start = end;
        return result;
    }

    protected final boolean checkEquals() {
        skipWhitespace();
        final var result = peek() == EQUALS;
        skipWhitespace();
        start = end;
        return result;
    }

    private void advance() {
        if (end >= input.length()) {
            return;
        }
        end++;
    }

    private char peek() {
        if (end >= input.length()) {
            return END;
        }
        return input.charAt(end);
    }

    private void skipWhitespace() {
        while (Character.isWhitespace(peek())) {
            advance();
        }
        start = end;
    }
}
