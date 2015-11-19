package com.crsn.maven.utils.osgirepo.search;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.Version;

import com.crsn.maven.utils.osgirepo.maven.MavenArtifact;
import com.crsn.maven.utils.osgirepo.maven.MavenRepository;
import com.crsn.maven.utils.osgirepo.maven.MavenVersion;
import com.crsn.maven.utils.osgirepo.maven.builder.MavenArtifactBuilder;
import com.crsn.maven.utils.osgirepo.maven.builder.MavenRepositoryBuilder;
import com.crsn.maven.utils.osgirepo.osgi.OsgiDependency;
import com.crsn.maven.utils.osgirepo.osgi.OsgiRepository;
import com.crsn.maven.utils.osgirepo.osgi.VersionRange;
import com.crsn.maven.utils.osgirepo.util.MockOsgiPlugin;
import com.crsn.maven.utils.osgirepo.util.OsgiToMavenMapper;

public class LuceneSearchServiceTest {
	

	private LuceneSearchService searchService;
	
	@Before
	public void createRepository() {
		
		MavenRepositoryBuilder builder=new MavenRepositoryBuilder();
		
		MavenArtifactBuilder artifactBuilder = builder.addArtifact();
		artifactBuilder.setArtifactId("testartifactid");
		artifactBuilder.setGroup("com.crsn.test");
		artifactBuilder.setName("first artifact name");
		artifactBuilder.setOrganization("firstArtifactOrganization");
		artifactBuilder.setVersion(new MavenVersion(1,0));
		artifactBuilder.setContent(new File("."));
		artifactBuilder.build();
		
		
		
		MavenRepository repository = builder.build();

		searchService = LuceneSearchService.createSearchService(repository);
	}
	
	@Test
	public void canFindArtifactByName() {
		List<MavenArtifact> foundArtifacts = searchService.findArtifacts("first");
		assertFalse(foundArtifacts.isEmpty());
	}
	
	@Test
	public void canFindArtifactByGroup() {
		List<MavenArtifact> foundArtifacts = searchService.findArtifacts("com.crsn.test");
		assertFalse(foundArtifacts.isEmpty());
	}
	
	@Test
	public void canFindArtifactByArtifactId() {
		List<MavenArtifact> foundArtifacts = searchService.findArtifacts("testartifactid");
		assertFalse(foundArtifacts.isEmpty());
	}
	
	@Test
	public void canFindArtifactByVersion() {
		List<MavenArtifact> foundArtifacts = searchService.findArtifacts("1.0");
		assertFalse(foundArtifacts.isEmpty());
	}

}
