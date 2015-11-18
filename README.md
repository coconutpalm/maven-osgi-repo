# maven-osgi-repo

Contains an HTTP server that turns any P2 repo into a M2-compliant repo.

Also contains an Eclipse plugin that:
* Installs the server into the current workspace's configuration area
* Runs the server on Eclipse launch.
* Terminates the server on Eclipse shutdown.

The result is that Eclipse becomes its own local M2 repository for all installed plug-ins.

See also: The README.md inside maven-repo-server.

## Notes

* Originally exported from code.google.com/p/maven-osgi-repo
* If anyone knows how to contact the original author, please let me know.  His code is licensed under GPLv3, which
means that unless he agrees to relicense it under an EPL-compatible license, I can't incorporate his code into
an Eclipse plug-in.
