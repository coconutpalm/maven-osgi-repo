package com.crsn.maven.utils.osgirepo.http.cor;

import java.io.IOException;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;

public interface RequestFilter {	
	public void filter(Request req, Response res, RequestContext context) throws IOException;
}
