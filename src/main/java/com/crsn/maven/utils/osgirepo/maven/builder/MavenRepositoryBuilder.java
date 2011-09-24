package com.crsn.maven.utils.osgirepo.maven.builder;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import com.crsn.maven.utils.osgirepo.maven.MavenArtifact;
import com.crsn.maven.utils.osgirepo.maven.MavenDependency;
import com.crsn.maven.utils.osgirepo.maven.MavenRepository;
import com.crsn.maven.utils.osgirepo.maven.MavenSourceArtifact;
import com.crsn.maven.utils.osgirepo.maven.MavenVersion;
import com.crsn.maven.utils.osgirepo.maven.MavenVersionRange;

public class MavenRepositoryBuilder {

	private final List<MavenArtifact> artefacts = new LinkedList<MavenArtifact>();

	public MavenRepositoryBuilder() {

	}

	public MavenArtifactBuilder addArtifact() {
		return new MavenArtifactBuilderInternal();
	}

	public MavenArtifactBuilder addSourceArtifact() {
		return new MavenSourceArtifactBuilderInternal();
	}

	public MavenRepository build() {
		return new MavenRepository(artefacts);
	}

	public class MavenSourceArtifactBuilderInternal extends MavenArtifactBuilderInternal {

		@Override
		public MavenDependencyBuilder addDependency() {
			throw new IllegalStateException("Source artifacts don't have dependencies.");
		}

		@Override
		public void build() {
			artefacts.add(new MavenSourceArtifact(group, artifactId, version, content));
		}

	}

	public class MavenArtifactBuilderInternal implements MavenArtifactBuilder {

		protected String group;
		protected String artifactId;
		protected MavenVersion version;
		private List<MavenDependency> dependencies = new LinkedList<MavenDependency>();
		protected File content;

		public MavenArtifactBuilderInternal() {

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.crsn.maven.utils.osgirepo.maven.builder.MavenAer#setGroup(java
		 * .lang.String)
		 */
		@Override
		public void setGroup(String groupId) {
			this.group = groupId;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.crsn.maven.utils.osgirepo.maven.builder.MavenAer#setArtefactId
		 * (java.lang.String)
		 */
		@Override
		public void setArtifactId(String artifactId) {
			this.artifactId = artifactId;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.crsn.maven.utils.osgirepo.maven.builder.MavenAer#setVersion(com
		 * .crsn.maven.utils.osgirepo.maven.MavenVersion)
		 */
		@Override
		public void setVersion(MavenVersion version) {
			this.version = version;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.crsn.maven.utils.osgirepo.maven.builder.MavenAer#addDependency()
		 */
		@Override
		public MavenDependencyBuilder addDependency() {
			return new InternalDependencyBuilder();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.crsn.maven.utils.osgirepo.maven.builder.MavenAer#setContent(java
		 * .io.File)
		 */
		@Override
		public void setContent(File content) {
			this.content = content;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.crsn.maven.utils.osgirepo.maven.builder.MavenAer#build()
		 */
		@Override
		public void build() {
			artefacts.add(new MavenArtifact(group, artifactId, version, dependencies, content));
		}

		private class InternalDependencyBuilder implements MavenDependencyBuilder {

			private String groupId;
			private String artefactId;
			private MavenVersionRange versionRange;

			@Override
			public void setGroupId(String groupId) {
				this.groupId = groupId;

			}

			@Override
			public void setArtefactId(String artefactId) {
				this.artefactId = artefactId;

			}

			@Override
			public void setVersionRange(MavenVersion from, boolean includingFrom, MavenVersion to, boolean includingTo) {
				this.versionRange = new MavenVersionRange(from, includingFrom, to, includingTo);

			}

			@Override
			public void build() {
				dependencies.add(new MavenDependency(groupId, artefactId, versionRange));

			}

		}

	}

}
