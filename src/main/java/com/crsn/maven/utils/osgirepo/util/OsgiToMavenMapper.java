package com.crsn.maven.utils.osgirepo.util;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.osgi.framework.Version;

import com.crsn.maven.utils.osgirepo.maven.MavenRepository;
import com.crsn.maven.utils.osgirepo.maven.MavenVersion;
import com.crsn.maven.utils.osgirepo.maven.builder.MavenArtifactBuilder;
import com.crsn.maven.utils.osgirepo.maven.builder.MavenDependencyBuilder;
import com.crsn.maven.utils.osgirepo.maven.builder.MavenRepositoryBuilder;
import com.crsn.maven.utils.osgirepo.osgi.OsgiBundle;
import com.crsn.maven.utils.osgirepo.osgi.OsgiDependency;
import com.crsn.maven.utils.osgirepo.osgi.OsgiRepository;
import com.google.code.mjl.Log;
import com.google.code.mjl.LogFactory;

public class OsgiToMavenMapper {
	private static final Log log = LogFactory.getLog();

	public static MavenRepository createRepository(OsgiRepository repository) {
		MavenRepositoryBuilder builder = new MavenRepositoryBuilder();
		for (OsgiBundle plugin : repository.getPlugins()) {
			String groupId = createGroupId(plugin.getName());

			String artifactId = createArtifactName(plugin.getName());

			Version version = plugin.getVersion();

			boolean isSourcePlugin = plugin.getName().endsWith("source");

			MavenArtifactBuilder artefactBuilder = isSourcePlugin ? builder.addSourceArtifact() : builder.addArtifact();

			artefactBuilder.setGroup(groupId);
			artefactBuilder.setArtifactId(artifactId);
			artefactBuilder.setVersion(createMavenVersion(version));
			artefactBuilder.setContent(plugin.getLocation());

			addDependencies(repository, plugin, artefactBuilder);

		}

		return builder.build();

	}

	private static void addDependencies(OsgiRepository repository, OsgiBundle plugin, MavenArtifactBuilder artefactBuilder) {
		for (OsgiDependency osgiDependency : plugin.getRequiredBundles()) {

			OsgiBundle osgiPlugin = repository.resolveDependency(osgiDependency);

			if (osgiPlugin == null) {
				log.warn("Could not resolve dependency %s.", osgiDependency);
				// throw new
				// RuntimeException(String.format("Could not resolve dependency %s.",
				// osgiDependency));
			} else {

				MavenDependencyBuilder dependencyBuilder = artefactBuilder.addDependency();
				dependencyBuilder.setArtefactId(createArtifactName(osgiPlugin.getName()));
				dependencyBuilder.setGroupId(createGroupId(osgiPlugin.getName()));

				// VersionRange versionRange =
				// osgiDependency.getVersionRange();

				dependencyBuilder.setVersionRange(createMavenVersion(osgiPlugin.getVersion()), true,
						createMavenVersion(osgiPlugin.getVersion()), true);
				dependencyBuilder.build();
			}
		}

		artefactBuilder.build();
	}

	static MavenVersion createMavenVersion(Version version) {
		if (version == null) {
			return null;
		}
		return new MavenVersion(version.getMajor(), version.getMinor(), version.getMicro());
	}

	static String createGroupId(String pluginName) {
		List<String> parts = Arrays.asList(pluginName.split("\\."));
		return StringUtils.join(parts.subList(0, parts.size() - (isSourcePlugin(parts) ? 2 : 1)), '.');
	}

	static String createArtifactName(String pluginName) {
		List<String> parts = Arrays.asList(pluginName.split("\\."));
		return isSourcePlugin(parts) ? parts.get(parts.size() - 2) : parts.get(parts.size() - 1);
	}

	private static boolean isSourcePlugin(List<String> parts) {
		boolean isSourcePlugin = parts.get(parts.size() - 1).equals("source");
		return isSourcePlugin;
	}

}
