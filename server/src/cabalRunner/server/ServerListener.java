package cabalRunner.server;

import cabalRunner.common.PluginConstants;
import jetbrains.buildServer.log.Loggers;
import jetbrains.buildServer.serverSide.BuildServerAdapter;
import jetbrains.buildServer.serverSide.BuildServerListener;
import jetbrains.buildServer.serverSide.SBuildServer;
import jetbrains.buildServer.util.EventDispatcher;
import org.jetbrains.annotations.NotNull;

public class ServerListener extends BuildServerAdapter {
  private SBuildServer myServer;

  public ServerListener(@NotNull final EventDispatcher<BuildServerListener> dispatcher, SBuildServer server) {
    dispatcher.addListener(this);
    myServer = server;
  }

  @Override
  public void serverStartup() {
    Loggers.SERVER.info("Plugin '" + PluginConstants.RUN_TYPE + "' is running on server version " + myServer.getFullServerVersion() + ".");
  }
}
