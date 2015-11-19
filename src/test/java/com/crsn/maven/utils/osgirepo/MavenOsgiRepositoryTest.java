package com.crsn.maven.utils.osgirepo;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.crsn.maven.utils.osgirepo.util.HttpResponseWrapper;
import com.crsn.maven.utils.osgirepo.util.TestUtil;

public class MavenOsgiRepositoryTest {

	private static final MavenOsgiRepository repository = new MavenOsgiRepository(
			TestUtil.getFileOfResource("mockrepo"), 0);

	@BeforeClass
	public static void setUp() {
		repository.start();
	}

	@AfterClass
	public static void tearDown() {
		repository.stop();
	}

	@Test
	public void willRespondWithPomFile() {
		HttpResponseWrapper response = HttpResponseWrapper
				.getResponse(repository.getServerUrl()
						+ "/org/eclipse/xtext/xtend2/lib/2.0.1/lib-2.0.1.pom");
		assertEquals(200, response.getStatusCode());
	}

	@Test
	public void willRespondWithJarFile() {
		HttpResponseWrapper response = HttpResponseWrapper
				.getResponse(repository.getServerUrl()
						+ "/org/eclipse/xtext/xtend2/lib/2.0.1/lib-2.0.1.jar");
		assertEquals(200, response.getStatusCode());
	}
	
	@Test
	public void willRespondWithArtifactMetaDataFile() {
		HttpResponseWrapper response = HttpResponseWrapper
				.getResponse(repository.getServerUrl()
						+ "/org/eclipse/xtext/xtend2/lib/maven-metadata.xml.md5");
		assertEquals(200, response.getStatusCode());
	}

	
	@Test
	public void willRespondWithArtifactMetaDataMd5File() {
		HttpResponseWrapper response = HttpResponseWrapper
				.getResponse(repository.getServerUrl()
						+ "/org/eclipse/xtext/xtend2/lib/maven-metadata.xml.md5");
		assertEquals(200, response.getStatusCode());
	}

	@Test
	public void willRespondWithArtifactMetaDataSha1File() {
		HttpResponseWrapper response = HttpResponseWrapper
				.getResponse(repository.getServerUrl()
						+ "/org/eclipse/xtext/xtend2/lib/maven-metadata.xml.sha1");
		assertEquals(200, response.getStatusCode());
	}

	
	@Test
	public void willRespondWithMd5FileForPomDigest() {
		HttpResponseWrapper response = HttpResponseWrapper
				.getResponse(repository.getServerUrl()
						+ "/org/eclipse/xtext/xtend2/lib/2.0.1/lib-2.0.1.pom.md5");
		assertEquals(200, response.getStatusCode());
	}

	@Test
	public void willRespondWithSha1FileForPomDigest() {
		HttpResponseWrapper response = HttpResponseWrapper
				.getResponse(repository.getServerUrl()
						+ "/org/eclipse/xtext/xtend2/lib/2.0.1/lib-2.0.1.pom.sha1");
		assertEquals(200, response.getStatusCode());
	}

	@Test
	public void willRespondWithMd5FileForFileDigest() {
		HttpResponseWrapper response = HttpResponseWrapper
				.getResponse(repository.getServerUrl()
						+ "/org/eclipse/xtext/xtend2/lib/2.0.1/lib-2.0.1.jar.md5");
		assertEquals(200, response.getStatusCode());
	}

	@Test
	public void willRespondWithSha1FileForFileDigest() {
		HttpResponseWrapper response = HttpResponseWrapper
				.getResponse(repository.getServerUrl()
						+ "/org/eclipse/xtext/xtend2/lib/2.0.1/lib-2.0.1.jar.sha1");
		assertEquals(200, response.getStatusCode());
	}

}
