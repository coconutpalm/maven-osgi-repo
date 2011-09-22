package com.crsn.maven.utils.osgirepo.util;

import java.io.File;
import java.util.List;

import org.osgi.framework.Version;

import com.crsn.maven.utils.osgirepo.osgi.OsgiDependency;
import com.crsn.maven.utils.osgirepo.osgi.OsgiPlugin;

public class MockOsgiPlugin implements OsgiPlugin {

	private final String name;
	private final Version version;
	private final List<OsgiDependency> dependencies;

	public MockOsgiPlugin(String name, Version version, List<OsgiDependency> dependencies) {
		this.name = name;
		this.version = version;
		this.dependencies = dependencies;

	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Version getVersion() {
		return version;
	}

	@Override
	public File getLocation() {
		return new File(".");
	}

	@Override
	public List<OsgiDependency> getRequiredBundles() {
		return dependencies;
	}

}
