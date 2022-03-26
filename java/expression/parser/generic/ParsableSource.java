package expression.parser.generic;

import bufferedScanning.ReaderBufferizer;
import java.io.IOException;
import java.util.function.IntPredicate;

public class ParsableSource {
    private final ReaderBufferizer in;
    private int nextPos = 0;

    public ParsableSource(ReaderBufferizer in) {
        this.in = in;
    }

    public char consumeChar() throws IOException {
        nextPos++;
        return in.nextChar();
    }
    
    public char viewChar() throws IOException {
        return in.viewNext();
    }

    public boolean testNextChar(IntPredicate predicate) throws IOException {
        return in.testNext(predicate);
    }

    public boolean testNextCharIs(char forTest) throws IOException {
        return testNextChar(ch -> ch == forTest);
    }

    public boolean isEof() throws IOException {
        return !in.hasNextChar();
    }

    // Methods under this boundary don't directly work with «in» and «nextPos»


    public boolean consumeIf(IntPredicate predicate) throws IOException {
        if (testNextChar(predicate)) {
            consumeChar();
            return true;
        }
        return false;
    }

    public boolean consumeIfExpected(char expected) throws IOException {
        return consumeIf(ch -> ch == expected);
    }

    public boolean consumeWhitespace() throws IOException {
        boolean consumedSomething = false;
        while (!isEof() && consumeIf(Character::isWhitespace)) { consumedSomething = true; }
        return consumedSomething;
    }

    public boolean nextIsBetweenInclusive(final char from, final char to) throws IOException {
        return testNextChar(ch -> from <= ch && ch <= to );
    }

    public int getNextPos() {
        return nextPos;
    }

    public IllegalArgumentException generateUnexpectedCharacterError(char expected) throws IOException {
        return new IllegalArgumentException(
            String.format(
                "Unexpected character at index %d: „%c” (expected: „%c”). Note that nobody expects Spanish Inquisition!",
                nextPos, viewChar(), expected
            )
        );
    }

    public void expectChar(char expected) throws IOException {
        if (!consumeIfExpected(expected)) {
            throw generateUnexpectedCharacterError(expected);
        }
    }

    public void expectString(String expectedString) throws IOException {
        for (final char c : expectedString.toCharArray()) {
            expectChar(c);
        }
    }

    public boolean expectStringIfFirstCharacterMatches(String expectedString) throws IOException {
        if (testNextCharIs(expectedString.charAt(0))) {
            int problematicShift = 0;
            while (problematicShift < expectedString.length() && consumeIfExpected(expectedString.charAt(problematicShift))) {
                problematicShift++;
            }
            if (problematicShift < expectedString.length()) {
                // Expected string is started, but it doesn't match ==> Throw exception
                throw new IllegalArgumentException(String.format(
                    "String „%s” is detected to start but input doesn't match is: „…%s…”. Problematic position is: %d",
                    expectedString, expectedString.substring(0, problematicShift) + viewChar(), nextPos
                ));
            }

            return true;
        }
        return false;
    }

    public boolean hasNChars(int n) throws IOException {
        return in.hasNCharacters(n);
    }

    public String viewNChars(int n) throws IOException {
        return in.viewNextN(n);
    }

    public int consumeCharsLeft() throws IOException {
        int consumed = 0;
        while (true) {
            if (isEof()) {
                break;
            }
            consumeChar();
            consumed++;
        }

        return consumed;
    }
}
