package cabalRunner.agent.os;

/**
 * Created with IntelliJ IDEA.
 * User: H.Poon
 * Date: 08/04/2012
 * Time: 19:34
 * To change this template use File | Settings | File Templates.
 */
public class WindowsOperatingSystemSpecifics implements OperatingSystemSpecifics {
    @Override
    public String getShell() {
        return "cmd";
    }

    @Override
    public String getShellArgumentCommandFlag() {
        return "/C";
    }

    @Override
    public String getConjunction() {
        return "&&";
    }

}
