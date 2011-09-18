package com.crsn.maven.utils.osgirepo.maven.builder;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.Test;

import com.crsn.maven.utils.osgirepo.maven.MavenArtifact;
import com.crsn.maven.utils.osgirepo.maven.MavenVersion;

public class MavenRepositoryBuilderTest {

	@Test
	public void canBuildArtefacts() {
		MavenRepositoryBuilder builder = new MavenRepositoryBuilder();

		MavenArtifactBuilder artefact = builder.addArtefact();
		artefact.setArtifactId("id");
		artefact.setGroup("group");
		artefact.setVersion(new MavenVersion(1, 0));
		artefact.setContent(new File("."));
		artefact.build();

		List<MavenArtifact> artefacts = builder.build().getArtefacts();
		assertFalse(artefacts.isEmpty());
		MavenArtifact first = artefacts.get(0);
		assertEquals("id", first.getId());
	}
	
	@Test
	public void canBuildDependency() {
		MavenRepositoryBuilder builder = new MavenRepositoryBuilder();

		MavenArtifactBuilder artefact = builder.addArtefact();
		artefact.setArtifactId("id");
		artefact.setGroup("group");
		artefact.setVersion(new MavenVersion(1, 0));
		artefact.setContent(new File("."));
		MavenDependencyBuilder dependency = artefact.addDependency();
		dependency.setArtefactId("dep");
		dependency.setGroupId("depgroup");
		dependency.setVersionRange(null, false, null, false);
		dependency.build();
		artefact.build();

		List<MavenArtifact> artefacts = builder.build().getArtefacts();
		assertFalse(artefacts.isEmpty());
		MavenArtifact first = artefacts.get(0);
		assertEquals("id", first.getId());
		assertFalse(first.getDependencies().isEmpty());
		
	}

}
