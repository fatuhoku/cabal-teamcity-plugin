/*
 * Copyright 2000-2010 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cabalRunner.agent;

import jetbrains.buildServer.agent.AgentBuildRunnerInfo;
import jetbrains.buildServer.agent.BuildAgentConfiguration;
import jetbrains.buildServer.agent.runner.CommandLineBuildService;
import jetbrains.buildServer.agent.runner.CommandLineBuildServiceFactory;
import org.jetbrains.annotations.NotNull;
import cabalRunner.common.PluginConstants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Performs basic discovery of the path of the cabal executable, as well as extract its version number.
 */
public class CabalRunnerBuildServiceFactory implements CommandLineBuildServiceFactory {

    public CabalRunnerBuildServiceFactory() {
    }

    @NotNull
    public CommandLineBuildService createService() {
        return new CabalRunnerBuildService();
    }

    /**
     * AgentBuildRunnerInfo returns some basic facts about the agent system.
     *
     * @return
     */
    @NotNull
    public AgentBuildRunnerInfo getBuildRunnerInfo() {
        return new AgentBuildRunnerInfo() {

            @NotNull
            public String getType() {
                return PluginConstants.RUN_TYPE;
            }

            /**
             * Cabal can be run on all three supported operating systems,
             * provided that cabal is found.
             * @param agentConfiguration
             * @return
             */
            public boolean canRun(@NotNull BuildAgentConfiguration agentConfiguration) {
                setupAgentConfiguration(agentConfiguration);
                return agentConfiguration.getSystemInfo().isWindows()
                        || agentConfiguration.getSystemInfo().isMac()
                        || agentConfiguration.getSystemInfo().isUnix();
            }

            private void setupAgentConfiguration(@NotNull BuildAgentConfiguration agentConfiguration) {
                // Adds cabal.path and cabal.version agent configuration parameters.
                // We make no assumption they exist already.
                performCabalDiscovery(agentConfiguration);
            }

            /**
             * TODO lift 'Cabal' discovery to canRun.
             * Attempts to discover facts about the cabal-install tool the agent, such as
             *  - the path to cabal-install, and
             *  - the version number of cabal-install
             * and adds it into the agentConfiguration.
             *
             * The order in which discovery should happen is as follows:
             * Absolute path > Haskell Platform path > command string ("cabal")
             *
             * To locate the absolute path, we use the 'which' command to locate the true location of the
             * cabal executable. If it doesn't exist, we look for environment variables that
             * point to the Haskell platform. If that doesn't exist, then we use 'cabal'.
             *
             * @param agentConfiguration build agent parameters.
             */
            private void performCabalDiscovery(BuildAgentConfiguration agentConfiguration) {
                String cabalPathProperty = agentConfiguration.getConfigurationParameters().get(PluginConstants.AGENT_CONFIGURATION_CABAL_PATH);
                if (cabalPathProperty == null) {
                    // If a call to query cabal's version succeeds, then we have verified that the
                    // command to invoke is correct.
                    String cabalCmd = "cabal";
                    try {
                        Process process = Runtime.getRuntime().exec(new String[]{ cabalCmd, "--numeric-version" });
                        BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
                        String cabalVersion = in.readLine().trim();
                        if (process.waitFor() == 0) {
                            process = Runtime.getRuntime().exec(new String[]{ "which", cabalCmd });
                            in = new BufferedReader(new InputStreamReader(process.getInputStream()));
                            String cabalAbsolutePath = in.readLine().trim();

                            if (process.waitFor() == 0) {
                                agentConfiguration.addConfigurationParameter(PluginConstants.AGENT_CONFIGURATION_CABAL_PATH, cabalAbsolutePath);
                                agentConfiguration.addConfigurationParameter(PluginConstants.AGENT_CONFIGURATION_CABAL_VERSION, cabalVersion);
                            }
                        }
                    } catch (IOException e) {
                        // TODO get rid of System.out.println calls and use logging instead
                        System.out.println("cabal-install (cabal) is not on the PATH.");
                    } catch (InterruptedException e) {
                        System.out.println("cabal-install (cabal) was found on PATH version could not be determined.");
                    }
                } else {
                    System.out.println(String.format("cabal.path property was found: %s",cabalPathProperty));
                }
            }
        };
    }
}
