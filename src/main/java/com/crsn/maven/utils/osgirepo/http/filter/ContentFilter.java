package com.crsn.maven.utils.osgirepo.http.filter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ConcurrentHashMap;

import org.simpleframework.http.Path;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;

import com.crsn.maven.utils.osgirepo.http.Content;
import com.crsn.maven.utils.osgirepo.http.cor.RequestContext;
import com.crsn.maven.utils.osgirepo.http.cor.RequestFilter;
import com.google.code.mjl.Log;
import com.google.code.mjl.LogFactory;

public class ContentFilter implements RequestFilter {

	private static final Log log = LogFactory.getLog();

	private final ConcurrentHashMap<String, Content> contents;

	public ContentFilter() {
		this.contents = new ConcurrentHashMap<String, Content>();
	}
	
	public void registerContent(String path, Content content) {
		if (path == null) {
			throw new NullPointerException("Null path.");
		}
		if (content == null) {
			throw new NullPointerException("Null content.");
		}

		log.info("Registered %s", path);
		contents.put(path, content);
	}

	@Override
	public void filter(Request req, Response res, RequestContext context) throws IOException {
		Path path = req.getPath();

		Content content = contents.get(path.toString());
		if (content != null) {
			res.set("Content-Type", content.contentType());
			res.setDate("Date", System.currentTimeMillis());
			res.setDate("Last-Modified", System.currentTimeMillis());
			OutputStream outputStream = res.getOutputStream();
			content.serializeContent(outputStream);
			outputStream.close();
			log.info("path: %s", path.getPath());
		} else {
			context.process(req, res);
		}

	}
}