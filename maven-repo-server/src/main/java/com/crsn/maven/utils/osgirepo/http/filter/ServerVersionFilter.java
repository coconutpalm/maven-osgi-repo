package com.crsn.maven.utils.osgirepo.http.filter;

import java.io.IOException;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;

import com.crsn.maven.utils.osgirepo.http.cor.RequestContext;
import com.crsn.maven.utils.osgirepo.http.cor.RequestFilter;

public class ServerVersionFilter implements RequestFilter {

	@Override
	public void filter(Request req, Response res, RequestContext context) throws IOException {
		res.set("Server", "Maven Osgi Repo/1.0 (Simple 4.0)");
		context.process(req, res);
	}

}
