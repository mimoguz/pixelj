package pixelj.util.bmreader2;

import it.unimi.dsi.fastutil.ints.IntArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Parser {

    private final List<Token> input;
    private final Map<Word, Value> assignments = new HashMap<>();

    private int cursor;

    public Parser(final List<Token> input) {
        this.input = input;
    }

    /**
     * Parse a tag line and return key-value pairs.
     *
     * @return Parsed tag
     * @throws ReadError
     */
    public Result parse() throws ReadError {
        assignments.clear();
        return line();
    }

    private Result line() throws ReadError {
        final var title = identifier();
        if (title == null) {
            throw new ReadError("Missing tag name");
        }

        while (cursor < input.size() && !input.get(cursor).equals(Token.Eol.INSTANCE)) {
            assignment();
        }

        return new Result(title, assignments);
    }

    private boolean assignment() throws ReadError {
        final var identifier = identifier();
        if (identifier == null) {
            return false;
        }
        if (!match(Token.Equals.INSTANCE)) {
            throw new ReadError("Expected equals after the identifier " + identifier);
        }
        final var value = value();
        if (value == null) {
            throw new ReadError(identifier + " is missing its value");
        }
        assignments.put(identifier, value);
        return true;
    }

    private Value value() throws ReadError {
        final var text = text();
        return text == null ? numbers() : text;
    }

    private Value.Numbers numbers() throws ReadError {
        final var x0 = number();
        if (x0 == null) {
            return null;
        }

        final var result = new IntArrayList();
        result.add(x0.value());

        while (match(Token.Comma.INSTANCE)) {
            final var x = number();
            if (x == null) {
                throw new ReadError("Missing number after comma");
            }
            result.add(x.value());
        }

        return new Value.Numbers(result);
    }

    private Value.Text text() {
        if (input.get(cursor) instanceof Token.Text t) {
            cursor++;
            return new Value.Text(t.value());
        }
        return null;
    }

    private Token.Number number() {
        if (input.get(cursor) instanceof Token.Number n) {
            cursor++;
            return n;
        }
        return null;
    }

    private Word identifier() throws ReadError {
        if (input.get(cursor) instanceof Token.Identifier identifier) {
            cursor++;
            final var word = Word.fromString(identifier.value());
            if (word == null) {
                throw new ReadError("Unknown key " + identifier.value());
            }
            return word;
        }
        return null;
    }

    private boolean match(final Token token) {
        if (input.get(cursor).equals(token)) {
            cursor++;
            return true;
        }
        return false;
    }

    public record Result(Word title, Map<Word, Value> values) { }
}
