package com.crsn.maven.utils.osgirepo.search;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.standard.ClassicAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import com.crsn.maven.utils.osgirepo.maven.MavenArtifact;
import com.crsn.maven.utils.osgirepo.maven.MavenRepository;
import com.crsn.maven.utils.osgirepo.maven.MavenSourceArtifact;

public class LuceneSearchService implements SearchService {

	private final IndexSearcher searcher;
	private QueryParser parser;
	private final Map<String, MavenArtifact> artifactByCoordinates;

	public LuceneSearchService(Directory directory, Map<String, MavenArtifact> artifactByCoordinates) {
		this.artifactByCoordinates = artifactByCoordinates;
		try {
			searcher = new IndexSearcher(directory);
		} catch (CorruptIndexException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		parser = new QueryParser(Version.LUCENE_34, "description", new ClassicAnalyzer(Version.LUCENE_34));

	}

	public static LuceneSearchService createSearchService(MavenRepository repository) {

		try {
			return createSearchServiceInternal(repository);
		} catch (CorruptIndexException e) {
			throw new RuntimeException(e);
		} catch (LockObtainFailedException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static LuceneSearchService createSearchServiceInternal(MavenRepository repository)
			throws CorruptIndexException, LockObtainFailedException, IOException {
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_34, new ClassicAnalyzer(Version.LUCENE_34));
		config.setOpenMode(OpenMode.CREATE);
		RAMDirectory directory = new RAMDirectory();
		IndexWriter writer = new IndexWriter(directory, config);

		HashMap<String, MavenArtifact> artifactByCoordinates = new HashMap<String, MavenArtifact>();

		for (MavenArtifact artifact : repository.getArtifacts()) {
			if (!(artifact instanceof MavenSourceArtifact)) {
				Document doc = createDocument(artifact);
				artifactByCoordinates.put(makeCoordinates(artifact), artifact);
				writer.addDocument(doc);
			}
		}
		writer.close();

		return new LuceneSearchService(directory, artifactByCoordinates);
	}

	private static Document createDocument(MavenArtifact artifact) {
		Document doc = new Document();
		String coordinate = makeCoordinates(artifact);
		doc.add(new Field("description", artifact.getName(), Field.Store.NO, Field.Index.ANALYZED));
		doc.add(new Field("description", artifact.getGroupId(), Field.Store.NO, Field.Index.NOT_ANALYZED));
		doc.add(new Field("description", artifact.getArtifactId(), Field.Store.NO, Field.Index.ANALYZED));
		doc.add(new Field("description", artifact.getVersion().toString(), Field.Store.NO, Field.Index.NOT_ANALYZED));	
		doc.add(new Field("coordinates", coordinate, Field.Store.YES, Field.Index.NO));
		return doc;
	}

	private static String makeCoordinates(MavenArtifact artifact) {
		String coordinate = String.format("%s:%s:%s", artifact.getGroupId(), artifact.getArtifactId(),
				artifact.getVersion());
		return coordinate;
	}

	@Override
	public List<MavenArtifact> findArtifacts(String queryString) {

		try {
			Query query = parser.parse(queryString);
			TopDocs docs = searcher.search(query, 20);

			List<MavenArtifact> result = new LinkedList<MavenArtifact>();

			for (ScoreDoc scoreDoc : docs.scoreDocs) {
				int doc = scoreDoc.doc;
				Document document = searcher.doc(doc);
				result.add(artifactByCoordinates.get(document.get("coordinates")));
			}

			return result;

		} catch (ParseException e) {
			return Collections.<MavenArtifact> emptyList();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
