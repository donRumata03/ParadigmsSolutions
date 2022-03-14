package bufferedScanning;
import java.io.IOException;

public interface DelimiterConsumer {
    boolean consumeOneDelimiter(BufferedScanner bs) throws IOException;
}
