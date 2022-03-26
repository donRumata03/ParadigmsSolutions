package expression.parser.tests;

import bufferedScanning.ReaderBufferizer;
import expression.parser.generic.ArithmeticExpressionTokenizer;
import expression.parser.generic.ParsableSource;
import expression.parser.generic.TokenizationError;
import expression.parser.generic.tokens.ArithmeticExpressionToken;
import expression.parser.generic.tokens.NumberToken;
import expression.parser.generic.tokens.OperatorToken;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.Assert;
import org.junit.Test;

public class TokenizerTests {

    List<ArithmeticExpressionToken> getTokensFomTokenizer(ArithmeticExpressionTokenizer tok) throws IOException {
        List<ArithmeticExpressionToken> tokens = new ArrayList<>();
        while (true) {
            Random r = new Random(2342543);
            if (r.nextBoolean()) {
                for (int i = 0; i < r.nextInt(10); i++) {
                    tok.viewNextToken();
                }
            }

            var nt = tok.nextToken();
            if (nt.isPresent()) {
                tokens.add(nt.get());
            } else {
                break;
            }
        }

        return tokens;
    }

    ArithmeticExpressionTokenizer constructTokenizer(String str) {
        return new ArithmeticExpressionTokenizer(new ParsableSource(new ReaderBufferizer(new StringReader(str))));
    }

    List<ArithmeticExpressionToken> parseTokens(String str) throws IOException {
        return getTokensFomTokenizer(constructTokenizer(str));
    }



    @Test
    public void test() throws IOException {
//        String testCase = "   (       ---7 -xyj * l0 988 / t0 -14234 ) >>> 123134 x << 12 >> 9700000";
        String testCase = "(0 >> 0)";


        var tokens = parseTokens(testCase);

    }

    void assertTokenListsEqual(List<ArithmeticExpressionToken> l1, List<ArithmeticExpressionToken> l2) {
        Assert.assertEquals(l1.size(), l2.size());

        for (int i = 0; i < l1.size(); i++) {
            Assert.assertEquals(l1, l2);
        }
    }

    @Test
    public void testOneNumber() throws IOException {
        assertTokenListsEqual(List.of(new NumberToken("1")), parseTokens("1"));
        assertTokenListsEqual(List.of(OperatorToken.MINUS, new NumberToken("1")), parseTokens("-1"));
        assertTokenListsEqual(List.of(OperatorToken.MINUS, new NumberToken("0")), parseTokens("-0"));
        assertTokenListsEqual(List.of(new NumberToken("3324")), parseTokens("     3324     "));
//        var tokens = parseTokens("-1");
//        var tokens = parseTokens("-0");
//        var tokens = parseTokens("0");
    }


    record ModeResults( boolean withNotAllowed, boolean withAllowed ) {}

    public void testPreviewSequence(String s, List<ModeResults> results) throws IOException {
        Random r = new Random(2342543);

        for (int i = 0; i < 1000; i++) {
            ArithmeticExpressionTokenizer tok = constructTokenizer(s);

            for (int tokenIndex = 0; tokenIndex < results.size(); tokenIndex++) {
                // Maybe: preview
                if (r.nextBoolean()) {
                    var previews = r.nextInt(Math.max(1, r.nextInt(20)));
                    for (int previewIndex = 0; previewIndex < previews; previewIndex++) {
                        if (r.nextBoolean()) {
                            // Preview no spaces:
                            Assert.assertEquals(tok.viewNextToken(false).isPresent(), results.get(tokenIndex).withNotAllowed());
                        } else {
                            // Preview spaces:
                            Assert.assertEquals(tok.viewNextToken(true).isPresent(), results.get(tokenIndex).withAllowed());
                        }
                    }
                }

                tok.nextToken();
            }
        }
    }

    @Test
    public void allowOrDisallowSpaceSkipping() throws IOException {
        String testWithSpaces = "      -323";

        testPreviewSequence(testWithSpaces, List.of(new ModeResults(false, true), new ModeResults(true, true)));

//        ArithmeticExpressionTokenizer tok = constructTokenizer(testWithSpaces);
//        Assert.assertTrue(tok.viewNextToken(false).isEmpty());
    }

    @Test
    public void testWithInsertion() throws IOException {
        String testCase = "( ( ( ( 0 A ) ) ) + ( 0 ) )";
        Assert.assertThrows(TokenizationError.class, () -> parseTokens(testCase));
    }
}
