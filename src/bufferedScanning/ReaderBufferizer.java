package bufferedScanning;


import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.util.NoSuchElementException;
import java.util.function.IntPredicate;

public class ReaderBufferizer implements Closeable, AutoCloseable {
    private static final int defaultCharBufferSize = 4096;

    private final Reader in;
    private final char[] charBuffer;
    private int currentBufferSize = 0;
    private int charBufferPtr = 0;
//    private int lastBufferSizeIfAny;
//    private boolean hasBuffer;


    public ReaderBufferizer(Reader reader, int bufferSize) {
        this.in = reader;
        charBuffer = new char[bufferSize];
        // lastBufferSizeIfAny = -1;
        // hasBuffer = false;
    }

    public ReaderBufferizer(Reader reader) { this(reader, defaultCharBufferSize); }

    private void tryReadNewChunk() throws IOException {
        if (charBufferPtr >= currentBufferSize) {
            do {
                currentBufferSize = in.read(charBuffer); // Can be -1, 0 or something meaningful…
            } while(currentBufferSize == 0);
            charBufferPtr = 0;
        }
    }

    /**
     * @param requiredSupplement number of additional chars to add after current end
     * @return if this number of chars exist in the source
     */
    private boolean trySupplementBuffer(int requiredSupplement) throws IOException {
        if (currentBufferSize + requiredSupplement > charBuffer.length) {
            throw new AssertionError("Too big supplement");
        }

        int readNow = 0;
        while (readNow < requiredSupplement) {
            // Read blocks only if there are exactly 0 characters ready, so we just specify the number of characters left
            int readResult = in.read(charBuffer, currentBufferSize, charBuffer.length - currentBufferSize);
            if (readResult == -1) {
                return false;
            }
            readNow += readResult;
            currentBufferSize += readResult;
        }


        return true;
    }


    private int readCharactersLeft() {
        return currentBufferSize - charBufferPtr;
    }

    private void compressBuffer() {
        int readCharactersLeft = readCharactersLeft();
        ScanningUtils.copyForward(charBuffer, charBufferPtr, 0, readCharactersLeft);
        charBufferPtr = 0;
        currentBufferSize = readCharactersLeft;
    }

    public boolean hasNextChar() throws IOException {
        // If already have some characters, result definitely exists:
        if (charBufferPtr < currentBufferSize) {
            return true;
        }

        // Currently, buffer is exhausted but stream might be not.
        // The actual answer can't be known until some data arrives or stream is closed.
        tryReadNewChunk();

        // Current buffer size is either -1 (EOS => return false) or > 0 => return true
        return currentBufferSize != -1;
    }


    public char viewNext() throws IOException {
        if (!hasNextChar()) {
            throw new NoSuchElementException("[Bufferizer] EndOfStream: can't view next, there are no symbols to test");
        }
        return charBuffer[charBufferPtr];
    }


    public boolean testNext(IntPredicate predicate) throws IOException {
        return predicate.test(viewNext());
    }

    public boolean consumeIf(IntPredicate predicate) throws IOException {
        if (hasNextChar() && testNext(predicate)) {
            nextChar();
            return true;
        }
        return false;
    }

    public boolean hasNCharacters(int n) throws IOException {
        if (readCharactersLeft() >= n) {
            return true;
        } else if (n > charBuffer.length) {
            return false;
        }
        compressBuffer();

        return trySupplementBuffer(n - readCharactersLeft());
    }

    /**
     * If necessary: shifts characters left and reads if possible
     */
    public String viewNextN(int n) throws IOException {
        if (n > charBuffer.length) {
            throw new IllegalArgumentException("for ReaderBufferizer::viewNextN(int n) «n» can't be greater than buffer size");
        }
        if (!hasNCharacters(n)) {
            throw new IndexOutOfBoundsException("[ReaderBufferizer::viewNextN(int n)]: don't have n characters");
        }

        return String.valueOf(charBuffer, charBufferPtr, n);
    }


    public char nextChar() throws IOException {
        if (!hasNextChar()) {
            throw new NoSuchElementException("[Bufferizer] EndOfStream: can't get next char, there are no symbols to read");
        }

        return charBuffer[charBufferPtr++];
    }

    @Override
    public void close() throws IOException {
        in.close();
    }
}
