# Cabal Teamcity Build Runner Plugin
(Relatively) painless continuous integration for Haskell.

### Known issues
There are a good number of issues with this plugin which arise from a single
implementation detail of the plugin.  All the trouble comes from the fact that
the plugin was written to be _unintrusive_ to either the Cabal or
test-framework projects. This design decision meant that it was difficult or
simply impossible to provide build progress information using TeamCity's
[TeamCity service messages][], the preferred way of reporting such information.
This plugin instead relies on parsing and matching lines of build output to
recover the structure of the build, which brings about many limitations to the
level of integration possible:

###### Reported test durations are incorrect
Since the beginning and end of tests are deduced by matching on build output lines, TeamCity would
can only reliably record the durations of tests run if the messages that signal their beginning and end
are reliably timely. test-framework however runs tests in parallel but buffers test results until the
very end, when everything is flushed out to standard output at the final instance. This makes
tests appear as though they have taken a very short time to run, when in fact this mightn't be the case.

###### Test output is not aligned with the test it came from
The plugin notifies TeamCity of build events only once a line has been seen and output to the build log.
For test output, this often is too late, causing TeamCity to get confused.

###### Some test successes or failures are counted incorrectly
Test success and failures are deduced by matching a pattern against output
lines on the build log. While every care has been made to ensure that
passing and failing test runs, test groups and test suites are detected
correctly, any changes in the format of test-framework's build output will
cause this plugin to behave abnormally as it fails to match lines it is expecting.
