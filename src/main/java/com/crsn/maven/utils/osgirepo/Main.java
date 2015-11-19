package com.crsn.maven.utils.osgirepo;

import java.io.File;

import com.sampullara.cli.Args;
import com.sampullara.cli.Argument;

public class Main {

	public static void main(String[] args) {
		Main main=new Main();
		try {
			parseArguments(args,main);
		} catch (IllegalArgumentException e) {
			System.err.println(e.getMessage());
			Args.usage(main);
			return;
		}
		
		
		main.execute();

	}
	
	
	private void execute() {
		MavenOsgiRepository repository=new MavenOsgiRepository(pluginRepository, port);		
		repository.start();
		try {
			Thread.sleep(Long.MAX_VALUE);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} finally {
			repository.stop();
		}
	}

	static Main parseArguments(String[] args, Main arguments) {	
		Args.parse(arguments, args);
		return arguments;
	}

	@Argument(value = "plugin_repository", alias = "r", description = "Directory containing OSGi plugins", required = true)
	private File pluginRepository;

	@Argument(value = "port", alias = "p", description = "Local port for HTTP service", required = true)
	private Integer port;

	public File getPluginRepository() {
		return pluginRepository;
	}

	public int getPort() {
		return port;
	}

}
