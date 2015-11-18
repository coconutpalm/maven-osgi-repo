package com.crsn.maven.utils.osgirepo.search;

import java.util.List;

import com.crsn.maven.utils.osgirepo.maven.MavenArtifact;

public interface SearchService {
	
	List<MavenArtifact> findArtifacts(String query);

}
