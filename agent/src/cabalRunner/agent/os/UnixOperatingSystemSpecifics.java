package cabalRunner.agent.os;

public class UnixOperatingSystemSpecifics implements OperatingSystemSpecifics {
    @Override
    public String getShell() {
        return "sh";
    }

    @Override
    public String getShellArgumentCommandFlag() {
        return "-c";
    }

    @Override
    public String getConjunction() {
        return "&&";
    }
}
