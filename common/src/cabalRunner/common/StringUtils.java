package cabalRunner.common;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: H.Poon
 * Date: 08/04/2012
 * Time: 20:09
 * To change this template use File | Settings | File Templates.
 */
public class StringUtils {
    public static String join(List<String> tokens, String sep) {
        StringBuilder result = new StringBuilder();
        if (!tokens.isEmpty()) {
            result.append(tokens.get(0));
        }
        for (String token : tokens.subList(1, tokens.size())) {
            result.append(sep);
            result.append(token);
        }
        return result.toString();
    }
}
