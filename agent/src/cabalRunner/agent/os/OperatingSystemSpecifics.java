package cabalRunner.agent.os;

public interface OperatingSystemSpecifics {
    /**
     * The command to launch a shell.
     * @return
     */
    public String getShell();

    /**
     * This is the shell flag that allows you to run a string argument as a command.
     * @return
     */
    public String getShellArgumentCommandFlag();

    /**
     * This is the operator used to glue together a bunch of commands.
     * @return
     */
    public String getConjunction();
}
