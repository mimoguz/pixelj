package pixelj.util.reader;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Parse a single line of a BmFont config file.
 * (You can easily see that I've never written a parser before :D)
 */
public final class LineReader {
    private static final char END = '\0';
    private static final char COMMA = ',';
    private static final char DOUBLE_QUOTES = '"';
    private static final char MINUS = '-';

    private int start;
    private int end;
    private final String input;
    private final ArrayList<BmToken> tokens = new ArrayList<>();

    public LineReader(final String input) {
        this.input = input;
        read();
    }

    /**
     * Extracts the key-value pairs.
     *
     * @return Read line
     */
    public BmLine build() {
        final var map = new HashMap<String, BmValue>();
        if (tokens.isEmpty()) {
            throw new IllegalArgumentException("Empty line");
        }
        final var line = getKeyword(0);
        if (line == null) {
            throw new IllegalArgumentException("No line name");
        }
        var index = 1;
        while (index < tokens.size()) {
            final var kw = getKeyword(index);
            if (kw == null) {
                throw new IllegalArgumentException("No field name");
            }
            index++;
            if (!isEquals(index)) {
                throw new IllegalArgumentException("Missing assignment");
            }
            index++;
            final var value = getValue(index);
            if (value == null) {
                throw new IllegalArgumentException("Missing value");
            }
            if (value instanceof BmVector vec) {
                index += vec.values().size();
            } else {
                index++;
            }
            map.put(kw.keyword().getText(), value);
        }
        return new BmLine(line.keyword(), map);
    }

    private void read() {
        while (peek() != END) {
            final var current = peek();
            if (Character.isWhitespace(current)) {
                skipWhitespace();
            } else if (Character.isAlphabetic(current)) {
                readToken();
            } else if (current == DOUBLE_QUOTES) {
                readString();
            } else if (Character.isDigit(current) || current == MINUS) {
                readNumbers();
            } else if (current == '=') {
                tokens.add(BmEquals.get());
                advance();
                start = end;
            }
        }
    }

    private void readNumbers() {
        readNumber();
        skipWhitespace();
        while (peek() == COMMA) {
            advance();
            start = end;
            readNumber();
            skipWhitespace();
        }
    }

    private void readString() {
        advance();
        while (peek() != DOUBLE_QUOTES) {
            advance();
        }
        if (peek() == END) {
            throw new IllegalArgumentException("Unpaired string");
        }
        advance();
        final var result = new BmString(input.substring(start + 1, end - 1));
        start = end;
        tokens.add(result);
    }

    private void readNumber() {
        if (peek() == MINUS) {
            advance();
        }
        while (Character.isDigit(peek())) {
            advance();
        }
        final var result = Integer.parseInt(input, start, end, 10);
        start = end;
        tokens.add(new BmNumber(result));
    }

    private void readToken() {
        while (Character.isAlphabetic(peek())) {
            advance();
        }
        final var string = input.substring(start, end);
        start = end;
        final var field = BmField.fromString(string);
        if (field == null) {
            throw new IllegalArgumentException("Invalid field name " + string);
        }
        tokens.add(new BmKeyword(field));
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

    private BmKeyword getKeyword(final int index) {
        if (index >= tokens.size()) {
            return null;
        }
        if (tokens.get(index) instanceof BmKeyword kw) {
            return kw;
        }
        return null;
    }

    private BmValue getValue(final int index) {
        if (index >= tokens.size()) {
            return null;
        }
        final var current = tokens.get(index);
        if (current instanceof BmString value) {
            return value;
        }
        if (current instanceof BmNumber) {
            final var vec = getVector(index);
            if (vec == null) {
                throw new IllegalArgumentException("Error reading numbers");
            } else if (vec.values().size() == 1) {
                return vec.values().get(0);
            }
            return vec;
        }
        return null;
    }

    private BmNumber getNumber(final int index) {
        if (index >= tokens.size()) {
            return null;
        }
        if (tokens.get(index) instanceof BmNumber value) {
            return value;
        }
        return null;
    }

    private BmVector getVector(final int index) {
        if (index >= tokens.size()) {
            return null;
        }
        final var values = new ArrayList<BmNumber>();
        var current = index;
        while (true) {
            final var number = getNumber(current);
            if (number != null) {
                values.add(number);
                current++;
            } else {
                break;
            }
        }
        return new BmVector(values);
    }

    private boolean isEquals(final int index) {
        if (index >= tokens.size()) {
            return false;
        }
        return tokens.get(index).equals(BmEquals.get());
    }
}
