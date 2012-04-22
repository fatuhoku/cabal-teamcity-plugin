import java.util.regex.Pattern;

import static org.testng.Assert.assertTrue;

/**
 * [created by: H.Poon on: 09/04/2012 at: 22:49]
 */
public class Asserts {
    public static void assertMatches(Pattern pattern, String s) {
        assertTrue(pattern.matcher(s).matches(),
                String.format("The string \"%s\"\ndidn't match pattern \"%s\".", s, pattern.toString()));
    }
}
