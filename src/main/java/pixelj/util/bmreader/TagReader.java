package pixelj.util.bmreader;

import java.util.ArrayList;
import java.util.HashMap;

// TODO: Maybe less type casting?
/**
 * Parse a single line of a BmFont config file.
 * (You can easily see that I've never written a parser before :D)
 */
public final class TagReader {

    private static final char END = '\0';
    private static final char COMMA = ',';
    private static final char DOUBLE_QUOTES = '"';
    private static final char MINUS = '-';
    private static final char EQUALS = '=';

    private int start;
    private int end;
    private final String input;
    private final ArrayList<Token> tokens = new ArrayList<>();

    public TagReader(final String input) throws ReaderException {
        this.input = input;
        scan();
    }

    /**
     * Extracts the tag type and key-value pairs from the tokenized line.
     *
     * @return Parsed tag
     */
    public Tag read() throws ReaderException {
        final var map = new HashMap<Ident, Value>();
        if (tokens.isEmpty()) {
            throw new ReaderException("Empty line");
        }
        final var tagType = getIdent(0);
        if (tagType == null) {
            throw new ReaderException("Missing tag name when reading\n" + input);
        }
        var index = 1;
        while (index < tokens.size()) {
            final var key = getIdent(index);
            if (key == null) {
                throw new ReaderException("Missing key when reading " + tagType.getText());
            }
            index++;
            if (!isEquals(index)) {
                throw new ReaderException("Missing assignment when reading " + tagType.getText());
            }
            index++;
            final var value = getValue(index);
            if (value == null) {
                throw new ReaderException("Missing value when reading " + tagType.getText());
            }
            if (value instanceof NumberList vec) {
                index += vec.values().size();
            } else {
                index++;
            }
            map.put(key, value);
        }
        return new Tag(tagType, map);
    }

    private void scan() throws ReaderException {
        // This is supposed to be a basic recursive descend parser.
        while (peek() != END) {
            final var current = peek();
            if (Character.isWhitespace(current)) {
                skipWhitespace();
            } else if (Character.isAlphabetic(current)) {
                ident();
            } else if (current == DOUBLE_QUOTES) {
                string();
            } else if (Character.isDigit(current) || current == MINUS) {
                numbers();
            } else if (current == EQUALS) {
                tokens.add(Equals.get());
                advance();
                start = end;
            }
        }
    }

    private void numbers() throws ReaderException {
        number();
        skipWhitespace();
        while (peek() == COMMA) {
            advance();
            start = end;
            number();
            skipWhitespace();
        }
    }

    private void number() throws ReaderException {
        if (peek() == MINUS) {
            advance();
        }
        while (Character.isDigit(peek())) {
            advance();
        }
        try {
            final var result = Integer.parseInt(input, start, end, 10);
            start = end;
            tokens.add(new Number(result));
        } catch (NumberFormatException exception) {
            throw new ReaderException(exception);
        }
    }

    private void string() throws ReaderException {
        advance();
        while (peek() != DOUBLE_QUOTES || peek() != END) {
            advance();
        }
        if (peek() == END) {
            throw new ReaderException("Unpaired double quotes");
        }
        advance();
        final var result = new Text(input.substring(start + 1, end - 1));
        start = end;
        tokens.add(result);
    }

    private void ident() throws ReaderException {
        while (Character.isAlphabetic(peek())) {
            advance();
        }
        final var string = input.substring(start, end);
        start = end;
        final var ident = Ident.fromString(string);
        if (ident == null) {
            throw new ReaderException("Invalid identifier name " + string);
        }
        tokens.add(ident);
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

    private Ident getIdent(final int index) {
        if (index >= tokens.size()) {
            return null;
        }
        if (tokens.get(index) instanceof Ident ident) {
            return ident;
        }
        return null;
    }

    private Value getValue(final int index) throws ReaderException {
        if (index >= tokens.size()) {
            return null;
        }
        final var current = tokens.get(index);
        if (current instanceof Number number) {
            if (getNumber(index + 1) != null) {
                final var numbers = getNumberList(index);
                if (numbers == null) {
                    // That shouldn't happen at all.
                    throw new ReaderException("Error reading numbers");
                }
                return numbers;
            }
            return number;
        }
        if (current instanceof Text text) {
            return text;
        }
        return null;
    }

    private Number getNumber(final int index) {
        if (index >= tokens.size()) {
            return null;
        }
        if (tokens.get(index) instanceof Number number) {
            return number;
        }
        return null;
    }

    private NumberList getNumberList(final int index) {
        if (index >= tokens.size()) {
            return null;
        }
        final var values = new ArrayList<Number>();
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
        if (values.size() == 0) {
            return null;
        }
        return new NumberList(values);
    }

    private boolean isEquals(final int index) {
        if (index >= tokens.size()) {
            return false;
        }
        return tokens.get(index).equals(Equals.get());
    }
}
