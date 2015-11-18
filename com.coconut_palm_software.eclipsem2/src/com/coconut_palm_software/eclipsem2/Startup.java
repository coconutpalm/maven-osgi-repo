package com.coconut_palm_software.eclipsem2;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.ui.IStartup;

public class Startup implements IStartup {


    @Override
    public void earlyStartup() {
        try {
            final URL MAVEN_REPO_SERVER = new URL("https://github.com/coconutpalm/maven-osgi-repo/releases/download/0.3.0/osgi-repo-0.3.0-jar-with-dependencies.jar");
        } catch (MalformedURLException e) {
            return;
        }
        final Process m2repoProcess = null;
        final String installLocation = Platform.getInstallLocation().getURL().toExternalForm();
        final File configurationLocation = ConfigurationScope.INSTANCE.getLocation().toFile();

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                m2repoProcess.destroy();
            }
        }, "Shutdown hook"));
    }

}
