package com.crsn.maven.utils.osgirepo.http.filter;

import java.io.IOException;
import java.io.PrintStream;

import org.simpleframework.http.Path;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;

import com.crsn.maven.utils.osgirepo.http.cor.RequestContext;
import com.crsn.maven.utils.osgirepo.http.cor.RequestFilter;
import com.google.code.mjl.Log;
import com.google.code.mjl.LogFactory;

public class NotFoundFilter implements RequestFilter {
	private static final Log log = LogFactory.getLog();

	@Override
	public void filter(Request req, Response res, RequestContext context) throws IOException {
		Path path = req.getPath();
		log.warn("failed request: %s", path.getPath());

		res.setCode(404);
		res.set("Content-Type", "text/plain");
		res.setDate("Date", System.currentTimeMillis());
		res.setDate("Last-Modified", System.currentTimeMillis());

		PrintStream ps = res.getPrintStream();
		ps.println("Page not found!");
		ps.close();

	}

}
