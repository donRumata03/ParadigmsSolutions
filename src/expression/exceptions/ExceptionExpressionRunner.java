package expression.exceptions;

import expression.TripleExpression;
import expression.general.exceptions.IntegerArithmeticException;
import expression.parser.generic.ParseException;
import expression.parser.generic.TokenizationError;
import java.util.Random;

public class ExceptionExpressionRunner {

    public static void main(String[] args) {
        String sampleForParsing = "y - (8 * y + x ((";

        String forParsing = args.length == 0 ? sampleForParsing : args[0];
        Random gen = new Random();
        int sampleX = gen.nextInt(10);
        int sampleY = gen.nextInt(10);
        int sampleZ = gen.nextInt(20);

        System.out.printf(
            "Trying to parse expression %s and compute it for arguments: x=%d, y=%d, z=%d%n",
            forParsing, sampleX, sampleY, sampleZ
        );

        var parser = new ExpressionParser();
        try {
            TripleExpression parsed = parser.parse(forParsing);
            System.out.println(parsed.evaluate(sampleX, sampleY, sampleZ));
        } catch (TokenizationError e) {
            System.err.println("Error while tokenization occurred: " + e.getMessage());
        } catch (ParseException e) {
            System.err.println("Syntax error while parsing occurred: " + e.getMessage());
        } catch (IntegerArithmeticException e) {
            System.err.println("Arithmetic exception occurred while trying to compute: " + e.getMessage());
        }


    }
}
