package com.crsn.maven.utils.osgirepo;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;

import com.crsn.maven.utils.osgirepo.http.Content;
import com.crsn.maven.utils.osgirepo.http.HttpServer;
import com.crsn.maven.utils.osgirepo.http.content.VelocityHtmlTemplateContent;
import com.crsn.maven.utils.osgirepo.maven.MavenRepository;
import com.crsn.maven.utils.osgirepo.osgi.OsgiRepository;
import com.crsn.maven.utils.osgirepo.util.MavenRepositoryToHttpContentsMapper;
import com.crsn.maven.utils.osgirepo.util.OsgiToMavenMapper;

public class MavenOsgiRepository {
	private final HttpServer server;

	public MavenOsgiRepository(File repositoryDirectory, int port) {
		server = new HttpServer(port);
		OsgiRepository osgiRepository = OsgiRepository.createRepository(repositoryDirectory);
		MavenRepository mavenRepository = OsgiToMavenMapper.createRepository(osgiRepository);
		
		Map<String, Content> contentMap = MavenRepositoryToHttpContentsMapper.createContentForRepository(mavenRepository);
		for (Entry<String, Content> entry : contentMap.entrySet()) {
			server.registerContent(entry.getKey(), entry.getValue());
		}
		
		
		server.registerContent("/", new VelocityHtmlTemplateContent("/index.vm",mavenRepository));
		
//		server.registerContent("/", new EmptyContent());

	}

	public void start() {
		server.start();
	}

	public void stop() {
		server.stop();
	}

	public String getServerUrl() {
		return server.getServerUrl();
	}

}
