package cabalRunner.common;

import java.util.Map;

/**
 * Utility class for pulling data from parameter map used by the agent, usually.
 */
public final class Parameters {
    public static boolean getBoolean(Map<String, String> parameters, String name) {
        return !(parameters.get(name) == null || !parameters.get(name).equals("true"));
    }

    public static String getString(Map<String, String> parameters, String name) {
        if (parameters.get(name) == null) {
            return "";
        }
        return parameters.get(name);
    }
}
